package server.com.webHandler.pages;

import server.htmlBuilder.HTMLFile;
import server.htmlBuilder.body.Body;
import server.htmlBuilder.body.Header;
import server.htmlBuilder.body.IBody;
import server.htmlBuilder.doctype.HTML5Doctype;
import server.htmlBuilder.head.Head;
import server.htmlBuilder.head.ILink;
import server.htmlBuilder.head.IMetaAttr;
import server.htmlBuilder.head.ITitle;
import server.htmlBuilder.head.Link;
import server.htmlBuilder.head.MetaAttr;
import server.htmlBuilder.head.Title;

/**
 * @author Andrew Vitkus
 *
 */
public class NotFoundPage extends HTMLFile implements INotFoundPage {
	
	private String page;
	
	@Override
	public void setPage(String page) {
		this.page = page;
	}
	
	@Override
	public String getPage() {
		return page;
	}

	@Override
	public String getHTML() {
		setDoctype(new HTML5Doctype());
		buildParts();
		return super.getHTML();
	}


	private void buildParts() {
		buildHead();
		buildBody();
	}
	
	private void buildHead() {
		ITitle title = new Title("404 Not Found");
		
		IMetaAttr charset = new MetaAttr();
		charset.addAttribute("charset", "UTF-8");
		
		ILink faviconLink = new Link();
		faviconLink.setRelation("shortcut icon");
		faviconLink.addLinkAttribtue("href", "favicon.ico");
		faviconLink.addLinkAttribtue("type", "image/vnd.microsoft.icon");
		
		setHead(new Head(title, charset, faviconLink));
	}
	
	private void buildBody() {
		IBody body = new Body();
		
		body.addElement(new Header("Error 404", 1));
		body.addElement(new Header("Page '" + page + "' not found!", 2));
		setBody(body);
	}
}
