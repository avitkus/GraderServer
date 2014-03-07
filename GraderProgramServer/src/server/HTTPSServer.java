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

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import server.com.webHandler.WebHandler;
import server.utils.KeyManagerUtil;

/**
 * @author Andrew Vitkus
 *
 */
public class HTTPSServer implements Runnable {

	private int COM_PORT;
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
	private static String[] PROTOCOLS = {"SSLv3",
		                                 "TLSv1",
		                                 "TLSv1.1",
		                                 "TLSv1.2"};
	
	public HTTPSServer(int port) {
		COM_PORT = port;
	}
	
	public void run() {
        SSLServerSocket serverSocket = null;
		try {
			serverSocket = setupSSLServerSocket();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        while(true) {
        	try {
        		new Thread(getConnectionRunnable((SSLSocket)serverSocket.accept())).start();
        	} catch (SocketException e) {
        		e.printStackTrace();
        	} catch (IOException e) {
				e.printStackTrace();
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
