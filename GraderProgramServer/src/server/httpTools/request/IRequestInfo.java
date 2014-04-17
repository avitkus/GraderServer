package server.httpTools.request;

import server.httpTools.util.HTTPMethod;
import server.httpTools.util.HTTPVersion;

/**
 *
 * @author Andrew Vitkus
 */
public interface IRequestInfo {
    public HTTPVersion getProtocol();
    public HTTPMethod getMethod();
    public String getResource();
}
