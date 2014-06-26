package server.htmlBuilder.table;

import server.htmlBuilder.body.IBodyElement;
import server.htmlBuilder.util.ITableStylable;

/**
 * @author Andrew Vitkus
 *
 */
public interface ITableRow extends IBodyElement, ITableStylable {

    public void addDataPart(ITableData data);

    public ITableData[] getDataParts();
}
