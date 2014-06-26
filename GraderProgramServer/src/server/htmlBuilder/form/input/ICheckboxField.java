package server.htmlBuilder.form.input;

import server.htmlBuilder.form.input.IInput;

/**
 * @author Andrew Vitkus
 *
 */
public interface ICheckboxField extends IInput {

    public void setChecked(boolean checked);

    public boolean getChecked();
}
