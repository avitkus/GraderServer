package server.htmlBuilder.form;

/**
 * @author Andrew Vitkus
 *
 */
public class RadioButton extends Input implements IRadioButton {

	/**
	 * 
	 */
	public RadioButton() {
		super();
		setType("radio");
	}
	
	@Override
	public void setChecked(boolean checked) {
		if(checked) {
			addAttribute("checked", null);
		} else {
			removeAttribute("checked");
		}
	}

	@Override
	public boolean getChecked() {
		return Boolean.parseBoolean(getAttribute("checked"));
	}

}
