package moe.edotee.kanakana.logic;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;

import moe.edotee.kanakana.kana.Kana;
import moe.edotee.kanakana.kana.Yoon;
import moe.edotee.kanakana.utils.CSS;
import moe.edotee.kanakana.utils.Options;

import java.util.HashSet;

import static moe.edotee.kanakana.utils.CSS.typeRomaji.*;

/**
 * @author edotee
 */
public class TypeTheRomaji<T extends Kana> extends KanaExercise<T> {

    private final int CURRENT_KANA = 1;

    private BorderPane layout;
    private Label questionlbl, currentKanalbl, previousKanalbl, upcomingKanalbl, previousRomajilbl, currentRomajilbl, upcomingRomajilbl;
    private VBox questionAreaNode;
    private TextField answerInputNode;
    private HBox answerAreaNode;

    /* We initiated the following fields to avoid unnecessary GC work, since they'll be (in)directly used in each pass of handleInput() */
    //input, yoon and resultCase are used in handleInput
    private String input;
    private Yoon yoon;
    private InputResult resultCase;
    //logOutput is virtually used in every sub-branch of handleInput()
    private String logOutput;
    //discardOffset is virtually used whenever the user makes typos - which should happen enough in a learning tool to justify the promotion
    //the discardOffset dictates which entry will be pushed/discarded
    //after some tinkering and thinking, it basically tells you which inputs are wrong
    ////-2 = none
    ////-1 = all
    //// 0 = the 1st entry is wrong
    //// 1 = the 2nd entry is wrong
    private int discardOffset;
    //call_nextProblem allows us to preemptively call nextProblem() if needed and bypass its usual call
    //at the end of handleInput()
    //  -> nextProblem() calls prepareNextProblem() which will set c_nP to false
    //  -> handleInput() resets c_nP at the beginning of the call and calls nextProblem() at the end of its call
    //      if c_nP is still true
    //  -> event handling occurs between resetting and the check, so any call in between will cancel the call at the end
    private boolean call_nextProblem;

    public TypeTheRomaji(HashSet<T> targetKana, String cssPath) {
        super(targetKana, cssPath);
    }


    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    /* Methods */

