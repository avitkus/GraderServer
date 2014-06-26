package server.htmlBuilder.body;

import server.htmlBuilder.util.IColorable;

/**
 * @author Andrew Vitkus
 *
 */
public interface INavigation extends IBodyElement, IColorable {

    public void addContent(IBodyElement link);

    public IBodyElement[] getContents();

    public void setClass(String className);
}
