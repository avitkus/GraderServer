package server.com.webHandler.pages.helpers;

import server.com.webHandler.pages.GraderPage;
import server.com.webHandler.pages.IGraderPage;
import server.httpTools.request.IRequest;
import server.httpTools.request.MultipartContent;
import server.httpTools.request.MultipartRequestBody;

/**
 *
 * @author Andrew Vitkus
 */
public class GraderPageSetup {
    public IGraderPage buildGraderPage(IRequest request) {
        IGraderPage page = new GraderPage();
        MultipartRequestBody body = (MultipartRequestBody)request.getBody();
        MultipartContent[] contents = body.getContents();
        System.out.println(request.getRequest());
        //contents[0].
        return page;
    }
}
