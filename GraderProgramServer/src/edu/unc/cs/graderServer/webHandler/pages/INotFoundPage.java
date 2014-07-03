package edu.unc.cs.graderServer.webHandler.pages;

import edu.unc.cs.htmlBuilder.IHTMLFile;

/**
 * @author Andrew Vitkus
 *
 */
public interface INotFoundPage extends IHTMLFile {

    public void setPage(String page);

    public String getPage();
}
