package logic;

import gui.KanaGui;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;

import kana.Kana;
import kana.Yoon;
import kana.hiragana.Hiragana;
import kana.hiragana.HiraganaYoon;
import kana.katakana.Katakana;
import kana.katakana.KatakanaYoon;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author edotee
 */
public class TypeTheRomaji<T extends Kana<T>> extends KanaExercise<T> {

    private final int CURRENT_KANA = 1;
    private final boolean SHOW_ROMAJI = true;
    private final String CSS_FILE_PATH = "gui/css/type_the_romaji.css";

    private BorderPane layout;
    private Label question, currentKana, previousKana, upcomingKana, previousRomaji, currentRomaji, upcomingRomaji;
    private VBox questionArea;
    private TextField answerInput;
    private HBox answerArea;

    //We initiated the following fields to avoid unnecessary GC work, since they'll be (in)directly used in each pass of handleInput()
    //input, yoon and resultCase are used in handleInput
    private String input;
    private Yoon yoon;
    private InputResult resultCase;
    //logOutput is virtually used in every sub-branch of handleInput()
    private String logOutput;
    //discardOffset is virtually used whenever the user makes typos - which should happen enough in a learning tool to justify the promotion
    private int discardOffset;
    //the discardOffset dictates which entry will be pushed/discarded
    //after some tinkering and thinking, it basically tells you which inputs are wrong
    ////-2 = none
    ////-1 = all
    //// 0 = the 1st entry is wrong
    //// 1 = the 2nd entry is wrong
    private boolean call_nextProblem;

    public TypeTheRomaji(ArrayList<T> completeList, HashSet<T> targetKana,
                         EventHandler<ActionEvent> onComplete) {
        super(completeList, targetKana, onComplete);
    }

