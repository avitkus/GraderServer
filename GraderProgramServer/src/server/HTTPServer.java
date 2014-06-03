package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ServerSocketFactory;
import server.com.webHandler.WebHandler;

public class HTTPServer implements Runnable {

    private static final Logger LOG = Logger.getLogger(HTTPServer.class.getName());
    private final int COM_PORT;

    public HTTPServer(int port) {
        COM_PORT = port;
    }

    @Override
    public void run() {
        final ServerSocket serverSocket;
        try {
            serverSocket = ServerSocketFactory.getDefault().createServerSocket(COM_PORT);
            while (true) {
                final Socket socket = serverSocket.accept();
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            getConnectionRunnable(socket).run();
                        } catch (SocketException e) {
                            LOG.log(Level.FINER, null, e);
                        } catch (IOException e) {
                            LOG.log(Level.FINER, null, e);
                        }
                    }
                }.start();
            }
        } catch (IOException e1) {
            LOG.log(Level.FINER, null, e1);
        }

    }

    public Runnable getConnectionRunnable(Socket socket) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String requestLine = br.readLine();
        StringBuilder request = new StringBuilder(200);
        request.append(requestLine).append("\r\n");
        String line = br.readLine();
        while(!line.isEmpty()) {
            request.append(line).append("\r\n");
            line = br.readLine();
        }
        request.append("\r\n");
        String requestStr = request.toString();
        int lenLoc = requestStr.indexOf("Content-Length:");
        for(lenLoc += 15; lenLoc < requestStr.length() && !Character.isDigit(requestStr.charAt(lenLoc)); lenLoc ++);
        int bodyLen = Integer.parseInt(requestStr.substring(lenLoc, requestStr.indexOf("\r\n", lenLoc)));
        while(bodyLen > 0) {
            request.append((char)br.read());
            bodyLen --;
        }
        requestStr = request.toString();
        if (requestLine != null && requestLine.startsWith("GET")) {
            return new WebHandler(socket, requestLine, null, requestStr);
        } else if (requestLine != null && requestLine.startsWith("POST")) {
            int split = requestStr.indexOf("\r\n\r\n");
            String args = requestStr.substring(split + 4);

            return new WebHandler(socket, requestLine, args, requestStr);
        } else {
            socket.close();
            return new Runnable() {
                public void run() {
                }
            };
        }
    }
    
    private char[] intToCharArr(int i) {
        char[] chars = new char[2];
        chars[1] = (char)i;
        i >>= 16;
        chars[0] = (char)i;
        return chars;
    }
}
