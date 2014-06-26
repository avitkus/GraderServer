package server.com.graderHandler.pages;

import server.htmlBuilder.IHTMLFile;

/**
 *
 * @author Andrew Vitkus
 */
public interface IGraderResponsePage extends IHTMLFile {

    public void setAssignmentName(String name);

    public String getAssignmentName();
}
