package server.htmlBuilder.head;

import server.htmlBuilder.IHTMLElement;

/**
 * @author Andrew Vitkus
 *
 */
public interface IHead extends IHTMLElement {

    public IHeadElement[] getElements();

    public void addElement(IHeadElement element);
}
