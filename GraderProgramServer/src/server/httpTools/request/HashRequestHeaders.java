package server.httpTools.request;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Andrew Vitkus
 */
public abstract class HashRequestHeaders implements IRequestHeaders {
    protected final LinkedHashMap<String, String[]> headers;
    private String headerText;
    
    protected HashRequestHeaders() {
        headers = new LinkedHashMap<>(5);
        headerText = "";
        init();
    }
    
    protected abstract void init();
    
    @Override
    public Map<String, String[]> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }
    
    public String getHeader() {
        if (headerText.isEmpty()) {
            StringBuilder header = new StringBuilder(100);
            // Lambdas are wonderful things
            headers.forEach((key, value) -> header.append(key).append(": ").append(value).append("\r\n"));
            header.append("\r\n");
            headerText = header.toString();
        }
        return headerText;
    }
}
