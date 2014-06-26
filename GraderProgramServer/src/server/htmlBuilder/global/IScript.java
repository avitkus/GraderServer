package server.htmlBuilder.global;

import java.util.Optional;
import server.htmlBuilder.util.ScriptGenerator;

/**
 *
 * @author Andrew Vitkus
 */
public interface IScript extends IGlobalElement {

    public void setScript(ScriptGenerator script);

    public ScriptGenerator getScript();

    public void setAsync(boolean async);

    public boolean getAsync();

    public void setDefer(boolean defer);

    public boolean getDefer();

    public void setCharset(String charset);

    public Optional<String> getCharset();

    public void setSource(String src);

    public Optional<String> getSource();

    public void setType(String mime);

    public Optional<String> getType();
}
