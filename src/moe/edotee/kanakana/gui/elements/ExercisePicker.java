package moe.edotee.kanakana.gui.elements;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import moe.edotee.kanakana.kana.Kana.Hiragana;
import moe.edotee.kanakana.kana.Kana.Katakana;
import moe.edotee.kanakana.logic.KanaExercise;
import moe.edotee.kanakana.logic.PickTheKana;
import moe.edotee.kanakana.logic.TypeTheRomaji;
import moe.edotee.kanakana.logic.WriteTheKana;
import moe.edotee.kanakana.utils.Options;
import org.controlsfx.control.SegmentedButton;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author edotee
 */
public class ExercisePicker extends Tab {

    //private HashSet<ToggleButton> exerciseButtonPool;
    private ToggleButton
            pickHiragana, pickKatakana,
            typeHiragana, typeKatakana,
            writeHiragana, writeKatakana;

    public ExercisePicker() {
        setText("Exercises");
        setClosable(false);

        pickHiragana = new ToggleButton("Hiragana");
        pickKatakana = new ToggleButton("Katakana");
        typeHiragana = new ToggleButton("Hiragana");
        typeKatakana = new ToggleButton("Katakana");
        writeHiragana = new ToggleButton("Hiragana");
        writeKatakana = new ToggleButton("Katakana");

        //exerciseButtonPool = new HashSet<>();
        VBox selectionLayout = new VBox();
        selectionLayout.getChildren().addAll(
                makeOption("Pick the Kana", pickHiragana, pickKatakana),
                makeOption("Type the Romaji", typeHiragana, typeKatakana),
                makeOption("Write the Kana", writeHiragana, writeKatakana)
        );
        setContent(selectionLayout);
    }
    private HBox makeOption(String name, ToggleButton left, ToggleButton right) {
        //exerciseButtonPool.add(left);
        //exerciseButtonPool.add(right);

        Label optionLabel = new Label(name);
        optionLabel.setTextAlignment(TextAlignment.CENTER);

        HBox configurator = new HBox();
        configurator.setAlignment(Pos.CENTER);
        configurator.getChildren().addAll(optionLabel, meldButtons(left, right));

        return configurator;
    }
    private SegmentedButton meldButtons(ToggleButton left, ToggleButton right) {
        SegmentedButton optionBar = new SegmentedButton();
        optionBar.setToggleGroup(null);
        optionBar.getButtons().addAll(left, right);
        return optionBar;
    }

    //TODO find a variable solution
    public ArrayList<KanaExercise> getExercises(HashSet<Hiragana> pickedHiragana, HashSet<Katakana> pickedKatakana) {
        ArrayList<KanaExercise> exerciseList = new ArrayList<>();
        if(pickHiragana.isSelected()) exerciseList.add(new PickTheKana<>(pickedHiragana, Options.CSS.PickKana));
        if(pickKatakana.isSelected()) exerciseList.add(new PickTheKana<>(pickedKatakana, Options.CSS.PickKana));
        if(typeHiragana.isSelected()) exerciseList.add(new TypeTheRomaji<>( pickedHiragana, Options.CSS.TypeKana));
        if(typeKatakana.isSelected()) exerciseList.add(new TypeTheRomaji<>( pickedKatakana, Options.CSS.TypeKana));
        if(writeHiragana.isSelected()) exerciseList.add(new WriteTheKana<>( pickedHiragana, Options.CSS.WriteKana));
        if(writeKatakana.isSelected()) exerciseList.add(new WriteTheKana<>( pickedKatakana, Options.CSS.WriteKana));
        return exerciseList;
    }
}