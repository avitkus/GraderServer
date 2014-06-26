package server.htmlBuilder.body;

import java.util.ArrayList;

import server.htmlBuilder.attributes.TextStyle;
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
public class Text implements IText {

    private ArrayList<IText> textParts;
    private ArrayList<TextStyle> textStyles;
    private IAttributeManager attrs;
    private IStyleManager styleManager;
    private String text;
    private String className;
    private String id;

    public Text() {
        this("");
    }

    public Text(String text, TextStyle... styles) {
        textParts = new ArrayList<>();
        this.textStyles = new ArrayList<>();
        attrs = new AttributeManager();
        styleManager = new StyleManager();
        className = "";
        id = "";

        setText(text);
        setTextStyle(styles);
    }

    @Override
    public String getTagType() {
        return "text";
    }

    @Override
    public TextStyle[] getTextStyles() {
        return textStyles.toArray(new TextStyle[textStyles.size()]);
    }

    @Override
    public void setTextStyle(TextStyle... styles) {
        clearStyles();
        addTextStyle(styles);
    }

    @Override
    public void addTextStyle(TextStyle... styles) {
        for (TextStyle style : styles) {
            this.textStyles.add(style);
        }
    }

    @Override
    public void removeStyle(TextStyle style) {
        textStyles.remove(style);
    }

    @Override
    public void clearStyles() {
        textStyles.clear();
    }

    @Override
    public void setText(String text) {
        text = TextScrubber.scrub(text);
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getText(int indent) {
        StringBuilder html = new StringBuilder();
        html.append(Offsetter.indent(indent));
        if (!styleManager.isEmpty() || className != "" || id != "") {
            html.append("<span");
            if (!styleManager.isEmpty()) {
                html.append(styleManager.getStyleHTML()).append(">");
            }
            if (className != "") {
                html.append(" class=\"").append(className).append("\"");
            }
            if (id != "") {
                html.append(" id=\"").append(id).append("\"");
            }
        }
        html.append(getStyleOpenTags()).append(text).append(getStyleEndTags());
        if (!styleManager.isEmpty() || className != "" || id != "") {
            html.append("</span>");
        }
        for (IText text : textParts) {
            html.append(text.getText(0));
        }
        return html.toString();
    }

    @Override
    public void appendText(String text, TextStyle... styles) {
        textParts.add(new Text(text, styles));
    }

    @Override
    public void addTextPart(IText text) {
        textParts.add(text);
    }

    @Override
    public void clearText() {
        text = "";
        textParts.clear();
    }

    @Override
    public void addLink(IHyperlink link) {
        textParts.add(new Text(link.getText(0)));
    }

    private String getStyleOpenTags() {
        StringBuilder tags = new StringBuilder();
        if (textStyles.contains(TextStyle.PLAIN)) {
            return "";
        }
        if (textStyles.contains(TextStyle.BOLD)) {
            tags.append("<b>");
        }
        if (textStyles.contains(TextStyle.ITALIC)) {
            tags.append("<i>");
        }
        if (textStyles.contains(TextStyle.UNDERLINE)) {
            tags.append("<u>");
        }
        if (textStyles.contains(TextStyle.SUPERSCRIPT)) {
            tags.append("<sup>");
        }
        if (textStyles.contains(TextStyle.SUBSCRIPT)) {
            tags.append("<sub>");
        }
        if (textStyles.contains(TextStyle.CODE)) {
            tags.append("<code>");
        }
        return tags.toString();
    }

    private String getStyleEndTags() {
        StringBuilder tags = new StringBuilder();

        if (textStyles.contains(TextStyle.PLAIN)) {
            return "";
        }
        if (textStyles.contains(TextStyle.CODE)) {
            tags.append("</code>");
        }
        if (textStyles.contains(TextStyle.SUBSCRIPT)) {
            tags.append("</sub>");
        }
        if (textStyles.contains(TextStyle.SUPERSCRIPT)) {
            tags.append("</sup>");
        }
        if (textStyles.contains(TextStyle.UNDERLINE)) {
            tags.append("</u>");
        }
        if (textStyles.contains(TextStyle.ITALIC)) {
            tags.append("</i>");
        }
        if (textStyles.contains(TextStyle.BOLD)) {
            tags.append("</b>");
        }
        return tags.toString();
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
