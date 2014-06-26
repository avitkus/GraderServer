package server.httpTools.request;

/**
 *
 * @author Andrew Vitkus
 */
public interface IContent {

    public IRequestHeaders getHeaders();

    public int getLength();

    public String getType();

    public IRequestBody getBody();
}
