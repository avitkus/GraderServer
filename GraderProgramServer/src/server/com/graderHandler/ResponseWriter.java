package server.com.graderHandler;

import server.com.graderHandler.pages.FailPage;
import server.com.graderHandler.pages.IGraderResponsePage;
import server.htmlBuilder.IHTMLFile;

public class ResponseWriter implements IResponseWriter {

    protected IGraderResponsePage response;

    protected ResponseWriter() {
        response = new FailPage();
    }

    @Override
    public IHTMLFile getResponse() {
        return response;
    }

    @Override
    public String getResponseText() {
        return response.getHTML();
    }

    @Override
    public void setAssignmentName(String name) {
        response.setAssignmentName(name);
    }
}
