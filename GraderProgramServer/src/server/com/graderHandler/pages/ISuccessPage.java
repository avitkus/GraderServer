package server.com.graderHandler.pages;

import server.com.gradingProgram.INoteData;
import server.htmlBuilder.IHTMLFile;

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
