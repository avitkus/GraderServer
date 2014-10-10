package edu.unc.cs.graderServer.webHandler.pages;

import edu.unc.cs.htmlBuilder.IHTMLFile;

/**
 *
 * @author Andrew Vitkus
 */
public interface IGradingInProgressPage extends IHTMLFile {

    public void setPageUUID(String uuid);
    public void setNumber(int number);
    public int getNumber();
}
