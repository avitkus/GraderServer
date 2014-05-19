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

    @Override
    public String getHeader() {
        if (headerText.isEmpty()) {
            StringBuilder header = new StringBuilder(100);
            headers.forEach((key, values) -> {
                header.append(key).append(": ");
                for (int i = 0; i < values.length - 1; i++) {
                    header.append(values[i]).append("; ");
                }
                header.append(values[values.length - 1]).append("\r\n");
            });
            headerText = header.toString();
        }
        return headerText;
    }
}
