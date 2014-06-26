package server.htmlBuilder.form;

import server.htmlBuilder.body.IBodyElement;
import server.htmlBuilder.util.AttributeManager;
import server.htmlBuilder.util.IAttributeManager;
import server.htmlBuilder.util.Offsetter;

/**
 * @author Andrew Vitkus
 *
 */
public class Label implements ILabel {

    private IAttributeManager attrs;
    private IFormElement element;
    private IBodyElement label;
    private boolean labelFirst;
    private String className;
    public String id;

    /**
     *
     */
    public Label() {
        attrs = new AttributeManager();
        element = null;
        label = null;
        labelFirst = true;
        className = "";
        id = "";
    }

    @Override
    public String getText(int depth) {
        StringBuilder html = new StringBuilder();
        html.append(Offsetter.indent(depth++)).append("<label");
        if (className != "") {
            html.append(" class=\"").append(className).append("\"");
        }
        if (id != "") {
            html.append(" id=\"").append(id).append("\"");
        }
        html.append(attrs.getHTML()).append(">\n");
        if (element != null) {
            if (labelFirst) {
                if (label != null) {
                    html.append(label.getText(depth)).append("\n");
                }
                html.append(element.getText(depth)).append("\n");
            } else {
                html.append(element.getText(depth)).append("\n");
                if (label != null) {
                    html.append(label.getText(depth)).append("\n");
                }
            }
        } else {
            if (label != null) {
                html.append(label.getText(depth)).append("\n");
            }
        }
        html.append(Offsetter.indent(depth - 1)).append("</label>");
        return html.toString();
    }

    @Override
    public String getTagType() {
        return "label";
    }

    @Override
    public void setElementID(String elementID) {
        if (element != null) {
            element = null;
        }
        attrs.addAttribute("for", elementID);
    }

    @Override
    public String getElementID() {
        return attrs.getAttribute("for");
    }

    @Override
    public void setElement(IFormElement element) {
        if (attrs.hasAttribute("for")) {
            attrs.removeAttribute("for");
        }
        this.element = element;
    }

    @Override
    public IFormElement getElement() {
        return element;
    }

    @Override
    public void setForm(String form) {
        attrs.addAttribute("form", form);
    }

    @Override
    public String getForm() {
        return attrs.getAttribute("form");
    }

    @Override
    public void setLabel(IBodyElement label) {
        this.label = label;
    }

    @Override
    public IBodyElement getLabel() {
        return label;
    }

    @Override
    public void labelFirst(boolean first) {
        labelFirst = first;
    }

    @Override
    public boolean isLableFirst() {
        return labelFirst;
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
