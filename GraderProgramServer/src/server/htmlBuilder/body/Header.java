package server.htmlBuilder.body;

import server.htmlBuilder.util.AttributeManager;
import server.htmlBuilder.util.IAttributeManager;
import server.htmlBuilder.util.IStyleManager;
import server.htmlBuilder.util.Offsetter;
import server.htmlBuilder.util.StyleManager;
import server.htmlBuilder.util.TextScrubber;

/**
 * @author Andrew Vitkus
 *
 */
public class Header implements IHeader {

    private IAttributeManager attrs;
    private int level;
    private String text;
    private IStyleManager styleManager;
    private String className;
    public String id;

    public Header() {
        this("", 1);
    }

    public Header(Text text, int level) {
        this(text.getText(0), level);
    }

    public Header(String text, int level) {
        setText(text);
        setLevel(level);
        attrs = new AttributeManager();
        styleManager = new StyleManager();
        className = "";
        id = "";
    }

    @Override
    public void setText(String text) {
        text = TextScrubber.scrub(text);
        this.text = text;
    }

    @Override
    public void setText(IText text) {
        this.text = text.getText(0);
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getText(int depth) {
        StringBuffer html = new StringBuffer();
        html.append(Offsetter.indent(depth)).append("<h").append(getLevel());
        if (className != "") {
            html.append(" class=\"").append(className).append("\"");
        }
        if (id != "") {
            html.append(" id=\"").append(id).append("\"");
        }
        html.append(styleManager.getStyleHTML()).append(attrs.getHTML()).append(">");
        html.append(text).append("</h").append(getLevel()).append(">");
        return html.toString();
    }

    @Override
    public String getTagType() {
        return "header level " + getLevel();
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
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
