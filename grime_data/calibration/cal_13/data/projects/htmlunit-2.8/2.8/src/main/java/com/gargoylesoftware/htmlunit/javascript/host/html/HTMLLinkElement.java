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

import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet;

/**
 * The JavaScript object "HTMLLinkElement".
 *
 * @version $Revision: 5618 $
 * @author Ahmed Ashour
 */
public class HTMLLinkElement extends HTMLElement {

    private static final long serialVersionUID = -6381573516360300401L;

    /**
     * The associated style sheet (only valid for links of type
     * <tt>&lt;link rel="stylesheet" type="text/css" href="..." /&gt;</tt>).
     */
    private CSSStyleSheet sheet_;

    /**
     * Creates an instance.
     */
    public HTMLLinkElement() {
        // Empty.
    }

    /**
     * Sets the href property.
     * @param href href attribute value
     */
    public void jsxSet_href(final String href) {
        getDomNodeOrDie().setAttribute("href", href);
    }

    /**
     * Returns the value of the href property.
     * @return the href property
     * @throws Exception if an error occurs
     */
    public String jsxGet_href() throws Exception {
        final HtmlLink link = (HtmlLink) getDomNodeOrDie();
        final String href = link.getHrefAttribute();
        if (href.length() == 0) {
            return href;
        }
        try {
            return ((HtmlPage) link.getPage()).getFullyQualifiedUrl(href).toString();
        }
        catch (final MalformedURLException e) {
            return href;
        }
    }

    /**
     * Sets the rel property.
     * @param rel rel attribute value
     */
    public void jsxSet_rel(final String rel) {
        getDomNodeOrDie().setAttribute("rel", rel);
    }

    /**
     * Returns the value of the rel property.
     * @return the rel property
     * @throws Exception if an error occurs
     */
    public String jsxGet_rel() throws Exception {
        return ((HtmlLink) getDomNodeOrDie()).getRelAttribute();
    }

    /**
     * Sets the type property.
     * @param type type attribute value
     */
    public void jsxSet_type(final String type) {
        getDomNodeOrDie().setAttribute("type", type);
    }

    /**
     * Returns the value of the type property.
     * @return the type property
     * @throws Exception if an error occurs
     */
    public String jsxGet_type() throws Exception {
        return ((HtmlLink) getDomNodeOrDie()).getTypeAttribute();
    }

    /**
     * Returns the associated style sheet (only valid for links of type
     * <tt>&lt;link rel="stylesheet" type="text/css" href="..." /&gt;</tt>).
     * @return the associated style sheet
     */
    public CSSStyleSheet getSheet() {
        if (sheet_ == null) {
            sheet_ = CSSStyleSheet.loadStylesheet(getWindow(), this, (HtmlLink) getDomNodeOrDie(), null);
        }
        return sheet_;
    }

}
