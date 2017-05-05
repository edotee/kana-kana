package gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import kana.hiragana.Hiragana;

import java.util.*;

/**
 * @author edotee
 */
public class KanaGui extends Application {

    private final static double WIDTH = 854.0f;
    private final static double HEIGHT = 480.0f;

    private Stage mainWindow;

    @Override
    public void start(Stage stage) throws Exception {
        mainWindow = stage;
        mainWindow.setTitle("Kana Test");
        mainWindow.setMinWidth(WIDTH);
        mainWindow.setMinHeight(HEIGHT);
        /*
            TODO change into mainWindow.setScene(initScene())
            TODO initScene() will call createProblem if needed
         */
        mainWindow.setScene(createProblem());
        mainWindow.sizeToScene();
        mainWindow.show();
    }

    /*
        TODO pretty sure, that replacing scenes is suboptimal for us
        TODO gonna change it so it returns a Node which we will set into the scene
     */
    private Scene createProblem() {

        Random rng = new Random();

        Hiragana[] chosenAnswers = pickAnswers(5, generateListOfPotentialAnswers());
        Hiragana rightAnswer = chosenAnswers[rng.nextInt(chosenAnswers.length)];

        BorderPane layout = new BorderPane();
        layout.setPrefSize(WIDTH, HEIGHT);
        layout.setTop(createQuestionArea(rightAnswer));
        layout.setCenter(createAnswerArea(chosenAnswers, rightAnswer));

        return new Scene(layout);
    }

    //TODO rewrite into foo(String question, Hirgana rightAnswer)
    private Node createQuestionArea(Hiragana rightAnswer) {
        Label question = new Label("Which Hiragana is…");
        question.setStyle("-fx-font: 36 arial;");
        question.setTextAlignment(TextAlignment.CENTER);

        Label inQuestion = new Label("" + rightAnswer.getRomanji());
        inQuestion.setStyle("-fx-font: 70 arial;");
        inQuestion.setTextAlignment(TextAlignment.CENTER);

        VBox questionArea = new VBox();
        questionArea.setAlignment(Pos.CENTER);
        questionArea.getChildren().addAll(question, inQuestion);
        return questionArea;
    }

    private Node createAnswerArea(Hiragana[] answerOptions, Hiragana rightAnswer) {
        HBox answerArea = new HBox();
        answerArea.setAlignment(Pos.CENTER);
        answerArea.setStyle("-fx-background-color: green; -fx-spacing: 10");
        for(Hiragana option : answerOptions)
            answerArea.getChildren().add(smartButton(option, rightAnswer));
        return answerArea;
    }

    private Button smartButton(Hiragana myKana, Hiragana rightAnswer) {
        Button button = new Button("" + myKana.getKana());
        button.setStyle("-fx-font: 36 arial; -fx-background-color: lightblue;");
        if(myKana == rightAnswer)
            button.setOnAction(e -> nextProblem());
        else //TODO do something on wrong choice
            ;
        return button;
    }

    private void nextProblem() {
        mainWindow.setScene(createProblem());
    }

    private HashSet<Hiragana> generateListOfPotentialAnswers() {
        Random rng = new Random();

        HashSet<Hiragana> potentialAnswers = new HashSet<>();
        Collections.addAll(potentialAnswers, Hiragana.basicsByVowel()[0]);

        Hiragana[] temp = Hiragana.dakutenValues();
        while (potentialAnswers.size() < 15) {
            potentialAnswers.add(temp[rng.nextInt(temp.length)]);
        }

        return potentialAnswers;
    }

    private Hiragana[] pickAnswers(int amount, HashSet<Hiragana> setOfOptions) {
        if(setOfOptions.size() < amount)
            return new Hiragana[0];

        Random rng = new Random();
        ArrayList<Hiragana> listOfOptions = new ArrayList<>(setOfOptions);
        HashSet<Hiragana> optionSet = new HashSet<>();
        while (optionSet.size() < amount)
            optionSet.add(listOfOptions.get(rng.nextInt(listOfOptions.size())));

        ArrayList<Hiragana> optionList = new ArrayList<>(optionSet);
        Hiragana[] answers = new Hiragana[optionList.size()];
        for(int i = 0; i < optionList.size(); i++)
            answers[i] = optionList.get(i);

        return answers;
    }

    public static void main(String[] args) {
        launch(args);
    }
}