package server.htmlBuilder.form;

import java.util.ArrayList;

import server.htmlBuilder.util.AttributeManager;
import server.htmlBuilder.util.IAttributeManager;
import server.htmlBuilder.util.Offsetter;

/**
 * @author Andrew Vitkus
 *
 */
public class OptionGroup implements IOptionGroup {
	private IAttributeManager attrs;
	private ArrayList<IOption> options;
	
	/**
	 * 
	 */
	public OptionGroup() {
		attrs = new AttributeManager();
		options = new ArrayList<>();
	}

	@Override
	public String getText(int depth) {
		StringBuilder html = new StringBuilder();
		
		html.append(Offsetter.indent(depth ++)).append("<optgroup").append(attrs.getHTML()).append(">\n");
		for(IOption option : options) {
			html.append(option.getText(depth)).append("\n");
		}
		html.append(Offsetter.indent(depth - 1)).append("</optgroup>");
		
		return html.toString();
	}

	@Override
	public String getTagType() {
		return "optgroup";
	}

	@Override
	public void addOption(IOption option) {
		options.add(option);
	}

	@Override
	public IOption[] getOptions() {
		return options.toArray(new IOption[options.size()]);
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
	public void setLabel(String label) {
		attrs.addAttribute("label", label);
	}

	@Override
	public String getLabel() {
		return attrs.getAttribute("label");
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
