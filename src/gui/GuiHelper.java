package gui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import kana.Kana;
import kana.KanaHelper;
import kana.KanaHelper.Letter;
import kana.KanaHelper.Vowel;
import kana.hiragana.Hiragana;
import kana.katakana.Katakana;
import org.controlsfx.control.SegmentedButton;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author edotee
 */
public class GuiHelper {

    private static <T extends Kana> GridPane getKanaPicker(HashMap<ToggleButton, T> guiButtonKanaIndex, HashMap<Letter, ArrayList<T>> kanaMap, String[] defaultSelected) {
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

                        for(String romaji : defaultSelected) {
                            if(kana.getRomanji().equals(romaji)) {
                                button.setSelected(true);
                                break;
                            }
                        }
                    }
                    x++;
                }
                y++;
            }
        }
        return kanaTable;
    }

    public static GridPane getHiraganaPicker(HashMap<ToggleButton, Hiragana> hiraganaIndex) {
        return getKanaPicker(hiraganaIndex, KanaHelper.hiraganaBaseByVowel(),
                "a i u e o ka ki ku ke ko sa shi su se so ta chi tsu te to na ni nu ne no n ha hi fu he ho ma mi mu me mo ya yu yo ra ri ru re ro wa wo".trim().split(" ")
        );
    }

    public static GridPane getKatakanaPicker(HashMap<ToggleButton, Katakana> katakanaIndex) {
        return getKanaPicker(katakanaIndex, KanaHelper.katakanaBaseByVowel(),
                "a i u e o ka ki ni ha fu he mi ya yu yo ri".trim().split(" ")
        );
    }

    public static Tab getHiraganaPickerTab(HashMap<ToggleButton, Hiragana> hiraganaIndex) {
        Tab hiraganaTab = new Tab("Hiragana", getHiraganaPicker(hiraganaIndex));
        hiraganaTab.setClosable(false);
        return hiraganaTab;
    }

    public static Tab getKatakanaPickerTab(HashMap<ToggleButton, Katakana> katakanaIndex) {
        Tab katakanaTab = new Tab("Katakana", getKatakanaPicker(katakanaIndex));
        katakanaTab.setClosable(false);
        return katakanaTab;
    }

    public static TabPane getTabbedKanaPicker(HashMap<ToggleButton, Hiragana> hiraganaIndex, HashMap<ToggleButton, Katakana> katakanaIndex) {
        TabPane result = KanaGui.makeRegionSuitable(new TabPane());
        result.getTabs().addAll(getHiraganaPickerTab(hiraganaIndex), getKatakanaPickerTab(katakanaIndex));
        return result;
    }

    public static Tab getExercisePickerTab() {
        VBox exerciseSelectorLayout = new VBox();
        exerciseSelectorLayout.getChildren().addAll(
                makeConfiguratorFor("Pick the Right Kana"),
                makeConfiguratorFor("Write the Right Kana"),
                makeConfiguratorFor("Type the Romaji")
        );
        Tab exerciseSelector = new Tab("Exercises");
        exerciseSelector.setClosable(false);
        exerciseSelector.setContent(exerciseSelectorLayout);
        return exerciseSelector;
    }

    private static HBox makeConfiguratorFor(String name) {
        ToggleButton optionForHiragana = new ToggleButton("Hiragana");
        ToggleButton optionForKatakana = new ToggleButton("Katakana");
        SegmentedButton optionBar = new SegmentedButton();
        optionBar.setToggleGroup(null);
        optionBar.getButtons().addAll(optionForHiragana, optionForKatakana);

        Label optionLabel = new Label(name);;
        optionLabel.setTextAlignment(TextAlignment.CENTER);
        HBox configurator = new HBox();
        configurator.setAlignment(Pos.CENTER);
        configurator.getChildren().addAll(optionLabel, optionBar);

        return configurator;
    }
}
