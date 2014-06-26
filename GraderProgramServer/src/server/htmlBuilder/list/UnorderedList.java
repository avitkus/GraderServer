package server.htmlBuilder.list;

import java.util.ArrayList;

import server.htmlBuilder.body.IBodyElement;
import server.htmlBuilder.util.AttributeManager;
import server.htmlBuilder.util.IAttributeManager;
import server.htmlBuilder.util.IStyleManager;
import server.htmlBuilder.util.StyleManager;
import server.htmlBuilder.util.Offsetter;

/**
 * @author Andrew Vitkus
 *
 */
public class UnorderedList implements IUnorderedList {

    private ArrayList<IListItem> items;
    private IAttributeManager attrs;
    private IStyleManager styleManager;
    private String className;
    public String id;

    public UnorderedList(IListItem... items) {
        styleManager = new StyleManager();
        attrs = new AttributeManager();
        className = "";
        id = "";
        this.items = new ArrayList<>();
        for (IListItem item : items) {
            this.items.add(item);
        }
    }

    @Override
    public String getText(int indent) {
        StringBuilder text = new StringBuilder();
        text.append(Offsetter.indent(indent++)).append("<ul");
        if (className != "") {
            text.append(" class=\"").append(className).append("\"");
        }
        if (id != "") {
            text.append(" id=\"").append(id).append("\"");
        }
        text.append(styleManager.getStyleHTML()).append(attrs.getHTML()).append(">\n");
        for (IListItem item : items) {
            text.append(item.getText(indent)).append("\n");
        }
        text.append(Offsetter.indent(indent - 1)).append("</ul>");
        return text.toString();
    }

    @Override
    public String getTagType() {
        return "ordered list";
    }

    @Override
    public void addListItem(IListItem item) {
        items.add(item);
    }

    @Override
    public void addContents(IBodyElement element) {
        addListItem(new ListItem(element));
    }

    @Override
    public IListItem[] getListItems() {
        return items.toArray(new IListItem[items.size()]);
    }

    @Override
    public IListItem[] getContents() {
        return getListItems();
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
