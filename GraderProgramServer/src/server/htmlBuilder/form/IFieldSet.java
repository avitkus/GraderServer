package server.htmlBuilder.form;

import server.htmlBuilder.IHTMLElement;

/**
 * @author Andrew Vitkus
 *
 */
public interface IFieldSet extends IFormField {

    public void addField(IHTMLElement element);

    public IHTMLElement[] getFields();

    public void setLegend(String legend);

    public String getLegend();

    public void setDisabled(boolean disable);

    public boolean getDisabled();

    public void setForm(String form);

    public String getForm();

    public void setName(String name);

    public String getName();
}
