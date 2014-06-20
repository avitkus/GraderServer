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
        init();
        boundary = boundary.trim();
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
                body.append("\r\n--").append(boundary).append("\r\n");
                String dispostion = content.getDispositionType();
                if (!dispostion.isEmpty()) {
                    body.append("Content-Disposition: ").append(content.getDispositionType());
                    content.getDisposition().forEach((key, value) -> body.append("; ").append(key).append("=\"").append(value).append("\""));
                    body.append("\r\n");
                }
                
                String type = content.getType();
                if (!type.isEmpty()) {
                    body.append("Content-Type: ").append(content.getType());
                }

                body.append(content.getContent());
            }
            body.append("--").append(boundary).append("--");
            bodyText = body.toString();
        }
        return bodyText;
    }
    
}
