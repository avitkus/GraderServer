package server.htmlBuilder.util;

/**
 * @author Andrew Vitkus
 *
 */
public interface ITableStylable extends IColorable, IBGColorable {
	public void setBorderColor(String color);
	public String getBorderColor();
	
	public String getBorderCollapse();
	public void setBorderCollapse(String collapse);
	
	public String getBorderWidth();
	public void setBorderWidth(String width);
	
	public String getBorderStyle();
	public void setBorderStyle(String style);
}
