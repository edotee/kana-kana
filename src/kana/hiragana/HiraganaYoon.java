package kana.hiragana;

import kana.Yoon;

import java.util.ArrayList;

import static kana.hiragana.Hiragana.*;

/**
 * @author edotee
 */
public enum HiraganaYoon implements Yoon {
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
    JYA(JI, YA, "ja"),      //according to wikipedia
    JYU(JI, YU, "ju"),      //according to wikipedia
    JYO(JI, YO, "jo"),      //according to wikipedia
    //DZI
    DZYA(DZI, YA, "ja"),    //according to wikipedia
    DZYU(DZI, YU, "ju"),    //according to wikipedia
    DZYO(DZI, YO, "jo"),    //according to wikipedia
    //BI
    BYA(BI, YA),
    BYU(BI, YU),
    BYO(BI, YO),
    //PI
    PYA(PI, YA),
    PYU(PI, YU),
    PYO(PI, YO),
    ;

    final Hiragana leading, following;
    final String alternateReading;

    HiraganaYoon(Hiragana leading, Hiragana following) {
        this(leading, following, null);
    }

    HiraganaYoon(Hiragana leading, Hiragana following, String alternateReading) {
        this.leading = leading;
        this.following = following;
        this.alternateReading = alternateReading;
    }

    @Override public String getDigraph() {
        return "" + leading.kana + following.kana;
    }

    @Override public String getRomanji() {
        if(alternateReading != null)
            return alternateReading;
        return "" + leading.consonant + following.getRomanji();
    }

    public static HiraganaYoon[] filter(Hiragana match) {
        ArrayList<HiraganaYoon> wizardHat = new ArrayList<>();
        for(HiraganaYoon y : HiraganaYoon.values())
            if(y.leading == match || y.following == match)
                wizardHat.add(y);
        HiraganaYoon[] result = new HiraganaYoon[wizardHat.size()];
        for(int i = 0; i < result.length; i++)
            result[i] = wizardHat.get(i);
        return result;
    }

    public static HiraganaYoon match(Hiragana leading, Hiragana following) {
        HiraganaYoon result = null;
        for(HiraganaYoon hiraganaYoon : filter(leading))
            if(hiraganaYoon.following == following)
                result = hiraganaYoon;
        return result;
    }
}