package server.htmlBuilder.body;

import java.util.ArrayList;
import java.util.Arrays;
import server.htmlBuilder.util.AttributeManager;
import server.htmlBuilder.util.EventManager;
import server.htmlBuilder.util.IAttributeManager;
import server.htmlBuilder.util.IEventManager;
import server.htmlBuilder.util.IStyleManager;
import server.htmlBuilder.util.Offsetter;
import server.htmlBuilder.util.ScriptGenerator;
import server.htmlBuilder.util.StyleManager;

/**
 * @author Andrew Vitkus
 *
 */
public class Body implements IBody {

    private String id;
    private final IAttributeManager attrs;
    private String className;
    private final ArrayList<IBodyElement> elements;
    private final IEventManager events;
    private final IStyleManager styleManager;

    public Body(IBodyElement... elements) {
        attrs = new AttributeManager();
        styleManager = new StyleManager();
        events = new EventManager();
        this.elements = new ArrayList<>(20);
        className = "";
        id = "";
        this.elements.addAll(Arrays.asList(elements));
    }

    @Override
    public void addAttribute(String name, String value) {
        attrs.addAttribute(name, value);
    }

    @Override
    public void addElement(IBodyElement element) {
        elements.add(element);
    }

    @Override
    public void addStyle(String name, String value) {
        styleManager.addStyle(name, value);
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
    public String getBGColor() {
        return styleManager.getStyle(IStyleManager.BGCOLOR);
    }

    @Override
    public void setBGColor(String color) {
        addStyle(IStyleManager.BGCOLOR, color);
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public IBodyElement[] getElements() {
        return elements.toArray(new IBodyElement[elements.size()]);
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public void setID(String id) {
        this.id = id;
    }

    @Override
    public ScriptGenerator getOnload() {
        return events.getEvent("onload");
    }

    @Override
    public void setOnload(ScriptGenerator script) {
        events.addEvent("onload", script);
    }

    @Override
    public String[][] getStyles() {
        return styleManager.getStyles();
    }

    @Override
    public String getTagType() {
        return "body";
    }

    @Override
    public String getText(int indent) {
        StringBuilder text = new StringBuilder();
        text.append(Offsetter.indent(indent++)).append("<body");
        if (!className.isEmpty()) {
            text.append(" class=\"").append(className).append("\"");
        }
        if (!id.isEmpty()) {
            text.append(" id=\"").append(id).append("\"");
        }
        text.append(styleManager.getStyleHTML()).append(attrs.getHTML());
        text.append(events.getHTML()).append(">\n");
        for (IBodyElement element : elements) {
            text.append(element.getText(indent)).append("\n");
        }
        text.append(Offsetter.indent(indent - 1)).append("</body>");
        return text.toString();
    }

    @Override
    public void removeAttribute(String name) {
        attrs.removeAttribute(name);
    }
}
