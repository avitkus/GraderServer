package server;

import java.io.IOException;

import javax.net.ssl.SSLSocket;

/**
 * @author Andrew Vitkus
 *
 */
public interface IHandlerChooser {
	public Runnable getHandler(SSLSocket socket) throws IOException;
}
