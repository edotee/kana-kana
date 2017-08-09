package moe.edotee.kanakana.kana;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static moe.edotee.kanakana.kana.Dakuten.MARU;
import static moe.edotee.kanakana.kana.Dakuten.TENTEN;

/**
 * @author edotee
 */
public interface Kana {

    char getKana();
    Romaji getRomajiEnum();
    default String getRomaji(){
        return getRomajiEnum().getRomaji();
    }

    enum Hiragana implements Kana {
        //TODO Hiragana chars a cheesed from copy-pasting - katakana-96
        // a i u e o
        A('ア', Romaji.A),
        I('イ', Romaji.I),
        U('ウ', Romaji.U),
        E('エ', Romaji.E),
        O('オ', Romaji.O),

        // k s t n h m y r w
        KA('カ', Romaji.KA),
        KI('キ', Romaji.KI),
        KU('ク', Romaji.KU),
        KE('ケ', Romaji.KE),
        KO('コ', Romaji.KO),

        GA('ガ', Romaji.GA, KA, TENTEN),
        GI('ギ', Romaji.GI, KI, TENTEN),
        GU('グ', Romaji.GU, KU, TENTEN),
        GE('ゲ', Romaji.GE, KE, TENTEN),
        GO('ゴ', Romaji.GO, KO, TENTEN),

        SA('サ', Romaji.SA),
        SI('シ', Romaji.SI),
        SU('ス', Romaji.SU),
        SE('セ', Romaji.SE),
        SO('ソ', Romaji.SO),

        ZA('ザ', Romaji.ZA, SA, TENTEN),
        JI('ジ', Romaji.ZI, SI, TENTEN),
        ZU('ズ', Romaji.ZU, SU, TENTEN),
        ZE('ゼ', Romaji.ZE, SE, TENTEN),
        ZO('ゾ', Romaji.ZO, SO, TENTEN),

        TA('タ', Romaji.TA),
        TI('チ', Romaji.TI),
        TU('ツ', Romaji.TU),
        TE('テ', Romaji.TE),
        TO('ト', Romaji.TO),

        DA('ダ', Romaji.DA, TA, TENTEN),
        DI('ヂ', Romaji.DI, TI, TENTEN),
        DU('ヅ', Romaji.DU, TU, TENTEN),
        DE('デ', Romaji.DE, TE, TENTEN),
        DO('ド', Romaji.DO, TO, TENTEN),

        NA('ナ', Romaji.NA),
        NI('ニ', Romaji.NI),
        NU('ヌ', Romaji.NU),
        NE('ネ', Romaji.NE),
        NO('ノ', Romaji.NO),

        HA('ハ', Romaji.HA),
        HI('ヒ', Romaji.HI),
        HU('フ', Romaji.HU),
        HE('ヘ', Romaji.HE),
        HO('ホ', Romaji.HO),

        BA('バ', Romaji.BA, HA, TENTEN),
        BI('ビ', Romaji.BI, HI, TENTEN),
        BU('ブ', Romaji.BU, HU, TENTEN),
        BE('ベ', Romaji.BE, HE, TENTEN),
        BO('ボ', Romaji.BO, HO, TENTEN),

        PA('パ', Romaji.PO, HA, MARU),
        PI('ピ', Romaji.PI, HI, MARU),
        PU('プ', Romaji.PU, HU, MARU),
        PE('ペ', Romaji.PE, HE, MARU),
        PO('ポ', Romaji.PO, HO, MARU),

        MA('マ', Romaji.MA),
        MI('ミ', Romaji.MI),
        MU('ム', Romaji.MU),
        ME('メ', Romaji.ME),
        MO('モ', Romaji.MO),

        YA('ヤ', Romaji.YA),
        YU('ユ', Romaji.YU),
        YO('ヨ', Romaji.YO),

        RA('ラ', Romaji.RA),
        RI('リ', Romaji.RI),
        RU('ル', Romaji.RU),
        RE('レ', Romaji.RE),
        RO('ロ', Romaji.RO),

        WA('ワ', Romaji.WA),
        //WI('ヰ', Romaji.WI),
        //WE('ヱ', Romaji.WE),
        WO('ヲ', Romaji.WO),

        N('ン', Romaji.N),
        ;

        final char kana;
        final Romaji romaji;

        Hiragana(char kana, Romaji romaji) {
            this.kana = (char)(kana-96);
            this.romaji = romaji;
        }

        Hiragana(char kana, Romaji romaji, Hiragana baseKana, Dakuten dakuten) {
            this(kana, romaji);
        }

        //Inherited Methods
        @Override public char getKana() { return kana; }
        @Override public Romaji getRomajiEnum() { return romaji; }
    }
    enum Katakana implements Kana {
        // a i u e o
        A('ア', Romaji.A),
        I('イ', Romaji.I),
        U('ウ', Romaji.U),
        E('エ', Romaji.E),
        O('オ', Romaji.O),

        // k s t n h m y r w
        KA('カ', Romaji.KA),
        KI('キ', Romaji.KI),
        KU('ク', Romaji.KU),
        KE('ケ', Romaji.KE),
        KO('コ', Romaji.KO),

