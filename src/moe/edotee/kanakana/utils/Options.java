package moe.edotee.kanakana.utils;

/**
 * @author edotee
 */
public interface Options {
    String TITLE = "Kana Test";
    double WIDTH = 854.0f;
    double HEIGHT = 480.0f;
    boolean SHOW_ROMAJI = true;
    int MINIMUM_KANA = 5;

    interface FXML {
        String START =      "fxml/startLayout.fxml";
        String EXERCISE =   "fxml/exerciseLayout.fxml";
    }

    interface CSS {
        String Main = "moe/edotee/kanakana/gui/css/main.css";
        String PickKana = "moe/edotee/kanakana/gui/css/pick_the_kana.css";
        String TypeKana = "moe/edotee/kanakana/gui/css/type_the_romaji.css";
        String WriteKana = "moe/edotee/kanakana/gui/css/write_the_kana.css";
    }

    interface Default {
        //String[] dHiragana = "a i u e o ka ki ku ke ko sa shi su se so ta chi tsu te to na ni nu ne no n ha hi fu he ho ma mi mu me mo ya yu yo ra ri ru re ro wa wo".trim().split(" ");
        String[] dHiragana = "ki shi chi ni hi mi ya yu yo ri".trim().split(" ");
        String[] dKatakana = "a i u e o ka ki ni ha fu he mi ya yu yo ri".trim().split(" ");
    }

    interface PickKana {
        int AMOUNT = 5;
        int DEPTH = 3;
    }

    interface TypeRomaji {
        int AMOUNT = 3;
        /**
         * when we have less than 4 items
         * the reason why we count to 4 and not 3 is because
         * otherwise we might instantly get the newly discarded one back into the queue
         * while stochastically unlikely, this could end in a prolonged phase of only seeing 3 Kana
         * in the same sequence over and over again
         * -> [a-b-c], discard c, draw c
         * -> [c-a-b], discard b, draw b
         * -> [b-c-a], discard a, draw a
         * -> [a-b-c], discard c, draw c
         * unique combinations: abc
         * ... ad infinitum...
         * the worst case for size = 4 would be something like that
         * where d = dummy / the invisible/unshown moe.kana
         * -> [a-b-c]-d, discard d, draw d
         * -> [d-a-b]-c, discard c, draw c
         * -> [c-d-a]-b, discard b, draw b
         * -> [b-c-d]-a, discard a, draw a
         * -> [a-b-c]-d, discard d, draw d
         * unique combinations: abc, abd, acd, bcd;
         * ... ad infinitum...
         */
        int DEPTH = AMOUNT + 1;
        interface Fouls {
            boolean CHECK = false;  //if false, will never check for fouls, if true, will only check for fouls…
            boolean WO = false;     //… that are checked as true
        }
    }

    interface WriteKana {
        int AMOUNT = 5;
        int DEPTH = AMOUNT * 2;
    }
}