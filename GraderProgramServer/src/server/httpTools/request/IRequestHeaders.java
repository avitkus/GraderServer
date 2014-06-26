package server.httpTools.request;

import java.util.Map;

/**
 *
 * @author Andrew Vitkus
 */
public interface IRequestHeaders {

    public Map<String, String[]> getHeaders();

    public String getHeader();
}
