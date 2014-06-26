package server.httpTools.request;

/**
 *
 * @author Andrew Vitkus
 */
public interface IRequest {

    public IRequestLine getRequestInfo();

    public IRequestHeaders getHeaders();

    public IRequestBody getBody();

    public boolean isMultipart();

    public String getRequest();
}