    /**
     *
     * Calls {@link #evaluateInput() evaluateInput()} to determine the type of input.
     * The returned type will be checked in a switch-case and appropriate actions
     * will be made in the methods called in the cases.
     * @param keyEvent
     */
    private void handleInput(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER || keyEvent.getCode() == KeyCode.SPACE) {
            //TODO add some smart regex to sort handle potato finger inputs (double consonants, ending with a consonant other than n, not ending on a vowel other than n
            //split input at each vowel(a,i,u,e,o) and at each n that is followed by a non-vowel / consonant or ' -> enka -> e nka _> e n ka
            //nana -> na na | nan'a -> na n'a -> na n a
            input = answerInput.getText().toLowerCase().replace(" ", "");
            answerInput.setText(input);
            yoon = isYoon(getAnswerLog().get(CURRENT_KANA), getAnswerLog().get(CURRENT_KANA-1));
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
            answerInput.clear();
        }
        /*if(keyEvent.getCode().equals(KeyCode.BACK_SPACE)) {
            answerInput.clear();
        }*/
    }

    /**
     * Helper method that is called in {@link #handleInput(KeyEvent keyEvent) handleInput()} and {@return}s the type of input given
     */
    private InputResult evaluateInput() {
        InputResult result = InputResult.INVALID;
        if(input.length() > 0) {
            if (getAnswerLog().get(CURRENT_KANA).getRomanji().equals(input)) {
                result = InputResult.CORRECT;
            } else if (yoon != null) {
                if (yoon.getRomanji().equals(input))
                    result = InputResult.YOON;
                else if ((getAnswerLog().get(CURRENT_KANA).getRomanji() + getAnswerLog().get(CURRENT_KANA-1).getRomanji()).equals(input))
                    result = InputResult.MISSED_YOON;
                else    //TODO maybe implement partly wrong (1st correct, 2nd wrong / the other way around / implement a boolean[] field that tells which input is wrong
                    result = InputResult.WRONG; //potential double_wrong / half_wrong -> 0/2 or 1/2
            } else if ((getAnswerLog().get(CURRENT_KANA).getRomanji() + getAnswerLog().get(CURRENT_KANA-1).getRomanji()).equals(input)) {
                if (getAnswerLog().get(CURRENT_KANA).getRomanji().equals("wo") || getAnswerLog().get(CURRENT_KANA-1).getRomanji().equals("wo"))
                    result = InputResult.FOUL;
                else
                    result = InputResult.DOUBLE_CORRECT;
            } else {
                result = InputResult.WRONG; //single_wrong -> 1 romaji input - 100% fail -> 0/1
            }
        }
        return result;
    }

    /**
     * Branched-off sub-routine for input evaluation that is called in {@link #handleInput(KeyEvent keyEvent) handleInput()}
     * according to the results from {@link #evaluateInput() evaluateInput()}.
     */
    private void onCorrect() {
        stylePrevious("previousKana", "romajiHide");
        //logOutput = "Correct!"
    }

    /**
     * Branched-off sub-routine for input evaluation that is called in {@link #handleInput(KeyEvent keyEvent) handleInput()}
     * according to the results from {@link #evaluateInput() evaluateInput()}.
     */
    private void onDoubleCorrect() {
        styleKana("previousKanaDouble", "currentKanaDouble");
        styleAllRomaji("romajiHide");
        //logOutput = "Double!";
    }

    /**
     * Branched-off sub-routine for input evaluation that is called in {@link #handleInput(KeyEvent keyEvent) handleInput()}
     * according to the results from {@link #evaluateInput() evaluateInput()}.
     */
    private void onYoon() {
        styleKana("previousKanaYoon", "currentKanaYoon");
        styleRomaji("romajiYoon", "romajiYoon", "romajiHide");
        /*
        previousKana.setText("" + getAnswerLog().get(CURRENT_KANA).getKana()    //leading Kana
                + (char)(  getAnswerLog().get(CURRENT_KANA-1).getKana()-1)  );  //ya/yu/yo in small
        */
        nextProblem();
        previousKana.setText(yoon.getKana());
        previousRomaji.setText(input); //TODO yoon.getRomanji() only return the romaji of the 1st kana and not of the yoon
        logOutput = "Yoon! – " + yoon.getKana() + " ~ " + input/*+ yoon.getRomaji()*/;
        //discardOffset = 1;
    }

    /**
     * Branched-off sub-routine for input evaluation that is called in {@link #handleInput(KeyEvent keyEvent) handleInput()}
     * according to the results from {@link #evaluateInput() evaluateInput()}.
     */
    private void onMissedYoon() {
        stylePrevious("previousKanaMissedYoon", "romajiMissedYoon");
        /*
        previousKana.setText("" + getAnswerLog().get(CURRENT_KANA).getKana()    //leading Kana
                + (char)(  getAnswerLog().get(CURRENT_KANA-1).getKana()-1)  );  //ya/yu/yo in small;
        */
        logOutput = "Missed a Yoon! – " + yoon.getKana() /* + " ~ " + yoon.getRomaji()*/;
    }

    /**
     * Branched-off sub-routine for input evaluation that is called in {@link #handleInput(KeyEvent keyEvent) handleInput()}
     * according to the results from {@link #evaluateInput() evaluateInput()}.
     */
    private void onFoul() {
        styleKana("previousKanaFoul", "currentKanaFoul");
        styleAllRomaji("romajiHide");
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
            styleKana("previousKanaWrong", "currentKanaWrong");
            styleRomaji("romajiWrong", "romajiWrong");
            logOutput = "" + getAnswerLog().get(CURRENT_KANA).getKana() + "|" + getAnswerLog().get(CURRENT_KANA - 1).getKana() + " != " + input;
        }
        else if(discardOffset == 0) {
            //1st kana wrong
            styleKana("previousKanaWrong", "currentKana");
            styleRomaji("romajiWrong", "romajiHide");
            logOutput = "" + getAnswerLog().get(CURRENT_KANA).getKana() + " != " + input.replace(getAnswerLog().get(CURRENT_KANA - 1).getRomanji(), "");
        }
        else if(discardOffset == 1){
            //2nd kana wrong
            styleKana("previousKana", "currentKanaWrong");
            styleRomaji("romajiHide" ,"romajiWrong");
            logOutput = "" + getAnswerLog().get(CURRENT_KANA - discardOffset).getKana() + " != " + input.replace(getAnswerLog().get(CURRENT_KANA).getRomanji(), "");
        } else /* if(discardOffset == -2) */ {
            System.out.println(
                "Mate, something went wrong. We're in onWrong() and nothing is wrong… something is wrong!"
            );
        }
    }

    /**
     * Helper method that {@return}s an int indicating which input(s) is/are wrong.
     */
    private int determineDiscardOffset() {
        boolean correctStart = input.startsWith( getAnswerLog().get(CURRENT_KANA).getRomanji() );
        boolean correctEnd = input.endsWith( getAnswerLog().get(CURRENT_KANA-1).getRomanji() );
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
        //boolean correctEnd = (input.length() > 1)? input.endsWith( getAnswerLog().get(CURRENT_KANA-1).getRomanji() ) : true;
        //boolean correctEnd = (input.length() <= 1 || input.endsWith( getAnswerLog().get(CURRENT_KANA-1).getRomanji() ));

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

    //1st thing called in the parent constructor followed by initGUI()
    @Override protected void initFields() {
        question = new Label();
        previousKana = new Label();
        currentKana = new Label();
        upcomingKana = new Label();
        previousRomaji = new Label();
        currentRomaji = new Label();
        upcomingRomaji = new Label();
        questionArea = new VBox();
        answerInput = new TextField();
        answerArea = new HBox();
        layout = new BorderPane();
    }

    //2nd method called in the parent constructor followed by prepareFirstProblem()
    @Override protected Region initGUI() {
        /* Question Area */
        question.setText("Type the romaji for…");

        /* Size is currently not supported by JavaFX */
        previousKana.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        currentKana.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        upcomingKana.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        previousRomaji.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        currentRomaji.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        upcomingRomaji.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        GridPane problemBar = new GridPane();
        GridPane.setHgrow(previousKana, Priority.ALWAYS);
        GridPane.setHgrow(currentKana, Priority.ALWAYS);
        GridPane.setHgrow(upcomingKana, Priority.ALWAYS);
        GridPane.setHgrow(previousRomaji, Priority.ALWAYS);
        GridPane.setHgrow(currentRomaji, Priority.ALWAYS);
        GridPane.setHgrow(upcomingRomaji, Priority.ALWAYS);
        GridPane.setConstraints(previousKana, 0, 0);
        GridPane.setConstraints(currentKana, 1, 0);
        GridPane.setConstraints(upcomingKana, 2, 0);
        GridPane.setConstraints(previousRomaji, 0, 1);
        GridPane.setConstraints(currentRomaji, 1, 1);
        GridPane.setConstraints(upcomingRomaji, 2, 1);
        problemBar.getChildren().addAll(previousKana, currentKana, upcomingKana, previousRomaji,  currentRomaji, upcomingRomaji);

        questionArea.getChildren().addAll(question, problemBar);

        /* Answer Area */

        answerInput.setOnKeyPressed( keyEvent -> handleInput(keyEvent) ); //IntelliJ will report that this can be replaced with a method reference - it can't, don't do it
        //answerInput.requestFocus(); //not needed, since we setFocusTraversable(false) on all other nodes
        answerArea.getChildren().addAll(answerInput);
        //answerArea.setPadding(new Insets(10.0f, 10.0f, 10.f, 10.f)); //doable in JFX CSS

        layout = KanaGui.makeRegionSuitable(layout); //TODO remove dependancy on KanaGui
        layout.setTop(questionArea);
        layout.setCenter(answerArea);

        return layout;
    }

    @Override protected void applyCSS() {
        layout.getStylesheets().add(CSS_FILE_PATH);
        setCssClass(question, "question");
        setCssClass(previousKana, "previousKana");
        setCssClass(currentKana, "currentKana");
        setCssClass(upcomingKana, "upcomingKana");
        styleAllRomaji("romajiHide");
        setCssClass(questionArea, "questionArea");
        setCssClass(answerArea, "answerArea");
        setCssClass(answerInput, "answerInput");
    }

    //LAST method called in the parent constructor.
    @Override protected void prepareFirstProblem() {
        T dummy = super.randomAnswerFromOptions(getRecentlyUsedOptions());
        getAnswerLog().add(dummy);   //we add the 1st Kana 2 times, because 1 will be a dummy
        getAnswerLog().add(dummy);   //not doing this would increase the number of Kanas to be excluded
        prepareNextProblem();   //we only exclude Kana that we already have tested
        styleAllRomaji("romajiHide");
        setCssClass(previousKana, "previousKanaHide");
        setCssClass(previousRomaji, "romajiHide");
    }

    @Override protected void prepareNextProblem() {
        call_nextProblem = false;
        answerInput.clear();
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
        previousKana.setText("" + getAnswerLog().get(2).getKana());
        currentKana.setText("" + getAnswerLog().get(1).getKana());
        upcomingKana.setText("" + getAnswerLog().get(0).getKana());
        previousRomaji.setText(getAnswerLog().get(2).getRomanji());
        currentRomaji.setText(getAnswerLog().get(1).getRomanji());
        upcomingRomaji.setText(getAnswerLog().get(0).getRomanji());
    }

    @Override protected int getAmount() {
        return 3;
    }

    //when we have less than 4 items
    //the reason why we count to 4 and not 3 is because
    //otherwise we might instantly get the newly discarded one back into the queue
    //while stochastically unlikely, this could end in a prolonged phase of only seeing 3 Kana
    //in the same sequence over and over again
    //-> [a-b-c], discard c, draw c
    //-> [c-a-b], discard b, draw b
    //-> [b-c-a], discard a, draw a
    //-> [a-b-c], discard c, draw c
    // unique combinations: abc
    //... ad infinitum...
    //the worst case for size = 4 would be something like that
    //where d = dummy / the invisible/unshown kana
    //-> [a-b-c]-d, discard d, draw d
    //-> [d-a-b]-c, discard c, draw c
    //-> [c-d-a]-b, discard b, draw b
    //-> [b-c-d]-a, discard a, draw a
    //-> [a-b-c]-d, discard d, draw d
    // unique combinations: abc, abd, acd, bcd;
    //... ad infinitum...
    @Override protected int getRelevantLogDepth() {
        return getAmount()+1;
    }

    /* CSS util methods */

    /**
     * Styles the previous kana its romaji according to {@param kanaCssStyle} and {@param romajiCssStyle}.
     */
    private void stylePrevious(String kanaCssStyle, String romajiCssStyle) {
        styleKana(kanaCssStyle, "currentKana", "upcomingKana");
        styleRomaji(romajiCssStyle, "romajiHide", "romajiHide");
    }

    /**
     * Styles the previous & current kana according to {@param previousCssStyle} and {@param currentCssStyle}.
     * The upcoming kana will be styled with the default style.
     */
    private void styleKana(String previousCssStyle, String currentCssStyle) {
        styleKana(previousCssStyle, currentCssStyle, "upcomingKana");
    }

    /**
     * Styles the previous, currentCssStyle & upcomingCssStyle kana according to {@param previous}, {@param currentCssStyle} and {@param upcomingCssStyle}.
     */
    private void styleKana(String previous, String currentCssStyle, String upcomingCssStyle) {
        setCssClass(previousKana, previous);
        setCssClass(currentKana, currentCssStyle);
        setCssClass(upcomingKana, upcomingCssStyle);
    }

    private void styleAllRomaji(String cssStyle) {
        styleRomaji(cssStyle, cssStyle, cssStyle);
    }

    private void styleRomaji(String previousCssStyle, String currentCssStyle) {
        styleRomaji(previousCssStyle, currentCssStyle, "romajiHide");
    }

    private void styleRomaji(String previousCssStyle, String currentCssStyle, String upcomingCssStyle) {
        if(SHOW_ROMAJI) {
            setCssClass(previousRomaji, (previousCssStyle.equals("romajiHide"))? "romaji" : previousCssStyle);
            setCssClass(currentRomaji, (currentCssStyle.equals("romajiHide"))? "romaji" : currentCssStyle);
            setCssClass(upcomingRomaji, (upcomingCssStyle.equals("romajiHide"))? "romaji" : upcomingCssStyle);
        } else {
            setCssClass(previousRomaji, previousCssStyle);
            setCssClass(currentRomaji, currentCssStyle);
            setCssClass(upcomingRomaji, upcomingCssStyle);
        }
    }

    /**
     * TODO Used in TypeTheRomaji.handleInput(KeyEvent ke): 1
     */
    private Yoon isYoon(Kana lead, Kana post) {
        Yoon result = null;
        if(lead instanceof Hiragana && post instanceof Hiragana) {
            switch((Hiragana)lead) {
                case KI:case GI:case SHI:case JI:case CHI:case DZI:case NI:case HI:case BI:case PI:case MI:case RI:
                    break;
                default: return null;
            }
            switch((Hiragana)post) {
                case YA:case YU:case YO:
                    break;
                default: return null;
            }
            result = HiraganaYoon.match((Hiragana)lead, (Hiragana)post);
        } else if(lead instanceof Katakana && post instanceof Katakana){
            switch((Katakana)lead) {
                case KI:case GI:case SHI:case JI:case CHI:case DZI:case NI:case HI:case BI:case PI:case MI:case RI:
                    break;
                default: return null;
            }
            switch((Katakana)post) {
                case YA:case YU:case YO:
                    break;
                default: return null;
            }
            result = KatakanaYoon.match((Katakana)lead, (Katakana)post);
        }
        return result;
    }
}