package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.net.ssl.SSLSocket;

import server.com.graderHandler.GraderHandler;
import server.com.webHandler.WebHandler;

/**
 * @author Andrew Vitkus
 *
 */
public class HandlerChooser implements IHandlerChooser {

	@Override
	public Runnable getHandler(SSLSocket socket) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		/*br.mark(1000);
		while(br.ready()) {
			System.out.println(br.readLine());
		}
		br.reset();*/
		System.out.println("nyarm");
		String message = br.readLine();
		System.out.println(message);
		if (message == null) {
			return new Runnable(){public void run(){}};
		}
		if(message.startsWith("GET")) {
			return new WebHandler(socket, message, null);
		} else if (message.startsWith("POST")) {
			System.out.println("post");
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			bw.write("HTTP/1.1 100 Continue");
			bw.flush();
			br.read();
			while(br.ready()) {
				System.out.println(br.readLine());
			}
			return new Runnable(){public void run(){}};
		} else {
			return new GraderHandler(socket);
		}
	}
}
