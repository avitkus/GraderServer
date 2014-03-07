package server.htmlBuilder.form;

import server.htmlBuilder.IHTMLElement;

/**
 * @author Andrew Vitkus
 *
 */
public interface IOptionGroup extends IHTMLElement {
	public void addOption(IOption option);
	public IOption[] getOptions();
	
	public void setDisabled(boolean disabled);
	public boolean getDisabled();
	public void setLabel(String label);
	public String getLabel();
}
