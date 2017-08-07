package kana;

import kana.hiragana.Hiragana;
import kana.katakana.Katakana;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static kana.KanaHelper.Consonant.*;
import static kana.KanaHelper.Vowel.*;

/**
 * TODO preparation notes - only used by GuiHelper(2 instances), KanaGui(1 instance) and TypeTheRomaji(1 instance)
 * @author edotee
 */
public final class KanaHelper {

    public interface Letter {
        String toString();
        char toChar();
    }

    public enum Vowel implements Letter {
        a('a'),
        i('i'),
        u('u'),
        e('e'),
        o('o'),
        NOVOW('∅')
        ;

        final char myChar;

        Vowel(char myChar) {
            this.myChar = myChar;
        }

        public String toString() {
            return "" + myChar;
        }

        public char toChar() {
            return myChar;
        }
    }

    public enum Consonant implements Letter {
        k('k'),
        g('g'),
        s('s'),
        z('z'),
        t('t'),
        d('d'),
        n('n'),
        h('h'),
        b('b'),
        p('p'),
        m('m'),
        y('y'),
        r('r'),
        w('w'),
        NOCON('∅')
        ;

        final char myChar;

        Consonant(char myChar) {
            this.myChar = myChar;
        }

        public String toString() {
            return "" + myChar;
        }

        public char toChar() {
            return myChar;
        }
    }

    /* General */

    /**
     * TODO Used in GuiHelper: 1
     */
    public static HashMap<Letter, ArrayList<Katakana>> katakanaBaseByVowel() {
        HashMap<Letter, ArrayList<Katakana>> result = new HashMap<>();
        addToHashMap(result, a, Katakana.A, Katakana.KA, Katakana.SA, Katakana.TA, Katakana.NA, Katakana.HA, Katakana.MA, Katakana.YA, Katakana.RA, Katakana.WA);

        addToHashMap(result, i, Katakana.I, Katakana.KI, Katakana.SHI, Katakana.CHI, Katakana.NI, Katakana.HI, Katakana.MI, null, Katakana.RI, null);
        addToHashMap(result, u, Katakana.U, Katakana.KU, Katakana.SU, Katakana.TSU, Katakana.NU, Katakana.FU, Katakana.MU, Katakana.YU, Katakana.RU, null);
        addToHashMap(result, e, Katakana.E, Katakana.KE, Katakana.SE, Katakana.TE, Katakana.NE, Katakana.HE, Katakana.ME, null, Katakana.RE, null);
        addToHashMap(result, o, Katakana.O, Katakana.KO, Katakana.SO, Katakana.TO, Katakana.NO, Katakana.HO, Katakana.MO, Katakana.YO, Katakana.RO, Katakana.WO);
        addToHashMap(result, NOVOW, Katakana.N);
        return result;
    }

    /**
     * TODO Used in GuiHelper: 1
     */
    public static HashMap<Letter, ArrayList<Hiragana>> hiraganaBaseByVowel() {
        HashMap<Letter, ArrayList<Hiragana>> result = new HashMap<>();
        addToHashMap(result, a, Hiragana.A, Hiragana.KA, Hiragana.SA, Hiragana.TA, Hiragana.NA, Hiragana.HA, Hiragana.MA, Hiragana.YA, Hiragana.RA, Hiragana.WA);
        addToHashMap(result, i, Hiragana.I, Hiragana.KI, Hiragana.SHI, Hiragana.CHI, Hiragana.NI, Hiragana.HI, Hiragana.MI, null, Hiragana.RI, null);
        addToHashMap(result, u, Hiragana.U, Hiragana.KU, Hiragana.SU, Hiragana.TSU, Hiragana.NU, Hiragana.FU, Hiragana.MU, Hiragana.YU, Hiragana.RU, null);
        addToHashMap(result, e, Hiragana.E, Hiragana.KE, Hiragana.SE, Hiragana.TE, Hiragana.NE, Hiragana.HE, Hiragana.ME, null, Hiragana.RE, null);
        addToHashMap(result, o, Hiragana.O, Hiragana.KO, Hiragana.SO, Hiragana.TO, Hiragana.NO, Hiragana.HO, Hiragana.MO, Hiragana.YO, Hiragana.RO, Hiragana.WO);
        addToHashMap(result, NOVOW, Hiragana.N);
        return result;
    }

