package kana.katakana;

import kana.Dakuten;
import kana.Kana;

/**
 * @author edotee
 */
public enum Katakana implements Kana<Katakana> {
    N('ン'),

    // a i u e o
    A('ア', '∅', 'a'),
    I('イ', '∅', 'i'),
    U('ウ', '∅', 'u'),
    E('エ', '∅', 'e'),
    O('オ', '∅', 'o'),

    // k s t n h m y r w
    KA('カ', 'k', 'a'),
    KI('キ', 'k', 'i'),
    KU('ク', 'k', 'u'),
    KE('ケ', 'k', 'e'),
    KO('コ', 'k', 'o'),

    SA('サ', 's', 'a'),
    SHI('シ', 's', 'i', "shi"), SI(SHI.kana, SHI.consonant, SHI.vowel, SHI.getRomanji()),
    SU('ス', 's', 'u'),
    SE('セ', 's', 'e'),
    SO('ソ', 's', 'o'),

    TA('タ', 't', 'a'),
    CHI('チ', 't', 'i', "chi"), TI(CHI.kana, CHI.consonant, CHI.vowel, CHI.getRomanji()),
    TSU('ツ', 't', 'u', "tsu"), TU(TSU.kana, TSU.consonant, TSU.vowel, TSU.getRomanji()),
    TE('テ', 't', 'e'),
    TO('ト', 't', 'o'),

    NA('ナ', 'n', 'a'),
    NI('ニ', 'n', 'i'),
    NU('ヌ', 'n', 'u'),
    NE('ネ', 'n', 'e'),
    NO('ノ', 'n', 'o'),

    HA('ハ', 'h', 'a'),
    HI('ヒ', 'h', 'i'),
    HU('フ', 'h', 'u'),
    HE('ヘ', 'h', 'e'),
    HO('ホ', 'h', 'o'),

    MA('マ', 'm', 'a'),
    MI('ミ', 'm', 'i'),
    MU('ム', 'm', 'u'),
    ME('メ', 'm', 'e'),
    MO('モ', 'm', 'o'),

    YA('ヤ', 'y', 'a'),
    YU('ユ', 'y', 'u'),
    YO('ヨ', 'y', 'o'),

    RA('ラ', 'r', 'a'),
    RI('リ', 'r', 'i'),
    RU('ル', 'r', 'u'),
    RE('レ', 'r', 'e'),
    RO('ロ', 'r', 'o'),

    WA('ワ', 'w', 'a'),
    WI('ヰ', 'w', 'i'),
    WE('ヱ', 'w', 'e'),
    WO('ヲ', 'w', 'o'),

    GA('ガ', 'g', KA, Dakuten.TENTEN),
    GI('ギ', 'g', KI, Dakuten.TENTEN),
    GU('グ', 'g', KU, Dakuten.TENTEN),
    GE('ゲ', 'g', KE, Dakuten.TENTEN),
    GO('ゴ', 'g', KO, Dakuten.TENTEN),

    ZA('ザ', 'z', SA, Dakuten.TENTEN),
    JI('ジ', 'z', SI, Dakuten.TENTEN, "ji"), ZI(JI.kana, JI.consonant, JI.vowel, JI.getRomanji()),
    ZU('ズ', 'z', SU, Dakuten.TENTEN),
    ZE('ゼ', 'z', SE, Dakuten.TENTEN),
    ZO('ゾ', 'z', SO, Dakuten.TENTEN),

    DA('ダ', 'd', TA, Dakuten.TENTEN),
    DZI('ヂ', 'd', TI, Dakuten.TENTEN, "ji"), DI(DZI.kana, DZI.consonant, DZI.vowel, DZI.getRomanji()),
    DZU('ヅ', 'd', TU, Dakuten.TENTEN, "zu"), DU(DZU.kana, DZU.consonant, DZU.vowel, DZU.getRomanji()),
    DE('デ', 'd', TE, Dakuten.TENTEN),
    DO('ド', 'd', TO, Dakuten.TENTEN),

    BA('バ', 'b', HA, Dakuten.TENTEN),
    BI('ビ', 'b', HI, Dakuten.TENTEN),
    BU('ブ', 'b', HU, Dakuten.TENTEN),
    BE('ベ', 'b', HE, Dakuten.TENTEN),
    BO('ボ', 'b', HO, Dakuten.TENTEN),

    PA('パ', 'p', HA, Dakuten.MARU),
    PI('ピ', 'p', HI, Dakuten.MARU),
    PU('プ', 'p', HU, Dakuten.MARU),
    PE('ペ', 'p', HE, Dakuten.MARU),
    PO('ポ', 'p', HO, Dakuten.MARU),
    ;

    final char kana, consonant, vowel;
    final String alternativeRomanji;

    Katakana(char kana) {
        this(kana, 'n', NIL, null);
    }

    Katakana(char kana, char vowel) {
        this(kana, NIL, vowel, null);
    }

    Katakana(char kana, char consonant, char vowel) {
        this(kana, consonant, vowel, null);
    }

    Katakana(char kana, char consonant, char vowel, String alternativeRomanji) {
        this.kana = kana;
        this.consonant = consonant;
        this.vowel = vowel;
        this.alternativeRomanji = alternativeRomanji;
    }

    Katakana(char kana, char consonant, Katakana katakana, Dakuten dakuten) {
        this(kana, consonant, katakana.vowel, null);
    }

    Katakana(char kana, char consonant, Katakana katakana, Dakuten dakuten, String alternativeRomanji) {
        this(kana, consonant, katakana.vowel, alternativeRomanji);
    }

    public char getKana() {
        return kana;
    }

    public char getConsonant() {
        return consonant;
    }

    public char getVowel() {
        return vowel;
    }

    public boolean hasIrregularReading() {
        return alternativeRomanji != null;
    }

    public String getIrregularReading() { return alternativeRomanji; }
}