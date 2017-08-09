package moe.edotee.kanakana.utils;
import javafx.scene.Node;
/**
 * Holds all CSS classes as enums.
 * Generated by {@link HelperClass_Generator_CSS}
 * @author edotee
 */
public interface CSS {
    enum main implements CSS {
        buttonBar,
        kanaPickerButton,
        ;

        @Override public String cssClass() {
            return name();
        }
    }
    enum pickKana implements CSS {
        question,
        inQuestion,
        questionArea,
        answerArea,
        buttonPoolButton,
        ;

        @Override public String cssClass() {
            return name();
        }
    }
    enum typeRomaji implements CSS {
        previousKana,
        previousKanaHide,
        previousKanaDouble,
        previousKanaYoon,
        previousKanaMissedYoon,
        previousKanaWrong,
        previousKanaFoul,
        currentKana,
        currentKanaDouble,
        currentKanaYoon,
        currentKanaFoul,
        currentKanaWrong,
        upcomingKana,
        romaji,
        romajiHide,
        romajiYoon,
        romajiMissedYoon,
        romajiWrong,
        question,
        questionArea,
        answerArea,
        answerInput,
        ;

        @Override public String cssClass() {
            return name();
        }
    }
    enum writeKana implements CSS {
        answerArea,
        question,
        questionArea,
        labelPoolLabel,
        labelPoolLabelHide,
        buttonPoolButton,
        ;

        @Override public String cssClass() {
            return name();
        }
    }
    String cssClass();
    static <N extends Node, C extends CSS> N style(N node, C style) {
        return style(node, style, true);
    }
    static <N extends Node, C extends CSS> N style(N node, C style, boolean clear) {
        if(clear) node.getStyleClass().clear();
        node.getStyleClass().add(style.cssClass());
        return node;
    }
}