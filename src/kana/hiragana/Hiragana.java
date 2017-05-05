package kana.hiragana;

/**
 * @author edotee
 */
public enum Hiragana {

    N('ん'),

    // a i u e o
    A('あ', 'a'),
    I('い', 'i'),
    U('う', 'u'),
    E('え', 'e'),
    O('お', 'o'),

    // k, s, t, n, h, m, y, r, w
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
    //
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
    PO('ぽ', 'p', HO, Dakuten.MARU),

    ;

    enum Combo {

        //KI
        KYA(KI, YA),
        KYU(KI, YU),
        KYO(KI, YO),

        //SI -> SHI
        SHA(SHI, YA, "sha"),
        SHU(SHI, YU, "shu"),
        SHO(SHI, YO, "sho"),
        //TI -> CHI
        CHA(CHI, YA, "cha"),
        CHU(CHI, YU, "chu"),
        CHO(CHI, YO, "cho"),

        //NI
        NYA(NI, YA),
        NYU(NI, YU),
        NYO(NI, YO),
        //MI
        MYA(MI, YA),
        MYU(MI, YU),
        MYO(MI, YO),
        //RI
        RYA(RI, YA),
        RYU(RI, YU),
        RYO(RI, YO),
        //HI
        HYA(HI, YA),
        HYU(HI, YU),
        HYO(HI, YO),

        //Dakuten

        //GI
        GYA(GI, YA),
        GYU(GI, YU),
        GYO(GI, YO),
        //JI
        JYA(JI, YA),
        JYU(JI, YU),
        JYO(JI, YO),
        //DZI
        DZYA(DZI, YA),
        DZYU(DZI, YU),
        DZYO(DZI, YO),
        //BI
        BYA(BI, YA),
        BYU(BI, YU),
        BYO(BI, YO),
        //PI
        PYA(PI, YA),
        PYU(PI, YU),
        PYO(PI, YO),

        ;

        Hiragana leading, following;
        ComboRomanji alternateReading;

        Combo(Hiragana leading, Hiragana following) {
            this(leading, following, null);
        }

        Combo(Hiragana leading, Hiragana following, String alternateReading) {
            this.leading = leading;
            this.following = following;
            this.alternateReading = (alternateReading == null)? null : () -> alternateReading;
        }

        public String getRomanji() {
            if(alternateReading != null)
                return alternateReading.getRomanji();
            return "" + leading.consonant + following.getRomanji();
        }
    }

    enum Dakuten {
        TENTEN('゛', "tenten"),
        MARU('゜', "maru");

        final char dakuten;
        final String name;

        Dakuten(char dakuten, String name) {
            this.dakuten = dakuten;
            this.name = name;
        }
    }

    @FunctionalInterface
    private interface AlternateRomanji {
        String getRomanji();
    }

    @FunctionalInterface
    private interface ComboRomanji {
        String getRomanji();
    }

    private final static char NIL = '∅';

    final char kana, consonant, vowel;
    final AlternateRomanji alternateRomanji;

    Hiragana(char kana) {
        this(kana, 'n', NIL, null);
    }

    Hiragana(char kana, char vowel) {
        this(kana, NIL, vowel, null);
    }

    Hiragana(char kana, char consonant, char vowel) {
        this(kana, consonant, vowel, null);
    }

    Hiragana(char kana, char consonant, char vowel, String alternateRomanji) {
        this.kana = kana;
        this.consonant = consonant;
        this.vowel = vowel;
        this.alternateRomanji = (alternateRomanji == null)? null : () -> alternateRomanji;
    }

    //Dakuten
    Hiragana(char kana, char consonant, Hiragana hiragana, Dakuten dakuten) {
        this(kana, consonant, hiragana.vowel, null);
    }

    Hiragana(char kana, char consonant, Hiragana hiragana, Dakuten dakuten, String alternateRomanji) {
        this(kana, consonant, hiragana.vowel, alternateRomanji);
    }

    //Methods

    public final char getKana() {
        return kana;
    }

    public char getConsonant() {
        return consonant;
    }

    public char getVowel() {
        return vowel;
    }

    public String getRomanji() {
        if(alternateRomanji != null)
            return alternateRomanji.getRomanji();

        String result = "";
        if(consonant != NIL) result += consonant;
        if(vowel != NIL) result += consonant;
        return result;
    }
}
