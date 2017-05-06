package kana.hiragana;

/**
 * @author edotee
 */
public enum Dakuten {
    TENTEN('゛', "tenten"),
    MARU('゜', "maru");

    final char dakuten;
    final String name;

    Dakuten(char dakuten, String name) {
        this.dakuten = dakuten;
        this.name = name;
    }
}
