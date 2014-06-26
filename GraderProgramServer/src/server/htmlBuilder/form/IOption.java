package server.htmlBuilder.form;

import server.htmlBuilder.IHTMLElement;

/**
 * @author Andrew Vitkus
 *
 */
public interface IOption extends IHTMLElement {

    public void setDisabled(boolean disable);

    public boolean getDisabled();

    public void setLabel(String label);

    public String getLabel();

    public void setText(String text);

    public String getText();

    public void setSelected(boolean selected);

    public boolean getSelected();

    public void setValue(String value);

    public String getValue();
}
