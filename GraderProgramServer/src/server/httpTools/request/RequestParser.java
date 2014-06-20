package server.httpTools.request;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
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
                                    for (int i = 0; i < values.length; i++) {
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
        headers.remove("Content-Length");
        headers.forEach((key, values) -> mrf.addHeader(key, values));
        BodyPartData[] bodyParts = getParts(parts[2], boundary);
        Arrays.stream(bodyParts).forEach((partData) -> mrf.addPart(partData.getData(), partData.getType(), partData.getDisposition(), partData.getDetails()));
        //headers.forEach((key, values) -> System.out.println(key + ": " + Arrays.toString(values)));
        return mrf.buildRequest();
    }

    private String[] split(String request) {
        request = request.trim();
        int infoEnd = request.indexOf("\r\n");
        String requestInfo = request.substring(0, infoEnd);
        String[] parts = request.split("[\r\n]+", 2)[1].split("\r\n\r\n", 2);
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

    private BodyPartData[] getParts(String body, String boundary) {
        String[] split = body.split("--" + boundary);
        ArrayList<BodyPartData> parts = new ArrayList<>(split.length);
        for (int i = 1; i < split.length - 1; i++) {
            String[] partParts = split[i].split("\r\n\r\n", 2);
            String disposition = "";
            String type = "";
            String[] details = new String[]{};
            String data = "";

            //System.out.println("==========");

            if (partParts.length == 2) {
                String[] headerParts = partParts[0].split("[\r\n]+");

                for (String headerPart : headerParts) {
                    if (!headerPart.isEmpty()) {
                        //System.out.println(headerPart);
                        String[] headerPartParts = magic(headerPart); // I'm so sorry for this naming...but that is what it is
                        //System.out.println(headerPartParts[0]);
                        switch (headerPartParts[0]) {
                            case "Content-Disposition":
                                disposition = headerPartParts[1];
                                details = Arrays.copyOfRange(headerPartParts, 2, headerPartParts.length);
                                break;
                            case "Content-Type":
                                type = headerPartParts[1];
                                break;
                        }
                    }
                }
            }
            data = partParts[partParts.length - 1];
            if (data.endsWith("\r\n")) {
                data = data.substring(0, data.length() - 2);
            }

            //System.out.println(disposition + ", " + type + ", " + data + ", " + Arrays.toString(details));
            parts.add(new BodyPartData(disposition, type, details, data));
            //System.out.println("1:" + parts.get(parts.size() - 1).getData());
            //parts.get(parts.size() - 1).getData().chars().forEach((c) -> System.out.println(Character.getName(c)));
            //System.out.println("2:" + Arrays.toString(parts.get(parts.size() - 1).getDetails()));
            //System.out.println("3:" + parts.get(parts.size() - 1).getDisposition());
            //System.out.println("4:" + parts.get(parts.size() - 1).getType());
            //Arrays.stream(partParts).forEach((s) -> System.out.println("--- " + Arrays.toString(magic(s))));
        }
        return parts.toArray(new BodyPartData[parts.size()]);
    }

    /**
     * This splits up lines. I can't think of how to word it correctly, thus it
     * performs magic.
     *
     * @param line
     *
     * @return parts of the line
     */
    private String[] magic(String line) {
        //System.out.println("***"+line+"***");
        String[] split = line.split("[\r\n]+", 2);
        String args = split[0].isEmpty() ? split[split.length - 1] : line;
        ArrayList<String> parts = new ArrayList<>(5);
        int colLoc = args.indexOf(':');
        if (colLoc > 0 && !split[0].isEmpty()) {
            parts.add(args.substring(0, colLoc));
            args = args.substring(args.indexOf(' ') + 1);
        }
        int semLoc = args.indexOf(';');
        int len = args.length();
        split = args.split("[\r\n]+");
        int check = semLoc >= 0 ? semLoc : args.endsWith("\r\n") ? len - 2 : len;
        parts.add(args.substring(0, check));
        args = args.substring(Math.min(check + 1, len));

        StringBuilder thing = new StringBuilder(10);
        try (StringReader sr = new StringReader(args)) {
            boolean inQuot = false;
            boolean out = true;
            boolean outOkay = false;

            int prev = sr.read();
            int cur;
            for (cur = sr.read(); cur != -1; cur = sr.read()) {
                if (inQuot) {
                    if (cur == '"') {
                        inQuot = false;
                        out = false;
                        if (thing.length() > 0) {
                            parts.add(thing.toString());
                            thing.setLength(0);
                        }
                    } else {
                        thing.append((char) cur);
                    }
                } else {
                    if (!outOkay) {
                        outOkay = Character.isAlphabetic(prev);
                    }
                    if (cur == '"') {
                        if (thing.length() > 0) {
                            parts.add(thing.toString());
                            thing.setLength(0);
                        }
                        inQuot = true;
                    } else if (out && outOkay) {
                        thing.append((char) prev);
                    } else {
                        out = true;
                        outOkay = false;
                    }
                }
                prev = cur;
            }
            if (prev != -1 && out) {
                thing.append((char) prev);
            }
            if (thing.length() > 0) {
                parts.add(thing.toString());
            }
        } catch (IOException e) {

        }

        return parts.toArray(new String[parts.size()]);
    }

    private class BodyPartData {

        private final String disposition;
        private final String type;
        private final String[] details;
        private final String data;

        BodyPartData(String disposition, String type, String[] details, String data) {
            this.disposition = disposition;
            this.type = type;
            this.details = details;
            this.data = data;
        }

        public String getDisposition() {
            return disposition;
        }

        public String getType() {
            return type;
        }

        public String[] getDetails() {
            return Arrays.copyOf(details, details.length);
        }

        public String getData() {
            return data;
        }
    }
}
