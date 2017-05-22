package logic;

import gui.KanaGui;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import kana.Kana;
import kana.KanaHelper;
import kana.Yoon;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author edotee
 */
public class TypeTheRomaji<T extends Kana<T>> extends KanaExercise<T> {

    private final int CURRENT_KANA = 1;

    private BorderPane layout;
    private Label question, currentProblem, previousProblem, upcomingProblem, previousRomaji;
    private VBox questionArea;
    private TextField answerInput;
    private HBox answerArea;

    //input and hiraganaYoon are used in handleInput - we initiate them as fields to avoid unnecessary GC work, since each pass of handleInput will need them
    private String input;
    private Yoon hiraganaYoon;
    private InputResult resultCase;
    private String logOutput;
    private int discardOffset;     //the anoption that has to be discarded - offset == 0 = normal operation, offset == 1 = discard the next kana instead of the current

    public TypeTheRomaji(ArrayList<T> completeList, HashSet<T> targetKana,
                         EventHandler<ActionEvent> onComplete) {
        super(completeList, targetKana, null, null, onComplete);
    }

    private void handleInput(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ENTER) || keyEvent.getCode().equals(KeyCode.SPACE)) {
            //TODO add some smart regex to sort handle potato finger inputs (double consonants, ending with a consonant other than n, not ending on a vowel other than n
            input = answerInput.getText().toLowerCase().trim();
            hiraganaYoon = KanaHelper.isYoon(getAnswerLog().get(CURRENT_KANA), getAnswerLog().get(CURRENT_KANA-1));
            resultCase = evaluateInput();
            logOutput = "";
            discardOffset = 0;

            //Logging Logic
            switch(resultCase) {
                case INVALID:
                    logOutput = "";
                    break;

                case FOUL:
                    logOutput = "FOUL: を is only used as a particle";
                    break;

                case WRONG:
                    logOutput = (getAnswerLog().get(CURRENT_KANA).getKana() + "(" + getAnswerLog().get(CURRENT_KANA).getRomanji() + ") != \""
                            + input
                            + "\"\t| next in line: " + getAnswerLog().get(CURRENT_KANA-1).getKana() + "(" + getAnswerLog().get(CURRENT_KANA-1).getRomanji() + ")"
                    );
                    break;

                case YOON:
                    logOutput = "Yoon!";
                case DOUBLE_CORRECT: case CORRECT:
                    if(logOutput.length() == 0) logOutput = "";
                    break;

                case MISSED_YOON:
                    logOutput = "Missed a Yoon";
                    break;
            }
            if(logOutput.length() > 0) System.out.println(logOutput);

            //Side Effect Logic
            switch(resultCase) {
                case INVALID:
                break;

                case FOUL: case WRONG:
                    onWrongAnswer();
                break;

                case CORRECT: case DOUBLE_CORRECT:
                    onRightAnswer();
                    //TODO seperate and add "styleDoubleCorrect()"
                break;

                case YOON:
                    //TODO this needs to display both kana and not just the last one
                    onRightAnswer();
                    //TODO replace with "onYoon" / styleYoon()
                break;

                case MISSED_YOON:
                    onRightAnswer();
                    //TODO replace with "onMissedYoon" / styleMissedYoon()
                break;
            }
        } /*if(keyEvent.getCode().equals(KeyCode.BACK_SPACE)) {
            answerInput.clear();
        }*/
    }

    private InputResult evaluateInput() {
        InputResult result = InputResult.INVALID;
        if(input.length() > 0) {
            if (getAnswerLog().get(CURRENT_KANA).getRomanji().equals(input)) {
                result = InputResult.CORRECT;
            } else if (hiraganaYoon != null) {
                if (hiraganaYoon.getRomanji().equals(input))
                    result = InputResult.YOON;
                else if ((getAnswerLog().get(CURRENT_KANA).getRomanji() + getAnswerLog().get(CURRENT_KANA-1).getRomanji()).equals(input))
                    result = InputResult.MISSED_YOON;
                else
                    result = InputResult.WRONG;
            } else if ((getAnswerLog().get(CURRENT_KANA).getRomanji() + getAnswerLog().get(CURRENT_KANA-1).getRomanji()).equals(input)) {
                if (getAnswerLog().get(CURRENT_KANA).getRomanji().equals("wo") || getAnswerLog().get(CURRENT_KANA-1).getRomanji().equals("wo"))
                    result = InputResult.FOUL;
                else
                    result = InputResult.DOUBLE_CORRECT;
            } else {
                //TODO maybe implement partly wrong (1st correct, 2nd wrong / the other way around / implement a boolean[] field that tells which input is wrong
                result = InputResult.WRONG;
            }
        }
        return result;
    }

    @Override public void onRightAnswer() {
        nextProblem();
        stylePreviousCorrect();
    }

    @Override public void onWrongAnswer() {
        discardOffset = determineDiscardOffset();
        nextProblem();  //TODO this line -> check which Kana is actually wrong
        stylePreviousWrong();
    }

    private int determineDiscardOffset() {
        int result = 0;
        for(int i = CURRENT_KANA; i >= 0 && result < CURRENT_KANA; i--, result++)
            if(!input.startsWith(getAnswerLog().get(i).getRomanji()))
                break;
        return result;
    }

    @Override protected void initFields() {
    }

    @Override protected Region initGUI() {
        /* Question Area */
        question = new Label("Write the Romaji for…");
        question.setStyle("-fx-font: 36 arial;");
        question.setTextAlignment(TextAlignment.CENTER);

        previousProblem = new Label();
        previousProblem.setStyle("-fx-font: 50 arial;");
        previousProblem.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        previousProblem.setTextAlignment(TextAlignment.CENTER);
        previousProblem.setAlignment(Pos.CENTER);

        previousRomaji = new Label();
        previousRomaji.setStyle("-fx-font: 24 arial;");
        previousRomaji.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        previousRomaji.setTextAlignment(TextAlignment.CENTER);
        previousRomaji.setAlignment(Pos.CENTER);

        currentProblem = new Label();
        currentProblem.setStyle("-fx-font: 70 arial;");
        currentProblem.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        currentProblem.setTextAlignment(TextAlignment.CENTER);
        currentProblem.setAlignment(Pos.CENTER);

        upcomingProblem = new Label();
        upcomingProblem.setStyle("-fx-font: 50 arial;");
        upcomingProblem.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        upcomingProblem.setTextAlignment(TextAlignment.CENTER);
        upcomingProblem.setAlignment(Pos.CENTER);

        GridPane problemBar = new GridPane();
        GridPane.setHgrow(previousProblem, Priority.ALWAYS);
        GridPane.setHgrow(previousRomaji, Priority.ALWAYS);
        GridPane.setHgrow(currentProblem, Priority.ALWAYS);
        GridPane.setHgrow(upcomingProblem, Priority.ALWAYS);
        GridPane.setConstraints(previousProblem, 0, 0);
        GridPane.setConstraints(previousRomaji, 0, 1);
        GridPane.setConstraints(currentProblem, 1, 0);
        GridPane.setConstraints(upcomingProblem, 2, 0);
        problemBar.getChildren().addAll(previousProblem, previousRomaji, currentProblem, upcomingProblem);
        problemBar.setAlignment(Pos.CENTER);
        questionArea = new VBox();
        questionArea.setAlignment(Pos.CENTER);
        questionArea.getChildren().addAll(question, problemBar);

        /* Answer Area */
        answerInput = new TextField();
        answerInput.setAlignment(Pos.CENTER);
        answerInput.setOnKeyPressed( keyEvent -> handleInput(keyEvent) ); //IntelliJ will report that this can be replaced with a method reference - it can't, don't do it
        HBox.setHgrow(answerInput, Priority.ALWAYS);

        answerArea = new HBox();
        answerArea.setAlignment(Pos.CENTER);
        answerArea.setStyle("-fx-background-color: green; -fx-spacing: 10");
        answerArea.setPadding(new Insets(10.0f, 10.0f, 10.f, 10.f));
        answerArea.getChildren().addAll(answerInput);

        layout = KanaGui.makeRegionSuitable(new BorderPane());
        layout.setTop(questionArea);
        layout.setCenter(answerArea);

        return layout;
    }

    @Override protected void prepareFirstProblem() {
        T dummy = super.randomAnswerFromOptions(getRecentlyUsedOptions());
        getAnswerLog().add(dummy);   //we add the 1st Kana 2 times, because 1 will be a dummy
        getAnswerLog().add(dummy);   //not doing this would increase the number of Kanas to be excluded
        prepareNextProblem();   //we only exclude Kana that we already have tested
        stylePreviousInvisible();
    }

    @Override protected void prepareNextProblem() {
        answerInput.clear();
        //+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~
        if(discardOffset != 0) {
            T temp = getAnswerLog().get(CURRENT_KANA-discardOffset);
            getAnswerLog().remove(CURRENT_KANA-discardOffset);
            getAnswerLog().add(CURRENT_KANA, temp);
        }
        //+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~
        getAnswerLog().add(0, randomAnswerFromOptions(getRecentlyUsedOptions()));
        //0 = next kana; 1 = current kana; 2 = previous kana
        previousProblem.setText("" + getAnswerLog().get(2).getKana());
        previousRomaji.setText(getAnswerLog().get(2).getRomanji());
        currentProblem.setText("" + getAnswerLog().get(1).getKana());
        upcomingProblem.setText("" + getAnswerLog().get(0).getKana());
    }

    @Override protected int getAmount() {
        return 3;
    }

    //when we have less than 4 items
    //the reason why we count to 4 and not 3 is because
    //otherwise we might instantly get the newly discarded one back into the queue
    //while stochastically unlikely, this could end in a prolonged phase of only seeing 3 Kana
    //in the same sequence over and over again
    //-> [a-b-c], discard c, draw c
    //-> [c-a-b], discard b, draw b
    //-> [b-c-a], discard a, draw a
    //-> [a-b-c], discard c, draw c
    // unique combinations: abc
    //... ad infinitum...
    //the worst case for size = 4 would be something like that
    //where d = dummy / the invisible/unshown kana
    //-> [a-b-c]-d, discard d, draw d
    //-> [d-a-b]-c, discard c, draw c
    //-> [c-d-a]-b, discard b, draw b
    //-> [b-c-d]-a, discard a, draw a
    //-> [a-b-c]-d, discard d, draw d
    // unique combinations: abc, abd, acd, bcd;
    //... ad infinitum...
    @Override protected int getRelevantLogDepth() {
        return getAmount()+1;
    }

    private void stylePreviousInvisible() {
        previousProblem.setStyle("-fx-font: 50 arial; -fx-text-fill: white;");
        previousRomaji.setStyle("-fx-font: 24 arial;-fx-text-fill: white;");
    }

    private void stylePreviousCorrect() {
        previousProblem.setStyle("-fx-font: 50 arial; -fx-text-fill: black;");
        previousRomaji.setStyle("-fx-font: 24 arial;-fx-text-fill: white;");
    }

    private void stylePreviousWrong() {
        previousProblem.setStyle("-fx-font: 50 arial; -fx-text-fill: red;");
        previousRomaji.setStyle("-fx-font: 24 arial;-fx-text-fill: red;");
    }
}