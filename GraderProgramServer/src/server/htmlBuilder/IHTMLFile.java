package server.htmlBuilder;

import server.cssBuilder.ICSSFile;
import server.htmlBuilder.body.IBody;
import server.htmlBuilder.doctype.IDoctype;
import server.htmlBuilder.head.IHead;

/**
 * @author Andrew Vitkus
 *
 */
public interface IHTMLFile {
	public void setDoctype(IDoctype doctype);
	public IDoctype getDoctype();
	
	public void addCSSFile(String file);
	
	public void addInlineCSS(ICSSFile file);
	
	public void setHead(IHead head);
	public IHead getHead();
	
	public void setBody(IBody body);
	public IBody getBody();
	
	public String getHTML();
}
