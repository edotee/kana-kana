package logic;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Region;
import kana.Kana;
import logic.wrapper.Exclude;
import logic.wrapper.From;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * Since any relevant method is called before the JVM
 * returns to the child's contructor block, it is recommended
 * to only invoke super() in it.
 * The parent constructor automatically calls initFields()
 * before initGui() and prepareFirstProblem().
 * If fields have to be initialized, do so in the initFields() method.
 *
 * @author edotee
 */
public abstract class KanaExercise<T extends Kana<T>> {

    private final HashSet<T> targetKana;
    private final EventHandler<ActionEvent> onRightAnswer, onWrongAnswer, onSkip, onComplete;
    private final Region gui;

    private Random rng;
    private ArrayList<T> answerOptions;

    //TODO remove this arbitrary number - maybe make it an contructor param or something like that...
    private static final int AMOUNT = 5;

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

        rng = new Random(System.currentTimeMillis());
        initFields();
        gui = initGUI();
        answerOptions = new ArrayList<>();
        pickMyOwnAnswers();
        prepareFirstProblem();
    }

    /* Abstract Methods */

    protected abstract void initFields();
    protected abstract Region initGUI();

    protected abstract void prepareFirstProblem();
    protected abstract void prepareNextProblem();

    /* Methods for external Logics */

    public void nextProblem() {
        pickMyOwnAnswers();
        prepareNextProblem();
    }

    /* Helper Methods */

    /**
     * Not sure if redundant or not - better have a little redundancy than having to deal with unknotting a nightmare later
     */
    private void pickMyOwnAnswers() {
        answerOptions.clear();
        answerOptions.addAll(pickAnswers());
    }

    protected ArrayList<T> pickAnswers() {
        ArrayList<T> listOfOptions = new ArrayList<>(targetKana);
        Random rng = new Random();
        //Scramble the list
        int size = listOfOptions.size();
        T kana;
        for(int i = 0; i < 8888; i++) {
            kana = listOfOptions.get(rng.nextInt(size));
            listOfOptions.remove(kana);
            listOfOptions.add(kana);
        }
        //turn into arrayList, so we can use random to fish out an option
        //use HashSet, so the items we get are unique
        //or remove items until the right amount
        while(listOfOptions.size() > AMOUNT)
            listOfOptions.remove(listOfOptions.get(rng.nextInt(listOfOptions.size())));
        return listOfOptions;
    }

    /**
     * @return returns a value from [0 ... upperRange-1]
     */
    protected int dice(int upperRange) {
        return rng.nextInt(upperRange);
    }

    /**
     * Primary use case: poolOfOptions.get(dice(poolOfOptions))
     * @return returns a value from [0 ... upperRange-1]
     */
    protected int dice(ArrayList<T> poolOfOptions) {
        return rng.nextInt(poolOfOptions.size());
    }

    /**
     * @return return a random answer out of {answerOptions}
     */
    protected T randomAnswer() {
        return randomFromAnswer(answerOptions);
    }

    /**
     * @param isNotThis exclude this from the result
     * @return return a random answer out of {answerOptions} that {isNotThis}
     */
    protected T randomAnswer(T isNotThis) {
        return randomFromAnswer(answerOptions, isNotThis);
    }

    /**
     * @param isNotOneOfThese exclude these from the pool of targets
     * @return pick & return a random answer that {isNotOneOfThese}
     */
    protected T randomAnswer(ArrayList<T> isNotOneOfThese) {
        return randomFromAnswer(answerOptions, isNotOneOfThese);
    }

    /**
     * @return return a random answer {outOfThis}
     */
    protected T randomFromAnswer(ArrayList<T> outOfThis) {
        return outOfThis.get(dice(outOfThis));
    }

    /**
     * @param outOfThis Pool of targets
     * @param isNotThis exclude these from the pool of targets
     * @return pick & return a random target {outOfThis} that {isNotThis}
     */
    protected T randomFromAnswer(ArrayList<T> outOfThis, T isNotThis) {
        T gandalf = randomFromAnswer(outOfThis);
        while(gandalf == isNotThis)
            gandalf = randomFromAnswer(outOfThis);
        return gandalf;
    }

    /**
     * @param outOfThis Pool of targets
     * @param isNotOneOfThese exclude these from the pool of targets
     * @return pick & return a random target {outOfThis} that {isNotOneOfThese}
     */
    protected T randomFromAnswer(ArrayList<T> outOfThis, ArrayList<T> isNotOneOfThese) {
        T magicShapedBlock = randomFromAnswer(outOfThis);
        HashSet<T> magicShapedHole = new HashSet<>(isNotOneOfThese);
        while(!magicShapedHole.add(magicShapedBlock))
            magicShapedBlock = randomFromAnswer(outOfThis);
        return magicShapedBlock;
    }

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

    public ArrayList<T> getAnswerOptions() {
        return answerOptions;
    }

    public int getAmount() {
        return AMOUNT;
    }

    public Region getGui() {
        return gui;
    }
}
