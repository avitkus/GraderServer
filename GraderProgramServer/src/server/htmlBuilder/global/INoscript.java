package server.htmlBuilder.global;

import server.htmlBuilder.IHTMLElement;

/**
 *
 * @author Andrew Vitkus
 */
public interface INoscript extends IHTMLElement {
    public void addElement(IHTMLElement element);
    public IHTMLElement[] getElements();
}
