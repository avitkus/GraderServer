package edu.unc.cs.graderserver.nio.pageWrappers;

import edu.unc.cs.httpserver.HTTPServerFactory;
import edu.unc.cs.httpserver.IHttpServer;
import edu.unc.cs.httpserver.pages.IPage;
import edu.unc.cs.httpserver.pages.nio.NFilePage;
import edu.unc.cs.httpserver.pages.nio.NImagePage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.entity.ContentType;

/**
 *
 * @author Andrew
 */
public class NIOServer {
    private static final Logger LOG = Logger.getLogger(NIOServer.class.getName());
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Path root = Paths.get("\\");
        System.out.println("Loading...");
        IHttpServer server = HTTPServerFactory.getNHTTPServer(root, 8080);
        server.addPage(new NFilePage(Paths.get("bla.html"), Paths.get("build.xml"), ContentType.APPLICATION_XML));
        try {
            server.addPage(new NImagePage(Paths.get("ex.png"), Paths.get("waveforms2.png").toAbsolutePath()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            server.addPage(new NImagePage(Paths.get("ex.gif"), Paths.get("waveforms2.png").toAbsolutePath()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("Loaded");
        try {
            server.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
