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
public class TableRow implements ITableRow {
	
	private ArrayList<ITableData> data;
	private IAttributeManager attrs;
	private IStyleManager styleManager;
	private String className;
	public String id;
	
	public TableRow(ITableData... data) {
		styleManager = new StyleManager();
		attrs = new AttributeManager();
		this.data = new ArrayList<>();
		className = "";
		id = "";
		for(ITableData datum : data) {
			this.data.add(datum);
		}
	}
	
	@Override
	public String getText(int indent) {
		StringBuilder text = new StringBuilder();
		text.append(Offsetter.indent(indent++)).append("<tr");
		if (className != "") {
			text.append(" class=\"").append(className).append("\"");
		}
		if (id != "") {
			text.append(" id=\"").append(id).append("\"");
		}
		text.append(styleManager.getStyleHTML()).append(attrs.getHTML()).append(">\n");
		for(ITableData row : data) {
			text.append(row.getText(indent)).append("\n");
		}
		text.append(Offsetter.indent(indent - 1)).append("</tr>");
		return text.toString();
	}

	@Override
	public String getTagType() {
		return "table";
	}

	@Override
	public ITableData[] getDataParts() {
		return data.toArray(new ITableData[data.size()]);
	}

	@Override
	public void addDataPart(ITableData datum) {
		fixDataStyles(datum);
		data.add(datum);
	}
	
	private void fixDataStyles(ITableData data) {
		if (styleManager.isSet(IStyleManager.BORDER_COLLAPSE)) {
			if (data.getBorderCollapse() == null) {
				data.setBorderCollapse(IBorderStyles.INHERIT);
			}
		}
		if (styleManager.isSet(IStyleManager.BORDER_COLOR)) {
			if (data.getBorderColor() == null) {
				data.setBorderColor(IBorderStyles.INHERIT);
			}
		}
		if (styleManager.isSet(IStyleManager.BORDER_STYLE)) {
			if (data.getBorderStyle() == null) {
				data.setBorderStyle(IBorderStyles.INHERIT);
			}
		}
		if (styleManager.isSet(IStyleManager.BORDER_WIDTH)) {
			if (data.getBorderWidth() == null) {
				data.setBorderWidth(IBorderStyles.INHERIT);
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
		for(ITableData datum : data) {
			fixDataStyles(datum);
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
