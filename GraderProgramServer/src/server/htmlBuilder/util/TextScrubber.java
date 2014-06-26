package server.htmlBuilder.util;

/**
 * @author Andrew Vitkus
 *
 */
public class TextScrubber {

    public static String scrub(String text) {
        //System.out.println(text);
        text = text.replaceAll("&(?![0-9a-z]{1,6};)", "&amp;");
        //text = text.replaceAll("&amp;nbsp;", "&nbsp;");
        text = text.replaceAll("<", "&lt;");
        text = text.replaceAll(">", "&gt;");
        text = text.replaceAll("---", "&mdash;");
        text = text.replaceAll("--", "&ndash;");
        text = text.replaceAll("[/.]{3}", "&hellip;");
        text = text.replaceAll("/(C/)", "&copy;");
        text = text.replaceAll("/(R/)", "&reg;");
        //System.out.println(text);
        return text;
    }
}
