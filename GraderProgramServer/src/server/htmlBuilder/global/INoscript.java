package server.htmlBuilder.global;

import server.htmlBuilder.IHTMLElement;

/**
 *
 * @author Andrew Vitkus
 */
public interface INoscript extends IGlobalElement {
    public void addElement(IHTMLElement element);
    public IHTMLElement[] getElements();
}
