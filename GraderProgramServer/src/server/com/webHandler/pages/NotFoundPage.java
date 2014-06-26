package server.com.webHandler.pages;

import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.com.webHandler.pages.parts.StudentDataNavBar;
import server.com.webHandler.sql.DatabaseReader;
import server.com.webHandler.sql.IDatabaseReader;
import server.htmlBuilder.HTMLFile;
import server.htmlBuilder.attributes.LinkTarget;
import server.htmlBuilder.body.Body;
import server.htmlBuilder.body.Division;
import server.htmlBuilder.body.Header;
import server.htmlBuilder.body.Hyperlink;
import server.htmlBuilder.body.IBody;
import server.htmlBuilder.body.IDivision;
import server.htmlBuilder.body.IHyperlink;
import server.htmlBuilder.body.ISpan;
import server.htmlBuilder.body.LineBreak;
import server.htmlBuilder.body.Span;
import server.htmlBuilder.body.Text;
import server.htmlBuilder.doctype.HTML5Doctype;
import server.htmlBuilder.head.Head;
import server.htmlBuilder.head.ILink;
import server.htmlBuilder.head.IMetaAttr;
import server.htmlBuilder.head.ITitle;
import server.htmlBuilder.head.Link;
import server.htmlBuilder.head.MetaAttr;
import server.htmlBuilder.head.Title;
import server.utils.ConfigReader;
import server.utils.IConfigReader;

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
