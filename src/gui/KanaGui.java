package gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import kana.hiragana.Hiragana;
import kana.katakana.Katakana;
import logic.KanaExercise;
import logic.TypeTheRomaji;
import logic.WriteTheRightKana;

import java.util.*;

/**
 * @author edotee
 */
public class KanaGui extends Application {

    private final static String TITLE = "Kana Test";
    private final static double WIDTH = 854.0f;
    private final static double HEIGHT = 480.0f;

    private Stage mainWindow;
    private Scene scene;
    private BorderPane startLayout, exerciseLayout;

    //TODO put refactor the following fields and their affiliated methods into an external controller class
    //The controller will observe KanaGui for changes -> register changes in KanaGui via Button clicks or whatever
    //The controller will pull information from the Gui, initiate / handle the required KanaExercise objects,
    //delegate the pulled information to the exercises, pull data from them and push them back to the Gui
    private HashMap<ToggleButton, Hiragana> hiraganaIndex;
    private HashMap<ToggleButton, Katakana> katakanaIndex;

    private HashSet<Hiragana> pickedHiragana;   //pressing the start button | -> pickKana
    private HashSet<Katakana> pickedKatakana;   //pressing the start button | -> pickKana
    //requires hiraganaIndex & katakanaIndex to be initialized & populized
    private Iterator<KanaExercise> exercises;   //pressing the start button | -> initExercise
    private KanaExercise currentExercise;       //pressing the start button | -> advanceToNextProblem

    public KanaGui() {
        //initializes hiraganaIndex & katakanaIndex
        hiraganaIndex = new HashMap<>();
        katakanaIndex = new HashMap<>();
    }

    @Override public void start(Stage stage) throws Exception {
        mainWindow = stage;
        mainWindow.setTitle(TITLE);
        mainWindow.setMinWidth(WIDTH);
        mainWindow.setMinHeight(HEIGHT);
        initScene();
        mainWindow.show();

    }

    private void initScene() {
        startLayout = makeRegionSuitable(new BorderPane());
        startLayout.setCenter(createCenterNode());
        startLayout.setBottom(createBottomNode());
        scene = new Scene(startLayout);
        mainWindow.setScene(scene);

        Button goToNextProblem, goToNextExercise;
        HBox buttonBox;
        goToNextProblem = new Button("Next Problem");
        goToNextProblem.setOnAction( e -> advanceToNextProblem() );
        goToNextExercise = new Button("Next Exercise");
        goToNextExercise.setOnAction( e -> advanceToNextExercise() );
        buttonBox = new HBox();
        buttonBox.setStyle("-fx-spacing: 10");
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(goToNextProblem, goToNextExercise);
        exerciseLayout = makeRegionSuitable(new BorderPane());
        exerciseLayout.setBottom(buttonBox);
    }

    //TODO rework this so the exercises are dynamically populated
    //requires hiraganaIndex & katakanaIndex to be initialized & populized
    private void initExercises() {
        ArrayList<KanaExercise> exerciseList = new ArrayList<>();
        /*
        exerciseList.add( new PickTheRightKana<>(
                pickedHiragana,             //Kanas to test
                e -> advanceToNextProblem(),        //perform this action on right answer
                e -> System.out.println("Idiot!"),  //... wrong answer
                e -> advanceToNextProblem(),        //... skipping a problem
                e -> advanceToNextExercise()        //.. on completing an exercise
        ));
        exerciseList.add( new PickTheRightKana<>(
                pickedKatakana,
                e -> advanceToNextProblem(),
                e -> System.out.println("あほ! Aho! ばか! Baka!"),
                e -> advanceToNextProblem(),
                e -> advanceToNextExercise()
        ));
        exerciseList.add( new WriteTheRightKana<>(
                pickedHiragana,
                e -> advanceToNextProblem(),
                e -> advanceToNextProblem(),
                e -> advanceToNextExercise()
        ));
        */
        exerciseList.add( new TypeTheRomaji<>(
                pickedHiragana,
                e -> advanceToNextProblem(),
                e -> System.out.println("あほ! Aho! ばか! Baka!"),
                e -> advanceToNextProblem(),
                e -> advanceToNextExercise()
        ));
        exerciseList.add( new WriteTheRightKana<>(
                pickedHiragana,
                e -> advanceToNextProblem(),
                e -> advanceToNextProblem(),
                e -> advanceToNextExercise()
        ));
        exercises = exerciseList.iterator();
    }

    private void advanceToNextExercise() {
        if(exercises.hasNext()) {
            currentExercise = exercises.next();
            exerciseLayout.setCenter(currentExercise.getGui());
            scene.setRoot(exerciseLayout);
        } else {
            scene.setRoot(startLayout);
        }
    }

    private void advanceToNextProblem() {
        currentExercise.nextProblem();
    }

    // GUI creation methods

    //requires hiraganaIndex & katakanaIndex to be initialized
    //populates hiraganaIndex & katakanaIndex
    private Node createCenterNode() {

        TabPane centerNode = KanaGui.makeRegionSuitable(new TabPane());
        centerNode.getTabs().addAll(
                GuiHelper.getExercisePickerTab(),
                GuiHelper.getHiraganaPickerTab(hiraganaIndex),
                GuiHelper.getKatakanaPickerTab(katakanaIndex));
        return centerNode;
    }

    //no pre-requisition
    private Node createBottomNode() {
        Button button = new Button("start");
        button.setOnAction(e -> {
            pickKana();
            String errorMessage = "";
            int arbitrarySize = 5;
            if(pickedHiragana.size() < arbitrarySize)
                errorMessage += "Please pick at least " + arbitrarySize + " Hiragana.";
            if(pickedKatakana.size() < arbitrarySize) {
                if(errorMessage.length() > 0)
                    errorMessage += "\n";
                errorMessage += "Please pick at least " + arbitrarySize + " Katakana.";
            }
            if(errorMessage.length() > 0) {
                System.out.println(errorMessage + "\n");
            } else {
                initExercises();
                advanceToNextExercise();
            }
        });
        HBox.getHgrow(button);
        HBox bottomNode = new HBox();
        bottomNode.getChildren().add(button);
        bottomNode.setAlignment(Pos.CENTER);
        return bottomNode;
    }

    //Helper Methods

    public static <T extends Region> T makeRegionSuitable(T region) {
        region.setPrefSize(WIDTH, HEIGHT);
        return region;
    }

    //requires hiraganaIndex to be initialized and populized
    private void pickKana() {
        pickedHiragana = fetchPickedHiraganaFromIndex();
        pickedKatakana = fetchPickedKatakanaFromIndex();
    }
    //requires hiraganaIndex to be initialized and populized
    private HashSet<Hiragana> fetchPickedHiraganaFromIndex() {
        HashSet<Hiragana> result = new HashSet<>();
        Set<ToggleButton> keys = hiraganaIndex.keySet();
        for(ToggleButton key : keys)
            if(key.isSelected())
                result.add(hiraganaIndex.get(key));
        return result;
    }
    //requires katakanaIndex to be initialized and populized
    private HashSet<Katakana> fetchPickedKatakanaFromIndex() {
        HashSet<Katakana> result = new HashSet<>();
        Set<ToggleButton> keys = katakanaIndex.keySet();
        for(ToggleButton key : keys)
            if(key.isSelected())
                result.add(katakanaIndex.get(key));
        return result;
    }

    public static void main(String[] args) { launch(args); }
}