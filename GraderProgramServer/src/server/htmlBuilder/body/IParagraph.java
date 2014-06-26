package server.htmlBuilder.body;

import server.htmlBuilder.util.IColorable;

/**
 * @author Andrew Vitkus
 *
 */
public interface IParagraph extends IBodyElement, IColorable {

    public void addContent(IBodyElement element);

    public IBodyElement[] getContents();

    public void insertLineBreak();
}
