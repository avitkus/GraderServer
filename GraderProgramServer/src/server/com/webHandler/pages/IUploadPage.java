package server.com.webHandler.pages;

import server.htmlBuilder.IHTMLFile;

/**
 * @author Andrew Vitkus
 *
 */
public interface IUploadPage extends IHTMLFile {
	public void setUser(String onyen);
	public void setArgs(String args);
	public void setAuth(String auth);
}
