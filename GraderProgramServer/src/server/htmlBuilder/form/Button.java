package server.htmlBuilder.form;

/**
 * @author Andrew Vitkus
 *
 */
public class Button extends Input implements IButton {

	public Button() {
		super();
		setType("button");
	}
	
	@Override
	public void setValue(String value) {
		addAttribute("value", value);
	}

	@Override
	public String getValue() {
		return getAttribute("value");
	}

}
