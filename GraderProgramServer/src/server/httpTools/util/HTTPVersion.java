package server.httpTools.util;

/**
 *
 * @author Andrew Vitkus
 */
public enum HTTPVersion {
    HTTP10 ("HTTP/1.0"),
    HTTP11 ("HTTP/1.1"),
    HTTP12 ("HTTP/1.2");
    
    private final String name;
    
    private HTTPVersion(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}