    /**
     * TODO Used in KanaGui: 1
     */
    public static ArrayList<Hiragana> hiraganaBaseValues() {
        return kanaFlatten(hiraganaBaseByConsonant());
    }


    //only used internally

    /**
     * used by all 3 kanaToBaseX methods
     */
    private static <T extends Kana> void addToHashMap(HashMap<Letter, ArrayList<T>> target, Letter key, T... elements) {
        ArrayList<T> kanaList = new ArrayList<>();
        Collections.addAll(kanaList, elements);
        target.put(key, kanaList);
    }

    /**
     * only used by hirganaBaseValues()
     */
    private static <T extends Kana> ArrayList<T> kanaFlatten(HashMap<Letter, ArrayList<T>> kanaMap) {
        ArrayList<T> result = new ArrayList<T>();
        Consonant[] consonantKeysss = Consonant.values();

        ArrayList<T> list;
        for(Consonant consonantKey  : consonantKeysss) {
            list = kanaMap.get(consonantKey);
            if(list != null) {
                result.addAll(list);
            }
        }
        while(result.remove(null)) {  }

        /*
        ArrayList<T> list;
        for(Consonant consonantKey  : consonantKeysss) {
            list = kanaMap.get(consonantKey);
            if(list != null) {
                for(T kana : list) {
                    if(kana != null) {
                        result.add(kana);
                    }
                }
            }
        }
        */

        return result;
    }

    /**
     * only used by hirganaBaseValues()
     */
    private static HashMap<Letter, ArrayList<Hiragana>> hiraganaBaseByConsonant() {
        HashMap<Letter, ArrayList<Hiragana>> result = new HashMap<>();
        addToHashMap(result, NOCON, Hiragana.A, Hiragana.I, Hiragana.U, Hiragana.E, Hiragana.O);
        addToHashMap(result, k, Hiragana.KA, Hiragana.KI, Hiragana.KU, Hiragana.KE, Hiragana.KO);
        addToHashMap(result, s, Hiragana.SA, Hiragana.SHI, Hiragana.SU, Hiragana.SE, Hiragana.SO);
        addToHashMap(result, d, Hiragana.TA, Hiragana.CHI, Hiragana.TSU, Hiragana.TE, Hiragana.TO);
        addToHashMap(result, n, Hiragana.NA, Hiragana.NI, Hiragana.NU, Hiragana.NE, Hiragana.NO);
        addToHashMap(result, h, Hiragana.HA, Hiragana.HI, Hiragana.FU, Hiragana.HE, Hiragana.HO);
        addToHashMap(result, m, Hiragana.MA, Hiragana.MI, Hiragana.MU, Hiragana.ME, Hiragana.MO);
        addToHashMap(result, y, Hiragana.YA, null, Hiragana.YU, null, Hiragana.YO);
        addToHashMap(result, r, Hiragana.RA, Hiragana.RI, Hiragana.RU, Hiragana.RE, Hiragana.RO);
        addToHashMap(result, w, Hiragana.WA, null, null, null, Hiragana.WO);
        return result;
    }

    /* only used by flatten
    private static HashMap<Letter, ArrayList<Hiragana>> hiraganaDakutenByConsonant() {
        HashMap<Letter, ArrayList<Hiragana>> result = new HashMap<>();
        addToHashMap(result, g, Hiragana.GA, Hiragana.GI, Hiragana.GU, Hiragana.GE, Hiragana.GO);
        addToHashMap(result, z, Hiragana.ZA, Hiragana.JI, Hiragana.ZU, Hiragana.ZE, Hiragana.ZO);
        addToHashMap(result, d, Hiragana.DA, Hiragana.DZI, Hiragana.DZU, Hiragana.DE, Hiragana.DO);
        addToHashMap(result, b, Hiragana.BA, Hiragana.BI, Hiragana.BU, Hiragana.BE, Hiragana.BO);
        addToHashMap(result, p, Hiragana.PA, Hiragana.PI, Hiragana.PU, Hiragana.PE, Hiragana.PO);
        return result;
    }
    */

