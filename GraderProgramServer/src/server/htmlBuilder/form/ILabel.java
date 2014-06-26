package server.htmlBuilder.form;

import server.htmlBuilder.body.IBodyElement;

/**
 * @author Andrew Vitkus
 *
 */
public interface ILabel extends IFormElement {

    public void setElementID(String elementID);

    public String getElementID();

    public void setElement(IFormElement element);

    public IFormElement getElement();

    public void setForm(String form);

    public String getForm();

    public void setLabel(IBodyElement label);

    public IBodyElement getLabel();

    public void labelFirst(boolean first);

    public boolean isLableFirst();
}
