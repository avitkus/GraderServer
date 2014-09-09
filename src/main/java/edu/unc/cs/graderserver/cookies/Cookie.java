package edu.unc.cs.graderServer.cookies;

import java.text.DateFormat;
import java.util.Date;

/**
 * @author Andrew Vitkus
 *
 */
public class Cookie implements ICookie {

    private String name;
    private String value;
    private int maxAge;
    private Date expiration;
    private String path;
    private String domain;
    private boolean secure;
    private boolean httpOnly;

    public Cookie() {
        name = null;
        value = null;
        maxAge = -1;
        expiration = null;
        path = null;
        domain = null;
        secure = false;
        httpOnly = false;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public String getDomain() {
        return domain;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void setMaxAge(int seconds) {
        this.maxAge = Math.max(-1, seconds);
    }

    @Override
    public int getMaxAge() {
        return maxAge;
    }

    @Override
    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    @Override
    public Date getExpiration() {
        return expiration;
    }

    @Override
    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    @Override
    public boolean getSecure() {
        return secure;
    }

    @Override
    public void setHTTPOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    @Override
    public boolean HTTPOnly() {
        return httpOnly;
    }

    @Override
    public String getText() {
        StringBuilder text = new StringBuilder();

        text.append("Set-Cookie: ").append(name).append("=").append(value);
        if (expiration != null) {
            text.append("; Expires=").append(DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(expiration));
        }
        if (maxAge != -1) {
            text.append("; Max-Age=").append(maxAge);
        }
        if (domain != null) {
            text.append("; Domain=").append(domain);
        }
        if (path != null) {
            text.append("; Path=").append(path);
        }
        if (secure) {
            text.append("; Secure");
        }
        if (httpOnly) {
            text.append("; HttpOnly");
        }

        return text.toString();
    }

}
