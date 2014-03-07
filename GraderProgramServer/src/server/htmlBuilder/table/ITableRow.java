package server.htmlBuilder.table;

import server.htmlBuilder.util.ITableStylable;
import server.htmlBuilder.body.IBodyElement;

/**
 * @author Andrew Vitkus
 *
 */
public interface ITableRow extends IBodyElement, ITableStylable {
	public void addDataPart(ITableData data);
	public ITableData[] getDataParts();
}
