package server.httpTools.request;

import server.httpTools.util.HTTPMethod;
import server.httpTools.util.HTTPVersion;

/**
 *
 * @author Andrew Vitkus
 */
public interface IRequestLine {
    public HTTPVersion getProtocol();
    public HTTPMethod getMethod();
    public String getResource();
    
    public String getRequestLine();
}
