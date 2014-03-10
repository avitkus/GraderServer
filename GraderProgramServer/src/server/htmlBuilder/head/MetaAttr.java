package server.htmlBuilder.head;

import java.util.ArrayList;

import server.htmlBuilder.util.AttributeManager;
import server.htmlBuilder.util.IAttributeManager;
import server.htmlBuilder.util.Offsetter;

/**
 * @author Andrew Vitkus
 *
 */
public class MetaAttr implements IMetaAttr {
	
	private ArrayList<String[]> attributes;
	private IAttributeManager attrs;
	private String className;
	public String id;
	
	public MetaAttr() {
		attrs = new AttributeManager();
		attributes = new ArrayList<>();
		className = "";
		id = "";
	}
	
	@Override
	public String getText(int indent) {
		StringBuilder text = new StringBuilder();
		text.append(Offsetter.indent(indent)).append("<meta");
		if (className != "") {
			text.append(" class=\"").append(className).append("\"");
		}
		if (id != "") {
			text.append(" id=\"").append(id).append("\"");
		}
		text.append(attrs.getHTML());
		for(String[] attr : attributes) {
			text.append(" ").append(attr[0]).append("=\"").append(attr[1]).append("\"");
		}
		text.append(">");
		return text.toString();
	}

	@Override
	public String getTagType() {
		return "meta";
	}

	@Override
	public String[] getMetaAttributes() {
		return attributes.toArray(new String[attributes.size()]);
	}

	@Override
	public void addMetaAttribute(String name, String value) {
		attributes.add(new String[] {name, value});
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
