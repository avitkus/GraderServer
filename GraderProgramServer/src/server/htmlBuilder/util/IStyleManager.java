package server.htmlBuilder.util;

/**
 * @author Andrew Vitkus
 *
 */
public interface IStyleManager {

    public final String COLOR = "color";
    public final String BGCOLOR = "background-color";
    public final String BORDER_COLOR = "border-color";
    public final String BORDER_WIDTH = "border-width";
    public final String BORDER_COLLAPSE = "border-collapse";
    public final String BORDER_STYLE = "border-style";

    public void addStyle(String name, String value);

    public void removeStyle(String name);

    public String getStyle(String name);

    public boolean isSet(String name);

    public void clear();

    public boolean isEmpty();

    public int size();

    public String[][] getStyles();

    public String getStyleHTML();
}
