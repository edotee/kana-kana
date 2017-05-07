package logic;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Region;
import kana.Kana;

import java.util.HashSet;

/**
 * @author edotee
 */
public abstract class KanaExercise<T extends Kana<T>> {

    private final HashSet<T> targetKana;
    private final EventHandler<ActionEvent> onRightAnswer, onWrongAnswer, onSkip, onComplete;
    private final Region gui;

    public KanaExercise(HashSet<T> targetKana,
                        EventHandler<ActionEvent> onRightAnswer,
                        EventHandler<ActionEvent> onWrongAnswer,
                        EventHandler<ActionEvent> onSkip,
                        EventHandler<ActionEvent> onComplete) {
        this.targetKana = targetKana;
        this.onRightAnswer = onRightAnswer;
        this.onWrongAnswer = onWrongAnswer;
        this.onSkip = onSkip;
        this.onComplete = onComplete;
        this.gui = initGUI();
        nextProblem();
    }

    /* Abstract Methods */

    protected abstract Region initGUI();

    public abstract void nextProblem();

    /* Getter */


    public HashSet<T> getTargetKana() {
        return targetKana;
    }

    public EventHandler<ActionEvent> getOnRightAnswer() {
        return onRightAnswer;
    }

    public EventHandler<ActionEvent> getOnWrongAnswer() {
        return onWrongAnswer;
    }

    public EventHandler<ActionEvent> getOnSkip() {
        return onSkip;
    }

    public EventHandler<ActionEvent> getOnComplete() {
        return onComplete;
    }

    public Region getGui() {
        return gui;
    }
}
