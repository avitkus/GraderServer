package server;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.XMLFormatter;
import server.utils.ConfigReader;
import server.utils.IConfigReader;


public class GraderProgramServer {
    private static final Logger LOG = Logger.getLogger(GraderProgramServer.class.getName());

    static {
        Logger serverLog = Logger.getLogger("");
        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(Level.FINE);
        serverLog.addHandler(ch);
        try {
            FileHandler fh = new FileHandler("logs/server-%g.%u.log", 0, 50);
            fh.setLevel(Level.ALL);
            fh.setFormatter(new XMLFormatter());
            serverLog.addHandler(fh);
        } catch (IOException | SecurityException ex) {
            LOG.log(Level.FINE, "Failed to add log file writer.", ex);
        }
    }
    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        IConfigReader config = new ConfigReader("config/config.properties");
        
        if (config.getBoolean("server.http.enabled")) {
            int port = config.getInt("server.http.port");
            LOG.log(Level.INFO, "Starting HTTP server on {0}", port);
            new Thread(new HTTPServer(port)).start();
        }
        if (config.getBoolean("server.https.enabled")) {
            int port = config.getInt("server.https.port");
            LOG.log(Level.INFO, "Starting HTTPS server on {0}", port);
            new Thread(new HTTPSServer(port)).start();
        }
        if (config.getBoolean("server.grader.enabled")) {
            int port = config.getInt("server.grader.port");
            LOG.log(Level.INFO, "Starting grader server on {0}", port);
            new Thread(new GraderServer(port)).start();
        }
    }
}
