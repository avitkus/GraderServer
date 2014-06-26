package server.htmlBuilder.head;

import server.htmlBuilder.util.AttributeManager;
import server.htmlBuilder.util.IAttributeManager;
import server.htmlBuilder.util.Offsetter;

/**
 * @author Andrew Vitkus
 *
 */
public class Title implements ITitle {

    private String title;
    private IAttributeManager attrs;
    private String className;
    public String id;

    public Title() {
        this("");
    }

    public Title(String title) {
        this.title = title;
        className = "";
        id = "";
        attrs = new AttributeManager();
    }

    @Override
    public String getText(int indent) {
        StringBuilder text = new StringBuilder();
        text.append(Offsetter.indent(indent)).append("<title");
        if (className != "") {
            text.append(" class=\"").append(className).append("\"");
        }
        if (id != "") {
            text.append(" id=\"").append(id).append("\"");
        }
        text.append(attrs.getHTML()).append(">").append(title).append("</title>");
        return text.toString();
    }

    @Override
    public String getTagType() {
        return "title";
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
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
