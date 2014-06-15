package server.htmlBuilder.util;

/**
 * This is is temporary, it needs to be integrated into IHTMLElement eventually.
 * 
 * @deprecated 
 * @author Andrew Vitkus
 */
public interface IEventful {
        public void setOnload(JavaScriptGenerator script);
        public JavaScriptGenerator getOnload();
        public void setOnchange(JavaScriptGenerator script);
        public JavaScriptGenerator getOnchange();
}
