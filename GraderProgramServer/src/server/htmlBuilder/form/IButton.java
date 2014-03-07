package server.htmlBuilder.form;

/**
 * @author Andrew Vitkus
 *
 */
public interface IButton extends IInput {
	public void setName(String name);
	public String getName();
	public void setValue(String value);
	public String getValue();
}
