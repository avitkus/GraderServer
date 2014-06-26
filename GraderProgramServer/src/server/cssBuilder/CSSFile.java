package server.cssBuilder;

import java.util.ArrayList;

import server.htmlBuilder.util.Offsetter;

/**
 * @author Andrew Vitkus
 *
 */
public class CSSFile implements ICSSFile {

    private ArrayList<IRule> rules;

    public CSSFile() {
        rules = new ArrayList<>();
    }

    @Override
    public void addRule(IRule rule) {
        rules.add(rule);
    }

    @Override
    public IRule[] getRules() {
        return rules.toArray(new IRule[rules.size()]);
    }

    @Override
    public String getCSS(int depth) {
        StringBuilder css = new StringBuilder();
        for (IRule rule : rules) {
            css.append(Offsetter.indent(depth)).append(rule.getCSS(depth)).append("\r\n");
        }
        return css.toString();
    }

}
