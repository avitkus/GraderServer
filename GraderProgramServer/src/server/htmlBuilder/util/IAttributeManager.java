package server.htmlBuilder.util;

/**
 * @author Andrew Vitkus
 *
 */
public interface IAttributeManager {
	public void addAttribute(String name, String value);
	
	public void removeAttribute(String name);
	
	public boolean hasAttribute(String name);
	
	public String getAttribute(String name);
	
	public String[][] getAttributes();
	
	public String getHTML();
}
