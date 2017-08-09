package moe.edotee.kanakana.logic;

import javafx.scene.Node;
import javafx.scene.layout.Region;
import moe.edotee.kanakana.kana.Kana;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * Since any relevant method is called before the JVM
 * returns to the child's constructor block, it is recommended
 * to only invoke super() in it.
 * The parent constructor automatically calls
 * 1) initFields()
 * 2) initGui()
 * 3) applyCSS()
 * last) prepareFirstProblem().
 * If fields have to be initialized, do so in the initFields() method.
 *
 * @author edotee
 */
public abstract class KanaExercise<T extends Kana> {

    private final HashSet<T> targetKana;                //The general pool of kana that will be tested
    private final ArrayList<T> targetKanaAsArrayList;   //The general pool of kana that will be tested
    private final Region gui;

    private Random rng;
    private ArrayList<T> answerLog;         //Log of the recently tested kana
    //TODO remove answerOptions - its functionality is redundant, we can instead add the picked options to answerLog and directly read from it
    //TODO if needed, add the utility method getOptions() which will return the currently relevant kana as an unmutable subset
    //TODO if needed, add the utility method getRecent() which will return the an unmutable subset of answerLog to exclude from getting chosen for answer picking
    private ArrayList<T> answerOptions;     //Pool of currently used kana

    public KanaExercise(HashSet<T> targetKana, String cssPath) {
        this.targetKana = targetKana;
        targetKanaAsArrayList = new ArrayList<T>(targetKana);

        rng = new Random(System.currentTimeMillis());
        initFields();
        gui = initGUI();
        applyCSS(cssPath);
        answerLog = new ArrayList<>();
        answerOptions = new ArrayList<>();
        firstProblem();
    }


    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    /* Abstract Methods */
    protected abstract void initFields();
    protected abstract Region initGUI();
    protected abstract void applyCSS(String css_file_path);

    protected abstract void prepareFirstProblem();
    protected abstract void prepareNextProblem();
    public abstract void onComplete();


    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    /* Option wrapper */
    protected abstract int getAmount();
    protected abstract int getRelevantLogDepth();


    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    /* Methods for external Logics */
    private void firstProblem() {
        pickMyOwnAnswers();
        prepareFirstProblem();
    }
    public void nextProblem() {
        pickMyOwnAnswers();
        prepareNextProblem();
    }


    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    /* Helper Methods */
    /**
     * Not sure if redundant or not - better have a little redundancy than having to deal with unknotting a nightmare later
     */
    private void pickMyOwnAnswers() {
        answerOptions.clear();
        answerOptions.addAll(pickOptions());
    }

    /**
     * Picks getAmount() options from targetKana, which was passed at instance initiation
     * 1) Create an ArrayList with targetKana
     * 2) Remove all recently used kana from it (for more consistent testing)
     * @return
     */
    protected ArrayList<T> pickOptions() {
        ArrayList<T> listOfOptions = new ArrayList<>(targetKana);
        listOfOptions.removeAll(getRecentlyUsedOptions());
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
        while(listOfOptions.size() > getAmount())
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

    protected ArrayList<T> getRecentlyUsedOptions() {
        ArrayList<T> result = new ArrayList<>();
        for(int i = 0; i < answerLog.size() && i < getRelevantLogDepth(); i++)
            result.add(answerLog.get(i));
        return result;
    }

    /**
     * @return return a random answer out of {answerOptions}
     */
    protected T randomAnswerFromOptions() {
        return randomAnswerFrom(answerOptions);
    }

    /**
     * @param isNotThis exclude this from the result
     * @return return a random answer out of {answerOptions} that {isNotThis}
     */
    protected T randomAnswerFromOptions(T isNotThis) {
        return randomAnswerFrom(answerOptions, isNotThis);
    }

    /**
     * @param isNotOneOfThese exclude these from the pool of targets
     * @return pick & return a random answer that {isNotOneOfThese}
     */
    protected T randomAnswerFromOptions(ArrayList<T> isNotOneOfThese) {
        return randomAnswerFrom(answerOptions, isNotOneOfThese);
    }

    /**
     * @return return a random answer {outOfThis}
     */
    private T randomAnswerFrom(ArrayList<T> outOfThis) {
        return outOfThis.get(dice(outOfThis));
    }

    /**
     * @param outOfThis Pool of targets
     * @param isNotThis exclude these from the pool of targets
     * @return pick & return a random target {outOfThis} that {isNotThis}
     */
    protected T randomAnswerFrom(ArrayList<T> outOfThis, T isNotThis) {
        T gandalf = randomAnswerFrom(outOfThis);
        while(gandalf == isNotThis)
            gandalf = randomAnswerFrom(outOfThis);
        return gandalf;
    }

    /**
     * @param outOfThis Pool of targets
     * @param isNotOneOfThese exclude these from the pool of targets
     * @return pick & return a random target {outOfThis} that {isNotOneOfThese}
     */
    protected T randomAnswerFrom(ArrayList<T> outOfThis, ArrayList<T> isNotOneOfThese) {
        T magicShapedBlock = randomAnswerFrom(outOfThis);
        HashSet<T> magicShapedHole = new HashSet<>(isNotOneOfThese);
        while(!magicShapedHole.add(magicShapedBlock))
            magicShapedBlock = randomAnswerFrom(outOfThis);
        return magicShapedBlock;
    }


    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    /* Getter */
    public HashSet<T> getTargetKana() {
        return targetKana;
    }

    public ArrayList<T> getTargetKanaAsArrayList() {
        return targetKanaAsArrayList;
    }

    public Region getGui() {
        return gui;
    }

    public ArrayList<T> getCurrentAnswerOptions() {
        return answerOptions;
    }

    public ArrayList<T> getAnswerLog() {
        return answerLog;
    }
}