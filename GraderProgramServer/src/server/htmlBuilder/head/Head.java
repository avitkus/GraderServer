package server.htmlBuilder.head;

import java.util.ArrayList;

import server.htmlBuilder.util.AttributeManager;
import server.htmlBuilder.util.IAttributeManager;
import server.htmlBuilder.util.Offsetter;

/**
 * @author Andrew Vitkus
 *
 */
public class Head implements IHead {

	private ArrayList<IHeadElement> elements;
	private IAttributeManager attrs;
	
	public Head(IHeadElement... elements) {
		this.elements = new ArrayList<>();
		attrs = new AttributeManager();
		for(IHeadElement element : elements) {
			this.elements.add(element);
		}
	}
	
	@Override
	public String getText(int indent) {
		StringBuilder text = new StringBuilder();
		text.append(Offsetter.indent(indent++)).append("<head" + attrs.getHTML() + ">\n");
		for(IHeadElement element : elements) {
			text.append(element.getText(indent)).append("\n");
		}
		text.append(Offsetter.indent(indent - 1)).append("</head>");
		return text.toString();
	}

	@Override
	public String getTagType() {
		return "head";
	}
	

	@Override
	public IHeadElement[] getElements() {
		return elements.toArray(new IHeadElement[elements.size()]);
	}

	@Override
	public void addElement(IHeadElement element) {
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
}
