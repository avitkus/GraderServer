package server.httpTools.request;

import static java.util.Arrays.asList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * Not thread safe
 *
 * @author Andrew Vitkus
 */
public class RequestHeaderFactory {

    public static RequestHeaderFactory getDefault() {
        return new RequestHeaderFactory();
    }

    private final Map<String, String[]> headerMap;

    protected RequestHeaderFactory() {
        headerMap = new LinkedHashMap<>(5);
    }

    public void addHeader(String key, String... value) {
        synchronized (headerMap) {
            headerMap.merge(key, value, (old, add) -> {
                LinkedHashSet<String> vals = new LinkedHashSet<>(2);
                vals.addAll(asList(old));
                vals.addAll(asList(add));
                return vals.toArray(new String[vals.size()]);
            });
        }
    }

    public IRequestHeaders getHeaders() {
        return new HashRequestHeaders() {

            @Override
            protected void init() {
                synchronized (headerMap) {
                    headers.putAll(headerMap);
                }
            }
        };
    }

}
