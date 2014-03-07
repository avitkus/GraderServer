package server.htmlBuilder.form;

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
