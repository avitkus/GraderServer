package server.com.webHandler.pages;

import server.com.webHandler.pages.helpers.IGradePageManager;
import server.htmlBuilder.IHTMLFile;

/**
 *
 * @author Andrew Vitkus
 */
public interface IGradeViewPage extends IHTMLFile {
    public void setID(String id);
    public void setArgs(String args);
    public void setAuth(String auth);
    public void setManager(IGradePageManager manager);
    public void setManager(IGradePageManager manager, boolean maintainPages);
}
