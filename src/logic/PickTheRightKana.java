package logic;

import gui.KanaGui;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import kana.Kana;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * @author edotee
 */
public class PickTheRightKana<T extends Kana<T>> extends KanaExercise<T> {

    private BorderPane layout;
    private Label question, inQuestion;
    private VBox questionArea;
    private HBox answerArea;
    private Button[] buttonPool;
    private Scene scene;

    private Random rng;
    private int rightAnswer;
    private ArrayList<T> answerChoices;

    ////////////////////////////////////
    private static final int AMOUNT = 5;

    public PickTheRightKana(HashSet<T> targetKana, EventHandler<ActionEvent> onRightAnswer, EventHandler<ActionEvent> onWrongAnswer, EventHandler<ActionEvent> onComplete) {
        super(targetKana, onRightAnswer, onWrongAnswer, onComplete);
    }

    @Override
    protected Scene initGUI() {
        rng = new Random();
        answerChoices = new ArrayList<>();

        /* Question Area */
        question = new Label("Which Hiragana is…");
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

        /* Buttons */
        buttonPool = new Button[AMOUNT];
        for(int i = 0; i < AMOUNT; i++) {
            buttonPool[i] = new Button();
            buttonPool[i].setStyle("-fx-font: 36 arial; -fx-background-color: lightblue;");
            answerArea.getChildren().add( buttonPool[i] );
        }

        layout = new BorderPane();
        layout.setPrefSize(KanaGui.WIDTH, KanaGui.HEIGHT);
        layout.setTop(questionArea);
        layout.setCenter(answerArea);

        scene = new Scene(layout);

        return scene;
    }

    public void nextProblem() {
        pickAnswers();
        rightAnswer = rng.nextInt(AMOUNT);
        inQuestion.setText("" + answerChoices.get(rightAnswer).getRomanji());
        for(int i = 0; i < AMOUNT; i++)
            updateButton(buttonPool[i], answerChoices.get(i));
    }

    //helper methods

    private void updateButton(Button button, T myKana) {
        button.setText("" + myKana.getKana());
        if(myKana == answerChoices.get(rightAnswer))
            button.setOnAction(getOnRightAnswer());
        else
            button.setOnAction(getOnWrongAnswer());
    }

    private void pickAnswers() {
        if (getTargetKana().size() < AMOUNT)
            return;
        //turn into arrayList, so we can use random to fish out an option
        ArrayList<T> listOfOptions = new ArrayList<>(getTargetKana());
        HashSet<T> validater = new HashSet<>();
        while(validater.size() < AMOUNT) {
            validater.add(listOfOptions.get(rng.nextInt(listOfOptions.size())));
        }
        answerChoices.clear();
        answerChoices.addAll(validater);
    }
}