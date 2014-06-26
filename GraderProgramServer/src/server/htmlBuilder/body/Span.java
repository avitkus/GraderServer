package server.htmlBuilder.body;

import java.util.ArrayList;

import server.htmlBuilder.util.AttributeManager;
import server.htmlBuilder.util.IAttributeManager;
import server.htmlBuilder.util.IStyleManager;
import server.htmlBuilder.util.Offsetter;
import server.htmlBuilder.util.StyleManager;

/**
 * @author Andrew Vitkus
 *
 */
public class Span implements ISpan {

    private ArrayList<IBodyElement> contents;
    private IAttributeManager attrs;
    private IStyleManager styleManager;
    private String className;
    public String id;

    public Span(IBodyElement... elements) {
        contents = new ArrayList<>();
        attrs = new AttributeManager();
        styleManager = new StyleManager();

        className = "";
        id = "";
        for (IBodyElement element : elements) {
            contents.add(element);
        }
    }

    @Override
    public String getText(int indent) {
        StringBuilder html = new StringBuilder();
        html.append(Offsetter.indent(indent++)).append("<span");
        if (className != "") {
            html.append(" class=\"").append(className).append("\"");
        }
        if (id != "") {
            html.append(" id=\"").append(id).append("\"");
        }
        html.append(styleManager.getStyleHTML()).append(attrs.getHTML()).append(">\n");
        for (IBodyElement content : contents) {
            html.append(content.getText(indent)).append("\n");
        }
        html.append(Offsetter.indent(indent - 1)).append("</span>");
        return html.toString();
    }

    @Override
    public String getTagType() {
        return "span";
    }

    @Override
    public void addContent(IBodyElement content) {
        contents.add(content);
    }

    @Override
    public IBodyElement[] getContents() {
        return contents.toArray(new IBodyElement[contents.size()]);
    }

    @Override
    public void setColor(String color) {
        addStyle(IStyleManager.COLOR, color);
    }

    @Override
    public String getColor() {
        return styleManager.getStyle(IStyleManager.COLOR);
    }

    @Override
    public void addStyle(String name, String value) {
        styleManager.addStyle(name, value);
    }

    @Override
    public String[][] getStyles() {
        return styleManager.getStyles();
    }

    @Override
    public void addAttribute(String name, String value) {
        attrs.addAttribute(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        attrs.removeAttribute(name);
    }

    @Override
    public String getAttribute(String name) {
        return attrs.getAttribute(name);
    }

    @Override
    public String[][] getAttributes() {
        return attrs.getAttributes();
    }

    @Override
    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public void setID(String id) {
        this.id = id;
    }

    @Override
    public String getID() {
        return id;
    }
}
