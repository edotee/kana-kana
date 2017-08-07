package logic;

import gui.KanaGui;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import kana.Kana;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author edotee
 */
public class WriteTheRightKana<T extends Kana<T>> extends KanaExercise<T> {

    private final String CSS_FILE_PATH = "gui/css/write_the_right_kana.css";

    private BorderPane layout;
    private Label question;
    private VBox questionArea;
    private GridPane answerArea;
    private Button[] buttonPool;
    private Label[] labelPool;

    public WriteTheRightKana(ArrayList<T> completeList, HashSet<T> targetKana,
                            EventHandler<ActionEvent> onComplete) {
        super(completeList, targetKana, onComplete);
    }

    @Override protected void initFields() {
        question = new Label();
        questionArea = new VBox();
        answerArea = new GridPane();
        buttonPool = new Button[getAmount()];
        labelPool = new Label[getAmount()];
        layout = new BorderPane();
    }

    @Override protected Region initGUI() {
        /* Question Area */
        question.setText("Write the Kana for…");
        questionArea.getChildren().addAll(question);

        /* Answer Area */
        populateLabelPool();

        layout = KanaGui.makeRegionSuitable(layout);
        layout.setTop(questionArea);
        layout.setCenter(answerArea);

        return layout;
    }

    private void populateLabelPool() {
        for(int i = 0; i < getAmount(); i++) {
            Button button = new Button(); Label label = new Label();
            button.setOnAction( e -> setCssClass(label, "labelPoolLabel") );
            GridPane.setConstraints(button, i, 0);
            GridPane.setConstraints(label, i, 1);

            label.setPrefSize(125.0f, 125.0f);//adhoc solution
            button.setPrefSize(125.0f, 125.0f);//adhoc solution

            /* binding */
            label.prefWidthProperty().bind(button.widthProperty());
            //label.prefHeightProperty().bind(button.heightProperty()); //this binding is actually causing size problems…

            /* adding the button and labels */
            buttonPool[i] = button; labelPool[i] = label;
            answerArea.getChildren().addAll( button, label );

            /* styling */
            setCssClass(button, "buttonPoolButton");
        }
    }

    @Override protected void applyCSS() {
        layout.getStylesheets().add(CSS_FILE_PATH);
        setCssClass(answerArea, "answerArea");
        setCssClass(question, "question");
        setCssClass(questionArea, "questionArea");
    }

    @Override protected void prepareFirstProblem() {
        prepareNextProblem();
    }

    @Override protected void prepareNextProblem() {
        for(int i = 0; i < getAmount(); i++) {
            buttonPool[i].setText("" + getCurrentAnswerOptions().get(i).getRomanji());
            labelPool[i].setText("" + getCurrentAnswerOptions().get(i).getKana());
            setCssClass(labelPool[i], "labelPoolLabelHide");
        }
        getAnswerLog().addAll(0, getCurrentAnswerOptions());
    }

    @Override protected int getAmount() {
        return 5;
    }

    @Override protected int getRelevantLogDepth() {
        return getAmount()*2;
    }
}