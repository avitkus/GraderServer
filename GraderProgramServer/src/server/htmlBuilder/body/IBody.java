package server.htmlBuilder.body;

import server.htmlBuilder.IHTMLElement;
import server.htmlBuilder.event.IWindowEventHandler;
import server.htmlBuilder.util.IBGColorable;

/**
 * @author Andrew Vitkus
 *
 */
public interface IBody extends IHTMLElement, IBGColorable, IWindowEventHandler {

    public IBodyElement[] getElements();

    public void addElement(IBodyElement element);
}
