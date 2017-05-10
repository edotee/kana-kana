package kana.katakana;

import kana.Dakuten;
import kana.Kana;
import kana.KanaHelper.Consonant;
import kana.KanaHelper.Vowel;

import static kana.Dakuten.*;
import static kana.KanaHelper.Consonant.*;
import static kana.KanaHelper.Vowel.*;

/**
 * @author edotee
 */
public enum Katakana implements Kana<Katakana> {
    // a i u e o
    A('ア', a),
    I('イ', i),
    U('ウ', u),
    E('エ', e),
    O('オ', o),

    // k s t n h m y r w
    KA('カ', k, a),
    KI('キ', k, i),
    KU('ク', k, u),
    KE('ケ', k, e),
    KO('コ', k, o),

    SA('サ', s, a),
    SHI('シ', s, i, "shi"), SI(SHI.kana, SHI.consonant, SHI.vowel, SHI.getRomanji()),
    SU('ス', s, u),
    SE('セ', s, e),
    SO('ソ', s, o),

    TA('タ', t, a),
    CHI('チ', t, i, "chi"), TI(CHI.kana, CHI.consonant, CHI.vowel, CHI.getRomanji()),
    TSU('ツ', t, u, "tsu"), TU(TSU.kana, TSU.consonant, TSU.vowel, TSU.getRomanji()),
    TE('テ', t, e),
    TO('ト', t, o),

    NA('ナ', n, a),
    NI('ニ', n, i),
    NU('ヌ', n, u),
    NE('ネ', n, e),
    NO('ノ', n, o),

    HA('ハ', h, a),
    HI('ヒ', h, i),
    FU('フ', h, u, "fu"), HU(FU.kana, FU.consonant, FU.vowel, FU.getRomanji()),
    HE('ヘ', h, e),
    HO('ホ', h, o),

    MA('マ', m, a),
    MI('ミ', m, i),
    MU('ム', m, u),
    ME('メ', m, e),
    MO('モ', m, o),

    YA('ヤ', y, a),
    YU('ユ', y, u),
    YO('ヨ', y, o),

    RA('ラ', r, a),
    RI('リ', r, i),
    RU('ル', r, u),
    RE('レ', r, e),
    RO('ロ', r, o),

    WA('ワ', w, a),
    WI('ヰ', w, i),
    WE('ヱ', w, e),
    WO('ヲ', w, o),

    N('ン'),

    //Dakuten
    GA('ガ', g, KA, TENTEN),
    GI('ギ', g, KI, TENTEN),
    GU('グ', g, KU, TENTEN),
    GE('ゲ', g, KE, TENTEN),
    GO('ゴ', g, KO, TENTEN),

    ZA('ザ', z, SA, TENTEN),
    JI('ジ', z, SI, TENTEN, "ji"), ZI(JI.kana, JI.consonant, JI.vowel, JI.getRomanji()),
    ZU('ズ', z, SU, TENTEN),
    ZE('ゼ', z, SE, TENTEN),
    ZO('ゾ', z, SO, TENTEN),

    DA('ダ', d, TA, TENTEN),
    DZI('ヂ', d, TI, TENTEN, "ji"), DI(DZI.kana, DZI.consonant, DZI.vowel, DZI.getRomanji()),
    DZU('ヅ', d, TU, TENTEN, "zu"), DU(DZU.kana, DZU.consonant, DZU.vowel, DZU.getRomanji()),
    DE('デ', d, TE, TENTEN),
    DO('ド', d, TO, TENTEN),

    BA('バ', b, HA, TENTEN),
    BI('ビ', b, HI, TENTEN),
    BU('ブ', b, FU, TENTEN),
    BE('ベ', b, HE, TENTEN),
    BO('ボ', b, HO, TENTEN),

    PA('パ', p, HA, MARU),
    PI('ピ', p, HI, MARU),
    PU('プ', p, FU, MARU),
    PE('ペ', p, HE, MARU),
    PO('ポ', p, HO, MARU),
    ;

    final char kana;
    final Consonant consonant;
    final Vowel vowel;
    final String alternativeRomanji;

    Katakana(char kana) {
        this(kana, n, NOVOW, null);
    }

    Katakana(char kana, Vowel vowel) {
        this(kana, NOCON, vowel, null);
    }

    Katakana(char kana, Consonant consonant, Vowel vowel) {
        this(kana, consonant, vowel, null);
    }

    Katakana(char kana, Consonant consonant, Vowel vowel, String alternativeRomanji) {
        this.kana = kana;
        this.consonant = consonant;
        this.vowel = vowel;
        this.alternativeRomanji = alternativeRomanji;
    }

    Katakana(char kana, Consonant consonant, Katakana katakana, Dakuten dakuten) {
        this(kana, consonant, katakana.vowel, null);
    }

    Katakana(char kana, Consonant consonant, Katakana katakana, Dakuten dakuten, String alternativeRomanji) {
        this(kana, consonant, katakana.vowel, alternativeRomanji);
    }

    //Inherited Methods
    public char getKana() { return kana; }

    public Consonant getConsonant() { return consonant; }

    public Vowel getVowel() { return vowel; }

    public boolean hasIrregularReading() { return alternativeRomanji != null; }

    public String getIrregularReading() { return alternativeRomanji; }
}