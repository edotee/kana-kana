package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import kana.Kana;
import kana.KanaHelper;
import kana.KanaHelper.Letter;
import kana.KanaHelper.Vowel;
import kana.hiragana.Hiragana;
import kana.katakana.Katakana;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author edotee
 */
public class KanaPicker {

    public static GridPane getHiraganaPicker(HashMap<ToggleButton, Hiragana> hiraganaIndex) {
        return getKanaPicker(hiraganaIndex, KanaHelper.hiraganaBaseByVowel());
    }

    public static GridPane getKatakanaPicker(HashMap<ToggleButton, Katakana> katakanaIndex) {
        return getKanaPicker(katakanaIndex, KanaHelper.katakanaBaseByVowel());
    }

    public static <T extends Kana> GridPane getKanaPicker(HashMap<ToggleButton, T> guiButtonKanaIndex, HashMap<Letter, ArrayList<T>> kanaMap) {
        GridPane kanaTable = KanaGui.makeRegionSuitable(new GridPane());
        kanaTable.setAlignment(Pos.CENTER);
        Letter[] keys = Vowel.values();
        ArrayList<T> list;
        int x, y;
        y = 0;
        for(Letter key : keys) {
            list = kanaMap.get(key);
            if(list != null) {
                x = 0;
                for(T kana : list) {
                    if(kana != null) {
                        ToggleButton button = new ToggleButton("" + kana.getKana());
                        GridPane.setConstraints(button, x, y);
                        button.setStyle("-fx-font: 36 arial; -fx-background-radius: 0;");
                        kanaTable.getChildren().add(button);
                        guiButtonKanaIndex.put(button, kana);
                    }
                    x++;
                }
                y++;
            }
        }
        return kanaTable;
    }

    public static TabPane getTabbedKanaPicker(HashMap<ToggleButton, Hiragana> hiraganaIndex, HashMap<ToggleButton, Katakana> katakanaIndex) {
        Tab hiraganaTab = new Tab("Hiragana", getHiraganaPicker(hiraganaIndex));
        hiraganaTab.setClosable(false);

        Tab katakanaTab = new Tab("Katakana", getKatakanaPicker(katakanaIndex));
        katakanaTab.setClosable(false);

        TabPane result = KanaGui.makeRegionSuitable(new TabPane());
        result.getTabs().addAll(hiraganaTab, katakanaTab);
        return result;
    }

}
