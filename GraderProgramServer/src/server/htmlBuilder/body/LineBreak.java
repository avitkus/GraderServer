package server.htmlBuilder.body;

import server.htmlBuilder.util.AttributeManager;
import server.htmlBuilder.util.IAttributeManager;
import server.htmlBuilder.util.Offsetter;

/**
 * @author Andrew Vitkus
 *
 */
public class LineBreak implements ILineBreak {

	private IAttributeManager attrs;
	
	public LineBreak() {
		attrs = new AttributeManager();
	}
	
	@Override
	public String getText(int depth) {
		return Offsetter.indent(depth) + "<br" + attrs.getHTML() + ">";
	}

	@Override
	public String getTagType() {
		return "line break";
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