    /* only used by "only used by 'only used by never used'"
    private static ArrayList<Hiragana> hiraganaValues() {
        return kanaOrder(hiraganaBaseByConsonant(), hiraganaDakutenByConsonant());
    }

    private static HashMap<Letter, ArrayList<Katakana>> katakanaBaseByConsonant() {
        HashMap<Letter, ArrayList<Katakana>> result = new HashMap<>();
        addToHashMap(result, NOCON, Katakana.A, Katakana.I, Katakana.U, Katakana.E, Katakana.O);
        addToHashMap(result, k, Katakana.KA, Katakana.KI, Katakana.KU, Katakana.KE, Katakana.KO);
        addToHashMap(result, s, Katakana.SA, Katakana.SHI, Katakana.SU, Katakana.SE, Katakana.SO);
        addToHashMap(result, d, Katakana.TA, Katakana.CHI, Katakana.TSU, Katakana.TE, Katakana.TO);
        addToHashMap(result, n, Katakana.NA, Katakana.NI, Katakana.NU, Katakana.NE, Katakana.NO);
        addToHashMap(result, h, Katakana.HA, Katakana.HI, Katakana.FU, Katakana.HE, Katakana.HO);
        addToHashMap(result, m, Katakana.MA, Katakana.MI, Katakana.MU, Katakana.ME, Katakana.MO);
        addToHashMap(result, y, Katakana.YA, null, Katakana.YU, null, Katakana.YO);
        addToHashMap(result, r, Katakana.RA, Katakana.RI, Katakana.RU, Katakana.RE, Katakana.RO);
        addToHashMap(result, w, Katakana.WA, null, null, null, Katakana.WO);
        return result;
    }

    private static HashMap<Letter, ArrayList<Katakana>> katakanaDakutenByConsonant() {
        HashMap<Letter, ArrayList<Katakana>> result = new HashMap<>();
        addToHashMap(result, g, Katakana.GA, Katakana.GI, Katakana.GU, Katakana.GE, Katakana.GO);
        addToHashMap(result, z, Katakana.ZA, Katakana.JI, Katakana.ZU, Katakana.ZE, Katakana.ZO);
        addToHashMap(result, d, Katakana.DA, Katakana.DZI, Katakana.DZU, Katakana.DE, Katakana.DO);
        addToHashMap(result, b, Katakana.BA, Katakana.BI, Katakana.BU, Katakana.BE, Katakana.BO);
        addToHashMap(result, p, Katakana.PA, Katakana.PI, Katakana.PU, Katakana.PE, Katakana.PO);
        return result;
    }

    private static <T extends Kana> ArrayList<T> kanaOrder(HashMap<Letter, ArrayList<T>> base, HashMap<Letter, ArrayList<T>> dakuten) {
        ArrayList<T> result = new ArrayList<>();
        Consonant[] consonants = Consonant.values();
        ArrayList<T> temp;
        for(Consonant key : consonants) {
            temp = base.get(key);
            if(temp == null)
                temp = dakuten.get(key);
            if(temp != null)
                result.addAll(temp);
        }
        while(result.remove(null)) { } //lol…

        return result;
                }
    */

    /* only used by "only used by never used"
    private static ArrayList<Katakana> katakanaValues() {
        return kanaOrder(katakanaBaseByConsonant(), katakanaDakutenByConsonant());
    }

    private static Hiragana kanaToHiragana(char searchThiskana) {
        for(Hiragana kana : hiraganaValues())
            if(kana.getKana() == searchThiskana)
                return kana;
        return null;
    }
    */

