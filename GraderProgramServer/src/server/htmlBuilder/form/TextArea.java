package server.htmlBuilder.form;

import server.htmlBuilder.util.AttributeManager;
import server.htmlBuilder.util.IAttributeManager;
import server.htmlBuilder.util.Offsetter;

/**
 * @author Andrew Vitkus
 *
 */
public class TextArea implements ITextArea {

	private IAttributeManager attrs;
	private String text;
	
	public TextArea() {
		attrs = new AttributeManager();
		text = "";
	}

	@Override
	public String getText(int depth) {
		StringBuilder html = new StringBuilder();
		
		html.append(Offsetter.indent(depth++)).append("<textarea").append(attrs.getHTML()).append(">\n");
		html.append(Offsetter.indent(depth)).append(text).append("\n");
		html.append(Offsetter.indent(depth - 1)).append("</textarea>");
		
		return html.toString();
	}

	@Override
	public String getTagType() {
		return "textarea";
	}

	@Override
	public void setAutoFocus(boolean autofocus) {
		if(autofocus) {
			attrs.addAttribute("autofocus", null);
		} else {
			attrs.removeAttribute("autofocus");
		}
	}

	@Override
	public boolean getAutoFocus() {
		return attrs.hasAttribute("autofocus");
	}

	@Override
	public void setColumnCount(int cols) {
		attrs.addAttribute("cols", Integer.toString(cols));
	}

	@Override
	public int getColumnCount() {
		return Integer.parseInt(attrs.getAttribute("cols"));
	}

	@Override
	public void setDisabled(boolean disable) {
		if(disable) {
			attrs.addAttribute("disabled", null);
		} else {
			attrs.removeAttribute("disabled");
		}
	}

	@Override
	public boolean getDisabled() {
		return attrs.hasAttribute("diabled");
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
	public void setMaxLength(int length) {
		attrs.addAttribute("maxlength", Integer.toString(length));
	}

	@Override
	public int getMaxLength() {
		return Integer.parseInt(attrs.getAttribute("maxlength"));
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
	public void setPlaceholder(String placeholder) {
		attrs.addAttribute("placeholder", placeholder);
	}

	@Override
	public String getPlaceholder() {
		return attrs.getAttribute("placeholder");
	}

	@Override
	public void setReadOnly(boolean readonly) {
		if(readonly) {
			attrs.addAttribute("readonly", null);
		} else {
			attrs.removeAttribute("readonly");
		}
	}

	@Override
	public boolean getReadOnly() {
		return attrs.hasAttribute("readonly");
	}

	@Override
	public void setRequired(boolean required) {
		if(required) {
			attrs.addAttribute("required", null);
		} else {
			attrs.removeAttribute("required");
		}
	}

	@Override
	public boolean getRequired() {
		return attrs.hasAttribute("required");
	}

	@Override
	public void setRowCount(int rows) {
		attrs.addAttribute("rows", Integer.toString(rows));
	}

	@Override
	public int getRowCount() {
		return Integer.parseInt(attrs.getAttribute("rows"));
	}

	@Override
	public void setWrap(String wrap) {
		attrs.addAttribute("wrap", wrap);
	}

	@Override
	public String getWrap() {
		return attrs.getAttribute("wrap");
	}

	@Override
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String getText() {
		return text;
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
