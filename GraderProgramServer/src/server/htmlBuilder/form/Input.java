package server.htmlBuilder.form;

import server.htmlBuilder.util.AttributeManager;
import server.htmlBuilder.util.IAttributeManager;
import server.htmlBuilder.util.Offsetter;

/**
 * @author Andrew Vitkus
 *
 */
public abstract class Input implements IInput {
	private IAttributeManager attrs;
	private String className;
	public String id;
	
	public Input() {
		attrs = new AttributeManager();
		className = "";
		id = "";
	}
	
	@Override
	public String getText(int depth) {
		StringBuilder html = new StringBuilder();
		html.append(Offsetter.indent(depth)).append("<input");
		if (className != "") {
			html.append(" class=\"").append(className).append("\"");
		}
		if (id != "") {
			html.append(" id=\"").append(id).append("\"");
		}
		html.append(attrs.getHTML()).append(">");
		return html.toString();
	}

	@Override
	public String getTagType() {
		return "input";
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
	public void setName(String name) {
		addAttribute("name", name);
	}

	@Override
	public String getName() {
		return getAttribute("name");
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
	public void setForm(String form) {
		addAttribute("form", form);
	}

	@Override
	public String getForm() {
		return getAttribute("form");
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
	public void setType(String type) {
		addAttribute("type", type);
	}

	@Override
	public String getType() {
		return getAttribute("type");
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
