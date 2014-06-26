package server.com.webHandler.pages.helpers;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.com.webHandler.pages.GraderPage;
import server.com.webHandler.pages.IGraderPage;
import server.httpTools.request.IRequest;
import server.httpTools.request.MultipartContent;
import server.httpTools.request.MultipartRequestBody;

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
                        page.setUID(content.getContent());
                        break;
                    case "id":
                        page.setPageUUID(content.getContent());
                        break;
                    case "ip":
                        page.setIP(content.getContent());
                        break;
                }
            }
        }
        //System.out.println(request.getRequest());
        //contents[0].
        return page;
    }
}
