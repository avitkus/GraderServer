package server.com.webHandler.pages;

import java.util.logging.Logger;
import server.com.webHandler.pages.parts.StudentDataNavBar;
import server.htmlBuilder.HTMLFile;
import server.htmlBuilder.attributes.LinkTarget;
import server.htmlBuilder.body.Body;
import server.htmlBuilder.body.Division;
import server.htmlBuilder.body.Header;
import server.htmlBuilder.body.Hyperlink;
import server.htmlBuilder.body.IBody;
import server.htmlBuilder.body.IDivision;
import server.htmlBuilder.body.IHyperlink;
import server.htmlBuilder.body.IParagraph;
import server.htmlBuilder.body.Paragraph;
import server.htmlBuilder.body.Text;
import server.htmlBuilder.doctype.HTML5Doctype;
import server.htmlBuilder.head.Head;
import server.htmlBuilder.head.ILink;
import server.htmlBuilder.head.IMetaAttr;
import server.htmlBuilder.head.ITitle;
import server.htmlBuilder.head.Link;
import server.htmlBuilder.head.MetaAttr;
import server.htmlBuilder.head.Title;

public class GradingInProgressPage extends HTMLFile implements IGradingInProgressPage {

    private static final Logger LOG = Logger.getLogger(GradingInProgressPage.class.getName());
    private String uuid;

    @Override
    public String getHTML() {
        setDoctype(new HTML5Doctype());
        buildParts();
        return super.getHTML();
    }

    @Override
    public void setPageUUID(String uuid) {
        this.uuid = uuid;
    }

    private void buildBody() {
        IBody body = new Body();

        IDivision bodyWrapper = new Division();
        bodyWrapper.setClass("body-wrapper");

        IDivision title = new Division();
        title.setClass("title");
        title.addContent(new Header("Grading In Progress", 1));
        bodyWrapper.addContent(title);

        IDivision content = new Division();
        content.setClass("content");
        content.addContent(new StudentDataNavBar());
        IDivision note = new Division();
        note.setClassName("center");
        IParagraph refreshNote = new Paragraph();
        refreshNote.addContent(new Text("This page will reload every 5 seconds to check for grading completion. If it does not, click below."));
        note.addContent(refreshNote);
        IHyperlink link = new Hyperlink();
        link.setTarget(LinkTarget.SELF);
        link.setURL("grading.php?id=" + uuid);
        link.addContent(new Text("Refresh"));
        note.addContent(link);
        content.addContent(note);
        bodyWrapper.addContent(content);
        body.addElement(bodyWrapper);

        setBody(body);
    }

    private void buildHead() {
        ITitle title = new Title("Grading in Process");

        IMetaAttr charset = new MetaAttr();
        charset.addAttribute("charset", "UTF-8");

        ILink faviconLink = new Link();
        faviconLink.setRelation("shortcut icon");
        faviconLink.addLinkAttribtue("href", "favicon.ico");
        faviconLink.addLinkAttribtue("type", "image/vnd.microsoft.icon");

        IMetaAttr refresh = new MetaAttr();
        refresh.addMetaAttribute("http-equiv", "refresh");
        refresh.addMetaAttribute("content", "5; url=grading.php?id=" + uuid);
        
        setHead(new Head(title, charset, faviconLink, refresh));
        
        addCSSFile("grader.css");
    }

    private void buildParts() {
        buildHead();
        buildBody();
    }

}
