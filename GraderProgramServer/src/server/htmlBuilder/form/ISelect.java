package server.htmlBuilder.form;

/**
 * @author Andrew Vitkus
 *
 */
public interface ISelect extends IFormField {
	public void addOption(IOption option);
	public void addOptionGroup(IOptionGroup group);
	public IOption[] getOptions();
	public IOptionGroup[] getOptionGroups();
	
	public void setAutoFocus(boolean autofocus);
	public boolean getAutoFocus();
	public void setDisabled(boolean disable);
	public boolean getDisabled();
	public void setForm(String form);
	public String getForm();
	public void setMultiple(boolean multiple);
	public boolean getMultiple();
	public void setName(String name);
	public String getName();
	public void setRequired(boolean required);
	public boolean getRequired();
	public void setSize(int size);
	public int getSize();
}
