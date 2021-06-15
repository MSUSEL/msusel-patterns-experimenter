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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.TextUtil;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.protocol.javascript.JavaScriptURLConnection;

/**
 * Wrapper for the HTML element "a".
 *
 * @version $Revision: 5742 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 * @author Dmitri Zoubkov
 */
public class HtmlAnchor extends HtmlElement {

    private static final long serialVersionUID = 7968778206454737178L;
    private static final Log LOG = LogFactory.getLog(HtmlAnchor.class);

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "a";

    /**
     * Creates a new instance.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlAnchor(final String namespaceURI, final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
    }

    /**
     * Same as {@link #doClickAction()}, except that it accepts an href suffix, needed when a click is
     * performed on an image map to pass information on the click position.
     *
     * @param hrefSuffix the suffix to add to the anchor's href attribute (for instance coordinates from an image map)
     * @throws IOException if an IO error occurs
     */
    protected void doClickAction(final String hrefSuffix) throws IOException {
        final String href = getHrefAttribute() + hrefSuffix;
        if (LOG.isDebugEnabled()) {
            final String w = getPage().getEnclosingWindow().getName();
            LOG.debug("do click action in window '" + w + "', using href '" + href + "'");
        }
        if (href.length() == 0) {
            return;
        }
        final HtmlPage page = (HtmlPage) getPage();
        if (TextUtil.startsWithIgnoreCase(href, JavaScriptURLConnection.JAVASCRIPT_PREFIX)) {
            final StringBuilder builder = new StringBuilder(href.length());
            for (int i = 0; i < href.length(); i++) {
                final char ch = href.charAt(i);
                if (ch == '%' && i + 2 < href.length()) {
                    final char ch1 = Character.toUpperCase(href.charAt(i + 1));
                    final char ch2 = Character.toUpperCase(href.charAt(i + 2));
                    if ((Character.isDigit(ch1) || ch1 >= 'A' && ch1 <= 'F')
                            && (Character.isDigit(ch2) || ch2 >= 'A' && ch2 <= 'F')) {
                        builder.append((char) Integer.parseInt(href.substring(i + 1, i + 3), 16));
                        i += 2;
                        continue;
                    }
                }
                builder.append(ch);
            }
            page.executeJavaScriptIfPossible(builder.toString(), "javascript url", getStartLineNumber());
            return;
        }
        final URL url = page.getFullyQualifiedUrl(href);
        final WebRequest webRequest = new WebRequest(url);
        webRequest.setAdditionalHeader("Referer", page.getWebResponse().getWebRequest().getUrl().toExternalForm());
        if (LOG.isDebugEnabled()) {
            LOG.debug(
                    "Getting page for " + url.toExternalForm()
                    + ", derived from href '" + href
                    + "', using the originating URL "
                    + page.getWebResponse().getWebRequest().getUrl());
        }
        page.getWebClient().download(page.getEnclosingWindow(),
                page.getResolvedTarget(getTargetAttribute()),
                webRequest, "Link click");
    }

    /**
     * This method will be called if there either wasn't an <tt>onclick</tt> handler, or
     * there was but the result of that handler was <tt>true</tt>. This is the default
     * behavior of clicking the element. For this anchor element, the default behavior is
     * to open the HREF page, or execute the HREF if it is a <tt>javascript:</tt> URL.
     *
     * @throws IOException if an IO error occurs
     */
    @Override
    protected void doClickAction() throws IOException {
        doClickAction("");
    }

    /**
     * Returns the value of the attribute "charset". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "charset" or an empty string if that attribute isn't defined
     */
    public final String getCharsetAttribute() {
        return getAttribute("charset");
    }

    /**
     * Returns the value of the attribute "type". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "type" or an empty string if that attribute isn't defined
     */
    public final String getTypeAttribute() {
        return getAttribute("type");
    }

    /**
     * Returns the value of the attribute "name". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "name" or an empty string if that attribute isn't defined
     */
    public final String getNameAttribute() {
        return getAttribute("name");
    }

    /**
     * Returns the value of the attribute "href". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "href" or an empty string if that attribute isn't defined
     */
    public final String getHrefAttribute() {
        return getAttribute("href").trim();
    }

    /**
     * Returns the value of the attribute "hreflang". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "hreflang" or an empty string if that attribute isn't defined
     */
    public final String getHrefLangAttribute() {
        return getAttribute("hreflang");
    }

    /**
     * Returns the value of the attribute "rel". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "rel" or an empty string if that attribute isn't defined
     */
    public final String getRelAttribute() {
        return getAttribute("rel");
    }

    /**
     * Returns the value of the attribute "rev". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "rev" or an empty string if that attribute isn't defined
     */
    public final String getRevAttribute() {
        return getAttribute("rev");
    }

    /**
     * Returns the value of the attribute "accesskey". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "accesskey" or an empty string if that attribute isn't defined
     */
    public final String getAccessKeyAttribute() {
        return getAttribute("accesskey");
    }

    /**
     * Returns the value of the attribute "shape". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "shape" or an empty string if that attribute isn't defined
     */
    public final String getShapeAttribute() {
        return getAttribute("shape");
    }

    /**
     * Returns the value of the attribute "coords". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "coords" or an empty string if that attribute isn't defined
     */
    public final String getCoordsAttribute() {
        return getAttribute("coords");
    }

    /**
     * Returns the value of the attribute "tabindex". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "tabindex" or an empty string if that attribute isn't defined
     */
    public final String getTabIndexAttribute() {
        return getAttribute("tabindex");
    }

    /**
     * Returns the value of the attribute "onfocus". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onfocus" or an empty string if that attribute isn't defined
     */
    public final String getOnFocusAttribute() {
        return getAttribute("onfocus");
    }

    /**
     * Returns the value of the attribute "onblur". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onblur" or an empty string if that attribute isn't defined
     */
    public final String getOnBlurAttribute() {
        return getAttribute("onblur");
    }

    /**
     * Returns the value of the attribute "target". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "target" or an empty string if that attribute isn't defined
     */
    public final String getTargetAttribute() {
        return getAttribute("target");
    }

    /**
     * Open this link in a new window, much as web browsers do when you shift-click a link or use the context
     * menu to open in a new window.
     *
     * It should be noted that even web browsers will sometimes not give the expected result when using this
     * method of following links. Links that have no real href and rely on JavaScript to do their work will
     * fail.
     *
     * @return the page opened by this link, nested in a new {@link com.gargoylesoftware.htmlunit.TopLevelWindow}
     * @throws MalformedURLException if the href could not be converted to a valid URL
     */
    public final Page openLinkInNewWindow() throws MalformedURLException {
        final URL target = ((HtmlPage) getPage()).getFullyQualifiedUrl(getHrefAttribute());
        final String windowName = "HtmlAnchor.openLinkInNewWindow() target";
        final WebWindow newWindow = getPage().getWebClient().openWindow(target, windowName);
        return newWindow.getEnclosedPage();
    }
}
