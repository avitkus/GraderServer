package server.httpTools.response;

import server.httpTools.util.HTTPStatusCode;
import server.httpTools.util.HTTPVersion;

public class Status implements IStatus {

    private final HTTPVersion protocol;
    private final HTTPStatusCode status;

    protected Status(HTTPVersion protocol, HTTPStatusCode status) {
        this.protocol = protocol;
        this.status = status;
    }

    @Override
    public HTTPVersion getProtocol() {
        return protocol;
    }

    @Override
    public HTTPStatusCode getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return protocol.getName() + " " + status.getNumber() + " " + status.getMeaning() + "\r\n";
    }
}
