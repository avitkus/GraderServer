package server.htmlBuilder.body;

import java.net.URL;

import server.htmlBuilder.attributes.LinkTarget;
import server.htmlBuilder.util.IColorable;

/**
 * @author Andrew Vitkus
 *
 */
public interface IHyperlink extends IBodyElement, IColorable {
	
	public void setURL(String url);
	public void setURL(URL url);
	public String getURL();

	public void addContent(IBodyElement element);
	public IBodyElement[] getContents();
	
	public void setTarget(LinkTarget target);
	public LinkTarget getTarget();
}
