package kana.hiragana;

import kana.Kana;

import java.util.*;

/**
 * @author edotee
 */
public enum Hiragana implements Kana<Hiragana> {

    N('ん'),

    // a i u e o
    A('あ', 'a'),
    I('い', 'i'),
    U('う', 'u'),
    E('え', 'e'),
    O('お', 'o'),

    // k s t n h m y r w
    KA('か', 'k', 'a'),
    KI('き', 'k', 'i'),
    KU('く', 'k', 'u'),
    KE('け', 'k', 'e'),
    KO('こ', 'k', 'o'),

    SA('さ', 's', 'a'),  //SHI(' ', 'S', 'i') {@Override public String getRomanji(){ return "sh"+this.vowel; }},
    SHI('し', 's', 'i', "shi"), SI(SHI.kana, SHI.consonant, SHI.vowel, SHI.getRomanji()),
    SU('す', 's', 'u'),
    SE('せ', 's', 'e'),
    SO('そ', 's', 'o'),

    TA('た', 't', 'a'),
    CHI('ち', 't', 'i', "chi"), TI(CHI.kana, CHI.consonant, CHI.vowel, CHI.getRomanji()),
    TSU('つ', 't', 'u', "tsu"), TU(TSU.kana, TSU.consonant, TSU.vowel, TSU.getRomanji()),
    TE('て', 't', 'e'),
    TO('と', 't', 'a'),

    NA('な', 'n', 'a'),
    NI('に', 'n', 'i'),
    NU('ぬ', 'n', 'u'),
    NE('ね', 'n', 'e'),
    NO('の', 'n', 'o'),

    HA('は', 'h', 'a'),
    HI('ひ', 'h', 'i'),
    HU('ふ', 'h', 'u'),
    HE('へ', 'h', 'e'),
    HO('ほ', 'h', 'o'),

    MA('ま', 'm', 'a'),
    MI('み', 'm', 'i'),
    MU('む', 'm', 'u'),
    ME('め', 'm', 'e'),
    MO('も', 'm', 'o'),

    YA('や', 'y', 'a'),
    YU('ゆ', 'y', 'u'),
    YO('よ', 'y', 'o'),
    //the theoretical hiragana yi and ye never existed in the japanese language

    RA('ら', 'r', 'a'),
    RI('り', 'r', 'i'),
    RU('る', 'r', 'u'),
    RE('れ', 'r', 'e'),
    RO('ろ', 'r', 'o'),

    WA('わ', 'w', 'a'),
    WO('を', 'w', 'o', "o"), //preserved in 1 use only: as particle
    //ゐ/wi and ゑ/we are officially obsolete and are replaced by い/i え/e respectively
    //the theoretical hiragana wu never existed in the japanese language

    //Dakuten

    //k-row
    GA('が', 'g', KA, Dakuten.TENTEN),
    GI('ぎ', 'g', KI, Dakuten.TENTEN),
    GU('ぐ', 'g', KU, Dakuten.TENTEN),
    GE('げ', 'g', KE, Dakuten.TENTEN),
    GO('ご', 'g', KO, Dakuten.TENTEN),

    //s-row
    ZA('ざ', 'z', SA, Dakuten.TENTEN),
    JI('じ', 'z', SHI, Dakuten.TENTEN, "ji"), ZI(JI.kana, JI.consonant, JI.vowel, JI.getRomanji()),
    ZU('ず', 'z', SU, Dakuten.TENTEN),
    ZE('ぜ', 'z', SE, Dakuten.TENTEN),
    ZO('ぞ', 'z', SO, Dakuten.TENTEN),

    //t-row
    DA('だ', 'd', TA, Dakuten.TENTEN),
    DZI('ぢ', 'd', CHI, Dakuten.TENTEN, "ji"), DI(DZI.kana, DZI.consonant, DZI.vowel, DZI.getRomanji()),
    DZU('づ', 'd', TSU, Dakuten.TENTEN, "zu"), DU(DZU.kana, DZU.consonant, DZU.vowel, DZU.getRomanji()),
    DE('で', 'd', TE, Dakuten.TENTEN),
    DO('ど', 'd', TO, Dakuten.TENTEN),

