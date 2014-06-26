package server.cssBuilder;

/**
 * @author Andrew Vitkus
 *
 */
public interface IRule {

    public void setSelector(String selector);

    public String getSelector();

    public void addProperty(String name, String value);

    public String[][] getProperties();

    public String getCSS(int indent);
}
