package edu.unc.cs.graderServer.webHandler.pages;

import edu.unc.cs.htmlBuilder.IHTMLFile;

/**
 * @author Andrew Vitkus
 *
 */
public interface IStudentDataStatisticsPage extends IHTMLFile {

    public void setOnyen(String onyen);

    public void setUser(String onyen);

    public void setCourse(String name);

    public void setSection(String section);

    public void setYear(String year);

    public void setSeason(String season);

    public void setAssignment(String assignment);

    public void setType(String type);

    public void setArgs(String args);

    public void setAuth(String auth);
}
