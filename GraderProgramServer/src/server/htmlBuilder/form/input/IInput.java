package server.htmlBuilder.form.input;

import server.htmlBuilder.form.IFormField;

/**
 * @author Andrew Vitkus
 *
 */
public interface IInput extends IFormField {

    public void setName(String name);

    public String getName();

    public void setDisabled(boolean disable);

    public boolean getDisabled();

    public void setAutoFocus(boolean autofocus);

    public boolean getAutoFocus();

    public void setRequired(boolean required);

    public boolean getRequired();

    public void setForm(String form);

    public String getForm();

    public void setReadOnly(boolean readonly);

    public boolean getReadOnly();

    public void setType(String type);

    public String getType();
}
