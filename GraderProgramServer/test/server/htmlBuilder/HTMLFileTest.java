package server.htmlBuilder;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

import server.htmlBuilder.HTMLFile;
import server.htmlBuilder.HTMLFile;
import server.htmlBuilder.IHTMLFile;
import server.htmlBuilder.IHTMLFile;
import server.htmlBuilder.attributes.TextStyle;
import server.htmlBuilder.body.Body;
import server.htmlBuilder.body.Header;
import server.htmlBuilder.body.HorizontalRule;
import server.htmlBuilder.body.IBody;
import server.htmlBuilder.body.IParagraph;
import server.htmlBuilder.body.IText;
import server.htmlBuilder.body.Paragraph;
import server.htmlBuilder.body.Text;
import server.htmlBuilder.doctype.HTML5Doctype;
import server.htmlBuilder.head.Head;
import server.htmlBuilder.head.IHead;
import server.htmlBuilder.head.IMetaAttr;
import server.htmlBuilder.head.ITitle;
import server.htmlBuilder.head.MetaAttr;
import server.htmlBuilder.head.Title;
import server.htmlBuilder.list.IOrderedList;
import server.htmlBuilder.list.IUnorderedList;
import server.htmlBuilder.list.ListItem;
import server.htmlBuilder.list.OrderedList;
import server.htmlBuilder.list.UnorderedList;
import server.htmlBuilder.table.ITable;
import server.htmlBuilder.table.ITableRow;
import server.htmlBuilder.table.Table;
import server.htmlBuilder.table.TableData;
import server.htmlBuilder.table.TableHeader;
import server.htmlBuilder.table.TableRow;
import server.htmlBuilder.util.IColors;

/**
 * @author Andrew Vitkus
 *
 */
public class HTMLFileTest {

    IHTMLFile html;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        html = new HTMLFile();

        IHead head = new Head();

        ITitle title = new Title("Title");

        IMetaAttr charset = new MetaAttr();
        charset.addAttribute("charset", "UTF-8");

        head.addElement(title);
        head.addElement(charset);

        IBody body = new Body();
        body.setBGColor(IColors.YELLOW);
        Header heading = new Header("&<Heading!>...", 1);
        heading.setColor(IColors.RED);
        body.addElement(heading);
        IText text = new Text("Text!", TextStyle.BOLD, TextStyle.ITALIC);
        text.appendText(" ");
        text.appendText("I am adding a rather long string now to see what will happen and stuff.", TextStyle.SUBSCRIPT);
        IParagraph p = new Paragraph(text);
        p.insertLineBreak();
        Text yay = new Text("HUZZAH!");
        yay.setColor(IColors.GREEN);
        p.addContent(yay);
        p.setColor(IColors.BLUE);
        body.addElement(p);

        body.addElement(new Text("Short text this time."));

        body.addElement(new HorizontalRule());

        ITable table = new Table();
        ITableRow headerRow = new TableRow(new TableHeader(new Text("Heading 1")), new TableHeader(new Text("Heading 2")));
        headerRow.setBGColor(IColors.BLACK);
        headerRow.setColor(IColors.WHITE);
        ITableRow row1 = new TableRow(new TableData(new Text("cell 1.1")), new TableData(new Text("cell 1.2")));
        row1.setBGColor(IColors.SILVER);
        ITableRow row2 = new TableRow(new TableData(new Text("cell 2.1")), new TableData(new Text("cell 2.2")));
        row2.setBGColor(IColors.DARK_GRAY);
        table.addRow(headerRow);
        table.addRow(row1);
        table.addRow(row2);
        body.addElement(table);

        IOrderedList ol = new OrderedList(new ListItem(new Text("This is one")), new ListItem(new Text("This is two")), new ListItem(new Text("This is before four")));
        IUnorderedList ul = new UnorderedList(new ListItem(new Text("This is a point")), new ListItem(new Text("This is also")), new ListItem(new Text("Gasp! Even this is!")));

        body.addElement(ol);
        body.addElement(ul);

        html.setDoctype(new HTML5Doctype());
        html.setHead(head);
        html.setBody(body);
    }

    @Test
    public void test() {
        String expectedHtml = "<!DOCTYPE html>\n"
                + "<html>\n"
                + "  <head>\n"
                + "    <title>Title</title>\n"
                + "    <meta charset=\"UTF-8\">\n"
                + "  </head>\n"
                + "  <body style=\"background-color:Yellow;\">\n"
                + "    <h1 style=\"color:Red;\">&amp;&lt;Heading!&gt;&hellip;</h1>\n"
                + "    <p style=\"color:Blue;\">\n"
                + "      <b><i>Text!</i></b> <sub>I am adding a rather long string now to see what will happen and stuff.</sub>\n"
                + "      <br>\n"
                + "      <span style=\"color:Green;\">HUZZAH!</span>\n"
                + "    </p>\n"
                + "    Short text this time.\n"
                + "    <hr>\n"
                + "    <table>\n"
                + "      <tr style=\"background-color:Black;color:White;\">\n"
                + "        <th>\n"
                + "          Heading 1\n"
                + "        </th>\n"
                + "        <th>\n"
                + "          Heading 2\n"
                + "        </th>\n"
                + "      </tr>\n"
                + "      <tr style=\"background-color:Silver;\">\n"
                + "        <td>\n"
                + "          cell 1.1\n"
                + "        </td>\n"
                + "        <td>\n"
                + "          cell 1.2\n"
                + "        </td>\n"
                + "      </tr>\n"
                + "      <tr style=\"background-color:DarkGray;\">\n"
                + "        <td>\n"
                + "          cell 2.1\n"
                + "        </td>\n"
                + "        <td>\n"
                + "          cell 2.2\n"
                + "        </td>\n"
                + "      </tr>\n"
                + "    </table>\n"
                + "    <ol>\n"
                + "      <li>\n"
                + "        This is one\n"
                + "      </li>\n"
                + "      <li>\n"
                + "        This is two\n"
                + "      </li>\n"
                + "      <li>\n"
                + "        This is before four\n"
                + "      </li>\n"
                + "    </ol>\n"
                + "    <ul>\n"
                + "      <li>\n"
                + "        This is a point\n"
                + "      </li>\n"
                + "      <li>\n"
                + "        This is also\n"
                + "      </li>\n"
                + "      <li>\n"
                + "        Gasp! Even this is!\n"
                + "      </li>\n"
                + "    </ul>\n"
                + "  </body>\n"
                + "</html>";
        String resultHtml = html.getHTML();
        assertEquals(expectedHtml, resultHtml);
    }

}
