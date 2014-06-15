package server.htmlBuilder.global;

import java.util.ArrayList;
import server.htmlBuilder.IHTMLElement;
import server.htmlBuilder.util.AttributeManager;
import server.htmlBuilder.util.IAttributeManager;
import server.htmlBuilder.util.Offsetter;

public class Noscript implements INoscript {

    private final ArrayList<IHTMLElement> elements;
    private final IAttributeManager attrs;
    private String className;
    private String id;

    public Noscript() {
        attrs = new AttributeManager();
        elements = new ArrayList<>(3);
        className = "";
        id = "";
    }

    @Override
    public String getText(int indent) {
        StringBuilder text = new StringBuilder(100);
        text.append(Offsetter.indent(indent++)).append("<noscript");
        if (!className.isEmpty()) {
            text.append(" class=\"").append(className).append("\"");
        }
        if (!id.isEmpty()) {
            text.append(" id=\"").append(id).append("\"");
        }
        text.append(">\n");
        for (IHTMLElement element : elements) {
            text.append(element.getText(indent)).append("\n");
        }
        text.append(Offsetter.indent(indent - 1)).append("</noscript>");
        return text.toString();
    }

    @Override
    public String getTagType() {
        return "noscript";
    }

    @Override
    public IHTMLElement[] getElements() {
        return elements.toArray(new IHTMLElement[elements.size()]);
    }

    @Override
    public void addElement(IHTMLElement element) {
        elements.add(element);
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
