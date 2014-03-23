package server.com.graderHandler;

/**
 * @author Andrew Vitkus
 *
 */
public interface IResponseWriter {

    public String getResponse();

    public void setAssignmentName(String name);
}
