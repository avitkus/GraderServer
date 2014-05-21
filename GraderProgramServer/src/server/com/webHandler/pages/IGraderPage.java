package server.com.webHandler.pages;

import java.nio.file.Path;
import server.htmlBuilder.IHTMLFile;

/**
 *
 * @author Andrew Vitkus
 */
public interface IGraderPage extends IHTMLFile {
    public void setToGradeFile(Path fileLoc);
    public void setOnyen(String onyen);
    public void setCourse(String course);
    public void setAuth(String auth);
}
