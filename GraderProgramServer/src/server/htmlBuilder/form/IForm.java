package server.htmlBuilder.form;

import server.htmlBuilder.body.IBodyElement;

/**
 * @author Andrew Vitkus
 *
 */
public interface IForm extends IBodyElement{
	public void addElement(IBodyElement element);
	public IBodyElement[] getElements();
	
	public void setAction(String url);
	public String getAction();
	public void setEncoding(String encoding);
	public String getEncoding();
	public void setMethod(String method);
	public String getMethod();
	public void setName(String name);
	public String getName();
	public void setTarget(String target);
	public String getTarget();
}
