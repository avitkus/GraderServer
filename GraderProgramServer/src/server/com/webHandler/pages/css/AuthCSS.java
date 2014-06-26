package server.com.webHandler.pages.css;

import server.cssBuilder.CSSFile;
import server.cssBuilder.IRule;
import server.cssBuilder.Rule;

/**
 * @author Andrew Vitkus
 *
 */
public class AuthCSS extends CSSFile implements IAuthCSS {

    public AuthCSS() {
        buildCSS();
    }

    private void buildCSS() {
        addRule(buildH1Rule());
        addRule(buildFailNoticeRule());
        addRule(buildCenterRule());
        addRule(buildBodyRule());
        addRule(buildFormLabelRule());
        addRule(buildFormFieldRule());
        addRule(buildAuthTableRule());
    }

    private IRule buildH1Rule() {
        IRule h1Rule = new Rule();
        h1Rule.setSelector("h1");
        h1Rule.addProperty("text-align", "center");
        return h1Rule;
    }

    private IRule buildFailNoticeRule() {
        IRule failNoticeRule = new Rule();
        failNoticeRule.setSelector("fail-notice");
        failNoticeRule.addProperty("text-align", "center");
        failNoticeRule.addProperty("color", "red");
        return failNoticeRule;
    }

    private IRule buildCenterRule() {
        IRule centerRule = new Rule();
        centerRule.setSelector(".center");
        centerRule.addProperty("text-align", "center");
        return centerRule;
    }

    private IRule buildBodyRule() {
        IRule centerRule = new Rule();
        centerRule.setSelector("body");
        centerRule.addProperty("width", "90%");
        centerRule.addProperty("margin-left", "auto");
        centerRule.addProperty("margin-right", "auto");
        return centerRule;
    }

    private IRule buildFormLabelRule() {
        IRule formLabelRule = new Rule();
        formLabelRule.setSelector(".form-label");
        formLabelRule.addProperty("align", "right");
        return formLabelRule;
    }

    private IRule buildFormFieldRule() {
        IRule formLabelRule = new Rule();
        formLabelRule.setSelector(".form-field");
        formLabelRule.addProperty("align", "left");
        return formLabelRule;
    }

    private IRule buildAuthTableRule() {
        IRule formLabelRule = new Rule();
        formLabelRule.setSelector(".auth-table");
        formLabelRule.addProperty("margin-left", "auto");
        formLabelRule.addProperty("margin-right", "auto");
        formLabelRule.addProperty("border", "2px solid white");
        return formLabelRule;
    }
}
