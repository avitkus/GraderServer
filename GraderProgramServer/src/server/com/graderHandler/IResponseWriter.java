package server.com.graderHandler;

import server.htmlBuilder.IHTMLFile;

/**
 * @author Andrew Vitkus
 *
 */
public interface IResponseWriter {

    public String getResponseText();
    public IHTMLFile getResponse();

    public void setAssignmentName(String name);
}
