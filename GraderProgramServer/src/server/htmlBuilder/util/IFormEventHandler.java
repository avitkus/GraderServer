package server.htmlBuilder.util;

/**
 * 
 * @author Andrew Vitkus
 */
public interface IFormEventHandler {

    public void setOnchange(ScriptGenerator script);

    public ScriptGenerator getOnchange();
}
