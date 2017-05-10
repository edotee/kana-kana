package logic;

import gui.KanaGui;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import kana.Kana;

import java.util.HashSet;

/**
 * @author edotee
 */
public class WriteTheRightKana<T extends Kana<T>> extends KanaExercise<T> {

    private BorderPane layout;
    private Label question;
    private VBox questionArea;
    private GridPane answerArea;
    private Button[] buttonPool;
    private Label[] labelPool;

    public WriteTheRightKana(HashSet<T> targetKana,
                            EventHandler<ActionEvent> onRightAnswer,
                            EventHandler<ActionEvent> onSkip,
                            EventHandler<ActionEvent> onComplete) {
        super(targetKana, onRightAnswer, null, onSkip, onComplete);
    }

    @Override protected void initFields() { }

    @Override protected Region initGUI() {
        /* Question Area */
        question = new Label("Write the Kana for…");
        question.setStyle("-fx-font: 36 arial;");
        question.setTextAlignment(TextAlignment.CENTER);
        questionArea = new VBox();
        questionArea.setAlignment(Pos.CENTER);
        questionArea.getChildren().addAll(question);

        /* Answer Area */
        answerArea = new GridPane();
        answerArea.setAlignment(Pos.CENTER);
        answerArea.setStyle("-fx-background-color: green; -fx-spacing: 10");
        buttonPool = new Button[getAmount()];
        labelPool = new Label[getAmount()];
        for(int i = 0; i < getAmount(); i++) {
            Label label = new Label();
            label.setAlignment(Pos.CENTER);
            label.setStyle("-fx-font: 36 arial; -fx-background-color: lightblue;");
            labelPool[i] = label;
            buttonPool[i] = new Button();

            //+++++++++++++++++++++++++++++++++++++++++++++
            //adhoc solution
            buttonPool[i].setPrefSize(125.0f, 125.0f);
            label.setPrefSize(1250.0f, 125.0f);
            //+++++++++++++++++++++++++++++++++++++++++++++

            buttonPool[i].setStyle("-fx-font: 36 arial; -fx-background-color: lightblue;");
            buttonPool[i].setOnAction( e -> label.setVisible(true) );
            label.prefWidthProperty().bind(buttonPool[i].widthProperty());
            label.prefHeightProperty().bind(buttonPool[i].heightProperty());
            GridPane.setConstraints(buttonPool[i], i, 0);
            GridPane.setConstraints(label, i, 1);
            answerArea.getChildren().addAll( buttonPool[i], label );
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
        for(int i = 0; i < getAmount(); i++) {
            buttonPool[i].setText("" + getAnswerOptions().get(i).getRomanji());
            labelPool[i].setText("" + getAnswerOptions().get(i).getKana());
            labelPool[i].setVisible(false);
        }
    }
}