    //h-row
    BA('ば', 'b', HA, Dakuten.TENTEN),
    BI('び', 'b', HI, Dakuten.TENTEN),
    BU('ぶ', 'b', HU, Dakuten.TENTEN),
    BE('べ', 'b', HE, Dakuten.TENTEN),
    BO('ぼ', 'b', HO, Dakuten.TENTEN),

    PA('ぱ', 'p', HA, Dakuten.MARU),
    PI('ぴ', 'p', HI, Dakuten.MARU),
    PU('ぷ', 'p', HU, Dakuten.MARU),
    PE('ぺ', 'p', HE, Dakuten.MARU),
    PO('ぽ', 'p', HO, Dakuten.MARU)
    ;

    final char kana, consonant, vowel;
    final String alternativeRomanji;

    Hiragana(char kana) {
        this(kana, 'n', NIL, null);
    }

    Hiragana(char kana, char vowel) {
        this(kana, NIL, vowel, null);
    }

    Hiragana(char kana, char consonant, char vowel) {
        this(kana, consonant, vowel, null);
    }

    Hiragana(char kana, char consonant, char vowel, String alternativeRomanji) {
        this.kana = kana;
        this.consonant = consonant;
        this.vowel = vowel;
        this.alternativeRomanji = alternativeRomanji;
    }

    //Dakuten
    Hiragana(char kana, char consonant, Hiragana hiragana, Dakuten dakuten) {
        this(kana, consonant, hiragana.vowel, null);
    }

    Hiragana(char kana, char consonant, Hiragana hiragana, Dakuten dakuten, String alternativeRomanji) {
        this(kana, consonant, hiragana.vowel, alternativeRomanji);
    }

    //Enum Methods
    public static Hiragana[][] basicsByVowel() {
        Hiragana[][] result = {
                {A, KA, SA, TA, NA, HA, MA, YA, RA, WA},
                {I, KI, SHI,CHI,NI, HI, MI, null, RI, null},
                {U, KU, SU, TSU,NU, HU, MU, YU, RU, null},
                {E, KE, SE, TE, NE, HE, ME, null, RE, null},
                {O, KO, SO, TO, NO, HO, MO, YO, RO, WO}
        };
        return result;
    }

    public static Hiragana[][] basicsByConsonant() {
        Hiragana[][] result = {
                {A, I, U, E, O},
                {KA, KI, KU, KE, KO},
                {SA, SHI, SU, SE, SO},
                {TA, CHI, TSU, TE, TO},
                {NA, NI, NU, NE, NO},
                {HA, HI, HU, HE, HO},
                {MA, MI, MU, ME, MO},
                {YA, null, YU, null, YO},
                {RA, RI, RU, RE, RO},
                {WA, null, null, null, WO}
        };
        return result;
    }

    public static Hiragana[][] dakutenByVowel() {
        Hiragana[][] result = {
                // k s t n h m y r w
                {GA, ZA, DA, BA, PA},
                {GI, JI, DZI, BI, PI},
                {GU, ZU, DZU, BU, PU},
                {GE, ZE, DE, BE, PE},
                {GO, ZO, DO, BO, PO}
        };
        return result;
    }

    public static Hiragana[][] dakutenByConsonant() {
        Hiragana[][] result = {
                // k s t n h m y r w
                {GA, GI, GU, GE, GO},
                {ZA, JI, ZU, ZE, ZO},
                {DA, DZI, DZU, DE, DO},
                {BA, BI, BU, BE, BO},
                {PA, PI, PU, PE, PO},
        };
        return result;
    }

    private static Hiragana[] flatten(Hiragana[][] arrays) {
        ArrayList<Hiragana> temp = new ArrayList<>();
        for(Hiragana[] kana : arrays)
            Collections.addAll(temp, kana);
        while(temp.remove(null)) {/* lol */}
        Hiragana[] pancake = new Hiragana[temp.size()];
        for(int i = 0; i < pancake.length; i++)
            pancake[i] = temp.get(i);
        return pancake;
    }

    public static Hiragana[] basicValues() {
        return flatten(basicsByConsonant());
    }

    public static Hiragana[] dakutenValues() {
        return flatten(dakutenByConsonant());
    }

    //Instance Methods
    public final char getKana() {
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
    public  String getIrregularReading() { return alternativeRomanji; }
}