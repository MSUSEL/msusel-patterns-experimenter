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

import java.io.StringReader;

import org.w3c.css.sac.InputSource;

import com.gargoylesoftware.htmlunit.Cache;
import com.gargoylesoftware.htmlunit.html.HtmlStyle;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet;

/**
 * The JavaScript object "HTMLStyleElement".
 *
 * @version $Revision: 5658 $
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public class HTMLStyleElement extends HTMLElement {

    private static final long serialVersionUID = 944381786297995169L;

    private CSSStyleSheet sheet_;

    /**
     * Creates an instance.
     */
    public HTMLStyleElement() {
        // Empty.
    }

    /**
     * Gets the associated sheet.
     * @see <a href="http://www.xulplanet.com/references/objref/HTMLStyleElement.html">Mozilla doc</a>
     * @return the sheet
     */
    public CSSStyleSheet jsxGet_sheet() {
        if (sheet_ != null) {
            return sheet_;
        }

        final HtmlStyle style = (HtmlStyle) getDomNodeOrDie();
        final String css = style.getTextContent();

        final Cache cache = getWindow().getWebWindow().getWebClient().getCache();
        final org.w3c.dom.css.CSSStyleSheet cached = cache.getCachedStyleSheet(css);
        final String uri = getDomNodeOrDie().getPage().getWebResponse().getWebRequest()
        .getUrl().toExternalForm();
        if (cached != null) {
            sheet_ = new CSSStyleSheet(this, cached, uri);
        }
        else {
            final InputSource source = new InputSource(new StringReader(css));
            sheet_ = new CSSStyleSheet(this, source, uri);
            cache.cache(css, sheet_.getWrappedSheet());
        }

        return sheet_;
    }

    /**
     * Gets the associated sheet (IE).
     * @return the sheet
     */
    public CSSStyleSheet jsxGet_styleSheet() {
        return jsxGet_sheet();
    }
}
