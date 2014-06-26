package server.com.webHandler.pages;

import server.htmlBuilder.IHTMLFile;

/**
 * @author Andrew Vitkus
 *
 */
public interface INotFoundPage extends IHTMLFile {

    public void setPage(String page);

    public String getPage();
}
