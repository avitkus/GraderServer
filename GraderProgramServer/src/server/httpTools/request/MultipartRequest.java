package server.httpTools.request;

/**
 *
 * @author Andrew Vitkus
 */
public class MultipartRequest implements IRequest {

    public static MultipartRequest getInstance(IRequestLine info, IRequestHeaders headers, IRequestBody body) {
        return new MultipartRequest(info, headers, body);
    }
    private final IRequestHeaders headers;
    private final IRequestBody body;
    private final IRequestLine requestLine;
    
    protected MultipartRequest(IRequestLine requestLine, IRequestHeaders headers, IRequestBody body) {
        this.headers = headers;
        this.body = body;
        this.requestLine = requestLine;
    }
    
    @Override
    public IRequestHeaders getHeaders() {
        return headers;
    }

    @Override
    public IRequestBody getBody() {
        return body;
    }
    
    @Override
    public IRequestLine getRequestInfo() {
        return requestLine;
    }

    @Override
    public boolean isMultipart() {
        return true;
    }

    @Override
    public String getRequest() {
        StringBuilder request = new StringBuilder(200);
        
        request.append(requestLine.getRequestLine());
        request.append(headers.getHeader());
        request.append(body.getBody());
        
        return request.toString();
    }
    
}
