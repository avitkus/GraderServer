package server.htmlBuilder.body;

import server.htmlBuilder.util.IColorable;

/**
 * @author Andrew Vitkus
 *
 */
public interface IHeader extends IBodyElement, IColorable {

    public void setText(String text);

    public void setText(IText text);

    public String getText();

    public int getLevel();

    public void setLevel(int level);
}
