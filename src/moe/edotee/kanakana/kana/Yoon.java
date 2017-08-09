package moe.edotee.kanakana.kana;

import java.util.ArrayList;

/**
 * @author edotee
 */
public enum Yoon {
    KYA(12365, 12420), KYU(12365, 12422), KYO(12365, 12424),
    GYA(12366, 12420), GYU(12366, 12422), GYO(12366, 12424),
    SYA("sha", 12375, 12420), SYU("shu", 12375, 12422), SYO("sho", 12375, 12424),
    ZYA("ja", 12376, 12420), ZYU("ju", 12376, 12422), ZYO("jo", 12376, 12424),
    TYA("cha", 12385, 12420), TYU("chu", 12385, 12422), TYO("cho", 12385, 12424),
    DYA("ja", 12386, 12420), DYU("ju", 12386, 12422), DYO("jo", 12386, 12424),
    NYA(12395, 12420), NYU(12395, 12422), NYO(12395, 12424),
    HYA(12402, 12420), HYU(12402, 12422), HYO(12402, 12424),
    BYA(12403, 12420), BYU(12403, 12422), BYO(12403, 12424),
    PYA(12404, 12420), PYU(12404, 12422), PYO(12404, 12424),
    MYA(12415, 12420), MYU(12415, 12422), MYO(12415, 12424),
    RYA(12426, 12420), RYU(12426, 12422), RYO(12426, 12424),
    ;

    private final String romaji;
    private final char leading, following;

    Yoon(int leading, int following) {
        this.romaji = this.name().toLowerCase();
        this.leading = (char)leading;
        this.following = (char)following;
    }
    Yoon(String alternativeRomaji, int leading, int following) {
        this.romaji = alternativeRomaji;
        this.leading = (char)leading;
        this.following = (char)following;
    }

    /**
     * Checks whether or not a yoon can be identified by the provided {@param romaji}(case sensitive)
     * and {@return}s true either {@link #checkRomajiByName(String) checkRomanjiByName()}
     * or {@link #checkRomajiByRomaji(String) checkRomanjiByRomaji()} return true.
     * @see #checkRomajiByName(String)
     * @see #checkRomajiByRomaji(String)
     */
    public boolean checkRomaji(String romaji) {
        return (checkRomajiByRomaji(romaji) || checkRomajiByName(romaji));
    }

    /**
     * Will {@return} true if {@param romaji}(case sensitive) matches the enums name/handle.
     * E.g. Yoon.SYA has the name SYA and will return true for any iteration of "sya" in either lower or uppercase.
     * Please use {@link #checkRomajiByRomaji(String) checkRomajiByRomaji()} if you want to check by Hepburn readings
     * or use {@link #checkRomaji(String) checkRomaji()} if any match is sufficient.
     * @see #checkRomaji(String)
     * @see #checkRomajiByRomaji(String)
     */
    public boolean checkRomajiByName(String romaji) {
        return this.name().toLowerCase().equals(romaji);
    }

    /**
     * Will {@return} true if {@param romaji}(case sensitive) matches the enums internally provided romaji.
     * By default, this will be identical to {@link #checkRomajiByName(String) checkRomajiByName()},
     * but in case of yoon that read differently than their input, this will matter.
     * E.g. The letter input for sha is sya.
     * Please use {@link #checkRomajiByName(String) checkRomajiByName()} if you want to check by input/name/handle
     * or use {@link #checkRomaji(String) checkRomaji()} if any match is sufficient.
     * @see #checkRomaji(String)
     * @see #checkRomajiByName(String)
     */
    public boolean checkRomajiByRomaji(String romaji) {
        return getRomaji().equals(romaji);
    }

    public String getKana(char kana) {
        if(isKana(kana))
            return (isHiragana(kana))? getHiragana() : getKatakana();
        else
            return "This shouldn't be happening, but the input isn't a moe.kana…";
    }
    public String getHiragana() { return "" + leading + (char)(following-1); }
    public String getKatakana() { return "" + (char)(leading+96) + (char)(following-95); }
    public String getRomaji() {
        return romaji;
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    /* Class methods */

    /**
     * Check whether or not {@param leading} and {@param following} can form a yoon
     * and {@return}s it – or null otherwise.
     */
    public static Yoon match(char leading, char following) {
        Yoon result = null;
        if(isKana(leading) && isKana(following)) {
            for(Yoon yoon : filter(ensureHiragana(leading)))
                if(yoon.following == (ensureHiragana(following)))
                    result = yoon;
        }
        return result;
    }

    /**
     * Checks and {@return}s each yoon that contains {@param match}.
     * E.g. filtering for NI will return {@link Yoon#NYA}, {@link Yoon#NYU} & {@link Yoon#NYO}.
     */
    public static Yoon[] filter(char match) {
        Yoon[] result = new Yoon[0];
        if(isKana(match)) {
            ArrayList<Yoon> wizardHat = new ArrayList<>();
            for(Yoon y : Yoon.values())
                if(y.leading == ensureHiragana(match) || y.following == ensureHiragana(match))
                    wizardHat.add(y);
            result = new Yoon[wizardHat.size()];
            for(int i = 0; i < result.length; i++)
                result[i] = wizardHat.get(i);
        }
        return result;
    }

    private static boolean isKana(char c) {
        return (c >= 12353  && c <= 12530);
    }
    private static boolean isHiragana(char c) {
        return !(c > 12434);
    }
    private static char ensureHiragana(char c) {
        return (c > 12434)? (char)(c-96) : c;
    }

    @Override public String toString() {
        return romaji;
    }
}
