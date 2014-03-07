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
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.net.ssl.SSLSocket;

import server.com.webHandler.pages.AuthPage;
import server.com.webHandler.pages.IAuthPage;
import server.com.webHandler.pages.INotFoundPage;
import server.com.webHandler.pages.IStudentDataLookupPage;
import server.com.webHandler.pages.NotFoundPage;
import server.com.webHandler.pages.StudentDataLookupPage;
import server.com.webHandler.pages.css.AuthCSS;
import server.com.webHandler.pages.css.IAuthCSS;
import server.utils.ConfigReader;
import server.utils.IConfigReader;

/**
 * @author Andrew Vitkus
 *
 */
public class WebHandler implements Runnable {
	private final Socket clientSocket;
	private String request;
	private String args;
	
	public WebHandler(Socket socket, String request, String args) {
		clientSocket = socket;
		this.request = request;
		this.args = args;
		System.out.println(request + ": " + args);
	}
	
	@Override
	public void run() {
		try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {
			//System.out.println("okay");
			BufferedReader sbr = new BufferedReader(new StringReader(getSite()));
			String line = null;
			while((line = sbr.readLine()) != null) {
				//System.out.println(line);
				bw.write(line);
				bw.write("\n");
			}
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String getSite() throws FileNotFoundException, IOException {
		if (request.contains(" / ")) {
			IAuthPage ap = new AuthPage();
			if (args != null) {
				ap.setArgs(args);
			}
			
			String authStatus = ap.checkAuth();
			if (authStatus != null) {
				int i = authStatus.indexOf(' ') + 1;
				System.out.println(authStatus.substring(i, i + 4));
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
		} else if (request.contains(" /index.html")) {
			//System.out.println(request);
			IStudentDataLookupPage sdlp = new StudentDataLookupPage();
			if (args != null) {
				sdlp.setArgs(args);
			}
			
			String html = sdlp.getHTML();
			return buildHeader(200, html.length()) + html;
		} else if (request.contains(" /auth.css")) {
			//System.out.println(request);
			IAuthCSS authCSS = new AuthCSS();
			
			String html = authCSS.getCSS(0);
			return buildHeader(200, html.length(), "text/css") + html;
		} else if (request.contains(" /favicon.ico ")) {
			//System.out.println("favicon");
			try(BufferedReader br = new BufferedReader(new FileReader(Paths.get("favicon.ico").toFile()))) {
				StringBuilder icon = new StringBuilder();
				while(br.ready()) {
					icon.append(br.readLine()).append("\r\n");
				}
				return icon.toString();
			}
		} else {
			//System.out.println("not found");
			INotFoundPage nfp = new NotFoundPage();
			int pageStart = request.indexOf(" /") + 2;
			int pageEnd = request.indexOf(' ', pageStart);
			String page = request.substring(pageStart, pageEnd);
			if (page.isEmpty()) {
				page = "index.html";
			}
			//System.out.println(page);
			nfp.setPage(page);
			String html = nfp.getHTML();
			return buildHeader(404, 0) + html;
		}
	}
	
	private String buildHeader(int status, int length) {
		return buildHeader(status, length, "text/html");
	}
	
	private String buildHeader(int status, int length, String type) {
		StringBuilder head = new StringBuilder();
		head.append("HTTP/1.1 ").append(status).append(" ");
		
		switch(status) {
			case 200:
				head.append("OK\r\n");
				break;
			case 404:
				head.append("Not Found\r\n");
				break;
			default:
				head.append("Internal Server Error\r\n");
		}
		
		head.append("Date: ").append(getServerTime()).append("\r\n");
		head.append("Connection: close\r\n");
		//head.append("Content-Length: ").append(length).append("\r\n");
		head.append("Content-Type: "+type+"; charset=UTF-8\r\n");
		head.append("\r\n");
		
		return head.toString();
	}

	private static String getServerTime() {
	    Calendar calendar = Calendar.getInstance();
	    SimpleDateFormat dateFormat = new SimpleDateFormat(
	        "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
	    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	    return dateFormat.format(calendar.getTime());
	}
}
