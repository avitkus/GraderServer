package server.htmlBuilder.form;

/**
 * @author Andrew Vitkus
 *
 */
public interface ITextArea extends IFormField {

    public void setAutoFocus(boolean autofocus);

    public boolean getAutoFocus();

    public void setColumnCount(int cols);

    public int getColumnCount();

    public void setDisabled(boolean disable);

    public boolean getDisabled();

    public void setForm(String form);

    public String getForm();

    public void setMaxLength(int length);

    public int getMaxLength();

    public void setName(String name);

    public String getName();

    public void setPlaceholder(String placeholder);

    public String getPlaceholder();

    public void setReadOnly(boolean readonly);

    public boolean getReadOnly();

    public void setRequired(boolean required);

    public boolean getRequired();

    public void setRowCount(int rows);

    public int getRowCount();

    public void setWrap(String wrap);

    public String getWrap();

    public void setText(String text);

    public String getText();
}
