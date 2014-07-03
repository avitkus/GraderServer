package edu.unc.cs.graderServer.graderHandler.pages;

import edu.unc.cs.graderServer.graderHandler.util.INoteData;
import edu.unc.cs.htmlBuilder.IHTMLFile;

/**
 *
 * @author Andrew
 */
public interface ISuccessPage extends IGraderResponsePage {

    public void setGrading(String[][] grading);

    public String[][] getGrading();

    public void setComments(String[] comments);

    public String[] getComments();

    public void setNotes(INoteData notes);

    public INoteData getNotes();
}
