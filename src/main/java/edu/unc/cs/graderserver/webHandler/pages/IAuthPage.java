package edu.unc.cs.graderServer.webHandler.pages;

import edu.unc.cs.htmlBuilder.IHTMLFile;

/**
 * @author Andrew Vitkus
 *
 */
public interface IAuthPage extends IHTMLFile {

    public void setArgs(String args);

    public String checkAuth();

    public void setFailed(boolean failed);
}
