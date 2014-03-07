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
}
