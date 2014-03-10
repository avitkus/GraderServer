package server.htmlBuilder;

/**
 * @author Andrew Vitkus
 *
 */
public interface IHTMLElement extends ITag {
	public void addAttribute(String name, String value);
	public String getAttribute(String name);
	public String[][] getAttributes();
	public void removeAttribute(String name);
	public void setClassName(String className);
	public String getClassName();
	public void setID(String id);
	public String getID();
}
