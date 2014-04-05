package server.htmlBuilder.form.input;

import server.htmlBuilder.form.input.Button;

/**
 * @author Andrew Vitkus
 *
 */
public class ResetButton extends Button implements IResetButton {
	public ResetButton() {
		super();
		setType("reset");
	}
}
