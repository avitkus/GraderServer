package server.cssBuilder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import server.htmlBuilder.util.Offsetter;

/**
 * @author Andrew Vitkus
 *
 */
public class PropertyManager implements IPropertyManager {

    private HashMap<String, String> props;

    public PropertyManager() {
        props = new HashMap<>();
    }

    @Override
    public void addProperty(String name, String value) {
        props.put(name, value);
    }

    @Override
    public String[][] getProperties() {
        String[][] propArr = new String[props.size()][2];
        Iterator<Entry<String, String>> propIter = props.entrySet().iterator();
        for (int i = 0; i < props.size(); i++) {
            Entry<String, String> entry = propIter.next();
            propArr[i][0] = entry.getKey();
            propArr[i][1] = entry.getValue();
        }
        return propArr;
    }

    @Override
    public String getCSS(int indent) {
        StringBuilder css = new StringBuilder();
        for (Entry<String, String> property : props.entrySet()) {
            css.append(Offsetter.indent(indent)).append(property.getKey());
            css.append(":").append(property.getValue()).append(";\r\n");
        }
        return css.toString();
    }

}
