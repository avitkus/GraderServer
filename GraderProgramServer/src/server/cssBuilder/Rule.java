package server.cssBuilder;

/**
 * @author Andrew Vitkus
 *
 */
public class Rule implements IRule {

    private IPropertyManager props;
    private String selector;

    public Rule() {
        props = new PropertyManager();
        selector = "";
    }

    @Override
    public void setSelector(String selector) {
        this.selector = selector;
    }

    @Override
    public String getSelector() {
        return selector;
    }

    @Override
    public void addProperty(String name, String value) {
        props.addProperty(name, value);
    }

    @Override
    public String[][] getProperties() {
        return props.getProperties();
    }

    @Override
    public String getCSS(int indent) {
        StringBuilder css = new StringBuilder();
        css.append(selector).append(" {\r\n");
        css.append(props.getCSS(indent++));
        css.append("}\r\n");
        return css.toString();
    }

}
