package server.htmlBuilder.util;

/**
 * @author Andrew Vitkus
 *
 */
public class Offsetter {

    public static final String INDENT_STRING = "  ";

    public static String indent(int indentCount) {
        StringBuilder indents = new StringBuilder(indentCount * INDENT_STRING.length());
        for (; indentCount > 0; indentCount--) {
            indents.append(INDENT_STRING);
        }
        return indents.toString();
    }
}
