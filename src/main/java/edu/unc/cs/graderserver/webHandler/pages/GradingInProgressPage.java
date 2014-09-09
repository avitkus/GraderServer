package edu.unc.cs.graderServer.webHandler.pages;

import edu.unc.cs.htmlBuilder.HTMLFile;
import edu.unc.cs.htmlBuilder.attributes.LinkTarget;
import edu.unc.cs.htmlBuilder.body.Body;
import edu.unc.cs.htmlBuilder.body.Division;
import edu.unc.cs.htmlBuilder.body.Header;
import edu.unc.cs.htmlBuilder.body.Hyperlink;
import edu.unc.cs.htmlBuilder.body.IBody;
import edu.unc.cs.htmlBuilder.body.IDivision;
import edu.unc.cs.htmlBuilder.body.IHyperlink;
import edu.unc.cs.htmlBuilder.body.IParagraph;
import edu.unc.cs.htmlBuilder.body.Paragraph;
import edu.unc.cs.htmlBuilder.body.Text;
import edu.unc.cs.htmlBuilder.doctype.HTML5Doctype;
import edu.unc.cs.htmlBuilder.head.Head;
import edu.unc.cs.htmlBuilder.head.ILink;
import edu.unc.cs.htmlBuilder.head.IMetaAttr;
import edu.unc.cs.htmlBuilder.head.ITitle;
import edu.unc.cs.htmlBuilder.head.Link;
import edu.unc.cs.htmlBuilder.head.MetaAttr;
import edu.unc.cs.htmlBuilder.head.Title;
import java.util.logging.Logger;
import edu.unc.cs.graderServer.webHandler.pages.parts.StudentDataNavBar;

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
        link.setURL("https://classroom.cs.unc.edu/~vitkus/grader/grading.php?id=" + uuid);
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
        refresh.addMetaAttribute("content", "5; url=https://classroom.cs.unc.edu/~vitkus/grader/grading.php?id=" + uuid);

        setHead(new Head(title, charset, faviconLink, refresh));

        addCSSFile("grader.css");
    }

    private void buildParts() {
        buildHead();
        buildBody();
    }

}
