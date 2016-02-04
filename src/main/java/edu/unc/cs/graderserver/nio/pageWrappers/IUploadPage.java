package edu.unc.cs.graderserver.nio.pageWrappers;

import edu.unc.cs.graderserver.webHandler.pages.*;
import edu.unc.cs.htmlBuilder.IHTMLFile;

/**
 * @author Andrew Vitkus
 *
 */
public interface IUploadPage extends IHTMLFilePage {

    public void setUser(String onyen);

    public void setArgs(String args);

    public void setAuth(String auth);
}
