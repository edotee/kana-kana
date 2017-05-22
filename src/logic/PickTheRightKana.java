package logic;

import gui.KanaGui;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import kana.Kana;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author edotee
 */
public class PickTheRightKana<T extends Kana<T>> extends KanaExercise<T> {

    private BorderPane layout;
    private Label question, inQuestion;
    private VBox questionArea;
    private HBox answerArea;
    private Button[] buttonPool;

    private int rightAnswer;

    public PickTheRightKana(ArrayList<T> completeList, HashSet<T> targetKana,
                            EventHandler<ActionEvent> onRightAnswer,
                            EventHandler<ActionEvent> onWrongAnswer,
                            EventHandler<ActionEvent> onComplete) {
        super(completeList, targetKana, onRightAnswer, onWrongAnswer, onComplete);
    }

    @Override public void onRightAnswer() {
        //TODO
    }

    @Override public void onWrongAnswer() {
        //TODO
    }

    @Override protected void initFields() { }

    @Override protected Region initGUI() {
        /* Question Area */
        question = new Label("Which Kana is…");
        question.setStyle("-fx-font: 36 arial;");
        question.setTextAlignment(TextAlignment.CENTER);
        inQuestion = new Label();
        inQuestion.setStyle("-fx-font: 70 arial;");
        inQuestion.setTextAlignment(TextAlignment.CENTER);
        questionArea = new VBox();
        questionArea.setAlignment(Pos.CENTER);
        questionArea.getChildren().addAll(question, inQuestion);

        /* Answer Area */
        answerArea = new HBox();
        answerArea.setAlignment(Pos.CENTER);
        answerArea.setStyle("-fx-background-color: green; -fx-spacing: 10");
        buttonPool = new Button[getAmount()];
        for(int i = 0; i < getAmount(); i++) {
            buttonPool[i] = new Button();
            buttonPool[i].setStyle("-fx-font: 36 arial; -fx-background-color: lightblue;");
            answerArea.getChildren().add( buttonPool[i] );
        }

        layout = KanaGui.makeRegionSuitable(new BorderPane());
        layout.setTop(questionArea);
        layout.setCenter(answerArea);

        return layout;
    }

    @Override protected void prepareFirstProblem() {
        prepareNextProblem();
    }

    @Override protected void prepareNextProblem() {
        rightAnswer = dice(getAmount());
        getAnswerLog().addAll(getCurrentAnswerOptions());
        while(getAnswerLog().size() > 10)
            getAnswerLog().remove(10);

        inQuestion.setText("" + getAnswerLog().get(rightAnswer).getRomanji());
        for(int myKana = 0; myKana < getAmount(); myKana++) {
            buttonPool[myKana].setText("" + getCurrentAnswerOptions().get(myKana).getKana());
            buttonPool[myKana].setOnAction(
                getCurrentAnswerOptions().get(myKana) == getAnswerLog().get(rightAnswer) ?
                getOnRightAnswer() : getOnWrongAnswer()
            );
        }
        /*
        rightAnswer = randomAnswerFromOptions();
        inQuestion.setText("" + rightAnswer.getRomanji());
        for(int myKana = 0; myKana < getAmount(); myKana++) {
            buttonPool[myKana].setText("" + getCurrentAnswerOptions().get(myKana).getKana());
            buttonPool[myKana].setOnAction(
                getCurrentAnswerOptions().get(myKana) == rightAnswer ?
                getOnRightAnswer() : getOnWrongAnswer()
            );
        }
         */
    }

    @Override protected int getAmount() {
        return 5;
    }

    @Override protected int getRelevantLogDepth() {
        return 3;
    }
}