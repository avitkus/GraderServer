package server.htmlBuilder.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * 
 * @author Andrew Vitkus
 *
 */
public class AttributeManager implements IAttributeManager {
	private HashMap<String, String> attributes;
	
	public AttributeManager() {
		attributes = new HashMap<>();
	}
	
	@Override
	public void addAttribute(String name, String value) {
		attributes.put(name, value);
	}
	
	@Override
	public void removeAttribute(String name) {
		attributes.remove(name);
	}
	
	@Override
	public boolean hasAttribute(String name) {
		return attributes.containsKey(name);
	}

	@Override
	public String getAttribute(String name) {
		return attributes.get(name);
	}

	@Override
	public String[][] getAttributes() {
		String[][] attrs = new String[attributes.size()][2];
		
		Iterator<Entry<String,String>> attrIter = attributes.entrySet().iterator();
		for(int i = 0; i < attributes.size(); i ++) {
			Entry<String, String> entry = attrIter.next();
			attrs[i][0] = entry.getKey();
			attrs[i][1] = entry.getValue();
		}
		
		return attrs;
	}

	@Override
	public String getHTML() {
		if (attributes.isEmpty()) {
			return "";
		}
		
		StringBuilder html = new StringBuilder();
		for(Entry<String,String> entry : attributes.entrySet()) {
			String value = entry.getValue();
			html.append(" ").append(entry.getKey());
			if (value != null) {
				html.append("=\"").append(entry.getValue()).append("\"");
			}
		}
		return html.toString();
	}

}
