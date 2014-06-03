package server.com.webHandler.pages.helpers;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import server.com.webHandler.pages.GraderPage;
import server.com.webHandler.pages.IGraderPage;
import server.httpTools.request.IRequest;
import server.httpTools.request.MultipartContent;
import server.httpTools.request.MultipartRequestBody;

/**
 *
 * @author Andrew Vitkus
 */
public class GraderPageSetup {
    public IGraderPage buildGraderPage(IRequest request) {
        IGraderPage page = new GraderPage();
        MultipartRequestBody body = (MultipartRequestBody)request.getBody();
        MultipartContent[] contents = body.getContents();
        for(MultipartContent content : contents) {
            String name = content.getDisposition("name");
            content.getDisposition().forEach((key, val) -> System.out.println(key + ": " + val));
            System.out.println("~~~\n" + content.getContent());
            if(name != null && name.equals("file")) {
                String b64Content = content.getContent();
                byte[] b64Data = Base64.getDecoder().decode(b64Content);
                ByteArrayInputStream bais = new ByteArrayInputStream(b64Data);
                int BUFFER_SIZE = 2048;
                try {
                    Path tmp = Files.createTempFile("uploaded", ".tmp");
                    try (FileOutputStream fos = new FileOutputStream(tmp.toFile())) {
                        byte[] data = new byte[BUFFER_SIZE];
                        int bytesRead = -1;
                        while ((bytesRead = bais.read(data)) != -1) {
                            fos.write(data, 0, bytesRead);
                        }

                        fos.flush();
                    } catch (IOException e) {
                    }
                    
                    System.out.println ("~~~~~^^^^^~~~~~");
                    try (FileReader fr = new FileReader(tmp.toFile())) {
                        while(fr.ready()) {
                            System.out.print(fr.read());
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        //System.out.println(request.getRequest());
        //contents[0].
        return page;
    }
}
