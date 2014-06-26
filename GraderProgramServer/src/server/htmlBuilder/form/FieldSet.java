package server.htmlBuilder.form;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import server.htmlBuilder.IHTMLElement;
import server.htmlBuilder.util.AttributeManager;
import server.htmlBuilder.util.IAttributeManager;
import server.htmlBuilder.util.Offsetter;

public class FieldSet implements IFieldSet {

    private final IAttributeManager attrs;
    private final ArrayList<IHTMLElement> fields;
    private String className;
    private String id;
    private Optional<String> legend;

    /**
     *
     */
    public FieldSet() {
        attrs = new AttributeManager();
        fields = new ArrayList<>(5);
        className = "";
        id = "";
        legend = Optional.empty();
    }

    @Override
    public void addField(IHTMLElement field) {
        fields.add(field);
    }

    @Override
    public IHTMLElement[] getFields() {
        return fields.toArray(new IHTMLElement[fields.size()]);
    }

    @Override
    public String getText(int depth) {
        StringBuilder html = new StringBuilder(50);

        html.append(Offsetter.indent(depth++)).append("<fieldset");
        if (!className.isEmpty()) {
            html.append(" class=\"").append(className).append("\"");
        }
        if (!id.isEmpty()) {
            html.append(" id=\"").append(id).append("\"");
        }
        html.append(attrs.getHTML()).append(">\n");
        if (legend.isPresent()) {
            html.append(Offsetter.indent(depth)).append("<legend>\n");
            html.append(Offsetter.indent(depth + 1)).append(legend.get()).append("\n");
            html.append(Offsetter.indent(depth)).append("</legend>\n");
        }
        for (IHTMLElement option : fields) {
            html.append(option.getText(depth)).append("\n");
        }
        html.append(Offsetter.indent(depth - 1)).append("</fieldset>");

        return html.toString();
    }

    @Override
    public String getTagType() {
        return "fieldset";
    }

    @Override
    public void setDisabled(boolean disable) {
        if (disable) {
            attrs.addAttribute("disabled", null);
        } else {
            attrs.removeAttribute("disabled");
        }
    }

    @Override
    public boolean getDisabled() {
        return attrs.hasAttribute("diabled");
    }

    @Override
    public void setForm(String form) {
        attrs.addAttribute("form", form);
    }

    @Override
    public String getForm() {
        return attrs.getAttribute("form");
    }

    @Override
    public void setName(String name) {
        attrs.addAttribute("name", name);
    }

    @Override
    public String getName() {
        return attrs.getAttribute("name");
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
    public void setLegend(String legend) {
        Objects.requireNonNull(legend, "The legend value cannot be null.");
        this.legend = Optional.of(legend);
    }

    @Override
    public String getLegend() {
        return legend.orElse("");
    }
}
