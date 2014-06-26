package server.htmlBuilder.list;

import server.htmlBuilder.body.IBodyElement;
import server.htmlBuilder.util.IColorable;

/**
 * @author Andrew Vitkus
 *
 */
public interface IListItem extends IBodyElement, IColorable {

    public void addContents(IBodyElement contents);

    public IBodyElement[] getContents();
}
