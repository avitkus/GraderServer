package server.htmlBuilder.event;

import server.htmlBuilder.util.ScriptGenerator;

/**
 *
 * @author Andrew Vitkus
 */
public interface IWindowEventHandler {
    
    public void setOnload(ScriptGenerator script);

    public ScriptGenerator getOnload();
}
