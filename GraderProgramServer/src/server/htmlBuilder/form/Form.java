package server.htmlBuilder.form;

import java.util.ArrayList;

import server.htmlBuilder.body.IBodyElement;
import server.htmlBuilder.util.AttributeManager;
import server.htmlBuilder.util.IAttributeManager;
import server.htmlBuilder.util.Offsetter;

/**
 *
 * @author Andrew Vitkus
 *
 */
public class Form implements IForm {

    private IAttributeManager attrs;
    private ArrayList<IBodyElement> elements;
    private String className;
    public String id;

    public Form() {
        attrs = new AttributeManager();
        elements = new ArrayList<>();
        className = "";
        id = "";
    }

    @Override
    public String getText(int depth) {
        StringBuilder html = new StringBuilder();
        html.append(Offsetter.indent(depth++)).append("<form");
        if (className != "") {
            html.append(" class=\"").append(className).append("\"");
        }
        if (id != "") {
            html.append(" id=\"").append(id).append("\"");
        }
        html.append(attrs.getHTML()).append(">\n");
        for (IBodyElement element : elements) {
            html.append(element.getText(depth)).append("\n");
        }
        html.append(Offsetter.indent(depth - 1)).append("</form>");
        return html.toString();
    }

    @Override
    public String getTagType() {
        return "form";
    }

    @Override
    public void addElement(IBodyElement element) {
        elements.add(element);
    }

    @Override
    public IBodyElement[] getElements() {
        return elements.toArray(new IBodyElement[elements.size()]);
    }

    @Override
    public void setAction(String url) {
        attrs.addAttribute("action", url);
    }

    @Override
    public String getAction() {
        return attrs.getAttribute("action");
    }

    @Override
    public void setEncoding(String encoding) {
        attrs.addAttribute("enctype", encoding);
    }

    @Override
    public String getEncoding() {
        return attrs.getAttribute("enctype");
    }

    @Override
    public void setMethod(String method) {
        attrs.addAttribute("method", method);
    }

    @Override
    public String getMethod() {
        return attrs.getAttribute("method");
    }

    @Override
    public void setName(String name) {
        attrs.addAttribute("name", name);
    }

    @Override
    public String getName() {
        return attrs.getAttribute("name");
    }

    @Override
    public void setTarget(String target) {
        attrs.addAttribute("target", target);
    }

    @Override
    public String getTarget() {
        return attrs.getAttribute("target");
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
