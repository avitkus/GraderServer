package server.httpTools.util;

/**
 *
 * @author Andrew Vitkus
 */
public enum HTTPStatusCode {
    C100 ("Continue"),
    C101 ("Switching Protocols"),
    C200 ("OK"),
    C201 ("Created"),
    C202 ("Accepted"),
    C203 ("Non-Authoritative Information"),
    C204 ("No Content"),
    C205 ("Reset Content"),
    C206 ("Partial Content"),
    C300 ("Multiple Choices"),
    C301 ("Moved Permanently"),
    C302 ("Found"),
    C303 ("See Other"),
    C304 ("Not Modified"),
    C305 ("Use Proxy"),
    C307 ("Temporary Redirect"),
    C400 ("Bad Request"),
    C401 ("Unauthorized"),
    C402 ("Payment Required"),
    C403 ("Forbidden"),
    C404 ("Not Found"),
    C405 ("Method Not Allowed"),
    C406 ("Not Acceptable"),
    C407 ("Proxy Authentication Required"),
    C408 ("Reqeust Timeout"),
    C409 ("Conflict"),
    C410 ("Gone"),
    C411 ("Length Required"),
    C412 ("Precondition Failed"),
    C413 ("Request Entity Too Large"),
    C414 ("Request-URI Too Long"),
    C415 ("Unsupported Media Type"),
    C416 ("Requested Range Not Satisfiable"),
    C417 ("Expectation Failed"),
    C418 ("I'm a teapot"),
    C500 ("Internal Server Error"),
    C501 ("Not Implemented"),
    C502 ("Bad Gateway"),
    C503 ("Service Unavailable"),
    C504 ("Gateway Timeout"),
    C505 ("HTTP Version Not Supported");
    
    private final String meaning;
    
    private HTTPStatusCode(String meaning) {
        this.meaning = meaning;
    }
    
    public String getMeaning() {
        return meaning;
    }
    
    public String getNumber() {
        return name().substring(1);
    }
}
