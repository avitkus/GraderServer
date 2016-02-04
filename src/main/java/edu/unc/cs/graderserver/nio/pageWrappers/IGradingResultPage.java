package edu.unc.cs.graderserver.nio.pageWrappers;

import edu.unc.cs.graderserver.webHandler.pages.*;
import edu.unc.cs.htmlBuilder.IHTMLFile;

/**
 *
 * @author Andrew Vitkus
 */
public interface IGradingResultPage extends IHTMLFilePage {

    public void setArgs(String args);

    public void setIP(String ip);

    public void setID(String id);

    public boolean isAllowed();

    public boolean exists();
}
