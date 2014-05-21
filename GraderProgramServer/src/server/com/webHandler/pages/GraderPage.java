package server.com.webHandler.pages;

import java.nio.file.Path;
import server.htmlBuilder.HTMLFile;
import server.htmlBuilder.body.Body;
import server.htmlBuilder.body.IBody;
import server.htmlBuilder.head.Head;
import server.htmlBuilder.head.ILink;
import server.htmlBuilder.head.IMetaAttr;
import server.htmlBuilder.head.Link;
import server.htmlBuilder.head.MetaAttr;
import server.htmlBuilder.head.Title;

/**
 * 
 * @author Andrew Vitkus
 */
public class GraderPage extends HTMLFile implements IGraderPage {

    @Override
    public void setToGradeFile(Path fileLoc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setOnyen(String onyen) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setCourse(String course) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setAuth(String auth) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getHTML() {
        buildParts();
        return super.getHTML();
    }
    
    private void buildParts() {
        buildBody();
        buildHead();
    }
    
    private void buildBody() {
        IBody body = new Body();
        
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
