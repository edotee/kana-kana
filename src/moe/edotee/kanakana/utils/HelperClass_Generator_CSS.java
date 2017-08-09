package moe.edotee.kanakana.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Generates the utils.CSS class.
 * @author edotee
 */
public final class HelperClass_Generator_CSS {
    private static String generate(String[]... pairs) {
        StringBuilder builder = new StringBuilder();
        builder.append("package moe.edotee.kanakana.utils;").append('\n')
                .append("import javafx.scene.Node;").append('\n')
                .append("/**\n* Holds all CSS classes as enums.\n* Generated by {@link HelperClass_Generator_CSS}\n* @author edotee\n*/").append('\n')
                .append("public interface CSS {").append('\n')
        ;
        for(String[] pair : pairs) builder.append(cssFileToEnum(pair[0], pair[1])).append('\n');
        builder.append("String cssClass();").append('\n')
                .append("static <N extends Node, C extends CSS> N style(N node, C style) {\nreturn style(node, style, true);\n}").append('\n')
                .append("static <N extends Node, C extends CSS> N style(N node, C style, boolean clear) {").append('\n')
                .append("if(clear) node.getStyleClass().clear();").append('\n')
                .append("node.getStyleClass().add(style.cssClass());").append('\n')
                .append("return node;").append('\n')
                .append("}\n}")
        ;
        return  builder.toString();
    }

    private static String cssFileToEnum(String name, String path) {
        String result = "enum " + name + " implements CSS {\n";
        String[] cssClasses = getCssClasses(path);
        for (String s: cssClasses)
            result += s + ",\n";
        return result + ";\n\n@Override public String cssClass() {\nreturn name();\n}\n}";
    }

    private static String[] getCssClasses(String path) {
        ArrayList<String> arrayList = new ArrayList<>();
        //regexe for finding classes: ^[.][a-z]*[{]
        String rootPath = "/home/edotee/IdeaProjects/kana-kana/src/";
        try {
            BufferedReader br = new BufferedReader(new FileReader(rootPath+path));
            String line = br.readLine();
            while (line != null) {
                if( line.startsWith(".") && line.endsWith("{") ) {
                    line = line.replace('.', ' ').replace('{', ' ').trim();
                    arrayList.add(line);
                }
                line = br.readLine();
            }
            br.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        String[] result = new String[arrayList.size()];
        Iterator<String> it = arrayList.iterator();
        for(int i = 0; it.hasNext(); i++)
            result[i] = it.next();
        return result;
    }

    public static void main(String[] args) {
        System.out.println(generate(
                pair("main", Options.CSS.Main),
                pair("pickKana", Options.CSS.PickKana),
                pair("typeRomaji", Options.CSS.TypeRomaji),
                pair("writeKana", Options.CSS.WriteKana)
        ));
    }

    private static String[] pair(String name, String path) {
        String[] result = {name, path};
        return result;
    }
}