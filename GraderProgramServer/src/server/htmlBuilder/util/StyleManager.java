package server.htmlBuilder.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * @author Andrew Vitkus
 *
 */
public class StyleManager implements IStyleManager {

	private HashMap<String, String> styles;
	
	public StyleManager() {
		styles = new HashMap<>();
	}
	
	@Override
	public void addStyle(String name, String value) {
		styles.put(name, value);
	}
	
	@Override
	public void removeStyle(String name) {
		styles.remove(name);
	}
	
	@Override
	public String getStyle(String name) {
		return styles.get(name);
	}
	
	@Override
	public boolean isSet(String name) {
		return styles.containsKey(name);
	}

	@Override
	public void clear() {
		styles.clear();
	}

	@Override
	public String[][] getStyles() {
		String[][] styleArr = new String[styles.size()][];
		Iterator<Entry<String,String>> styleIter = styles.entrySet().iterator();
		
		for(int i = 0; styleIter.hasNext(); i++) {
			Entry<String,String> style = styleIter.next();
			styleArr[i] = new String[]{style.getKey(), style.getValue()};
		}
		
		return styleArr;
	}

	@Override
	public String getStyleHTML() {
		if (styles.isEmpty()) {
			return "";
		}
		StringBuffer html = new StringBuffer();
		html.append(" style=\"");
		String[][] styles = getStyles();
		for(int i = 0; i < styles.length; i ++) {
			String[] style = styles[i];
			html.append(style[0]).append(":").append(style[1]).append(";");
		}
		html.append("\"");
		return html.toString();
	}

	@Override
	public boolean isEmpty() {
		return styles.isEmpty();
	}

	@Override
	public int size() {
		return styles.size();
	}

}
