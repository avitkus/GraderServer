package server.htmlBuilder.head;

import server.cssBuilder.ICSSFile;
import server.htmlBuilder.util.AttributeManager;
import server.htmlBuilder.util.IAttributeManager;
import server.htmlBuilder.util.Offsetter;

/**
 * @author Andrew Vitkus
 *
 */
public class Style implements IStyle {
	private ICSSFile css;
	private IAttributeManager attrs;
	
	public Style() {
		css = null;
		attrs = new AttributeManager();
	}
	
	@Override
	public String getText(int depth) {
		StringBuilder html = new StringBuilder();
		html.append(Offsetter.indent(depth++)).append("<style" + attrs.getHTML() + ">\n");
		html.append(css.getCSS(depth)).append("\n");
		html.append(Offsetter.indent(depth - 1)).append("<style>");
		return html.toString();
	}

	@Override
	public String getTagType() {
		return "style";
	}

	@Override
	public void setCSS(ICSSFile file) {
		css = file;
	}

	@Override
	public ICSSFile getCSS() {
		return css;
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
