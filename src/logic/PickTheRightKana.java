package logic;

import gui.KanaGui;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import kana.Kana;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author edotee
 */
public class PickTheRightKana<T extends Kana<T>> extends KanaExercise<T> {

    private final String CSS_FILE_PATH = "gui/css/pick_the_right_romaji.css";

    private BorderPane layout;
    private Label question, inQuestion;
    private VBox questionArea;
    private HBox answerArea;
    private Button[] buttonPool;

    private int rightAnswer;

    public PickTheRightKana(ArrayList<T> completeList, HashSet<T> targetKana,
                            EventHandler<ActionEvent> onComplete) {
        super(completeList, targetKana, onComplete);
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
        layout = KanaGui.makeRegionSuitable(layout);
        layout.setOnKeyPressed(ke -> handleKeyboardInput(ke));
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

    @Override protected void applyCSS() {
        layout.getStylesheets().add(CSS_FILE_PATH);
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

        inQuestion.setText("" + getAnswerLog().get(rightAnswer).getRomanji());
        for(int myKana = 0; myKana < getAmount(); myKana++) {
            buttonPool[myKana].setText("" + getAnswerLog().get(myKana).getKana());
            buttonPool[myKana].setOnAction( (myKana == rightAnswer)? e -> onCorrect() : e -> onWrong() );
            //getCurrentAnswerOptions().get(myKana) == getAnswerLog().get(rightAnswer) ?
        }
    }

    @Override protected int getAmount() {
        return 5;
    }

    @Override protected int getRelevantLogDepth() {
        return 3;
    }
}