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

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;

/**
 * Wrapper for the HTML element "link". <b>Note:</b> This is not a clickable link,
 * that one is an HtmlAnchor
 *
 * @version $Revision: 5661 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public class HtmlLink extends HtmlElement {

    private static final long serialVersionUID = 323745155296983364L;

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "link";
    private WebResponse cachedWebResponse_;

    /**
     * Creates an instance of HtmlLink
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the HtmlPage that contains this element
     * @param attributes the initial attributes
     */
    HtmlLink(final String namespaceURI, final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
    }

    /**
     * Returns the value of the attribute "charset". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "charset"
     * or an empty string if that attribute isn't defined.
     */
    public final String getCharsetAttribute() {
        return getAttribute("charset");
    }

    /**
     * Returns the value of the attribute "href". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "href"
     * or an empty string if that attribute isn't defined.
     */
    public final String getHrefAttribute() {
        return getAttribute("href");
    }

    /**
     * Returns the value of the attribute "hreflang". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "hreflang"
     * or an empty string if that attribute isn't defined.
     */
    public final String getHrefLangAttribute() {
        return getAttribute("hreflang");
    }

    /**
     * Returns the value of the attribute "type". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "type"
     * or an empty string if that attribute isn't defined.
     */
    public final String getTypeAttribute() {
        return getAttribute("type");
    }

    /**
     * Returns the value of the attribute "rel". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "rel"
     * or an empty string if that attribute isn't defined.
     */
    public final String getRelAttribute() {
        return getAttribute("rel");
    }

    /**
     * Returns the value of the attribute "rev". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "rev"
     * or an empty string if that attribute isn't defined.
     */
    public final String getRevAttribute() {
        return getAttribute("rev");
    }

    /**
     * Returns the value of the attribute "media". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "media"
     * or an empty string if that attribute isn't defined.
     */
    public final String getMediaAttribute() {
        return getAttribute("media");
    }

    /**
     * Returns the value of the attribute "target". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "target"
     * or an empty string if that attribute isn't defined.
     */
    public final String getTargetAttribute() {
        return getAttribute("target");
    }

    /**
     * <span style="color:red">POTENIAL PERFORMANCE KILLER - DOWNLOADS THE RESOURCE - USE AT YOUR OWN RISK.</span><br/>
     * If the linked content is not already downloaded it triggers a download. Then it stores the response
     * for later use.<br/>
     *
     * @param downloadIfNeeded indicates if a request should be performed this hasn't been done previously
     * @return <code>null</code> if no download should be performed and when this wasn't already done; the response
     * received when performing a request for the content referenced by this tag otherwise
     * @throws IOException if an error occurs while downloading the content
     */
    public WebResponse getWebResponse(final boolean downloadIfNeeded) throws IOException {
        if (downloadIfNeeded && cachedWebResponse_ == null) {
            final WebClient webclient = getPage().getWebClient();
            cachedWebResponse_ = webclient.loadWebResponse(getWebRequest());
        }
        return cachedWebResponse_;
    }

    /**
     * Returns the request settings which will allow us to retrieve the content referenced by the "href" attribute.
     * @return the request settings which will allow us to retrieve the content referenced by the "href" attribute
     * @throws MalformedURLException in case of problem resolving the URL
     * @deprecated as of 2.8, please use {@link #getWebRequest()} instead
     */
    @Deprecated
    public WebRequest getWebRequestSettings() throws MalformedURLException {
        return getWebRequest();
    }

    /**
     * Returns the request which will allow us to retrieve the content referenced by the "href" attribute.
     * @return the request which will allow us to retrieve the content referenced by the "href" attribute
     * @throws MalformedURLException in case of problem resolving the URL
     */
    public WebRequest getWebRequest() throws MalformedURLException {
        final HtmlPage page = (HtmlPage) getPage();
        final URL url = page.getFullyQualifiedUrl(getHrefAttribute());
        final WebRequest request = new WebRequest(url);
        request.setAdditionalHeader("Referer", page.getWebResponse().getWebRequest().getUrl().toExternalForm());
        return request;
    }
}
