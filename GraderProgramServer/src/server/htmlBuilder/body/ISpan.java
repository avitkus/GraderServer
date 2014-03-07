package server.htmlBuilder.body;

import server.htmlBuilder.util.IColorable;


/**
 * @author Andrew Vitkus
 *
 */
public interface ISpan extends IBodyElement, IColorable {
	public void addContent(IBodyElement content);
	public IBodyElement[] getContents();
}
