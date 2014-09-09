package edu.unc.cs.graderServer.webHandler.pages;

import edu.unc.cs.htmlBuilder.IHTMLFile;

/**
 *
 * @author Andrew Vitkus
 */
public interface IGradingResultPage extends IHTMLFile {

    public void setArgs(String args);

    public void setIP(String ip);

    public void setID(String id);

    public boolean isAllowed();

    public boolean exists();
}
