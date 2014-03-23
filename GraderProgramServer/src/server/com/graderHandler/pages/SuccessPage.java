package server.com.graderHandler.pages;

import server.com.gradingProgram.INoteData;
import server.htmlBuilder.HTMLFile;
import server.htmlBuilder.body.Body;
import server.htmlBuilder.body.Header;
import server.htmlBuilder.body.HorizontalRule;
import server.htmlBuilder.body.IBody;
import server.htmlBuilder.body.IHorizontalRule;
import server.htmlBuilder.body.Text;
import server.htmlBuilder.doctype.HTML5Doctype;
import server.htmlBuilder.head.Head;
import server.htmlBuilder.head.IHead;
import server.htmlBuilder.head.IMetaAttr;
import server.htmlBuilder.head.ITitle;
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
public class SuccessPage extends HTMLFile implements ISuccessPage {

    private String name;
    private String[][] grading;
    private INoteData notes;
    private String[] comments;

    @Override
    public void setAssignmentName(String name) {
        this.name = name;
    }

    @Override
    public String getAssignmentName() {
        return name;
    }

    @Override
    public void setGrading(String[][] grading) {
        this.grading = grading;
    }

    @Override
    public String[][] getGrading() {
        return grading;
    }

    @Override
    public void setNotes(INoteData notes) {
        this.notes = notes;
    }

    @Override
    public INoteData getNotes() {
        return notes;
    }

    @Override
    public void setComments(String[] comments) {
        this.comments = comments;
    }

    @Override
    public String[] getComments() {
        return comments;
    }

    private void buildParts() {
        setDoctype(new HTML5Doctype());
        setHead(buildHead());
        setBody(buildBody());
    }

    private IHead buildHead() {

        ITitle title = new Title(name + " Grading");

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

        body.addElement(new Header("Grading response for " + name, 1));
        IHorizontalRule headingRule = new HorizontalRule();
        headingRule.addStyle(IStyleManager.BGCOLOR, IColors.DARK_GRAY);
        headingRule.addStyle("height", "2px");
        body.addElement(headingRule);

        body.addElement(new Header("Grading:", 2));
        body.addElement(buildGradeTable());

        if (notes != null || !notes.isEmpty()) {
            body.addElement(new Header("Notes:", 2));
            body.addElement(buildNoteList());
        }

        if (comments != null || comments.length == 0) {
            body.addElement(new Header("Comments:", 2));
            body.addElement(buildCommentList());
        }
        return body;
    }

    private ITable buildGradeTable() {
        ITable table = new Table();

        table.setBorder(2, IBorderStyles.SOLID);
        table.addStyle("width", "90%");

        ITableRow headerRow = new TableRow();
        headerRow.setBGColor("#B0B0B0");

        ITableHeader requirementsHeader = new TableHeader(new Text("Requirement"));
        requirementsHeader.addStyle("width", "55%");

        ITableHeader autoGradeHeader = new TableHeader(new Text("%&nbsp;Autograded"));
        autoGradeHeader.addStyle("width", "15%");

        ITableHeader pointsHeader = new TableHeader(new Text("Points"));
        pointsHeader.addStyle("width", "15%");

        ITableHeader possibleHeader = new TableHeader(new Text("Possible"));
        possibleHeader.addStyle("width", "15%");

        headerRow.addDataPart(requirementsHeader);
        headerRow.addDataPart(autoGradeHeader);
        headerRow.addDataPart(pointsHeader);
        headerRow.addDataPart(possibleHeader);
        table.addRow(headerRow);

        for (int i = 0; i < grading.length; i++) {
            String[] requirement = grading[i];
            ITableRow row = new TableRow();
            if (i % 2 == 1) {
                row.setBGColor(IColors.LIGHT_GRAY);
            }
            for (String part : requirement) {
                row.addDataPart(new TableData(new Text(part)));
            }
            table.addRow(row);
        }

        return table;
    }

    private IList buildCommentList() {
        IUnorderedList list = new UnorderedList();

        for (String comment : comments) {
            list.addListItem(new ListItem(new Text(comment)));
        }

        return list;
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
    
    @Override
    public String getHTML() {
        buildParts();
        return super.getHTML();
    }
}
