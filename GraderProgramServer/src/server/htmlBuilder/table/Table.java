package server.htmlBuilder.table;

import java.util.ArrayList;

import server.htmlBuilder.util.AttributeManager;
import server.htmlBuilder.util.IAttributeManager;
import server.htmlBuilder.util.IBorderStyles;
import server.htmlBuilder.util.IStyleManager;
import server.htmlBuilder.util.Offsetter;
import server.htmlBuilder.util.StyleManager;

/**
 * @author Andrew Vitkus
 *
 */
public class Table implements ITable {
	
	private ArrayList<ITableRow> rows;
	private IAttributeManager attrs;
	private IStyleManager styleManager;
	private String border;
	private String className;
	private String id;
	
	public Table(ITableRow... rows) {
		styleManager = new StyleManager();
		attrs = new AttributeManager();
		this.rows = new ArrayList<>();
		className = "";
		id = "";
		for(ITableRow row : rows) {
			addRow(row);
		}
		border = "";
	}
	
	@Override
	public String getText(int indent) {
		StringBuilder text = new StringBuilder();
		text.append(Offsetter.indent(indent++)).append("<table");
		if (className != "") {
			text.append(" class=\"").append(className).append("\"");
		}
		if (id != "") {
			text.append(" id=\"").append(id).append("\"");
		}
		if (border != "") {
			text.append(border);
		}
		text.append(styleManager.getStyleHTML()).append(attrs.getHTML()).append(">\n");
		for(ITableRow row : rows) {
			text.append(row.getText(indent)).append("\n");
		}
		text.append(Offsetter.indent(indent - 1)).append("</table>");
		return text.toString();
	}

	@Override
	public String getTagType() {
		return "table";
	}

	@Override
	public ITableRow[] getRows() {
		return rows.toArray(new ITableRow[rows.size()]);
	}

	@Override
	public void addRow(ITableRow row) {
		fixRowStyles(row);
		rows.add(row);
	}
	
	private void fixRowStyles(ITableRow row) {
		if (styleManager.isSet(IStyleManager.BORDER_COLLAPSE)) {
			if (row.getBorderCollapse() == null) {
				row.setBorderCollapse(IBorderStyles.INHERIT);
			}
		}
		if (styleManager.isSet(IStyleManager.BORDER_COLOR)) {
			if (row.getBorderColor() == null) {
				row.setBorderColor(IBorderStyles.INHERIT);
			}
		}
		if (styleManager.isSet(IStyleManager.BORDER_STYLE)) {
			if (row.getBorderStyle() == null) {
				row.setBorderStyle(IBorderStyles.INHERIT);
			}
		}
		if (styleManager.isSet(IStyleManager.BORDER_WIDTH)) {
			if (row.getBorderWidth() == null) {
				row.setBorderWidth(IBorderStyles.INHERIT);
			}
		}
	}

	@Override
	public void setColor(String color) {
		addStyle(IStyleManager.COLOR, color);
	}

	@Override
	public String getColor() {
		return styleManager.getStyle(IStyleManager.COLOR);
	}

	@Override
	public void addStyle(String name, String value) {
		styleManager.addStyle(name, value);
		for(ITableRow row : rows) {
			fixRowStyles(row);
		}
	}

	@Override
	public String[][] getStyles() {
		return styleManager.getStyles();
	}

	@Override
	public void setBGColor(String color) {
		addStyle(IStyleManager.BGCOLOR, color);
	}

	@Override
	public String getBGColor() {
		return styleManager.getStyle(IStyleManager.BGCOLOR);
	}

	@Override
	public void setBorderColor(String color) {
		addStyle(IStyleManager.BORDER_COLOR, color);
	}

	@Override
	public String getBorderColor() {
		return styleManager.getStyle(IStyleManager.BORDER_COLOR);
	}

	@Override
	public String getBorderCollapse() {
		return styleManager.getStyle(IStyleManager.BORDER_COLLAPSE);
	}

	@Override
	public void setBorderCollapse(String collapse) {
		addStyle(IStyleManager.BORDER_COLLAPSE, collapse);
	}
	
	@Override
	public String getBorderWidth() {
		return styleManager.getStyle(IStyleManager.BORDER_WIDTH);
	}

	@Override
	public void setBorderWidth(String width) {
		addStyle(IStyleManager.BORDER_WIDTH, width);
	}

	@Override
	public String getBorderStyle() {
		return styleManager.getStyle(IStyleManager.BORDER_STYLE);
	}

	@Override
	public void setBorderStyle(String style) {
		addStyle(IStyleManager.BORDER_STYLE, style);
	}

	@Override
	public void setBorder(String thickness, String style) {
		border = " border=\"" + thickness + " " + style + "\"";
	}

	@Override
	public void setBorder(int thickness, String style) {
		setBorder(Integer.toString(thickness), style);
	}
	
	@Override
	public void removeBorder() {
		border = "";
	}

	@Override
	public void addAttribute(String name, String value) {
		attrs.addAttribute(name, value);
	}
	
	@Override
	public void removeAttribute(String name) {
		attrs.removeAttribute(name);
	}

	@Override
	public String getAttribute(String name) {
		return attrs.getAttribute(name);
	}

	@Override
	public String[][] getAttributes() {
		return attrs.getAttributes();
	}
	@Override
	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public String getClassName() {
		return className;
	}

	@Override
	public void setID(String id) {
		this.id = id;
	}

	@Override
	public String getID() {
		return id;
	}
}
