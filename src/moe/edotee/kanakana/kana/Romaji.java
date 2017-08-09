package moe.edotee.kanakana.kana;

/**
 * @author edotee
 */
public enum Romaji {
    A, I, U, E, O,
    KA, KI, KU, KE, KO,
    SA, SI("shi"), SU, SE, SO,
    TA, TI("chi"), TU("tsu"), TE, TO,
    NA, NI, NU, NE, NO, N,
    HA("wa"), HI, HU("fu"), HE("e"), HO,
    MA, MI, MU, ME, MO,
    YA, YU, YO,
    RA, RI, RU, RE, RO,
    WA, WO("o"),

    GA, GI, GU, GE, GO,
    ZA, ZI("ji"), ZU, ZE, ZO,
    DA, DI("ji"), DU("zu"), DE, DO,
    BA, BI, BU, BE, BO,
    PA, PI, PU, PE, PO
    ;
    /* Overloaded romaji/names:
        ji -> ZI / DI
        zu -> ZU / DU
        wa -> HA / WA
        e -> E / HE
        o -> O / WO
     */

    private final String romaji;

    Romaji() {
        this.romaji = this.name().toLowerCase();
    }

    Romaji(String alternativeRomaji) {
        this.romaji = alternativeRomaji;
    }

    /**
     * Looks for the Romaji Enum of {@param romaji} and returns the first (and only) match that is identical to Romaji.name().toLowerCase().
     * If there's no match for .name(), it will return the last match for .romaji.
     * Will {@return} null, when no match is found.
     *
     * Example of the 1st case:
     * {@param romaji} = "o" will return O instead of WO
     *
     * Example of the 2nd case:
     * {@param romaji} = "ji" / "ja" / "ju" / "jo" will return DYA, DYU, DYO instead of JYA, JYU, JYO
     */
    public static Romaji getEnumOf(String romaji) {
        Romaji result = null;
        Romaji semiResult = null;

        Romaji[] romaj = Romaji.values();
        for(int i = 0; result == null && i < romaj.length; i++) {
            if(romaj[i].name().toLowerCase().equals(romaji))
                result = romaj[i];
            else if(romaj[i].romaji.equals(romaji))
                semiResult = romaj[i];
        }

        return (result != null)? result : semiResult;
    }

    public static Romaji[][] getRomajiGrid() {
        Romaji[] _A = {A, KA, SA, TA, NA, HA, MA, YA, RA, WA};
        Romaji[] _I = {I, KI, SI, TI, NI, HI, MI, null, RI, null};
        Romaji[] _U = {U, KU, SU, TU, NU, HU, MU, YU, RU, null};
        Romaji[] _E = {E, KE, SE, TE, NE, HE, ME, null, RE, null};
        Romaji[] _O = {O, KO, SO, TO, NO, HO, MO, YO, RO, WO};
        Romaji[] _N = {N};
        return new Romaji[][]{_A, _I, _U, _E, _O, _N};
    }

    public boolean checkRomaji(String romaji) {
        return (checkRomajiByRomaji(romaji) || checkRomajiByName(romaji));
    }

    public boolean checkRomajiByName(String romaji) {
        return this.name().toLowerCase().equals(romaji);
    }

    public boolean checkRomajiByRomaji(String romaji) {
        return getRomaji().equals(romaji);
    }

    public String getRomaji() {
        return romaji;
    }

    @Override public String toString() {
        return romaji;
    }
}