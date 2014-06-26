package server.htmlBuilder.body;

import server.htmlBuilder.util.IColorable;

/**
 * @author Andrew Vitkus
 *
 */
public interface IDivision extends IBodyElement, IColorable {

    public void addContent(IBodyElement content);

    public IBodyElement[] getContents();

    public void setClass(String className);
}