    /* only used by never used
    private static String hiraganaToRomaji(char searchThiskana) {
        Hiragana result = kanaToHiragana(searchThiskana);
        return (result == null)? "" : result.getRomanji();
    }

    private static Katakana kanaToKatakana(char searchThiskana) {
        for(Katakana kana : katakanaValues())
            if(kana.getKana() == searchThiskana)
                return kana;
        return null;
    }

    private static Hiragana romajiToHiragana(String searchThisRomaji) {
        Hiragana result;
        try {
            result = Hiragana.valueOf(searchThisRomaji.trim().toUpperCase());
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
            result = null;
        }
        return result;
    }

    private static Katakana romajiToKatakana(String searchThisRomaji) {
        Katakana result;
        try {
            result = Katakana.valueOf(searchThisRomaji.trim().toUpperCase());
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
            result = null;
        }
        return result;
    }
    */

    /* never used
    public static HashMap<Letter, ArrayList<Hiragana>> hiraganaDakutenByVowel() {
        HashMap<Letter, ArrayList<Hiragana>> result = new HashMap<>();
        addToHashMap(result, a, Hiragana.GA, Hiragana.ZA, Hiragana.DA, Hiragana.BA, Hiragana.PA);
        addToHashMap(result, i, Hiragana.GI, Hiragana.JI, Hiragana.DZI, Hiragana.BI, Hiragana.PI);
        addToHashMap(result, u, Hiragana.GU, Hiragana.ZU, Hiragana.DZU, Hiragana.BU, Hiragana.PU);
        addToHashMap(result, e, Hiragana.GE, Hiragana.ZE, Hiragana.DE, Hiragana.BE, Hiragana.PE);
        addToHashMap(result, o, Hiragana.GO, Hiragana.ZO, Hiragana.DO, Hiragana.BO, Hiragana.PO);
        return result;
    }

    public static ArrayList<Hiragana> hiraganaDakutenValues() {
        return kanaFlatten(hiraganaDakutenByConsonant());
    }

    public static String kanaToRomaji(char searchThiskana) {
        //we only search one kana syllabary since they should be identical
        //in case we should ever need to look through both - uncomment the following piece of code

        //String romaji = hiraganaToRomaji(searchThiskana);
        //if(romaji.length() == 0)  //
        //    romaji = katakanaToRomaji(searchThiskana);
        //return romaji;

        return hiraganaToRomaji(searchThiskana);
    }

    public static String katakanaToRomaji(char searchThiskana) {
        Katakana result = kanaToKatakana(searchThiskana);
        return (result == null)? "" : result.getRomanji();
    }

    public static Katakana hiraganaToKatakana(Hiragana hiragana) {
        return romajiToKatakana(hiragana.getRomanji());
    }

    public static Hiragana katakanaToHiragana(Hiragana katakana) {
        return romajiToHiragana(katakana.getRomanji());
    }
    */

    /*
    public static HashMap<Letter, ArrayList<Katakana>> katakanaDakutenByVowel() {
        HashMap<Letter, ArrayList<Katakana>> result = new HashMap<>();
        addToHashMap(result, a, Katakana.GA, Katakana.ZA, Katakana.DA, Katakana.BA, Katakana.PA);
        addToHashMap(result, i, Katakana.GI, Katakana.JI, Katakana.DZI, Katakana.BI, Katakana.PI);
        addToHashMap(result, u, Katakana.GU, Katakana.ZU, Katakana.DZU, Katakana.BU, Katakana.PU);
        addToHashMap(result, w, Katakana.GE, Katakana.ZE, Katakana.DE, Katakana.BE, Katakana.PE);
        addToHashMap(result, o, Katakana.GO, Katakana.ZO, Katakana.DO, Katakana.BO, Katakana.PO);
        return result;
    }
    public static ArrayList<Katakana> katakanaBaseValues() {
        return kanaFlatten(katakanaBaseByConsonant());
    }

    public static ArrayList<Katakana> katakanaDakutenValues() {
        return kanaFlatten(katakanaDakutenByConsonant());
    }
    */
}