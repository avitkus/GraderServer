package server.httpTools.request;


public class RequestParser implements IRequestParser {

    @Override
    public IRequest parse(String request) {
        
        return null;
    }
    
    private String[] splitHeaders(String headers) {
        return headers.trim().split("\r\n");
    }
}
