package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import server.com.webHandler.WebHandler;
import server.utils.KeyManagerUtil;

public class HTTPSServer implements Runnable {

    private static final String SSL_PROTOCOL = "TLSv1";
    private static final String KEYSTORE_FILENAME = "keystore.jks";
    private static final String KEYSTORE_PASSWORD = "1qaz3edc5tgb";
    private static final String[] CIPHERS = {"TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256",
        "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256",
        "TLS_RSA_WITH_AES_128_CBC_SHA256",
        "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA",
        "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA",
        "TLS_ECDH_ECDSA_WITH_RC4_128_SHA",
        "TLS_ECDH_RSA_WITH_RC4_128_SHA"};
    private static final String[] PROTOCOLS = {"SSLv3",
        "TLSv1",
        "TLSv1.1",
        "TLSv1.2"};
    private static final Logger LOG = Logger.getLogger(HTTPSServer.class.getName());
    private final int COM_PORT;

    public HTTPSServer(int port) {
        COM_PORT = port;
    }

    @Override
    @SuppressWarnings("null")
    public void run() {
        SSLServerSocket serverSocket = null;
        try {
            serverSocket = setupSSLServerSocket();
        } catch (KeyManagementException | KeyStoreException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException | IOException e) {
            LOG.log(Level.FINER, null, e);
        }

        while (true) {
            try {
                new Thread(getConnectionRunnable((SSLSocket) serverSocket.accept())).start();
            } catch (SocketException e) {
                LOG.log(Level.FINER, null, e);
            } catch (IOException e) {
                LOG.log(Level.FINER, null, e);
            }
        }
    }

    private SSLServerSocket setupSSLServerSocket() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, KeyManagementException, UnrecoverableKeyException {
        KeyManagerFactory kmf = KeyManagerUtil.getKeyManagerFactory(KEYSTORE_FILENAME, KEYSTORE_PASSWORD);
        SSLContext ctx = SSLContext.getInstance(SSL_PROTOCOL);
        ctx.init(kmf.getKeyManagers(), null, null);
        SSLServerSocketFactory factory = ctx.getServerSocketFactory();

        SSLServerSocket serverSocket = (SSLServerSocket) factory.createServerSocket(COM_PORT);
        serverSocket.setEnabledCipherSuites(CIPHERS);
        serverSocket.setEnabledProtocols(PROTOCOLS);
        return serverSocket;
    }

    public Runnable getConnectionRunnable(SSLSocket socket) throws IOException {
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
}
