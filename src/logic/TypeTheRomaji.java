package logic;

import gui.KanaGui;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import kana.Kana;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author edotee
 */
public class TypeTheRomaji<T extends Kana<T>> extends KanaExercise<T> {

    private BorderPane layout;
    private Label question, currentProblem, previousProblem, upcomingProblem;
    private VBox questionArea;
    private TextField answerInput;

    private ArrayList<T> answerLog;
    private Button answerCheckButton;
    private HBox answerArea;

    public TypeTheRomaji(HashSet<T> targetKana,
                         EventHandler<ActionEvent> onRightAnswer,
                         EventHandler<ActionEvent> onWrongAnswer,
                         EventHandler<ActionEvent> onSkip,
                         EventHandler<ActionEvent> onComplete) {
        super(targetKana, onRightAnswer, onWrongAnswer, onSkip, onComplete);
    }

    @Override protected void initFields() {
        answerLog = new ArrayList<>();
    }

    @Override protected Region initGUI() {
        /* Question Area */
        question = new Label("Write the Romaji for…");
        question.setStyle("-fx-font: 36 arial;");
        question.setTextAlignment(TextAlignment.CENTER);
        previousProblem = new Label();
        previousProblem.setStyle("-fx-font: 70 arial;");
        previousProblem.setTextAlignment(TextAlignment.CENTER);
        currentProblem = new Label();
        currentProblem.setStyle("-fx-font: 70 arial;");
        currentProblem.setTextAlignment(TextAlignment.CENTER);
        upcomingProblem = new Label();
        upcomingProblem.setStyle("-fx-font: 70 arial;");
        upcomingProblem.setTextAlignment(TextAlignment.CENTER);
        HBox problemBar = new HBox();
        problemBar.setAlignment(Pos.CENTER);
        problemBar.getChildren().addAll(previousProblem, currentProblem, upcomingProblem);
        questionArea = new VBox();
        questionArea.setAlignment(Pos.CENTER);
        questionArea.getChildren().addAll(question, problemBar);

        /* Answer Area */
        answerInput = new TextField();
        answerInput.setPromptText("please enter the answerLog here");
        answerInput.setAlignment(Pos.CENTER);
        answerInput.setOnKeyPressed( keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                if(answerLog.get(1).getRomanji().equals(answerInput.getText().toLowerCase().trim()))
                    getOnRightAnswer().handle(null);
                else
                    getOnWrongAnswer().handle(null);
            }

        });
        HBox.setHgrow(answerInput, Priority.ALWAYS);

        answerCheckButton = new Button("Check!");
        answerCheckButton.setOnAction( actionEvent -> {
            if(answerLog.get(1).getRomanji().equals(answerInput.getText().toLowerCase().trim()))
                getOnRightAnswer().handle(actionEvent);
            else
                getOnWrongAnswer().handle(actionEvent);
        });
        answerArea = new HBox();
        answerArea.setAlignment(Pos.CENTER);
        answerArea.setStyle("-fx-background-color: green; -fx-spacing: 10");
        answerArea.setPadding(new Insets(10.0f, 10.0f, 10.f, 10.f));
        answerArea.getChildren().addAll(answerInput, answerCheckButton);

        layout = KanaGui.makeRegionSuitable(new BorderPane());
        layout.setTop(questionArea);
        layout.setCenter(answerArea);

        return layout;
    }

    @Override protected void prepareFirstProblem() {
        T dummy = super.randomAnswer(answerLog);
        answerLog.add(dummy);   //we add the 1st Kana 2 times, because 1 will be a dummy
        answerLog.add(dummy);   //not doing this would increase the number of Kanas to be excluded
        prepareNextProblem();   //we only exclude Kana that we already have tested
        //TODO don't set text change style, so it's invisible
        previousProblem.setText("");
    }

    @Override protected void prepareNextProblem() {
        //0 = next kana; 1 = current kana; 2 = previous kana
        answerLog.add(0, super.randomAnswer(answerLog));
        //just in case that for whatever reason there should be more than 4 items in the list
        //we would had to check if(same statement) anyway for the first runs
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
        while(answerLog.size() > 4) {
            answerLog.remove(answerLog.size() - 1);
        }
        //TODO for previousProblem: setStyle depending on a boolean field answeredCorrectly
        //true = romaji text color = same text color as background; border = normal
        //false = romaji text color = something visible; border = visibly wrong
        previousProblem.setText("" + answerLog.get(2).getKana() + "\n" + answerLog.get(2).getRomanji());
        currentProblem.setText("" + answerLog.get(1).getKana());
        upcomingProblem.setText("" + answerLog.get(0).getKana());
        answerInput.clear();
    }
}