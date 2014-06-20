package server.htmlBuilder.util;

/**
 * This is is temporary, it needs to be integrated into IHTMLElement eventually.
 * 
 * @deprecated 
 * @author Andrew Vitkus
 */
public interface IEventful {
        public void setOnload(ScriptGenerator script);
        public ScriptGenerator getOnload();
        public void setOnchange(ScriptGenerator script);
        public ScriptGenerator getOnchange();
}
