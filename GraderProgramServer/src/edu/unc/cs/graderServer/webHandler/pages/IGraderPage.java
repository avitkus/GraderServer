package edu.unc.cs.graderServer.webHandler.pages;

import java.nio.file.Path;
import edu.unc.cs.htmlBuilder.IHTMLFile;

/**
 *
 * @author Andrew Vitkus
 */
public interface IGraderPage extends IHTMLFile {

    public void setToGradeFile(Path fileLoc);

    public void setOnyen(String onyen);

    public void setUID(String uid);

    public void setPID(String pid);

    public void setFirstName(String firstName);

    public void setLastName(String lastName);

    public void setName(String first, String last);

    public void setCourse(String course);

    public void setAuth(String auth);

    public void setAssignment(String assignment);

    public void setIP(String ip);

    public void setPageUUID(String id);
    
    public String getPageUUID();
}
