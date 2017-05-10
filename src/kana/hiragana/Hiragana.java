package kana.hiragana;

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
public enum Hiragana implements Kana<Hiragana> {
    // a i u e o
    A('あ', a),
    I('い', i),
    U('う', u),
    E('え', e),
    O('お', o),

    // k s t n h m y r w
    KA('か', k, a),
    KI('き', k, i),
    KU('く', k, u),
    KE('け', k, e),
    KO('こ', k, o),

    SA('さ', s, a),
    SHI('し', s, i, "shi"), SI(SHI.kana, SHI.consonant, SHI.vowel, SHI.getRomanji()),
    SU('す', s, u),
    SE('せ', s, e),
    SO('そ', s, o),

    TA('た', t, a),
    CHI('ち', t, i, "chi"), TI(CHI.kana, CHI.consonant, CHI.vowel, CHI.getRomanji()),
    TSU('つ', t, u, "tsu"), TU(TSU.kana, TSU.consonant, TSU.vowel, TSU.getRomanji()),
    TE('て', t, e),
    TO('と', t, o),

    NA('な', n, a),
    NI('に', n, i),
    NU('ぬ', n, u),
    NE('ね', n, e),
    NO('の', n, o),

    HA('は', h, a),
    HI('ひ', h, i),
    FU('フ', h, u, "fu"), HU(FU.kana, FU.consonant, FU.vowel, FU.getRomanji()),
    HE('へ', h, e),
    HO('ほ', h, o),

    MA('ま', m, a),
    MI('み', m, i),
    MU('む', m, u),
    ME('め', m, e),
    MO('も', m, o),

    YA('や', y, a),
    YU('ゆ', y, u),
    YO('よ', y, o),

    RA('ら', r, a),
    RI('り', r, i),
    RU('る', r, u),
    RE('れ', r, e),
    RO('ろ', r, o),

    WA('わ', w, a),
    WI('ゐ', w, i),
    WE('ゑ', w, e),
    WO('を', w, o),

    N('ん'),

    //Dakuten
    GA('が', g, KA, TENTEN),
    GI('ぎ', g, KI, TENTEN),
    GU('ぐ', g, KU, TENTEN),
    GE('げ', g, KE, TENTEN),
    GO('ご', g, KO, TENTEN),

    ZA('ざ', z, SA, TENTEN),
    JI('じ', z, SI, TENTEN, "ji"), ZI(JI.kana, JI.consonant, JI.vowel, JI.getRomanji()),
    ZU('ず', z, SU, TENTEN),
    ZE('ぜ', z, SE, TENTEN),
    ZO('ぞ', z, SO, TENTEN),

    DA('だ', d, TA, TENTEN),
    DZI('ぢ', d, TI, TENTEN, "ji"), DI(DZI.kana, DZI.consonant, DZI.vowel, DZI.getRomanji()),
    DZU('づ', d, TU, TENTEN, "zu"), DU(DZU.kana, DZU.consonant, DZU.vowel, DZU.getRomanji()),
    DE('で', d, TE, TENTEN),
    DO('ど', d, TO, TENTEN),

    BA('ば', b, HA, TENTEN),
    BI('び', b, HI, TENTEN),
    BU('ぶ', b, FU, TENTEN),
    BE('べ', b, HE, TENTEN),
    BO('ぼ', b, HO, TENTEN),

    PA('ぱ', p, HA, MARU),
    PI('ぴ', p, HI, MARU),
    PU('ぷ', p, FU, MARU),
    PE('ぺ', p, HE, MARU),
    PO('ぽ', p, HO, MARU),
    ;

    final char kana;
    final Consonant consonant;
    final Vowel vowel;
    final String alternativeRomanji;

    Hiragana(char kana) {
        this(kana, n, NOVOW, null);
    }

    Hiragana(char kana, Vowel vowel) {
        this(kana, NOCON, vowel, null);
    }

    Hiragana(char kana, Consonant consonant, Vowel vowel) {
        this(kana, consonant, vowel, null);
    }

    Hiragana(char kana, Consonant consonant, Vowel vowel, String alternativeRomanji) {
        this.kana = kana;
        this.consonant = consonant;
        this.vowel = vowel;
        this.alternativeRomanji = alternativeRomanji;
    }

    Hiragana(char kana, Consonant consonant, Hiragana hiragana, Dakuten dakuten) {
        this(kana, consonant, hiragana.vowel, null);
    }

    Hiragana(char kana, Consonant consonant, Hiragana hiragana, Dakuten dakuten, String alternativeRomanji) {
        this(kana, consonant, hiragana.vowel, alternativeRomanji);
    }

    //Inherited Methods
    public char getKana() { return kana; }

    public Consonant getConsonant() { return consonant; }

    public Vowel getVowel() { return vowel; }

    public boolean hasIrregularReading() { return alternativeRomanji != null; }

    public String getIrregularReading() { return alternativeRomanji; }
}