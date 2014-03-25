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
        String message = br.readLine();
        if (message != null && message.startsWith("GET")) {
            return new WebHandler(socket, message, null);
        } else if (message != null && message.startsWith("POST")) {
            StringBuilder sb = new StringBuilder(30);
            while (br.ready()) {
                sb.append((char) br.read());
            }

            String response = sb.toString();
            int split = response.indexOf(System.lineSeparator() + System.lineSeparator());
            String args = response.substring(split + System.lineSeparator().length() * 2);

            return new WebHandler(socket, message, args);
        } else {
            socket.close();
            return new Runnable() {
                public void run() {
                }
            };
        }
    }
}
