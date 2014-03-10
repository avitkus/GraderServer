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
public class Body implements IBody {

	private ArrayList<IBodyElement> elements;
	private IAttributeManager attrs;
	private IStyleManager styleManager;
	private String className;
	public String id;
	
	public Body(IBodyElement... elements) {
		attrs = new AttributeManager();
		styleManager = new StyleManager();
		this.elements = new ArrayList<>();
		className = "";
		id = "";
		for(IBodyElement element : elements) {
			this.elements.add(element);
		}
	}
	
	@Override
	public String getText(int indent) {
		StringBuilder text = new StringBuilder();
		text.append(Offsetter.indent(indent++)).append("<body");
		if (className != "") {
			text.append(" class=\"").append(className).append("\"");
		}
		if (id != "") {
			text.append(" id=\"").append(id).append("\"");
		}
		text.append(styleManager.getStyleHTML()).append(attrs.getHTML()).append(">\n");
		for(IBodyElement element : elements) {
			text.append(element.getText(indent)).append("\n");
		}
		text.append(Offsetter.indent(indent - 1)).append("</body>");
		return text.toString();
	}

	@Override
	public String getTagType() {
		return "body";
	}
	

	@Override
	public IBodyElement[] getElements() {
		return elements.toArray(new IBodyElement[elements.size()]);
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
	public String[][] getStyles() {
		return styleManager.getStyles();
	}

	@Override
	public void setBGColor(String color) {
		addStyle(IStyleManager.BGCOLOR, color);
	}

	@Override
	public String getBGColor() {
		return styleManager.getStyle(IStyleManager.BGCOLOR);
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
