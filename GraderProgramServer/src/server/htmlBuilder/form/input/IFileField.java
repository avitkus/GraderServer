package server.htmlBuilder.form.input;

/**
 * @author Andrew Vitkus
 *
 */
public interface IFileField extends IInput {

    public void addAcceptType(String mime, String... mimes);

    public String[] getAcceptTypes();

    public void setValue(String value);

    public String getValue();
}
