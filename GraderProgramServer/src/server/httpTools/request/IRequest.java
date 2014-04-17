package server.httpTools.request;

/**
 *
 * @author Andrew Vitkus
 */
public interface IRequest {
    
    public IRequestHeaders getHeaders();
    public IRequestBody getBody();
    public IRequestInfo getRequestInfo();
    public boolean isMultipart();
}
