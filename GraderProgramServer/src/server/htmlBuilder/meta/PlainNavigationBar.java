package server.htmlBuilder.meta;

import java.util.ArrayList;
import server.htmlBuilder.attributes.LinkTarget;
import server.htmlBuilder.body.Hyperlink;
import server.htmlBuilder.body.IBodyElement;
import server.htmlBuilder.body.IHyperlink;
import server.htmlBuilder.body.INavigation;
import server.htmlBuilder.body.Navigation;
import server.htmlBuilder.body.Text;
import server.htmlBuilder.util.AttributeManager;
import server.htmlBuilder.util.IAttributeManager;

/**
 *
 * @author Andrew Vitkus
 */
public class PlainNavigationBar implements INavigationBar {
    private final INavigation nav;
    private final IAttributeManager attrs;
    private String className;
    private String id;
    private String separator;

    public PlainNavigationBar(IHyperlink... elements) {
        nav = new Navigation(elements);
        attrs = new AttributeManager();
        className = "";
        id = "";
    }
    
    @Override
    public void addLink(String display, String dest) {
        addLink(display, dest, LinkTarget.PARENT);
    }
    
    @Override
    public void addLink(String display, String dest, LinkTarget target) {
        IHyperlink link = new Hyperlink();
        link.addContent(new Text(display));
        link.setURL(dest);
        link.setTarget(target);
        
        addLink(link);
    }

    @Override
    public void addLink(IHyperlink link) {
        nav.addContent(link);
    }

    @Override
    public IHyperlink[] getLinks() {
        ArrayList<IHyperlink> links = new ArrayList<>(5);
        for(IBodyElement element : nav.getContents()) {
            if (element instanceof IHyperlink) {
                links.add((IHyperlink) element);
            }
        }
        return links.toArray(new IHyperlink[links.size()]);
    }

    @Override
    public void setSeparator(String separator) {
        this.separator = separator;
    }

    @Override
    public String getSeparator() {
        return separator;
    }

    @Override
    public String getText(int indent) {
        return nav.getText(indent);
    }

    @Override
    public String getTagType() {
        return "meta - nav navbar";
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
}
