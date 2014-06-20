package server.htmlBuilder.global;

import java.util.Arrays;
import java.util.Optional;
import server.htmlBuilder.util.AttributeManager;
import server.htmlBuilder.util.IAttributeManager;
import server.htmlBuilder.util.Offsetter;
import server.htmlBuilder.util.ScriptGenerator;

public class Script implements IScript {

    private ScriptGenerator script;

    private final IAttributeManager attrs;
    private String className;
    private String id;
    private boolean async;
    private boolean defer;
    private boolean external;

    public Script() {
        attrs = new AttributeManager();
        script = () -> "";
        className = "";
        id = "";
        async = false;
        defer = false;
        external = false;
    }

    @Override
    public void setScript(ScriptGenerator script) {
        external = false;
        this.script = script;
        script.type().ifPresent((type) -> setType(type));
    }

    @Override
    public ScriptGenerator getScript() {
        return script;
    }

    @Override
    public String getText(final int indent) {
        StringBuilder text = new StringBuilder(100);
        text.append(Offsetter.indent(indent)).append("<script");
        if (!className.isEmpty()) {
            text.append(" class=\"").append(className).append("\"");
        }
        if (!id.isEmpty()) {
            text.append(" id=\"").append(id).append("\"");
        }
        if (external && async) {
            text.append(" async");
        }
        if (external && defer) {
            text.append(" defer");
        }
        text.append(">\n");
        if (!external) {
            Arrays.stream(script.getText().split("\n")).forEach((line) -> text.append(Offsetter.indent(indent + 1)).append(line).append("\n"));
        }
        text.append(Offsetter.indent(indent - 1)).append("</script>");
        return text.toString();
    }

    @Override
    public String getTagType() {
        return "script";
    }

    @Override
    public void addAttribute(String name, String value) {
        attrs.addAttribute(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        attrs.removeAttribute(name);
    }

    @Override
    public String getAttribute(String name) {
        return attrs.getAttribute(name);
    }

    @Override
    public String[][] getAttributes() {
        return attrs.getAttributes();
    }

    @Override
    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public void setID(String id) {
        this.id = id;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public void setAsync(boolean async) {
        this.async = async;
    }

    @Override
    public boolean getAsync() {
        return async;
    }

    @Override
    public void setDefer(boolean defer) {
        this.defer = defer;
    }

    @Override
    public boolean getDefer() {
        return defer;
    }

    @Override
    public void setCharset(String charset) {
        attrs.addAttribute("charset", charset);
    }

    @Override
    public Optional<String> getCharset() {
        String charset = attrs.getAttribute("charset");
        if (charset == null) {
            return Optional.empty();
        } else {
            return Optional.of(charset);
        }
    }

    @Override
    public void setSource(String src) {
        external = true;
        attrs.addAttribute("src", src);
    }

    @Override
    public Optional<String> getSource() {
        String charset = attrs.getAttribute("src");
        if (charset == null) {
            return Optional.empty();
        } else {
            return Optional.of(charset);
        }
    }

    @Override
    public void setType(String mime) {
        attrs.addAttribute("type", mime);
    }

    @Override
    public Optional<String> getType() {
        String charset = attrs.getAttribute("type");
        if (charset == null) {
            return Optional.empty();
        } else {
            return Optional.of(charset);
        }
    }
}
