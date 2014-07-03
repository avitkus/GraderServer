package edu.unc.cs.graderServer.cookies;

import java.util.Date;

/**
 * @author Andrew Vitkus
 *
 */
public interface ICookie {

    public void setName(String name);

    public String getName();

    public void setValue(String value);

    public String getValue();

    public void setDomain(String domain);

    public String getDomain();

    public void setPath(String path);

    public String getPath();

    public void setMaxAge(int seconds);

    public int getMaxAge();

    public void setExpiration(Date expiration);

    public Date getExpiration();

    public void setSecure(boolean secure);

    public boolean getSecure();

    public void setHTTPOnly(boolean httpOnly);

    public boolean HTTPOnly();

    public String getText();
}