        GA('ガ', Romaji.GA, KA, TENTEN),
        GI('ギ', Romaji.GI, KI, TENTEN),
        GU('グ', Romaji.GU, KU, TENTEN),
        GE('ゲ', Romaji.GE, KE, TENTEN),
        GO('ゴ', Romaji.GO, KO, TENTEN),

        SA('サ', Romaji.SA),
        SI('シ', Romaji.SI),
        SU('ス', Romaji.SU),
        SE('セ', Romaji.SE),
        SO('ソ', Romaji.SO),

        ZA('ザ', Romaji.ZA, SA, TENTEN),
        JI('ジ', Romaji.ZI, SI, TENTEN),
        ZU('ズ', Romaji.ZU, SU, TENTEN),
        ZE('ゼ', Romaji.ZE, SE, TENTEN),
        ZO('ゾ', Romaji.ZO, SO, TENTEN),

        TA('タ', Romaji.TA),
        TI('チ', Romaji.TI),
        TU('ツ', Romaji.TU),
        TE('テ', Romaji.TE),
        TO('ト', Romaji.TO),

        DA('ダ', Romaji.DA, TA, TENTEN),
        DI('ヂ', Romaji.DI, TI, TENTEN),
        DU('ヅ', Romaji.DU, TU, TENTEN),
        DE('デ', Romaji.DE, TE, TENTEN),
        DO('ド', Romaji.DO, TO, TENTEN),

        NA('ナ', Romaji.NA),
        NI('ニ', Romaji.NI),
        NU('ヌ', Romaji.NU),
        NE('ネ', Romaji.NE),
        NO('ノ', Romaji.NO),

        HA('ハ', Romaji.HA),
        HI('ヒ', Romaji.HI),
        HU('フ', Romaji.HU),
        HE('ヘ', Romaji.HE),
        HO('ホ', Romaji.HO),

        BA('バ', Romaji.BA, HA, TENTEN),
        BI('ビ', Romaji.BI, HI, TENTEN),
        BU('ブ', Romaji.BU, HU, TENTEN),
        BE('ベ', Romaji.BE, HE, TENTEN),
        BO('ボ', Romaji.BO, HO, TENTEN),

        PA('パ', Romaji.PO, HA, MARU),
        PI('ピ', Romaji.PI, HI, MARU),
        PU('プ', Romaji.PU, HU, MARU),
        PE('ペ', Romaji.PE, HE, MARU),
        PO('ポ', Romaji.PO, HO, MARU),

        MA('マ', Romaji.MA),
        MI('ミ', Romaji.MI),
        MU('ム', Romaji.MU),
        ME('メ', Romaji.ME),
        MO('モ', Romaji.MO),

        YA('ヤ', Romaji.YA),
        YU('ユ', Romaji.YU),
        YO('ヨ', Romaji.YO),

        RA('ラ', Romaji.RA),
        RI('リ', Romaji.RI),
        RU('ル', Romaji.RU),
        RE('レ', Romaji.RE),
        RO('ロ', Romaji.RO),

        WA('ワ', Romaji.WA),
        //WI('ヰ', Romaji.WI),
        //WE('ヱ', Romaji.WE),
        WO('ヲ', Romaji.WO),

        N('ン', Romaji.N),
        ;

        final char kana;
        final Romaji romaji;

        Katakana(char kana, Romaji romaji) {
            this.kana = kana;
            this.romaji = romaji;
        }

        Katakana(char kana, Romaji romaji, Katakana baseKana, Dakuten dakuten) {
            this(kana, romaji);
        }

        //Inherited Methods
        @Override public char getKana() { return kana; }
        @Override public Romaji getRomajiEnum() { return romaji; }

    }

    final class HiraganaDict {
        final static Map<Romaji, Hiragana> ionary = createDictionary();
        private static Map<Romaji, Hiragana> createDictionary() {
            HashMap<Romaji, Hiragana> result = new HashMap<>();
            for(Hiragana kana: Hiragana.values()){
                result.put(kana.getRomajiEnum(), kana);
            }
            return Collections.unmodifiableMap(result);
        }
    }
    static Hiragana getHiragana(Romaji romaji) {
        return HiraganaDict.ionary.get(romaji);
    }
    static char getHiraganaChar(Romaji romaji) {
        return getHiragana(romaji).getKana();
    }

    final class KatakanaDict {
        final static Map<Romaji, Katakana> ionary = createDictionary();
        private static Map<Romaji, Katakana> createDictionary() {
            HashMap<Romaji, Katakana> result = new HashMap<>();
            for(Katakana kana: Katakana.values()){
                result.put(kana.getRomajiEnum(), kana);
            }
            return Collections.unmodifiableMap(result);
        }
    }
    static Katakana getKatakana(Romaji romaji) {
        return KatakanaDict.ionary.get(romaji);
    }
    static char getKatakanaChar(Romaji romaji) {
        return getKatakana(romaji).getKana();
    }

    static <T extends Kana> T getKana(Romaji romaji, T tokenKana) {
        return (T)((tokenKana instanceof Hiragana)? getHiragana(romaji) : getKatakana(romaji));
    }
}