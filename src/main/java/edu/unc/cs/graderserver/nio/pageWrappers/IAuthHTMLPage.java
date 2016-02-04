package edu.unc.cs.graderserver.nio.pageWrappers;

import edu.unc.cs.graderserver.webHandler.pages.*;
import edu.unc.cs.htmlBuilder.IHTMLFile;

/**
 * @author Andrew Vitkus
 *
 */
public interface IAuthHTMLPage extends IHTMLFilePage {

    public void setArgs(String args);

    public String checkAuth();

    public void setFailed(boolean failed);
}
