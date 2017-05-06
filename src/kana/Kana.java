package kana;

/**
 * @author edotee
 */
public interface Kana<T extends Kana> {
    char NIL = '∅'; // implicitly public final static
    char getKana();
    char getConsonant();
    char getVowel();
    boolean hasIrregularReading();
    String getIrregularReading();

    default String getRomanji() {
        if(hasIrregularReading())
            return getIrregularReading();

        String result = "";
        if(getConsonant() != NIL) result += getConsonant();
        if(getVowel() != NIL) result += getVowel();
        return result;
    }
}
