package server.htmlBuilder;

/**
 * @author Andrew Vitkus
 *
 */
public interface ITag {
	public String getText(int depth);
	public String getTagType();
}
