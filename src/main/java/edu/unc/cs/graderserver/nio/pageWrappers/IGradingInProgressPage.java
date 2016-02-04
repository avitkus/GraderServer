package edu.unc.cs.graderserver.nio.pageWrappers;

import edu.unc.cs.graderserver.webHandler.pages.*;
import edu.unc.cs.htmlBuilder.IHTMLFile;

/**
 *
 * @author Andrew Vitkus
 */
public interface IGradingInProgressPage extends IHTMLFilePage {

    public void setPageUUID(String uuid);
    public void setNumber(int number);
    public int getNumber();
}
