package server.httpTools.request;

import java.util.Arrays;
import static java.util.Arrays.asList;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import server.httpTools.request.exceptions.IllegalBoundaryException;
import server.httpTools.request.exceptions.IllegalRequestMethodException;
import server.httpTools.request.exceptions.IllegalRequestVersionException;
import server.httpTools.request.exceptions.MalformedRequestException;
import server.httpTools.request.exceptions.MalformedRequestLineException;
import server.httpTools.util.HTTPMethod;
import server.httpTools.util.HTTPVersion;

public class RequestParser implements IRequestParser {

    @Override
    public IRequest parse(String request) throws MalformedRequestException {
        String[] parts = split(request);
        //Arrays.stream(parts).forEach((part) -> System.out.println("***\n" + part));
        Map<String, String[]> headers = Arrays.stream(parts[1].split("\r\n"))
                .collect(Collectors.toMap(
                                (line) -> { // Keys are value before colon
                                    return line.substring(0, line.indexOf(':'));
                                },
                                (line) -> { // Values are after the colon separated by semicolons
                                    String[] values = line.substring(line.indexOf(':') + 1).split(";");
                                    for(int i = 0; i < values.length; i ++) {
                                        values[i] = values[i].trim();
                                    }
                                    return values;
                                },
                                (old, add) -> { // If a key already exists add the new values to the mapped array if they aren't already there
                                    LinkedHashSet<String> vals = new LinkedHashSet<>(old.length + add.length);
                                    vals.addAll(asList(old));
                                    vals.addAll(asList(add));
                                    return vals.toArray(new String[vals.size()]);
                                }));
        String boundary = getBoundary(headers);
        HTTPMethod method = getMethod(parts[0]);
        String resource = getResource(parts[0]);
        HTTPVersion version = getVersion(parts[0]);
        
        MultipartRequestFactory mrf = MultipartRequestFactory.getCustomBoundaryAndMethod(boundary, version, method, resource);
        headers.forEach((key, values) -> mrf.addHeader(key, values));
        //headers.forEach((key, values) -> System.out.println(key + ": " + Arrays.toString(values)));
        return mrf.buildRequest();
    }

    private String[] split(String request) {
        request = request.trim();
        int infoEnd = request.indexOf("\r\n");
        String requestInfo = request.substring(0, infoEnd);
        String[] parts = request.substring(infoEnd + 2).split("\r\n\r\n", 2);
        String[] split = new String[3];
        split[0] = requestInfo;
        split[1] = parts[0];
        if (parts.length > 1) {
            split[2] = parts[1];
        } else {
            split[2] = "";
        }
        return split;
    }
    
    private String getBoundary(Map<String, String[]> headers) throws IllegalBoundaryException {
        String[] contentType = headers.getOrDefault("Content-Type", new String[]{});
        
        Optional<String> boundaryOpt = Arrays.stream(contentType).filter((value) -> value.startsWith("boundary")).findFirst();
        try {
            return boundaryOpt.orElseThrow(IllegalBoundaryException::new).split("=")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalBoundaryException();
        }
    }
    
    private HTTPMethod getMethod(String info) throws IllegalRequestMethodException {
        String[] parts = info.split("\\s");
        try {
            return HTTPMethod.valueOf(parts[0]);
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new IllegalRequestMethodException();
        }
    }
    
    private String getResource(String info) throws MalformedRequestLineException {
        String[] parts = info.split("\\s");
        try {
            return parts[1];
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new MalformedRequestLineException();
        }
    }
    
    private HTTPVersion getVersion(String info) throws IllegalRequestVersionException {
        String[] parts = info.split("\\s");
        try {
        return HTTPVersion.valueOf(parts[2].replaceAll("[//.////]*", ""));
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new IllegalRequestVersionException();
        }
    }
}
