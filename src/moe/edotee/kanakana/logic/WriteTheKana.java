package moe.edotee.kanakana.logic;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import moe.edotee.kanakana.kana.Kana;
import moe.edotee.kanakana.utils.Options;

import java.util.HashSet;

/**
 * @author edotee
 */
public class WriteTheKana<T extends Kana> extends KanaExercise<T> {

    private BorderPane layout;
    private Label question;
    private VBox questionArea;
    private GridPane answerArea;
    private Button[] buttonPool;
    private Label[] labelPool;

    public WriteTheKana(HashSet<T> targetKana, String cssPath) {
        super(targetKana, cssPath);
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

        layout.setPrefSize(Options.WIDTH, Options.HEIGHT);
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

    @Override protected void applyCSS(String css_file_path) {
        layout.getStylesheets().add(css_file_path);
        setCssClass(answerArea, "answerArea");
        setCssClass(question, "question");
        setCssClass(questionArea, "questionArea");
    }

    @Override protected void prepareFirstProblem() {
        prepareNextProblem();
    }

    @Override protected void prepareNextProblem() {
        for(int i = 0; i < getAmount(); i++) {
            buttonPool[i].setText("" + getCurrentAnswerOptions().get(i).getRomaji());
            labelPool[i].setText("" + getCurrentAnswerOptions().get(i).getKana());
            setCssClass(labelPool[i], "labelPoolLabelHide");
        }
        getAnswerLog().addAll(0, getCurrentAnswerOptions());
    }

    @Override public void onComplete() {

    }

    @Override protected int getAmount() {
        return Options.WriteKana.AMOUNT;
    }

    @Override protected int getRelevantLogDepth() {
        return Options.WriteKana.DEPTH;
    }
}