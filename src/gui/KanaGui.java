package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kana.hiragana.Hiragana;
import logic.KanaExercise;
import logic.PickTheRightKana;

import java.util.*;

/**
 * @author edotee
 */
public class KanaGui extends Application {

    public final static String TITLE = "Kana Test";
    public final static double WIDTH = 854.0f;
    public final static double HEIGHT = 480.0f;

    private Stage mainWindow;

    private KanaExercise exercise;

    @Override
    public void start(Stage stage) throws Exception {
        mainWindow = stage;
        mainWindow.setTitle(TITLE);
        mainWindow.setMinWidth(WIDTH);
        mainWindow.setMinHeight(HEIGHT);

        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        exercise = new PickTheRightKana(generateListOfPotentialAnswers(), e -> advanceToNextProblem(), null, e -> advanceToNextExercise());

        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        mainWindow.setScene(initScene());
        mainWindow.sizeToScene();
        mainWindow.show();
    }

    private Scene initScene() {
        //TODO temporarely only
        return exercise.getGUI();
    }

    /**
     * TODO don't setScene()
     */
    public void advanceToNextExercise() { mainWindow.setScene(exercise.getGUI());}


    /**
     * TODO don't setScene() - work it out so it only calls exercise.nextProblem()
     */
    public void advanceToNextProblem() {
        exercise.nextProblem();
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

    public static void main(String[] args) {
        launch(args);
    }
}