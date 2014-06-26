package server.com.graderHandler.pages;

import java.util.Arrays;
import server.com.graderHandler.util.INoteData;
import server.com.webHandler.pages.parts.StudentDataNavBar;
import server.htmlBuilder.HTMLFile;
import server.htmlBuilder.attributes.LinkTarget;
import server.htmlBuilder.body.Body;
import server.htmlBuilder.body.Division;
import server.htmlBuilder.body.Header;
import server.htmlBuilder.body.HorizontalRule;
import server.htmlBuilder.body.Hyperlink;
import server.htmlBuilder.body.IBody;
import server.htmlBuilder.body.IDivision;
import server.htmlBuilder.body.IHorizontalRule;
import server.htmlBuilder.body.IHyperlink;
import server.htmlBuilder.body.IParagraph;
import server.htmlBuilder.body.Paragraph;
import server.htmlBuilder.body.Text;
import server.htmlBuilder.doctype.HTML5Doctype;
import server.htmlBuilder.head.Head;
import server.htmlBuilder.head.IHead;
import server.htmlBuilder.head.ILink;
import server.htmlBuilder.head.IMetaAttr;
import server.htmlBuilder.head.ITitle;
import server.htmlBuilder.head.Link;
import server.htmlBuilder.head.MetaAttr;
import server.htmlBuilder.head.Title;
import server.htmlBuilder.list.IList;
import server.htmlBuilder.list.IUnorderedList;
import server.htmlBuilder.list.ListItem;
import server.htmlBuilder.list.UnorderedList;
import server.htmlBuilder.table.ITable;
import server.htmlBuilder.table.ITableHeader;
import server.htmlBuilder.table.ITableRow;
import server.htmlBuilder.table.Table;
import server.htmlBuilder.table.TableData;
import server.htmlBuilder.table.TableHeader;
import server.htmlBuilder.table.TableRow;
import server.htmlBuilder.util.IBorderStyles;
import server.htmlBuilder.util.IColors;
import server.htmlBuilder.util.IStyleManager;

/**
 *
 * @author Andrew
 */
public class StylizedSuccessPage extends HTMLFile implements ISuccessPage {

    private String[] comments;
    private String[][] grading;
    private String name;
    private INoteData notes;

    @Override
    public String getAssignmentName() {
        return name;
    }

    @Override
    public void setAssignmentName(String name) {
        this.name = name;
    }

    @Override
    public String[] getComments() {
        return Arrays.copyOf(comments, comments.length);
    }

    @Override
    public void setComments(String[] comments) {
        this.comments = Arrays.copyOf(comments, comments.length);
    }

    @Override
    public String[][] getGrading() {
        return Arrays.copyOf(grading, grading.length);
    }

    @Override
    public void setGrading(String[][] grading) {
        this.grading = Arrays.copyOf(grading, grading.length);
    }

    @Override
    public String getHTML() {
        buildParts();
        return super.getHTML();
    }

    @Override
    public INoteData getNotes() {
        return notes;
    }

    @Override
    public void setNotes(INoteData notes) {
        this.notes = notes;
    }

    private void buildBody() {
        IBody body = new Body();IDivision bodyWrapper = new Division();
        bodyWrapper.setClass("body-wrapper");

        IDivision title = new Division();
        title.setClass("title");
        title.addContent(new Header("Grading response for " + name, 1));
        bodyWrapper.addContent(title);

        IDivision content = new Division();
        content.setClass("content");
        content.addContent(new StudentDataNavBar());
        content.addContent(new Header("Grading:", 2));
        content.addContent(buildGradeTable());

        if (notes != null && !notes.isEmpty()) {
            content.addContent(new Header("Notes:", 2));
            content.addContent(buildNoteList());
        }

        if (comments != null && comments.length != 0) {
            content.addContent(new Header("Comments:", 2));
            content.addContent(buildCommentList());
        }
        bodyWrapper.addContent(content);
        body.addElement(bodyWrapper);
        setBody(body);
    }

    private IList buildCommentList() {
        IUnorderedList list = new UnorderedList();
        
        for (String comment : comments) {
            list.addListItem(new ListItem(new Text(comment)));
        }
        
        return list;
    }

    private ITable buildGradeTable() {
        ITable table = new Table();

        ITableRow headerRow = new TableRow();

        ITableHeader requirementsHeader = new TableHeader(new Text("Requirement"));
        //requirementsHeader.addStyle("width", "55%");

        ITableHeader autoGradeHeader = new TableHeader(new Text("%&nbsp;Autograded"));
        //autoGradeHeader.addStyle("width", "15%");

        ITableHeader pointsHeader = new TableHeader(new Text("Points"));
        //pointsHeader.addStyle("width", "15%");

        ITableHeader possibleHeader = new TableHeader(new Text("Possible"));
        //possibleHeader.addStyle("width", "15%");

        headerRow.addDataPart(requirementsHeader);
        headerRow.addDataPart(autoGradeHeader);
        headerRow.addDataPart(pointsHeader);
        headerRow.addDataPart(possibleHeader);
        table.addRow(headerRow);

        for (int i = 0; i < grading.length; i++) {
            String[] requirement = grading[i];
            ITableRow row = new TableRow();
            if (i % 2 == 1) {
                row.setClassName("highlight-row");
            }
            for (String part : requirement) {
                row.addDataPart(new TableData(new Text(part)));
            }
            table.addRow(row);
        }

        return table;
    }

    private void buildHead() {
        Title title = new Title(name + " Grading");

        IMetaAttr charset = new MetaAttr();
        charset.addAttribute("charset", "UTF-8");

        ILink faviconLink = new Link();
        faviconLink.setRelation("shortcut icon");
        faviconLink.addLinkAttribtue("href", "favicon.ico");
        faviconLink.addLinkAttribtue("type", "image/vnd.microsoft.icon");

        setHead(new Head(title, charset, faviconLink));

        addCSSFile("grader.css");
    }

    private IList buildNoteList() {
        IUnorderedList list = new UnorderedList();

        for (String section : notes.getSections()) {
            list.addListItem(new ListItem(new Text(section)));
            IUnorderedList partList = new UnorderedList();

            for (String part : notes.getPartsForSection(section)) {
                partList.addListItem(new ListItem(new Text(part)));
                IUnorderedList noteList = new UnorderedList();

                for (String note : notes.getNotesForPart(section, part)) {
                    noteList.addListItem(new ListItem(new Text(note)));

                }
                partList.addListItem(noteList);
            }
            list.addListItem(partList);
        }

        return list;
    }

    private void buildParts() {
        setDoctype(new HTML5Doctype());
        buildHead();
        buildBody();
    }
}
