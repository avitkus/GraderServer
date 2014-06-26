package server.com.webHandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.Socket;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import server.com.webHandler.pages.AuthPage;
import server.com.webHandler.pages.GradingResultPage;
import server.com.webHandler.pages.IAuthPage;
import server.com.webHandler.pages.IGraderPage;
import server.com.webHandler.pages.IGradingResultPage;
import server.com.webHandler.pages.INotFoundPage;
import server.com.webHandler.pages.IStudentDataLookupPage;
import server.com.webHandler.pages.IStudentDataStatisticsPage;
import server.com.webHandler.pages.IUploadPage;
import server.com.webHandler.pages.NotFoundPage;
import server.com.webHandler.pages.StudentDataLookupPage;
import server.com.webHandler.pages.StudentDataStatisticsPage;
import server.com.webHandler.pages.UploadPage;
import server.com.webHandler.pages.css.AuthCSS;
import server.com.webHandler.pages.css.IAuthCSS;
import server.com.webHandler.pages.helpers.GraderPageSetup;
import server.httpTools.request.IRequest;
import server.httpTools.request.RequestParser;
import server.httpTools.request.exceptions.MalformedRequestException;
import server.utils.ConfigReader;
import server.utils.IConfigReader;

public class WebHandler implements Runnable {

    private static final Logger LOG = Logger.getLogger(WebHandler.class.getName());

    private static String getServerTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime());
    }
    private final String args;
    private final Socket clientSocket;
    private final String request;
    private final String requestLine;

    public WebHandler(Socket socket, String requestLine, String args, String request) {
        clientSocket = socket;
        this.requestLine = requestLine;
        this.args = args;
        this.request = request;
        //System.out.println(request + ": " + args);
    }

    @Override
    public void run() {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {
            //System.out.println("okay");
            String site = getSite();
            BufferedReader sbr = new BufferedReader(new StringReader(site));
            String line;
            while ((line = sbr.readLine()) != null) {
                //System.out.println(line);
                bw.write(line);
                bw.write("\n");
            }
            bw.flush();
        } catch (IOException e) {
            LOG.log(Level.FINER, null, e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                LOG.log(Level.FINER, null, e);
            }
        }
    }
    
    private String buildHeader(int status, int length, String... headers) {
        boolean hasType = false;
        for(String header : headers) {
            if (header.startsWith("Content-Type:")) {
                hasType = true;
                break;
            }
        }
        if (!hasType) {
            headers = Arrays.copyOf(headers, headers.length + 1);
            headers[headers.length - 1] = "ContentType: " + "text/html";
        }
        StringBuilder head = new StringBuilder(50);
        head.append("HTTP/1.1 ").append(status).append(" ");

        switch (status) {
            case 200:
                head.append("OK\r\n");
                break;
            case 303:
                head.append("See Other\r\n");
                break;
            case 404:
                head.append("Not Found\r\n");
                break;
            default:
                head.append("Internal Server Error\r\n");
        }

        head.append("Date: ").append(getServerTime()).append("\r\n");
        head.append("Connection: close\r\n");
        head.append("Content-Length: ").append(length).append("\r\n");
        for(String header : headers) {
            head.append(header).append("\r\n");
        }
        head.append("\r\n");

        return head.toString();
    }

    private String getSite() throws FileNotFoundException, IOException {
        if (requestLine.contains(" / ")) {
            IAuthPage ap = new AuthPage();
            if (args != null) {
                ap.setArgs(args);
            }

            String authStatus = ap.checkAuth();
            if (authStatus != null) {
                int i = authStatus.indexOf(' ') + 1;
                //System.out.println(authStatus.substring(i, i + 4));
                if (authStatus.substring(i, i + 4).equals("pass")) {
                    IConfigReader config = new ConfigReader(Paths.get("config", "config.properties").toString());
                    if (clientSocket instanceof SSLSocket) {
                        return "HTTP/1.1 303 See Other\r\nLocation: https://classroom.cs.unc.edu:" + config.getInt("server.https.port") + "/index.html\r\n\r\n";
                    } else {
                        return "HTTP/1.1 303 See Other\r\nLocation: http://classroom.cs.unc.edu:" + config.getInt("server.http.port") + "/index.html\r\n\r\n";
                    }
                } else {
                    ap.setFailed(true);
                }
            }

            String html = ap.getHTML();
            return buildHeader(200, html.length()) + html;
        } else if (requestLine.contains(" /lookup.html")) {
            //System.out.println(request);
            IStudentDataLookupPage sdlp = new StudentDataLookupPage();
            if (args != null) {
                sdlp.setArgs(args);
            }

            String html = sdlp.getHTML();
            return buildHeader(200, html.length()) + html;
        } else if (requestLine.contains(" /stats.html")) {
            //System.out.println(request);
            IStudentDataStatisticsPage sdsp = new StudentDataStatisticsPage();
            if (args != null) {
                sdsp.setArgs(args);
            }

            String html = sdsp.getHTML();
            return buildHeader(200, html.length()) + html;
        } else if (requestLine.contains(" /upload.html")) {
            //System.out.println(request);
            IUploadPage up = new UploadPage();
            if (args != null) {
                up.setArgs(args);
            }

            String html = up.getHTML();
            return buildHeader(200, html.length()) + html;
        } else if (requestLine.contains(" /auth.css")) {
            //System.out.println(request);
            IAuthCSS authCSS = new AuthCSS();

            String html = authCSS.getCSS(0);
            return buildHeader(200, html.length(), "text/css") + html;
        } else if (requestLine.contains(" /favicon.ico ")) {
            //System.out.println("favicon");
            try (BufferedReader br = new BufferedReader(new FileReader(Paths.get("favicon.ico").toFile()))) {
                StringBuilder icon = new StringBuilder(100);
                while (br.ready()) {
                    icon.append(br.readLine()).append("\r\n");
                }
                String icn = icon.toString();
                return buildHeader(200, icn.length()) + icn;
            }
        } else if (requestLine.contains(" /submit.html ")) {// && request.contains("multipart")) {
            RequestParser parser = new RequestParser();
            //System.out.println("***----***\n" + request + "***----***");
            try {
                IRequest requestObj = parser.parse(request);
                if (requestObj.isMultipart()) {
                    //System.out.println("submit");
                    //System.out.println("+++++\n" + requestObj.getRequest() + "\n+++++");
                    GraderPageSetup gps = new GraderPageSetup();
                    IGraderPage page = gps.buildGraderPage(requestObj);
                    String uuid = page.getPageUUID();
                    String html = page.getHTML();
                    return buildHeader(303, html.length(), "Location: /grading.php?id="+uuid) + html;
                } else {
                    return buildHeader(405, 0);
                }
            } catch (MalformedRequestException ex) {
                LOG.log(Level.SEVERE, null, ex);
                return "";
            }
        } else if (requestLine.contains(" /grading.html")) {
            //System.out.println(request);
            IGradingResultPage grp = new GradingResultPage();
            if (args != null) {
                grp.setArgs(args);
            }

            String html = grp.getHTML();
            return buildHeader(200, html.length()) + html;
        }
        //System.out.println("not found");
        INotFoundPage nfp = new NotFoundPage();
        int pageStart = requestLine.indexOf(" /") + 2;
        int pageEnd = requestLine.indexOf(' ', pageStart);
        String page = requestLine.substring(pageStart, pageEnd);
        if (page.isEmpty()) {
            page = "index.html";
        }
        //System.out.println(page);
        nfp.setPage(page);
        String html = nfp.getHTML();
        return buildHeader(404, html.length()) + html;
    }

}
