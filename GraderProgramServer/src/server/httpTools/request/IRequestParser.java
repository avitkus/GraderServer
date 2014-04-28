package server.httpTools.request;

import server.httpTools.request.exceptions.MalformedRequestException;

/**
 *
 * @author Andrew Vitkus
 */
public interface IRequestParser {
    public IRequest parse(String request) throws MalformedRequestException;
}
