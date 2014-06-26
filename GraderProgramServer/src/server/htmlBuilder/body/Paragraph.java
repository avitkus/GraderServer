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
public class Paragraph implements IParagraph {

    private ArrayList<IBodyElement> contents;
    private IAttributeManager attrs;
    private IStyleManager styleManager;
    private String className;
    public String id;

    public Paragraph(IBodyElement... elements) {
        contents = new ArrayList<>();
        className = "";
        id = "";
        for (IBodyElement element : elements) {
            contents.add(element);
        }
        attrs = new AttributeManager();
        styleManager = new StyleManager();
    }

    @Override
    public String getText(int indent) {
        StringBuilder text = new StringBuilder();
        text.append(Offsetter.indent(indent++)).append("<p");
        if (className != "") {
            text.append(" class=\"").append(className).append("\"");
        }
        if (id != "") {
            text.append(" id=\"").append(id).append("\"");
        }
        text.append(styleManager.getStyleHTML()).append(attrs.getHTML()).append(">\n");
        for (IBodyElement row : contents) {
            text.append(row.getText(indent)).append("\n");
        }
        text.append(Offsetter.indent(indent - 1)).append("</p>");
        return text.toString();
    }

    @Override
    public String getTagType() {
        return "table header";
    }

    @Override
    public IBodyElement[] getContents() {
        return contents.toArray(new IBodyElement[contents.size()]);
    }

    @Override
    public void addContent(IBodyElement content) {
        contents.add(content);
    }

    @Override
    public void insertLineBreak() {
        contents.add(new LineBreak());
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
