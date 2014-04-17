package server.httpTools.response;

import server.httpTools.util.HTTPMethod;
import server.httpTools.util.HTTPStatusCode;
import server.httpTools.util.HTTPVersion;

/**
 *
 * @author Andrew Vitkus
 */
public interface IStatus {
    public HTTPVersion getProtocol();
    public HTTPStatusCode getStatus();
}
