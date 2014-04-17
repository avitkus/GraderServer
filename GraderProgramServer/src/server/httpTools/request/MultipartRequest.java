package server.httpTools.request;

/**
 *
 * @author Andrew Vitkus
 */
public class MultipartRequest implements IRequest {

    public static MultipartRequest getInstance(IRequestInfo info, IRequestHeaders headers, IRequestBody body) {
        return new MultipartRequest(info, headers, body);
    }
    private final IRequestHeaders headers;
    private final IRequestBody body;
    private final IRequestInfo info;
    
    protected MultipartRequest(IRequestInfo info, IRequestHeaders headers, IRequestBody body) {
        this.headers = headers;
        this.body = body;
        this.info = info;
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
    public IRequestInfo getRequestInfo() {
        return info;
    }

    @Override
    public boolean isMultipart() {
        return true;
    }
    
}
