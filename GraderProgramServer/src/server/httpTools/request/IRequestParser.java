package server.httpTools.request;

/**
 *
 * @author Andrew Vitkus
 */
public interface IRequestParser {
    public IRequest parse(String request);
}
