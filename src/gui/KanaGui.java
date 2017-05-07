package gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import kana.hiragana.Hiragana;
import kana.katakana.Katakana;
import logic.KanaExercise;
import logic.PickTheRightKana;

import java.util.*;

/**
 * @author edotee
 */
public class KanaGui extends Application {

    private final static String TITLE = "Kana Test";
    private final static double WIDTH = 854.0f;
    private final static double HEIGHT = 480.0f;

    private Stage mainWindow;
    private BorderPane startLayout;

    private HashMap<ToggleButton, Hiragana> hiraganaIndex;
    private HashMap<ToggleButton, Katakana> katakanaIndex;

    private Iterator<KanaExercise> exercises;
    private KanaExercise currentExercise;


    @Override public void start(Stage stage) throws Exception {
        mainWindow = stage;
        mainWindow.setTitle(TITLE);
        mainWindow.setMinWidth(WIDTH);
        mainWindow.setMinHeight(HEIGHT);
        initScene();
        mainWindow.show();

    }

    private void initScene() {
        hiraganaIndex = new HashMap<>();
        katakanaIndex = new HashMap<>();
        TabPane centerNode = KanaPicker.getTabbedKanaPicker(hiraganaIndex, katakanaIndex);

        //+++++++++++++++++++++++++++++++++++++++++++++++++
        Button button = new Button("start");
        button.setOnAction(e -> {
            initExercises();
            advanceToNextExercise();
        });
        HBox.getHgrow(button);
        HBox bottomNode = new HBox();
        bottomNode.getChildren().add(button);
        bottomNode.setAlignment(Pos.CENTER);
        //+++++++++++++++++++++++++++++++++++++++++++++++++

        startLayout = makeRegionSuitable(new BorderPane());
        startLayout.setCenter(centerNode);
        startLayout.setBottom(bottomNode);
        mainWindow.setScene(new Scene(startLayout));
    }

    private void initExercises() {
        ArrayList<KanaExercise> exerciseList = new ArrayList<>();
        exerciseList.add( new PickTheRightKana<>(
                generateHiraganaList(),             //Kanas to test
                e -> advanceToNextProblem(),        //perform this action on right answer
                e -> System.out.println("Idiot!"),  //... wrong answer
                e -> advanceToNextProblem(),        //... skipping a problem
                e -> advanceToNextExercise()        //.. on completing an exercise
        ));
        exerciseList.add( new PickTheRightKana<>(
                generateKatakanaList(),
                e -> advanceToNextProblem(),
                e -> System.out.println("あほ! Aho! ばか! Baka!"),
                e -> advanceToNextProblem(),
                e -> advanceToNextExercise()
        ));
        exercises = exerciseList.iterator();
    }

    private void advanceToNextExercise() {
        if(exercises.hasNext()) {
            currentExercise = exercises.next();
            mainWindow.getScene().setRoot(currentExercise.getGui());
        } else {
            mainWindow.getScene().setRoot(startLayout);
        }
    }

    private void advanceToNextProblem() {
        currentExercise.nextProblem();
    }

    //Helper Methods

    public static <T extends Region> T makeRegionSuitable(T region) {
        region.setPrefSize(WIDTH, HEIGHT);
        return region;
    }

    private HashSet<Katakana> generateKatakanaList() {
        HashSet<Katakana> result = new HashSet<>();
        Set<ToggleButton> keys = katakanaIndex.keySet();
        for(ToggleButton key : keys)
            if(key.isSelected())
                result.add(katakanaIndex.get(key));
        return result;
    }
    private HashSet<Hiragana> generateHiraganaList() {
        HashSet<Hiragana> result = new HashSet<>();
        Set<ToggleButton> keys = hiraganaIndex.keySet();
        for(ToggleButton key : keys)
            if(key.isSelected())
                result.add(hiraganaIndex.get(key));
        return result;
    }

    public static void main(String[] args) { launch(args); }
}