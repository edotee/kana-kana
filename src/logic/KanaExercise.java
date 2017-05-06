package logic;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import kana.Kana;
import kana.hiragana.Hiragana;

import java.util.HashSet;

/**
 * @author edotee
 * TODO create implement getGUI() method that returns the (singleton) UI of the current exercise
 * TODO rework nextProblem() so it updates its own GUI
 * TODO maybe somehow turn this into a pseudo stream that automatically starts the next exercise in line
 */
public abstract class KanaExercise {

    private final HashSet<Hiragana> targetKana;
    private final EventHandler<ActionEvent> onRightAnswer, onWrongAnswer, onComplete;
    private Scene gui;

    public KanaExercise(HashSet<Hiragana> targetKana, EventHandler<ActionEvent> onRightAnswer, EventHandler<ActionEvent> onWrongAnswer, EventHandler<ActionEvent> onComplete) {
        this.targetKana = targetKana;
        this.onRightAnswer = onRightAnswer;
        this.onWrongAnswer = onWrongAnswer;
        this.onComplete = onComplete;
        this.gui = initGUI();
        nextProblem();
    }

    /* Abstract Methods */

    protected abstract Scene initGUI();

    public abstract void nextProblem();

    /* Getter */


    public HashSet<Hiragana> getTargetKana() {
        return targetKana;
    }

    public EventHandler<ActionEvent> getOnRightAnswer() {
        return onRightAnswer;
    }

    public EventHandler<ActionEvent> getOnWrongAnswer() {
        return onWrongAnswer;
    }

    public EventHandler<ActionEvent> getOnComplete() {
        return onComplete;
    }

    public Scene getGUI() {
        return gui;
    }
}
