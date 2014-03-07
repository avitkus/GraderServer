package server;

import java.io.IOException;

import server.utils.ConfigReader;
import server.utils.IConfigReader;

/**
 * @author Andrew Vitkus
 *
 */
public class GraderProgramServer {
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		IConfigReader config = new ConfigReader("config/config.properties");
		
		if (config.getBoolean("server.http.enabled")) {
			int port = config.getInt("server.http.port");
			System.out.println("Starting HTTP server on " + port);
			new Thread(new HTTPServer(port)).start();
		}
		if (config.getBoolean("server.https.enabled")) {
			int port = config.getInt("server.https.port");
			System.out.println("Starting HTTPS server on " + port);
			new Thread(new HTTPSServer(port)).start();
		}
		if (config.getBoolean("server.grader.enabled")) {
			int port = config.getInt("server.grader.port");
			System.out.println("Starting grader server on " + port);
			new Thread(new GraderServer(port)).start();
		}
	}
}
