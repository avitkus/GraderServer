package server.com.webHandler.pages;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.htmlBuilder.HTMLFile;
import server.htmlBuilder.body.Body;
import server.htmlBuilder.body.Header;
import server.htmlBuilder.body.IBody;
import server.htmlBuilder.body.Text;
import server.htmlBuilder.doctype.HTML5Doctype;
import server.htmlBuilder.head.Head;
import server.htmlBuilder.head.ILink;
import server.htmlBuilder.head.IMetaAttr;
import server.htmlBuilder.head.Link;
import server.htmlBuilder.head.MetaAttr;
import server.htmlBuilder.head.Title;
import server.htmlBuilder.table.ITable;
import server.htmlBuilder.table.ITableData;
import server.htmlBuilder.table.ITableRow;
import server.htmlBuilder.table.Table;
import server.htmlBuilder.table.TableData;
import server.htmlBuilder.table.TableRow;

/**
 * 
 * @author Andrew Vitkus
 */
public class GraderPage extends HTMLFile implements IGraderPage {
    private Path fileLoc;
    private String onyen;
    private String course;
    private String auth;

    @Override
    public void setToGradeFile(Path fileLoc) {
        this.fileLoc = fileLoc;
    }

    @Override
    public void setOnyen(String onyen) {
        this.onyen = onyen;
    }

    @Override
    public void setCourse(String course) {
        this.course = course;
    }

    @Override
    public void setAuth(String auth) {
        this.auth = auth;
    }

    @Override
    public String getHTML() {
        setDoctype(new HTML5Doctype());
        buildParts();
        return super.getHTML();
    }
    
    private void buildParts() {
        buildBody();
        buildHead();
    }
    
    private void buildBody() {
        IBody body = new Body();
        
        body.addElement(new Header(new Text("File recieved"), 2));
        StringBuilder fileContentsBin = new StringBuilder(100);
        StringBuilder fileContentsChar = new StringBuilder(100);
        try (BufferedReader br = new BufferedReader(new FileReader(fileLoc.toFile()))) {
            br.lines().forEach((line) -> fileContentsChar.append(line).append("\n"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GraderPage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GraderPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        try (BufferedReader br = new BufferedReader(new FileReader(fileLoc.toFile()))) {
            int i = -1;
            while((i = br.read()) != -1) {
                fileContentsBin.append(i);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GraderPage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GraderPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        ITable fileTable = new Table();
        ITableRow row = new TableRow();
        ITableData bin = new TableData(new Text(fileContentsBin.toString()));
        ITableData chars = new TableData(new Text(fileContentsChar.toString()));
        row.addDataPart(bin);
        row.addDataPart(chars);
        row.addStyle("width", "100%");
        fileTable.addRow(row);
        body.addElement(fileTable);
        setBody(body);
    }
    
    private void buildHead() {
        Title title = new Title("Grader temp page");

        IMetaAttr charset = new MetaAttr();
        charset.addAttribute("charset", "UTF-8");

        ILink faviconLink = new Link();
        faviconLink.setRelation("shortcut icon");
        faviconLink.addLinkAttribtue("href", "favicon.ico");
        faviconLink.addLinkAttribtue("type", "image/vnd.microsoft.icon");
        
        setHead(new Head(title, charset, faviconLink));

        addCSSFile("grader.css");
    }
    
}
