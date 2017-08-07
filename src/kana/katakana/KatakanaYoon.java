package kana.katakana;

import kana.Yoon;

import java.util.ArrayList;

import static kana.katakana.Katakana.*;

/**
 * TODO preparation notes - only used by TypeTheRomaji.isYoon()
 * @author edotee
 */
public enum KatakanaYoon implements Yoon {
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

    final Katakana leading, following;
    final String alternateReading;

    KatakanaYoon(Katakana leading, Katakana following) {
        this(leading, following, null);
    }

    KatakanaYoon(Katakana leading, Katakana following, String alternateReading) {
        this.leading = leading;
        this.following = following;
        this.alternateReading = alternateReading;
    }

    @Override public String getKana() {
        return "" + leading.kana + (char)(following.kana -1);
    }

    @Override public String getRomanji() {
        if(alternateReading != null)
            return alternateReading;
        return "" + leading.consonant + following.getRomanji();
    }

    public static KatakanaYoon[] filter(Katakana match) {
        ArrayList<KatakanaYoon> wizardHat = new ArrayList<>();
        for(KatakanaYoon y : KatakanaYoon.values())
            if(y.leading == match || y.following == match)
                wizardHat.add(y);
        KatakanaYoon[] result = new KatakanaYoon[wizardHat.size()];
        for(int i = 0; i < result.length; i++)
            result[i] = wizardHat.get(i);
        return result;
    }

    public static KatakanaYoon match(Katakana leading, Katakana following) {
        KatakanaYoon result = null;
        for(KatakanaYoon yoon : filter(leading))
            if(yoon.following == following)
                result = yoon;
        return result;
    }
}