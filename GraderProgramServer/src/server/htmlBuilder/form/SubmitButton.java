package server.htmlBuilder.form;

/**
 * @author Andrew Vitkus
 *
 */
public class SubmitButton extends Button implements ISubmitButton {
	public SubmitButton() {
		super();
		setType("submit");
	}
}
