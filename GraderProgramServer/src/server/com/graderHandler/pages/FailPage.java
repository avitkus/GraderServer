package server.com.graderHandler.pages;

import server.htmlBuilder.HTMLFile;
import server.htmlBuilder.body.Body;
import server.htmlBuilder.body.Header;
import server.htmlBuilder.body.HorizontalRule;
import server.htmlBuilder.body.IBody;
import server.htmlBuilder.body.IHorizontalRule;
import server.htmlBuilder.body.IParagraph;
import server.htmlBuilder.body.Paragraph;
import server.htmlBuilder.body.Text;
import server.htmlBuilder.doctype.HTML5Doctype;
import server.htmlBuilder.head.Head;
import server.htmlBuilder.head.IHead;
import server.htmlBuilder.head.IMetaAttr;
import server.htmlBuilder.head.ITitle;
import server.htmlBuilder.head.MetaAttr;
import server.htmlBuilder.head.Title;
import server.htmlBuilder.util.IColors;
import server.htmlBuilder.util.IStyleManager;

/**
 *
 * @author Andrew Vitkus
 */
public class FailPage extends HTMLFile implements IFailPage {

    private String name;

    @Override
    public void setAssignmentName(String name) {
        this.name = name;
    }

    @Override
    public String getAssignmentName() {
        return name;
    }

    private void buildParts() {
        setDoctype(new HTML5Doctype());
        setHead(buildHead());
        setBody(buildBody());
    }

    private IHead buildHead() {

        ITitle title = new Title(name + " Grading Error");

        IMetaAttr charset = new MetaAttr();
        charset.addAttribute("charset", "UTF-8");

        IHead head = new Head(title, charset);
        return head;
    }

    private IBody buildBody() {
        IBody body = new Body();
        body.addStyle("width", "90%");
        body.addStyle("margin-left", "auto");
        body.addStyle("margin-right", "auto");
        body.setBGColor("#F8F8F8");

        body.addElement(new Header("Error grading " + name, 1));
        IHorizontalRule headingRule = new HorizontalRule();
        headingRule.addStyle(IStyleManager.BGCOLOR, IColors.DARK_GRAY);
        headingRule.addStyle("height", "2px");
        body.addElement(headingRule);
        IParagraph p = new Paragraph();
        p.addContent(new Text("Grading data unavailable, please contact your professor or a TA."));
        body.addElement(p);
        return body;
    }

    @Override
    public String getHTML() {
        buildParts();
        return super.getHTML();
    }
}
