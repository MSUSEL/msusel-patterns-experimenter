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
package com.gargoylesoftware.htmlunit.javascript.host.html;

/**
 * The JavaScript object "HTMLMetaElement".
 *
 * @version $Revision: 5301 $
 * @author Ahmed Ashour
 */
public class HTMLMetaElement extends HTMLElement {

    private static final long serialVersionUID = 2709850253223688489L;

    /**
     * Creates an instance.
     */
    public HTMLMetaElement() {
        // Empty.
    }

    /**
     * Returns "charset" attribute.
     * @return the charset attribute
     */
    public String jsxGet_charset() {
        return "";
    }

    /**
     * Sets the "charset" attribute.
     * @param charset the charset attribute
     */
    public void jsxSet_charset(final String charset) {
        //empty
    }

    /**
     * Returns "content" attribute.
     * @return the content attribute
     */
    public String jsxGet_content() {
        return getDomNodeOrDie().getAttribute("content");
    }

    /**
     * Sets the "content" attribute.
     * @param content the content attribute
     */
    public void jsxSet_content(final String content) {
        getDomNodeOrDie().setAttribute("content", content);
    }

    /**
     * Returns "http-equiv" attribute.
     * @return the http-equiv attribute
     */
    public String jsxGet_httpEquiv() {
        return getDomNodeOrDie().getAttribute("http-equiv");
    }

    /**
     * Sets the "http-equiv" attribute.
     * @param httpEquiv the http-equiv attribute
     */
    public void jsxSet_httpEquiv(final String httpEquiv) {
        getDomNodeOrDie().setAttribute("http-equiv", httpEquiv);
    }

    /**
     * Returns "name" attribute.
     * @return the name attribute
     */
    public String jsxGet_name() {
        return getDomNodeOrDie().getAttribute("name");
    }

    /**
     * Sets the "name" attribute.
     * @param name the name attribute
     */
    public void jsxSet_name(final String name) {
        getDomNodeOrDie().setAttribute("name", name);
    }

    /**
     * Returns "scheme" attribute.
     * @return the scheme attribute
     */
    public String jsxGet_scheme() {
        return getDomNodeOrDie().getAttribute("scheme");
    }

    /**
     * Sets the "scheme" attribute.
     * @param scheme the scheme attribute
     */
    public void jsxSet_scheme(final String scheme) {
        getDomNodeOrDie().setAttribute("scheme", scheme);
    }

    /**
     * Returns "url" attribute.
     * @return the url attribute
     */
    public String jsxGet_url() {
        return "";
    }

    /**
     * Sets the "url" attribute.
     * @param url the url attribute
     */
    public void jsxSet_url(final String url) {
        //empty
    }

}
