package moe.edotee.kanakana.gui.elements;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;

import moe.edotee.kanakana.kana.Kana;
import moe.edotee.kanakana.kana.Kana.Hiragana;
import moe.edotee.kanakana.kana.Romaji;
import moe.edotee.kanakana.utils.CSS;
import moe.edotee.kanakana.utils.Options;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author edotee
 */
public class KanaPicker<T extends Kana> extends Tab {

    private HashMap<ToggleButton, T> kanaButtonPool;

    private KanaPicker(String title, Node content, HashMap<ToggleButton, T> buttonPool) {
        super(title, content);
        this.kanaButtonPool = buttonPool;
        setClosable(false);
    }

    public static <T extends Kana> KanaPicker create(T tokenKana) {
        String title = (tokenKana instanceof Hiragana)? "Hiragana" : "Katakana";
        HashMap<ToggleButton, T> buttonPool = new HashMap<>();
        GridPane kanaTable = new GridPane();
        kanaTable.setPrefSize(Options.WIDTH, Options.HEIGHT);
        kanaTable.setAlignment(Pos.CENTER);

        Romaji[][] romajiGrid = Romaji.getRomajiGrid();
        int xCord = 0;
        int yCord = 0;
        Romaji romaji;
        ToggleButton button;
        for(; yCord < romajiGrid.length; yCord++) {
            for(; xCord < romajiGrid[0].length; xCord++) {
                romaji = romajiGrid[yCord][xCord];
                if(romaji != null) {
                    T kana = Kana.getKana(romaji, tokenKana);
                    button = CSS.style(new ToggleButton(""+kana.getKana()), CSS.main.kanaPickerButton, false);
                    buttonPool.put(button, kana);

                    for(String r : ((tokenKana instanceof Hiragana)? Options.Default.dHiragana : Options.Default.dKatakana)) {
                        if(romaji.getRomaji().equals(r)) {
                            button.setSelected(true);
                            break;
                        }
                    }
                    GridPane.setConstraints(button, xCord, yCord);
                    kanaTable.getChildren().add(button);
                }
                if(xCord == 0 && yCord == 5) break; //5|0 = n = last entry
            }
            xCord = 0;
        }
        return new KanaPicker<>(title, kanaTable, buttonPool);
    }

    public HashSet<T> fetchChoices() {
        HashSet<T> result = new HashSet<>();
        Set<ToggleButton> keys = kanaButtonPool.keySet();
        for(ToggleButton key : keys)
            if(key.isSelected())
                result.add(kanaButtonPool.get(key));
        return result;
    }
}