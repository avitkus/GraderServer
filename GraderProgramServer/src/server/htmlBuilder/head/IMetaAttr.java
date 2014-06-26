package server.htmlBuilder.head;

/**
 * @author Andrew Vitkus
 *
 */
public interface IMetaAttr extends IHeadElement {

    public String[] getMetaAttributes();

    public void addMetaAttribute(String name, String value);
}
