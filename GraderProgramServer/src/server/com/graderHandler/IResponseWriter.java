package server.com.graderHandler;

import server.com.gradingProgram.INoteData;

/**
 * @author Andrew Vitkus
 *
 */
public interface IResponseWriter {
	public void setAssignmentName(String name);
	public String getAssignmentName();
	
	public void setGrading(String[][] grading);
	public String[][] getGrading();

	public void setComments(String[] comments);
	public String[] getComments();
	
	public void setNotes(INoteData notes);
	public INoteData getNotes();
	
	public String getResponse();
}
