package server.htmlBuilder.form;

/**
 * @author Andrew Vitkus
 *
 */
public class TextField extends Input implements ITextField {
	
	public TextField() {
		super();
		setType("text");
	}

	@Override
	public void setMinLength(int length) {
		addAttribute("minlength", Integer.toString(length));
	}

	@Override
	public int getMinLength() {
		return Integer.parseInt(getAttribute("minlength"));
	}

	@Override
	public void setMaxLength(int length) {
		addAttribute("maxlength", Integer.toString(length));
	}

	@Override
	public int getMaxLength() {
		return Integer.parseInt(getAttribute("maxlength"));
	}

	@Override
	public void setSize(int size) {
		addAttribute("size", Integer.toString(size));
	}

	@Override
	public int getSize() {
		return Integer.parseInt(getAttribute("size"));
	}

	@Override
	public void setPattern(String pattern) {
		addAttribute("pattern", pattern);
	}

	@Override
	public String getPattern() {
		return getAttribute("pattern");
	}

	@Override
	public void setPlaceholder(String placeholder) {
		addAttribute("placeholder", placeholder);
	}

	@Override
	public String getPlaceholder() {
		return getAttribute("placeholder");
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
