package server.httpTools.request;

import server.httpTools.util.HTTPMethod;
import server.httpTools.util.HTTPVersion;


public class RequestLine implements IRequestLine {
    private final HTTPVersion protocol;
    private final HTTPMethod method;
    private final String resource;
    
    protected RequestLine(HTTPVersion protocol, HTTPMethod method, String resource) {
        this.protocol = protocol;
        this.method = method;
        this.resource = resource;
    }

    @Override
    public HTTPVersion getProtocol() {
        return protocol;
    }

    @Override
    public HTTPMethod getMethod() {
        return method;
    }

    @Override
    public String getResource() {
        return resource;
    }
    
    @Override
    public String getRequestLine() {
        return method.name() + " " + resource + " " + protocol.getName() + "\r\n";
    }
}
