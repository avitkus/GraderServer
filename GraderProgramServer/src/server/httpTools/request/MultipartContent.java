package server.httpTools.request;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Andrew Vitkus
 */
public abstract class MultipartContent {
    protected final HashMap<String, String> disposition;
    protected String dispositionType;
    protected String type;
    protected String contents;

    protected MultipartContent() {
        disposition = new HashMap<>(3);
        init();
    }
    
    protected abstract void init();

    public String getType() {
        return type;
    }

    public String getContent() {
        return contents;
    }
    
    public String getDispositionType() {
        return dispositionType;
    }

    public String getDisposition(String key) {
        return disposition.get(key);
    }
    
    public Map<String, String> getDisposition() {
        return disposition;
    }
}
