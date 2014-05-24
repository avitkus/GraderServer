package server.htmlBuilder;

import server.cssBuilder.ICSSFile;
import server.htmlBuilder.body.IBody;
import server.htmlBuilder.doctype.IDoctype;
import server.htmlBuilder.head.IHead;
import server.htmlBuilder.head.ILink;
import server.htmlBuilder.head.IStyle;
import server.htmlBuilder.head.Link;
import server.htmlBuilder.head.Style;

/**
 * @author Andrew Vitkus
 *
 */
public class HTMLFile implements IHTMLFile {
	private IDoctype doctype;
	private IHead head;
	private IBody body;
	
	public HTMLFile() {
		this(null, null, null);
	}
	
	public HTMLFile(IDoctype doctype, IHead head, IBody body) {
		this.doctype = doctype;
		this.head = head;
		this.body = body;
	}
	
	@Override
	public void setHead(IHead head) {
		this.head = head;
	}

	@Override
	public IHead getHead() {
		return head;
	}

	@Override
	public void setBody(IBody body) {
		this.body = body;
	}

	@Override
	public IBody getBody() {
		return body;
	}

	@Override
	public void setDoctype(IDoctype doctype) {
		this.doctype = doctype;
	}

	@Override
	public IDoctype getDoctype() {
		return doctype;
	}
	
	@Override
	public String getHTML() {
		StringBuilder text = new StringBuilder(500);
		text.append(doctype.getText(0)).append("\n");
		text.append("<html>\n");
		text.append(head.getText(1)).append("\n");
		text.append(body.getText(1)).append("\n");
		text.append("</html>");
		return text.toString();
	}

	@Override
	public void addCSSFile(String file) {
		ILink cssLink = new Link();
		cssLink.addLinkAttribtue("rel", "stylesheet");
		cssLink.addLinkAttribtue("type", "text/css");
		cssLink.addLinkAttribtue("href", file);
		head.addElement(cssLink);
	}

	@Override
	public void addInlineCSS(ICSSFile file) {
		IStyle style = new Style();
		style.setCSS(file);
		head.addElement(style);
	}
}
