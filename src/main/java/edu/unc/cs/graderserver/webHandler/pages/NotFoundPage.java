package edu.unc.cs.graderServer.webHandler.pages;

import edu.unc.cs.htmlBuilder.HTMLFile;
import edu.unc.cs.htmlBuilder.body.Body;
import edu.unc.cs.htmlBuilder.body.Division;
import edu.unc.cs.htmlBuilder.body.Header;
import edu.unc.cs.htmlBuilder.body.IBody;
import edu.unc.cs.htmlBuilder.body.IDivision;
import edu.unc.cs.htmlBuilder.body.Text;
import edu.unc.cs.htmlBuilder.doctype.HTML5Doctype;
import edu.unc.cs.htmlBuilder.head.Head;
import edu.unc.cs.htmlBuilder.head.ILink;
import edu.unc.cs.htmlBuilder.head.IMetaAttr;
import edu.unc.cs.htmlBuilder.head.ITitle;
import edu.unc.cs.htmlBuilder.head.Link;
import edu.unc.cs.htmlBuilder.head.MetaAttr;
import edu.unc.cs.htmlBuilder.head.Title;
import edu.unc.cs.graderServer.webHandler.pages.parts.StudentDataNavBar;

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
        IDivision bodyWrapper = new Division();
        bodyWrapper.setClass("body-wrapper");

        IDivision title = new Division();
        title.setClass("title");
        title.addContent(new Header("Error 404", 1));
        bodyWrapper.addContent(title);

        IDivision content = new Division();
        content.setClass("content");
        content.addContent(new StudentDataNavBar());
        content.addContent(new Text("Page '" + page + "' not found!"));
        bodyWrapper.addContent(content);
        body.addElement(bodyWrapper);

        setBody(body);
    }
}
