package server.htmlBuilder.body;

import server.htmlBuilder.IHTMLElement;
import server.htmlBuilder.util.IBGColorable;

/**
 * @author Andrew Vitkus
 *
 */
public interface IBody extends IHTMLElement, IBGColorable {
	public IBodyElement[] getElements();
	public void addElement(IBodyElement element);
}
