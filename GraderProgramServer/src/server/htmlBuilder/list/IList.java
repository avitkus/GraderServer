package server.htmlBuilder.list;

import server.htmlBuilder.body.IBodyElement;
import server.htmlBuilder.util.IColorable;

/**
 * @author Andrew Vitkus
 *
 */
public interface IList extends IBodyElement, IColorable, IListItem {

    public void addListItem(IListItem item);

    public IListItem[] getListItems();
}
