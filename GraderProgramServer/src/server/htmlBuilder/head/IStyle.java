package server.htmlBuilder.head;

import server.cssBuilder.ICSSFile;

/**
 * @author Andrew Vitkus
 *
 */
public interface IStyle extends IHeadElement {
	public void setCSS(ICSSFile file);
	public ICSSFile getCSS();
}
