package moe.edotee.kanakana.gui.fxml;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import moe.edotee.kanakana.utils.Options;

import java.io.IOException;

public class TestKanaGuiFXML extends Application {

    public Button switchToExerciseLayoutButton;
    public void onSwitchToExerciseLayoutButtonClick(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) switchToExerciseLayoutButton.getScene().getWindow();
        stage.setScene(loadScene(loadExerciseLayout()));
        stage.show();
        System.out.println("Switched to exerciseLayout");
    }

    public Button switchToStartLayoutButton;
    public void onSwitchToStartLayoutButtonClick(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) switchToStartLayoutButton.getScene().getWindow();
        stage.setScene(loadScene(loadStartLayout()));
        stage.show();
        System.out.println("Switched to startLayout");
    }

    /*

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

        Button goToNextProblem, goToNextExercise, exitExercise;
        HBox buttonBox;
        goToNextProblem = new Button("Next Problem");
        goToNextProblem.setOnAction( e -> advanceToNextProblem() );
        goToNextExercise = new Button("Next Exercise");
        goToNextExercise.setOnAction( e -> advanceToNextExercise() );
        exitExercise = new Button("Exit Exercise");
        exitExercise.setOnAction( e -> scene.setRoot(startLayout) );
        buttonBox = new HBox();
        buttonBox.setStyle("-fx-spacing: 10");
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(goToNextProblem, goToNextExercise, exitExercise);
        exerciseLayout = makeRegionSuitable(new BorderPane());
        exerciseLayout.setBottom(buttonBox);
     */

    private final static String TITLE = "Kana FXML Test GUI";

    /* There should be no need to touch anything below this */

    private Stage mainWindow;
    private Scene scene;

    @Override public void start(Stage primaryStage) throws Exception {
        mainWindow = primaryStage;
        mainWindow.setTitle(TITLE);
        mainWindow.setScene(loadScene(loadStartLayout()));
        mainWindow.show();
    }

    private Scene loadScene(Parent root) {
        scene = new Scene(root, Options.WIDTH, Options.HEIGHT);
        return scene;
    }

    private Parent loadStartLayout() throws IOException {
        return FXMLLoader.load(getClass().getResource(Options.FXML.START));
    }

    private Parent loadExerciseLayout() throws IOException {
        return FXMLLoader.load(getClass().getResource(Options.FXML.EXERCISE));
    }

    public static void main(String[] args) { launch(args); }
}