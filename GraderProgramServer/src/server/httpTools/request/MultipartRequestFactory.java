package server.httpTools.request;

import server.httpTools.util.HTTPMethod;
import server.httpTools.util.HTTPVersion;
import static server.httpTools.util.HTTPMethod.POST;
import static server.httpTools.util.HTTPVersion.HTTP11;

/**
 *
 * @author Andrew Vitkus
 */
public class MultipartRequestFactory {
    private static final String DEFAULT_BOUNDARY = "----1234567890f0987654321";
    private static final HTTPVersion DEFAULT_HTTP_VERSION = HTTP11;
    private static final HTTPMethod DEFAULT_HTTP_METHOD = POST;

    public static MultipartRequestFactory getDefault(String resource) {
        return new MultipartRequestFactory(DEFAULT_BOUNDARY, DEFAULT_HTTP_VERSION, DEFAULT_HTTP_METHOD, resource);
    }
    
    public static MultipartRequestFactory getCustomBoundary(String boundary, String resource) {
        return new MultipartRequestFactory(boundary, DEFAULT_HTTP_VERSION, DEFAULT_HTTP_METHOD, resource);
    }
    
    public static MultipartRequestFactory getCustomMethod(HTTPVersion version, HTTPMethod method, String resource) {
        return new MultipartRequestFactory(DEFAULT_BOUNDARY, version, method, resource);
    }
    
    public static MultipartRequestFactory getCustomBoundaryAndMethod(String boundary, HTTPVersion version, HTTPMethod method, String resource) {
        return new MultipartRequestFactory(boundary, version, method, resource);
    }
    
    private final RequestHeaderFactory headerFactory;
    private final MultipartRequestBodyFactory bodyFactory;
    private final String boundary;
    private final HTTPVersion version;
    private final HTTPMethod method;
    private final String resource;
    
    private MultipartRequestFactory(String boundary, HTTPVersion version, HTTPMethod method, String resource) {
        headerFactory = RequestHeaderFactory.getDefault();
        bodyFactory = MultipartRequestBodyFactory.getDefault();
        this.boundary = boundary;
        this.version = version;
        this.method = method;
        this.resource = resource;
    }
    
    public void addPart(String data, String type, String dispositionType, String... disposition) {
        bodyFactory.addPart(data, type, dispositionType, disposition);
    }
    
     public void addHeader(String key, String... value) {
         headerFactory.addHeader(key, value);
     }
    
    public IRequest buildRequest() {
        IRequestBody body = bodyFactory.buildBody(boundary);
        
        headerFactory.addHeader("Content-Type", "multipart/mixed", "boundary=" + boundary);
        headerFactory.addHeader("Content-Length", Integer.toString(body.getBody().length()));
        
        IRequestHeaders header = headerFactory.getHeaders();
        
        IRequestInfo request = new RequestInfo(version, method, resource);
        
        return MultipartRequest.getInstance(request, header, body);
    }
}
