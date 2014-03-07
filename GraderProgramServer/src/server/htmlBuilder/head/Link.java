package server.htmlBuilder.head;

import java.util.HashMap;
import java.util.Map.Entry;

import server.htmlBuilder.util.AttributeManager;
import server.htmlBuilder.util.IAttributeManager;
import server.htmlBuilder.util.Offsetter;

/**
 * @author Andrew Vitkus
 *
 */
public class Link implements ILink {

	private HashMap<String, String> attributes;
	private IAttributeManager attrs;
	
	public Link() {
		attrs = new AttributeManager();
		attributes = new HashMap<>();
	}
	
	@Override
	public String getText(int indent) {
		StringBuilder html = new StringBuilder();
		html.append(Offsetter.indent(indent)).append("<link").append(attrs.getHTML());
		for(String[] attr : getLinkAttributes()) {
			html.append(" ").append(attr[0]).append("=\"").append(attr[1]).append("\"");
		}
		html.append(">");
		return html.toString();
	}

	@Override
	public String getTagType() {
		return "link";
	}

	@Override
	public void setRelation(String relation) {
		attributes.put("rel", relation);
	}

	@Override
	public String getRelation() {
		return attributes.get("rel");
	}

	@Override
	public void addLinkAttribtue(String name, String value) {
		attributes.put(name, value);
	}

	@Override
	public String getLinkAttribute(String name) {
		return attributes.get(name);
	}

	@Override
	public String[][] getLinkAttributes() {
		String[][] attrArr = new String[attributes.size()][2];
		@SuppressWarnings("unchecked")
		Entry<String, String>[] attrs = (Entry<String,String>[])attributes.entrySet().toArray(new Entry[attributes.size()]);
		for(int i = 0; i < attrArr.length; i ++) {
			Entry<String, String> entry = attrs[i];
			attrArr[i][0] = entry.getKey();
			attrArr[i][1] = entry.getValue();
		}
		
		return attrArr;
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
}
