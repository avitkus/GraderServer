package server.htmlBuilder.body;

import java.net.URL;
import java.util.ArrayList;

import server.htmlBuilder.attributes.LinkTarget;
import server.htmlBuilder.util.AttributeManager;
import server.htmlBuilder.util.IAttributeManager;
import server.htmlBuilder.util.IStyleManager;
import server.htmlBuilder.util.Offsetter;
import server.htmlBuilder.util.StyleManager;

/**
 * @author Andrew Vitkus
 *
 */
public class Hyperlink implements IHyperlink {
	
	private IAttributeManager attrs;
	private IStyleManager styleManager;
	private String className;
	private String id;
	private ArrayList<IBodyElement> contents;
	private String href;
	private LinkTarget target;
	
	public Hyperlink() {
		className = "";
		id = "";
		attrs = new AttributeManager();
		styleManager = new StyleManager();
		contents = new ArrayList<>();
		href = "";
		target = LinkTarget.BLANK;
	}
	
	
	@Override
	public String getText(int indent) {
		StringBuilder text = new StringBuilder();
		text.append(Offsetter.indent(indent++)).append("<a");
		if (className != "") {
			text.append(" class=\"").append(className).append("\"");
		}
		if (id != "") {
			text.append(" id=\"").append(id).append("\"");
		}
		text.append(styleManager.getStyleHTML()).append(attrs.getHTML());
		text.append(" href=\"").append(href).append("\"");
		text.append(" target=\"_").append(target.name().toLowerCase()).append("\">\n");
		for(IBodyElement element : contents) {
			text.append(Offsetter.indent(indent)).append(element.getText(indent)).append("\n");
		}
		text.append(Offsetter.indent(indent - 1)).append("</a>");
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


	@Override
	public void setURL(String url) {
		href = url;
	}


	@Override
	public void setURL(URL url) {
		href= url.toString();
	}


	@Override
	public String getURL() {
		return href;
	}


	@Override
	public void setTarget(LinkTarget target) {
		this.target = target;
	}


	@Override
	public LinkTarget getTarget() {
		return target;
	}
}
