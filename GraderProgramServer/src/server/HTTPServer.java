package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.net.ServerSocketFactory;

import server.com.webHandler.WebHandler;

/**
 * @author Andrew Vitkus
 *
 */
public class HTTPServer implements Runnable {
	private int COM_PORT;
	
	public void run() {
        final ServerSocket serverSocket;
		try {
			serverSocket = ServerSocketFactory.getDefault().createServerSocket(COM_PORT);
	        while(true) {
        		final Socket socket =  serverSocket.accept();
	        	new Thread() {
		        	@Override
		        	public void run() {
		        		try {
		        			//System.out.println("---New Connection---");
			        		getConnectionRunnable(socket).run();
			        	} catch (SocketException e) {
			        		e.printStackTrace();
			        	} catch (IOException e) {
							e.printStackTrace();
						}
		        	}
	        	}.start();
	        }
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        
	}
	
	public HTTPServer(int port) {
		COM_PORT = port;
	}
	
	public Runnable getConnectionRunnable(Socket socket) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String message = br.readLine();
		//System.out.println("***"+message+"***");
		if(message != null && message.startsWith("GET")) {
			return new WebHandler(socket, message, null);
		} else if (message != null && message.startsWith("POST")) {
			//System.out.println("post");
			StringBuilder sb = new StringBuilder();
			while(br.ready()) {
				sb.append((char)br.read());
			}
			
			String response = sb.toString();
			int split = response.indexOf(System.lineSeparator() + System.lineSeparator());
			String args = response.substring(split + System.lineSeparator().length() * 2);
			
			return new WebHandler(socket, message, args);
		} else {
			socket.close();
			return new Runnable(){public void run(){}};
		}
	}
}
