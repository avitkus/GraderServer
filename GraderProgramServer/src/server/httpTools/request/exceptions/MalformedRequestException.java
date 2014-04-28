package server.httpTools.request.exceptions;

/**
 *
 * @author Andrew Vitkus
 */
public class MalformedRequestException extends Exception {

    /**
     * Creates a new instance of <code>MalformedRequestException</code> without
     * detail message.
     */
    public MalformedRequestException() {
    }

    /**
     * Constructs an instance of <code>MalformedRequestException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public MalformedRequestException(String msg) {
        super(msg);
    }
}
