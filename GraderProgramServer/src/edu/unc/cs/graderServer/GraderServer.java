package edu.unc.cs.graderServer;

import java.io.IOException;
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
import javax.net.ssl.TrustManagerFactory;
import edu.unc.cs.graderServer.graderHandler.JavaBasedGraderHandler;
import edu.unc.cs.graderServer.utils.KeyManagerUtil;
import edu.unc.cs.graderServer.utils.TrustManagerUtil;

public class GraderServer implements Runnable {

    private static final String SSL_PROTOCOL = "TLSv1.2";
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

    private static final Logger LOG = Logger.getLogger(GraderServer.class.getName());
    private final int COM_PORT;

    public GraderServer(int port) {
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
                new Thread(new JavaBasedGraderHandler((SSLSocket) serverSocket.accept())).start();
            } catch (SocketException e) {
                LOG.log(Level.FINER, null, e);
            } catch (IOException e) {
                LOG.log(Level.FINER, null, e);
            }
        }
    }

    private SSLServerSocket setupSSLServerSocket() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, KeyManagementException, UnrecoverableKeyException {
        KeyManagerFactory kmf = KeyManagerUtil.getKeyManagerFactory(KEYSTORE_FILENAME, KEYSTORE_PASSWORD);
        TrustManagerFactory tmf = TrustManagerUtil.getTrustManagerFactory(KEYSTORE_FILENAME, KEYSTORE_PASSWORD);
        SSLContext ctx = SSLContext.getInstance(SSL_PROTOCOL);
        ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        SSLServerSocketFactory factory = ctx.getServerSocketFactory();
        SSLServerSocket serverSocket = (SSLServerSocket) factory.createServerSocket(COM_PORT);
        serverSocket.setEnabledCipherSuites(CIPHERS);
        serverSocket.setEnabledProtocols(PROTOCOLS);
        return serverSocket;
    }
}