    /**
     * Calls {@link #evaluateInput() evaluateInput()} to determine the type of input.
     * The returned type will be checked in a switch-case and appropriate actions
     * will be made in the methods called in the cases.
     * @param keyEvent
     */
    private void handleInput(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER || keyEvent.getCode() == KeyCode.SPACE) {
            //TODO add some smart regex to sort handle / potato finger inputs (double consonants, ending with a consonant other than n, not ending on a vowel (excluding n)
            //split input at each vowel(a,i,u,e,o) and at each n that is followed by a non-vowel / consonant or ' -> enka -> e nka _> e n ka
            //nana -> na na | nan'a -> na n'a -> na n a
            input = answerInputNode.getText().toLowerCase().replace(" ", "");
            answerInputNode.setText(input);
            yoon = fetchYoon(getAnswerLog().get(CURRENT_KANA), getAnswerLog().get(CURRENT_KANA-1));
            resultCase = evaluateInput();
            logOutput = "";
            discardOffset = 0;
            call_nextProblem = true;

            switch(resultCase) {
                case INVALID:
                break;
                case FOUL: onFoul();
                break;
                case WRONG: onWrong();
                break;
                case CORRECT: onCorrect();
                break;
                case DOUBLE_CORRECT: onDoubleCorrect();
                break;
                case YOON: onYoon();
                break;
                case MISSED_YOON: onMissedYoon();
                break;
            }
            if(call_nextProblem) nextProblem();
            if(logOutput.length() > 0) System.out.println(logOutput);
        }
        if(keyEvent.getCode() == KeyCode.SPACE) {
            answerInputNode.clear();
        }
        /*if(keyEvent.getCode().equals(KeyCode.BACK_SPACE)) {
            answerInputNode.clear();
        }*/
    }

    /**
     * Helper method that is called in {@link #handleInput(KeyEvent keyEvent) handleInput()} and {@return}s the type of input given
     */ //TODO maybe implement partly wrong (1st correct, 2nd wrong / the other way around / implement a boolean[] field that tells which input is wrong
    private InputResult evaluateInput() {
        InputResult result = InputResult.INVALID;
        if(input.length() > 0) {
            if (getAnswerLog().get(CURRENT_KANA).getRomaji().equals(input))
                result = InputResult.CORRECT;
            else if(yoon != null) {
                if(yoon.getRomaji().equals(input)) result = InputResult.YOON;
                else if(  returnsDoubleCorrect()  ) result = InputResult.MISSED_YOON;
                else result = InputResult.WRONG;    //potential double_wrong / half_wrong -> 0/2 or 1/2
            } else if(  returnsDoubleCorrect()  ) {
                if(  isFoul_WO()  ) result = InputResult.FOUL;
                else result = InputResult.DOUBLE_CORRECT;
            } else result = InputResult.WRONG;      //single_wrong -> 1 romaji input - 100% fail -> 0/1
        }
        return result;
    }

    // Helper methods for evaluateInput() | +1 readability
    private boolean returnsDoubleCorrect() {
        return (  getAnswerLog().get(CURRENT_KANA).getRomaji() + getAnswerLog().get(CURRENT_KANA-1).getRomaji()  ).equals(input);
    }
    private boolean isFoul_WO() {
        return  Options.TypeRomaji.Fouls.CHECK && Options.TypeRomaji.Fouls.WO
        && (    getAnswerLog().get(CURRENT_KANA).getRomaji().equals("wo")
              ||getAnswerLog().get(CURRENT_KANA-1).getRomaji().equals("wo")
        );
    }


    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    /* Result handler */

    /**
     * Branched-off sub-routine for input evaluation that is called in {@link #handleInput(KeyEvent keyEvent) handleInput()}
     * according to the results from {@link #evaluateInput() evaluateInput()}.
     */
    private void onCorrect() {
        stylePrevious(previousKana, romajiHide);
        //logOutput = "Correct!"
    }

    /**
     * Branched-off sub-routine for input evaluation that is called in {@link #handleInput(KeyEvent keyEvent) handleInput()}
     * according to the results from {@link #evaluateInput() evaluateInput()}.
     */
    private void onDoubleCorrect() {
        styleKana(previousKanaDouble, currentKanaDouble);
        styleAllRomaji(romajiHide);
        //logOutput = "Double!";
    }

    /**
     * Branched-off sub-routine for input evaluation that is called in {@link #handleInput(KeyEvent keyEvent) handleInput()}
     * according to the results from {@link #evaluateInput() evaluateInput()}.
     */
    private void onYoon() {
        styleKana(previousKanaYoon, currentKanaYoon);
        styleRomaji(romajiYoon, romajiYoon, romajiHide);
        nextProblem();
        previousKanalbl.setText(getYoonKana());
        previousRomajilbl.setText(input); //or input
        logOutput = "Yoon! – " + getYoonKana() + " ~ " + input;
        //discardOffset = 1;
    }

    /**
     * Branched-off sub-routine for input evaluation that is called in {@link #handleInput(KeyEvent keyEvent) handleInput()}
     * according to the results from {@link #evaluateInput() evaluateInput()}.
     */
    private void onMissedYoon() {
        stylePrevious(previousKanaMissedYoon, romajiMissedYoon);
        logOutput = "Missed a Yoon! – " + getYoonKana() + " ~ " + yoon.getRomaji();
    }

    /**
     * Branched-off sub-routine for input evaluation that is called in {@link #handleInput(KeyEvent keyEvent) handleInput()}
     * according to the results from {@link #evaluateInput() evaluateInput()}.
     */
    private void onFoul() {
        styleKana(previousKanaFoul, currentKanaFoul);
        styleAllRomaji(romajiHide);
        logOutput = "FOUL: を is only used as a particle";
        discardOffset = determineDiscardOffset();   //TODO replace with checkFoul()
    }

    /**
     * Branched-off sub-routine for input evaluation that is called in {@link #handleInput(KeyEvent keyEvent) handleInput()}
     * according to the results from {@link #evaluateInput() evaluateInput()}.
     */
    private void onWrong() {
        discardOffset = determineDiscardOffset();
        if(discardOffset == -1) {
            //every kana wrong
            styleKana(previousKanaWrong, currentKanaWrong);
            styleRomaji(romajiWrong, romajiWrong);
            logOutput = "" + getAnswerLog().get(CURRENT_KANA).getKana() + "|" + getAnswerLog().get(CURRENT_KANA - 1).getKana() + " != " + input;
        }
        else if(discardOffset == 0) {
            //1st kana wrong
            styleKana(previousKanaWrong, currentKana);
            styleRomaji(romajiWrong, romajiHide);
            logOutput = "" + getAnswerLog().get(CURRENT_KANA).getKana() + " != " + input.replace(getAnswerLog().get(CURRENT_KANA - 1).getRomaji(), "");
        }
        else if(discardOffset == 1){
            //2nd kana wrong
            styleKana(previousKana, currentKanaWrong);
            styleRomaji(romajiHide, romajiWrong);
            logOutput = "" + getAnswerLog().get(CURRENT_KANA - discardOffset).getKana() + " != " + input.replace(getAnswerLog().get(CURRENT_KANA).getRomaji(), "");
        } else /* if(discardOffset == -2) */ {
            System.out.println(
                "Mate, something went wrong. We're in onWrong() and nothing is wrong… something is wrong!"
            );
        }
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    /* Helper methods */

    /**
     * Helper method that {@return}s an int indicating which input(s) is/are wrong.
     */
    private int determineDiscardOffset() {
        boolean correctStart = input.startsWith( getAnswerLog().get(CURRENT_KANA).getRomaji() );
        boolean correctEnd = input.endsWith( getAnswerLog().get(CURRENT_KANA-1).getRomaji() );
        int result;
        if(correctStart && correctEnd)              //fat fingering / extry chars inbetween 1st romaji and 2nd romaji
            result = -2;                            //0% happening (after the fat fingering check is included)
        else if(correctStart && !correctEnd)        //only 2nd kana wrong
            result = 1;
        else if(correctEnd || input.length() < 3)   //1st romaji is guaranteed to be wrong - we check wether the 2nd is correct...
            result = 0;                             //... or the length of the input is shorter than x - which indicates that it's only a try at single
        else                                        //everything wrong
            result = -1;
        return result;

        //we do not check for fat fingering after the 2nd romaji, because this doesn't belong here.
        //this is the job for the fat fingering check, which will remove the most obvious cases
        //of ff after the inputs - all other cases will be deemed as user errors
        //the 1st if-statement doesn't need to be here if the FFC would be in place
        //but we leave it here commented for the sake of completeness
        //and just to make sure future me doesn't do so wacky jackshit that messes up the code

        /*
        if(correctStart && correctEnd)          //fat fingering, should not be happening (after the fat fingering check is included)
            result = -2;
        else if(correctStart && !correctEnd)    //only 2nd kana wrong
            result = 1;
        else if(input.length() < 3) {           //at this point, the 1st romaji is 100% wrong & less than 3 char means that it's probably only the input for a single and not a double
            result = 0;                         //which is why only return the 1st romaji as wrong
        } else {                                //otherwise, we explicitly check whether the check for the 2nd char returned true
            if (correctEnd)                     //only the 1st kana wrong
                result = 0;
            else                                //everything wrong
                result = -1;
        }
        return result;
        */

        /*
        if(input.length() < 3) {
            if(correctStart && correctEnd)          //fat fingering, should not be happening (after the fat fingering check is included)
                result = -2;
            else if(correctStart && !correctEnd)    //2nd kana wrong
                result = 1;
            else                                    //1st kana wrong
                result = 0;
        } else {
            if(correctStart && correctEnd)          //fat fingering, should not be happening (after the fat fingering check is included)
                result = -2;
            else if(correctStart && !correctEnd)    //2nd kana wrong
                result = 1;
            else if (!correctStart && correctEnd)   //1st kana wrong
                result = 0;
            else                                    //everything wrong
                result = -1;
        }
         */

        /*
        //boolean correctEnd = (input.length() > 1)? input.endsWith( getAnswerLog().get(CURRENT_KANA-1).getRomaji() ) : true;
        //boolean correctEnd = (input.length() <= 1 || input.endsWith( getAnswerLog().get(CURRENT_KANA-1).getRomaji() ));

        if(correctStart && correctEnd)          //fat fingering, should not be happening (after the fat fingering check is included)
            result = -2;
        else if(correctStart && !correctEnd)    //2nd kana wrong
            result = 1;
        else if (!correctStart && correctEnd)   //1st kana wrong
            result = 0;
        else                                    //everything wrong
            result = -1;
        */
    }
    /**
     * Wrapper for readability.
     * Checks whether or not {@param lead} and {@param post} can form a yoon
     * and {@return}s it – or null otherwise.
     */
    private Yoon fetchYoon(Kana lead, Kana post) {
        return Yoon.match(lead.getKana(), post.getKana());
    }
    /**
     * Brevity wrapper that {@return}s the current yoons kana.
     */
    private String getYoonKana() {
        return yoon.getKana(getAnswerLog().get(CURRENT_KANA).getKana());
    }


    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    /* Promises */
    //1st thing called in the parent constructor followed by initGUI()
    @Override protected void initFields() {
        questionlbl = new Label();
        previousKanalbl = new Label();
        currentKanalbl = new Label();
        upcomingKanalbl = new Label();
        previousRomajilbl = new Label();
        currentRomajilbl = new Label();
        upcomingRomajilbl = new Label();
        questionAreaNode = new VBox();
        answerInputNode = new TextField();
        answerAreaNode = new HBox();
        layout = new BorderPane();
    }
    //2nd method called in the parent constructor followed by applyCSS()
    @Override protected Region initGUI() {
        /* Question Area */
        questionlbl.setText("Type the romaji for…");

        /* Size is currently not supported by JavaFX */
        previousKanalbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        currentKanalbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        upcomingKanalbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        previousRomajilbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        currentRomajilbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        upcomingRomajilbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        GridPane problemBar = new GridPane();
        GridPane.setHgrow(previousKanalbl, Priority.ALWAYS);
        GridPane.setHgrow(currentKanalbl, Priority.ALWAYS);
        GridPane.setHgrow(upcomingKanalbl, Priority.ALWAYS);
        GridPane.setHgrow(previousRomajilbl, Priority.ALWAYS);
        GridPane.setHgrow(currentRomajilbl, Priority.ALWAYS);
        GridPane.setHgrow(upcomingRomajilbl, Priority.ALWAYS);
        GridPane.setConstraints(previousKanalbl, 0, 0);
        GridPane.setConstraints(currentKanalbl, 1, 0);
        GridPane.setConstraints(upcomingKanalbl, 2, 0);
        GridPane.setConstraints(previousRomajilbl, 0, 1);
        GridPane.setConstraints(currentRomajilbl, 1, 1);
        GridPane.setConstraints(upcomingRomajilbl, 2, 1);
        problemBar.getChildren().addAll(previousKanalbl, currentKanalbl, upcomingKanalbl, previousRomajilbl, currentRomajilbl, upcomingRomajilbl);

        questionAreaNode.getChildren().addAll(questionlbl, problemBar);

        /* Answer Area */

        answerInputNode.setOnKeyPressed(keyEvent -> handleInput(keyEvent) ); //don't listen to IntelliJ - handleInput() isn't static -> no method reference
        //answerInputNode.requestFocus(); //not needed, since we setFocusTraversable(false) on all other nodes
        answerAreaNode.getChildren().addAll(answerInputNode);
        //answerAreaNode.setPadding(new Insets(10.0f, 10.0f, 10.f, 10.f)); //doable in JFX CSS

        layout.setPrefSize(Options.WIDTH, Options.HEIGHT);
        layout.setTop(questionAreaNode);
        layout.setCenter(answerAreaNode);

        return layout;
    }
    //3rd method called in the parent constructor
    @Override protected void applyCSS(String css_file_path) {
        layout.getStylesheets().add(css_file_path);
        CSS.style(questionlbl, question);
        CSS.style(previousKanalbl, previousKana);
        CSS.style(currentKanalbl, currentKana);
        CSS.style(upcomingKanalbl, upcomingKana);
        styleAllRomaji(romajiHide);
        CSS.style(questionAreaNode, questionArea);
        CSS.style(answerAreaNode, answerArea);
        CSS.style(answerInputNode, answerInput);
    }
    //LAST method called in the parent constructor.
    @Override protected void prepareFirstProblem() {
        T dummy = super.randomAnswerFromOptions(getRecentlyUsedOptions());
        getAnswerLog().add(dummy);   //we add the 1st Kana 2 times, because 1 will be a dummy
        getAnswerLog().add(dummy);   //not doing this would increase the number of Kanas to be excluded
        prepareNextProblem();   //we only exclude Kana that we already have tested
        styleAllRomaji(romajiHide);
        CSS.style(previousKanalbl, previousKanaHide);
        CSS.style(previousRomajilbl, romajiHide);
    }
    @Override protected void prepareNextProblem() {
        call_nextProblem = false;
        answerInputNode.clear();
        //+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~
        /* discard the mistyped kana and not the current one
        if(discardOffset > 0) {
            T temp = getAnswerLog().get(CURRENT_KANA-discardOffset);
            getAnswerLog().remove(CURRENT_KANA-discardOffset);
            getAnswerLog().add(CURRENT_KANA, temp);
        }
        */
        //+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~
        getAnswerLog().add(0, randomAnswerFromOptions(getRecentlyUsedOptions()));
        //0 = next kana; 1 = current kana; 2 = previous kana
        previousKanalbl.setText("" + getAnswerLog().get(2).getKana());
        currentKanalbl.setText("" + getAnswerLog().get(1).getKana());
        upcomingKanalbl.setText("" + getAnswerLog().get(0).getKana());
        previousRomajilbl.setText(getAnswerLog().get(2).getRomaji());
        currentRomajilbl.setText(getAnswerLog().get(1).getRomaji());
        upcomingRomajilbl.setText(getAnswerLog().get(0).getRomaji());
    }
    @Override public void onComplete() {

    }


    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    /* Option wrapper */
    @Override protected int getAmount() {
        return Options.TypeRomaji.AMOUNT;
    }
    @Override protected int getRelevantLogDepth() {
        return Options.TypeRomaji.DEPTH;
    }


    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    /* CSS util methods */
    /**
     * Styles the previous kana its romaji according to {@param kanaCssStyle} and {@param romajiCssStyle}.
     */
    private void stylePrevious(CSS kanaCssStyle, CSS romajiCssStyle) {
        styleKana(kanaCssStyle, currentKana, upcomingKana);
        styleRomaji(romajiCssStyle, romajiHide, romajiHide);
    }
    /**
     * Styles the previous & current kana according to {@param previousCssStyle} and {@param currentCssStyle}.
     * The upcoming kana will be styled with the default style.
     */
    private void styleKana(CSS previousCssStyle, CSS currentCssStyle) {
        styleKana(previousCssStyle, currentCssStyle, upcomingKana);
    }
    /**
     * Styles the previous, currentCssStyle & upcomingCssStyle kana according to {@param previous}, {@param currentCssStyle} and {@param upcomingCssStyle}.
     */
    private void styleKana(CSS previous, CSS currentCssStyle, CSS upcomingCssStyle) {
        CSS.style(previousKanalbl, previous);
        CSS.style(currentKanalbl, currentCssStyle);
        CSS.style(upcomingKanalbl, upcomingCssStyle);
    }
    private void styleAllRomaji(CSS cssStyle) {
        styleRomaji(cssStyle, cssStyle, cssStyle);
    }
    private void styleRomaji(CSS previousCssStyle, CSS currentCssStyle) {
        styleRomaji(previousCssStyle, currentCssStyle, romajiHide);
    }
    private void styleRomaji(CSS previousCssStyle, CSS currentCssStyle, CSS upcomingCssStyle) {
        if(Options.SHOW_ROMAJI) {
            CSS.style(previousRomajilbl, (previousCssStyle == romajiHide)? romaji : previousCssStyle);
            CSS.style(currentRomajilbl, (currentCssStyle == romajiHide)? romaji : currentCssStyle);
            CSS.style(upcomingRomajilbl, (upcomingCssStyle == romajiHide)? romaji : upcomingCssStyle);
        } else {
            CSS.style(previousRomajilbl, previousCssStyle);
            CSS.style(currentRomajilbl, currentCssStyle);
            CSS.style(upcomingRomajilbl, upcomingCssStyle);
        }
    }
}