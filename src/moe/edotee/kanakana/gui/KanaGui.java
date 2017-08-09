package moe.edotee.kanakana.gui;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import moe.edotee.kanakana.gui.elements.ExercisePicker;
import moe.edotee.kanakana.gui.elements.KanaPicker;
import moe.edotee.kanakana.kana.Kana.Hiragana;
import moe.edotee.kanakana.kana.Kana.Katakana;
import moe.edotee.kanakana.logic.KanaExercise;
import moe.edotee.kanakana.utils.CSS;
import moe.edotee.kanakana.utils.Options;

import java.util.*;

/**
 * @author edotee
 */
public class KanaGui extends Application {

    private Stage mainWindow;
    private Scene scene;
    private BorderPane exerciseLayout, startLayout;

    private ExercisePicker exercisePicker;
    private KanaPicker<Hiragana> hiraganaPicker;
    private KanaPicker<Katakana> katakanaPicker;

    private HashSet<Hiragana> pickedHiragana;  //pressing the start button | -> fetchKana
    private HashSet<Katakana> pickedKatakana;  //pressing the start button | -> fetchKana
    private Iterator<KanaExercise> exercises;       //pressing the start button | -> initExercise
    private KanaExercise currentExercise;           //pressing the start button | -> advanceToNextProblem

    public KanaGui() { }

    @Override public void start(Stage stage) throws Exception {
        mainWindow = stage;
        mainWindow.setTitle(Options.TITLE);
        mainWindow.setMinWidth(Options.WIDTH);
        mainWindow.setMinHeight(Options.HEIGHT);
        initScene();
        mainWindow.show();
    }


    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    /* GUI creation */
    private void initScene() {
        startLayout = createStartLayout();
        exerciseLayout = createExerciseLayout();
        scene = new Scene(startLayout);
        mainWindow.setScene(scene);
        scene.getStylesheets().add(Options.CSS.Main);
    }
    private BorderPane createExerciseLayout() {
        BorderPane result = new BorderPane();
        result.setPrefSize(Options.WIDTH, Options.HEIGHT);
        result.setBottom(createButtonBarExercise());
        return result;
    }
    private BorderPane createStartLayout() {
        exercisePicker = new ExercisePicker();
        hiraganaPicker = KanaPicker.create(Hiragana.A);    //intelliJ will tell you that they are uncheck…
        katakanaPicker = KanaPicker.create(Katakana.A);    //… they aren't.

        TabPane centerNode = new TabPane();
        centerNode.setPrefSize(Options.WIDTH, Options.HEIGHT);
        centerNode.getTabs().addAll(exercisePicker, hiraganaPicker, katakanaPicker);

        BorderPane result = new BorderPane();
        result.setPrefSize(Options.WIDTH, Options.HEIGHT);
        result.setCenter(centerNode);
        result.setBottom(createButtonBarStartScreen());
        return result;
    }
    private Node createButtonBarStartScreen() {
        Button startButton = new Button("start");
        startButton.setOnAction(e -> startExercises());
        HBox.getHgrow(startButton);

        HBox bottomNode = new HBox();
        bottomNode.getChildren().add(startButton);
        CSS.style(bottomNode, CSS.main.buttonBar);
        return bottomNode;
    }
    private Node createButtonBarExercise() {
        Button goToNextProblem, goToNextExercise, exitExercise;
        goToNextProblem = new Button("Next Problem");
        goToNextProblem.setFocusTraversable(false);
        goToNextProblem.setOnAction( e -> currentExercise.nextProblem() );
        goToNextExercise = new Button("Next Exercise");
        goToNextExercise.setFocusTraversable(false);
        goToNextExercise.setOnAction( e -> advanceToNextExercise() );
        exitExercise = new Button("Exit Exercise");
        exitExercise.setFocusTraversable(false);
        exitExercise.setOnAction( e -> scene.setRoot(startLayout) );

        HBox buttonBox = new HBox();
        buttonBox.getChildren().addAll(goToNextProblem, goToNextExercise, exitExercise);
        CSS.style(buttonBox, CSS.main.buttonBar);
        return  buttonBox;
    }


    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    /* reactive methods */
    private void startExercises() {
        pickedHiragana = hiraganaPicker.fetchChoices();
        pickedKatakana = katakanaPicker.fetchChoices();
        String errorMessage = "";
        if(pickedHiragana.size() < Options.MINIMUM_KANA)
            errorMessage += "Please pick at least " + Options.MINIMUM_KANA + " Hiragana.";
        if(pickedKatakana.size() < Options.MINIMUM_KANA) {
            if(errorMessage.length() > 0)
                errorMessage += "\n";
            errorMessage += "Please pick at least " + Options.MINIMUM_KANA + " Katakana.";
        }
        if(errorMessage.length() > 0) {
            System.out.println(errorMessage + "\n");
        } else {
            exercises = exercisePicker.getExercises(pickedHiragana, pickedKatakana).iterator();
            advanceToNextExercise();
        }
    }   // called by the [startButton]
    private void advanceToNextExercise() {
        if(exercises.hasNext()) {
            currentExercise = exercises.next();
            exerciseLayout.setCenter(currentExercise.getGui());
            scene.setRoot(exerciseLayout);
        } else {
            System.out.println("No exercise left.");
            scene.setRoot(startLayout);
        }
    }   // called by startExercises() & the [next] button


    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    /* main */
    public static void main(String[] args) { launch(args); }
}