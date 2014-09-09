package edu.unc.cs.graderServer.webHandler.pages.helpers;

import edu.unc.cs.graderServer.graderHandler.sql.DatabaseWriter;
import edu.unc.cs.graderServer.graderHandler.sql.DatabaseReader;
import edu.unc.cs.graderServer.graderHandler.sql.IDatabaseReader;
import edu.unc.cs.graderServer.graderHandler.sql.IDatabaseWriter;
import edu.unc.cs.graderServer.utils.ConfigReader;
import edu.unc.cs.graderServer.utils.IConfigReader;
import edu.unc.cs.graderServer.utils.URLConnectionHelper;
import edu.unc.cs.graderServer.webHandler.pages.GraderPage;
import edu.unc.cs.graderServer.webHandler.pages.IGraderPage;
import edu.unc.cs.httpTools.request.IRequest;
import edu.unc.cs.httpTools.request.MultipartContent;
import edu.unc.cs.httpTools.request.MultipartRequestBody;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;

public class GraderPageSetup {
    
    private static final Logger LOG = Logger.getLogger(GraderPageSetup.class.getName());
    
    public IGraderPage buildGraderPage(IRequest request) {
        IGraderPage page = new GraderPage();
        MultipartRequestBody body = (MultipartRequestBody) request.getBody();
        MultipartContent[] contents = body.getContents();
        for (MultipartContent content : contents) {
            //System.out.println("~~~");
            String name = content.getDisposition("name");
            //content.getDisposition().forEach((key, val) -> System.out.println(key + ": " + val));
            //System.out.println(content.getContent());

            if (name != null) {
                switch (name) {
                    case "file":
                        String b64Content = content.getContent();
                        byte[] b64Data = Base64.getDecoder().decode(b64Content);
                        ByteArrayInputStream bais = new ByteArrayInputStream(b64Data);
                        int BUFFER_SIZE = 2048;
                        try {
                            String fileName = content.getDisposition().get("filename");
                            String ext = "";
                            if (fileName != null && !fileName.isEmpty()) {
                                String[] fileNameSplit = fileName.split("\\.", 2);
                                if (fileNameSplit.length > 1) {
                                    ext = "." + fileNameSplit[1];
                                }
                            }
                            Path tmp = Files.createTempFile("uploaded", ext);
                            page.setToGradeFile(tmp);
                            try (FileOutputStream fos = new FileOutputStream(tmp.toFile())) {
                                byte[] data = new byte[BUFFER_SIZE];
                                int bytesRead;
                                while ((bytesRead = bais.read(data)) != -1) {
                                    fos.write(data, 0, bytesRead);
                                }
                                
                                fos.flush();
                            } catch (IOException e) {
                                LOG.log(Level.WARNING, "Failed to write submission to file");
                            }

                            /*
                             * System.out.println ("~~~~~^^^^^~~~~~"); try
                             * (FileReader fr = new FileReader(tmp.toFile())) {
                             * while(fr.ready()) {
                             * System.out.print((char)fr.read()); }
                             * }
                             */
                        } catch (IOException ex) {
                            LOG.log(Level.WARNING, "Error in reading submitted file");
                        }
                        break;
                    case "onyen":
                        page.setOnyen(content.getContent());
                        break;
                    case "auth":
                        page.setAuth(content.getContent());
                        break;
                    case "course":
                        page.setCourse(content.getContent());
                        break;
                    case "assignment":
                        page.setAssignment(content.getContent());
                        break;
                    case "uid":
                        String uid = content.getContent();
                        page.setUID(content.getContent());
                        try {
                            IConfigReader config = new ConfigReader("./config/config.properties");
                            String username = config.getString("database.username").orElseThrow(IllegalArgumentException::new);
                            String password = config.getString("database.password").orElseThrow(IllegalArgumentException::new);
                            String url = config.getString("database.url").orElseThrow(IllegalArgumentException::new);
                            if (config.getBoolean("database.ssl", false).get()) {
                                url += "?verifyServerCertificate=true&useSSL=true&requireSSL=true";
                            }
                            try (IDatabaseReader dr = new DatabaseReader(username, password, "jdbc:" + url) {}) {
                                String[] nameParts = dr.readNameForUID(uid);
                                page.setName(nameParts[0], nameParts[1]);
                            }
                        } catch (IOException | SQLException ex) {
                            Logger.getLogger(GraderPageSetup.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case "id":
                        page.setPageUUID(content.getContent());
                        break;
                    case "ip":
                        page.setIP(content.getContent());
                        break;
                    case "vfykey":
                        try {
                            page.setAllowed(checkVfykey(content.getContent(), page));
                        } catch (IOException ex) {
                            page.setAllowed(false);
                            Logger.getLogger(GraderPageSetup.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                }
            }
        }
        //System.out.println(request.getRequest());
        //contents[0].
        return page;
    }
    
    private boolean checkVfykey(String vfykey, IGraderPage page) throws IOException, MalformedURLException {
        URL authURL = new URL("https", "onyen.unc.edu", 443, "/cgi-bin/unc_id/authenticator.pl/" + vfykey);
        HttpsURLConnection auth = (HttpsURLConnection) authURL.openConnection();
        
        auth.setDoOutput(true);
        auth.setDoInput(true);
        auth.getOutputStream().write(42);

        /*
         * build a map of the auth server's key-value pair response
         */
        String response = URLConnectionHelper.getResponse(auth);
        HashMap<String, String> responseMap = new HashMap<>(7);
        Arrays.stream(response.split("\n")).forEach((String responseLine) -> {
            String[] lineParts = responseLine.split(": ");
            responseMap.put(lineParts[0], lineParts[1]);
        });
        
        int colon1Loc = response.indexOf(':');
        int endLnLoc = response.indexOf('\n');
        String authStatus;
        if (endLnLoc > 0) {
            authStatus = response.substring(colon1Loc + 2, endLnLoc);
        } else {
            authStatus = response.substring(colon1Loc + 2);
        }
        //System.out.println(authStatus);
        if (authStatus.equals("pass")) {
            String onyen = response.substring(0, colon1Loc);
            String uid = responseMap.get("uid");
            String pid = responseMap.get("pid");
            String first = responseMap.get("givenName");
            String last = responseMap.get("uncPreferredSurname");
            
            try {
                addUser(onyen, uid, pid, first, last);
            } catch (SQLException ex) {
                Logger.getLogger(GraderPageSetup.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            page.setOnyen(onyen);
            page.setUID(uid);
            page.setPID(pid);
            page.setFirstName(first);
            page.setLastName(last);
            
            return true;
        } else {
            return false;
        }
    }
    
    private void addUser(String onyen, String uid, String pid, String firstName, String lastName) throws IOException, SQLException {
        IConfigReader config = new ConfigReader("./config/config.properties");
        String username = config.getString("database.username").orElseThrow(IllegalArgumentException::new);
        String password = config.getString("database.password").orElseThrow(IllegalArgumentException::new);
        String url = config.getString("database.url").orElseThrow(IllegalArgumentException::new);
        if (config.getBoolean("database.ssl", false).get()) {
            url += "?verifyServerCertificate=true&useSSL=true&requireSSL=true";
        }

        try (IDatabaseWriter dw = new DatabaseWriter(username, password, "jdbc:" + url)) {
            dw.writeUser(onyen, uid, pid, firstName, lastName);
        }
    }
}
