package server.htmlBuilder.form.input;

import server.htmlBuilder.form.input.Button;

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
