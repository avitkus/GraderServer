package edu.unc.cs.graderServer.graderHandler.pages;

import edu.unc.cs.htmlBuilder.IHTMLFile;

/**
 *
 * @author Andrew Vitkus
 */
public interface IGraderResponsePage extends IHTMLFile {

    public void setAssignmentName(String name);

    public String getAssignmentName();
}
