package server.htmlBuilder.form;

/**
 * @author Andrew Vitkus
 *
 */
public class PasswordField extends Input implements IPasswordField {

	/**
	 * 
	 */
	public PasswordField() {
		super();
		setType("password");
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

}
