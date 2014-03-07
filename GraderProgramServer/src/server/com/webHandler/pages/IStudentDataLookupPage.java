package server.com.webHandler.pages;

import server.htmlBuilder.IHTMLFile;

/**
 * @author Andrew Vitkus
 *
 */
public interface IStudentDataLookupPage extends IHTMLFile {
	public void setStudent(String onyen);
	public void setCourse(String name);
	public void setSection(String section);
	public void setYear(String year);
	public void setSeason(String season);
	public void setAssignment(String assignment);
	public void setType(String type);
	public void setView(String view);
	public void setArgs(String args);
}
