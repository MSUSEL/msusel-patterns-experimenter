/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.gargoylesoftware.htmlunit.html;

import java.net.URL;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;

/**
 * Wrapper for the HTML element "meta".
 *
 * @version $Revision: 5726 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 */
public class HtmlMeta extends HtmlElement {

    private static final long serialVersionUID = 7408601325303605790L;

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "meta";

    /**
     * Creates an instance of HtmlMeta
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the HtmlPage that contains this element
     * @param attributes the initial attributes
     */
    HtmlMeta(final String namespaceURI, final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);

        if ("set-cookie".equalsIgnoreCase(getHttpEquivAttribute())) {
            performSetCookie();
        }
    }

    /**
     * Handles the cookies specified in meta tags,
     * like <tt>&lt;meta http-equiv='set-cookie' content='webm=none; path=/;'&gt;</tt>.
     */
    protected void performSetCookie() {
        final String[] parts = getContentAttribute().split("\\s*;\\s*");
        final String name = StringUtils.substringBefore(parts[0], "=");
        final String value = StringUtils.substringAfter(parts[0], "=");
        final URL url = getPage().getWebResponse().getWebRequest().getUrl();
        final String host = url.getHost();
        final boolean secure = "https".equals(url.getProtocol());
        String path = null;
        Date expires = null;
        for (int i = 1; i < parts.length; i++) {
            final String partName = StringUtils.substringBefore(parts[i], "=").trim().toLowerCase();
            final String partValue = StringUtils.substringAfter(parts[i], "=").trim();
            if ("path".equals(partName)) {
                path = partValue;
            }
            else if ("expires".equals(partName)) {
                expires = com.gargoylesoftware.htmlunit.util.StringUtils.parseHttpDate(partValue);
            }
            else {
                notifyIncorrectness("set-cookie http-equiv meta tag: unknown attribute '" + partName + "'.");
            }
        }
        final Cookie cookie = new Cookie(host, name, value, path, expires, secure);
        getPage().getWebClient().getCookieManager().addCookie(cookie);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean mayBeDisplayed() {
        return false;
    }

    /**
     * Returns the value of the attribute "http-equiv". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "http-equiv"
     * or an empty string if that attribute isn't defined.
     */
    public final String getHttpEquivAttribute() {
        return getAttribute("http-equiv");
    }

    /**
     * Returns the value of the attribute "name". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "name"
     * or an empty string if that attribute isn't defined.
     */
    public final String getNameAttribute() {
        return getAttribute("name");
    }

    /**
     * Returns the value of the attribute "content". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "content"
     * or an empty string if that attribute isn't defined.
     */
    public final String getContentAttribute() {
        return getAttribute("content");
    }

    /**
     * Returns the value of the attribute "scheme". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "scheme"
     * or an empty string if that attribute isn't defined.
     */
    public final String getSchemeAttribute() {
        return getAttribute("scheme");
    }
}
