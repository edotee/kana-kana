package kana;

import kana.KanaHelper.Consonant;
import kana.KanaHelper.Vowel;
import static kana.KanaHelper.Consonant.NOCON;
import static kana.KanaHelper.Vowel.NOVOW;
/**
 * @author edotee
 */
public interface Kana<T extends Kana> {
    char getKana();
    Consonant getConsonant();
    Vowel getVowel();
    boolean hasIrregularReading();
    String getIrregularReading();

    default String getRomanji() {
        if(hasIrregularReading())
            return getIrregularReading();

        String result = "";
        if(getConsonant() != NOCON) result += getConsonant();
        if(getVowel() != NOVOW) result += getVowel();
        return result;
    }
}