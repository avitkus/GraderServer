package server.httpTools.request;

import java.util.ArrayList;

/**
 * 
 * @author Andrew Vitkus
 */
public abstract class MultipartRequestBody implements IRequestBody {
    protected final ArrayList<MultipartContent> contents;
    protected String boundary;
    private String bodyText;
    
    protected MultipartRequestBody() {
        contents = new ArrayList<>(2);
        boundary = "";
        bodyText = "";
    }
    
    protected abstract void init();
        
    @Override
    public String getBody() {
        return computeBody();
    }
    
    public MultipartContent[] getContents() {
        return contents.toArray(new MultipartContent[contents.size()]);
    }
    
    private String computeBody() {
        if (bodyText.isEmpty()) {
            StringBuilder body = new StringBuilder(100);
            for(MultipartContent content : contents) {
                body.append("--").append(boundary);
                body.append("\r\nContent-Disposition: ").append(content.getDispositionType());

                content.getDisposition().forEach((key, value) -> body.append("; ").append(key).append("=\"").append(value).append("\""));

                body.append("\r\nContent-Type: ").append(content.getType()).append("\r\n\r\n");

                body.append(content.getContent()).append("\r\n");
            }
            body.append("--").append(boundary).append("--");
            bodyText = body.toString();
        }
        return bodyText;
    }
    
}
