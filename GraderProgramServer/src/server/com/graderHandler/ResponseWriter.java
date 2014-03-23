package server.com.graderHandler;

import server.com.graderHandler.pages.FailPage;
import server.com.graderHandler.pages.IGraderResponsePage;


public class ResponseWriter implements IResponseWriter {

    protected IGraderResponsePage response;
    
    protected ResponseWriter() {
        response = new FailPage();
    }

    @Override
    public String getResponse() {
        return response.getHTML();
    }

    @Override
    public void setAssignmentName(String name) {
        response.setAssignmentName(name);
    }

}
