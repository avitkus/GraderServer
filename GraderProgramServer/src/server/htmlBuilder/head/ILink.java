package server.htmlBuilder.head;

/**
 * @author Andrew Vitkus
 *
 */
public interface ILink extends IHeadElement {
	public void setRelation(String relation);
	public String getRelation();
	
	public void addLinkAttribtue(String name, String value);
	public String getLinkAttribute(String name);
	public String[][] getLinkAttributes();
}
