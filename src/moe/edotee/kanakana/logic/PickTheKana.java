package moe.edotee.kanakana.logic;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import moe.edotee.kanakana.kana.Kana;
import moe.edotee.kanakana.utils.Options;

import java.util.HashSet;

/**
 * @author edotee
 */
public class PickTheKana<T extends Kana> extends KanaExercise<T> {

    private BorderPane layout;
    private Label question, inQuestion;
    private VBox questionArea;
    private HBox answerArea;
    private Button[] buttonPool;

    private int rightAnswer;

    public PickTheKana(HashSet<T> targetKana, String cssPath) {
        super(targetKana, cssPath);
    }

    private void onCorrect() {
        //TODO
        System.out.println("Correct");
        nextProblem();
    }

    private void onWrong() {
        //TODO
        System.out.println("Wrong");
        nextProblem();
    }

    @Override protected void initFields() {
        question = new Label();
        inQuestion = new Label();
        questionArea = new VBox();
        answerArea = new HBox();
        buttonPool = new Button[getAmount()];
        layout = new BorderPane();
    }

    @Override protected Region initGUI() {
        /* Question Area */
        question.setText("Pick the right kana!");
        questionArea.getChildren().addAll(question, inQuestion);

        /* Answer Area */
        populateButtonPool();

        /* Layout */
        layout.setPrefSize(Options.WIDTH, Options.HEIGHT);
        layout.setOnKeyPressed(ke -> handleKeyboardInput(ke));  // don't listen to IntelliJ - handleKeyboardInput() isn't static -> no method reference
        layout.setTop(questionArea);
        layout.setCenter(answerArea);

        return layout;
    }

    private void populateButtonPool() {
        for(int i = 0; i < getAmount(); i++) {
            buttonPool[i] = new Button();
            answerArea.getChildren().add( buttonPool[i] );
            setCssClass(buttonPool[i], "buttonPoolButton");
        }
    }

    @Override protected void applyCSS(String css_file_path) {
        layout.getStylesheets().add(css_file_path);
        setCssClass(question, "question");
        setCssClass(inQuestion, "inQuestion");
        setCssClass(questionArea, "questionArea");
        setCssClass(answerArea, "answerArea");
    }

    private void handleKeyboardInput(KeyEvent ke) {
        switch(ke.getCode()) {
            case DIGIT1: case NUMPAD1: buttonPool[0].fire();
                break;
            case DIGIT2: case NUMPAD2: buttonPool[1].fire();
                break;
            case DIGIT3: case NUMPAD3: buttonPool[2].fire();
                break;
            case DIGIT4: case NUMPAD4: buttonPool[3].fire();
                break;
            case DIGIT5: case NUMPAD5: buttonPool[4].fire();
                break;
        }
        /*
        switch(ke.getCode()) {
            case LEFT: case NUMPAD4:    //left
                buttonPool[0].fire();
                break;
            case NUMPAD5:               //middle
                buttonPool[1].fire();
                break;
            case RIGHT: case NUMPAD6:   //right
                buttonPool[2].fire();
                break;
            case UP: case NUMPAD8:      //up
                buttonPool[3].fire();
                break;
            case DOWN: case NUMPAD2:    //down
                buttonPool[4].fire();
                break;
        }
        */
    }

    @Override protected void prepareFirstProblem() {
        prepareNextProblem();
    }

    @Override protected void prepareNextProblem() {
        rightAnswer = dice(getAmount());
        getAnswerLog().addAll(0, getCurrentAnswerOptions());
        while(getAnswerLog().size() > 10)
            getAnswerLog().remove(10);

        inQuestion.setText("" + getAnswerLog().get(rightAnswer).getRomaji());
        for(int myKana = 0; myKana < getAmount(); myKana++) {
            buttonPool[myKana].setText("" + getAnswerLog().get(myKana).getKana());
            buttonPool[myKana].setOnAction( (myKana == rightAnswer)? e -> onCorrect() : e -> onWrong() );
            //getCurrentAnswerOptions().get(myKana) == getAnswerLog().get(rightAnswer) ?
        }
    }

    @Override public void onComplete() {

    }

    @Override protected int getAmount() {
        return Options.PickKana.AMOUNT;
    }

    @Override protected int getRelevantLogDepth() {
        return Options.PickKana.DEPTH;
    }
}