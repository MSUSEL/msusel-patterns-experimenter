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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import com.gargoylesoftware.htmlunit.javascript.host.MediaList;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;

/**
 * A JavaScript object for a CSSImportRule.
 *
 * @version $Revision: 5726 $
 * @author Daniel Gredler
 */
public class CSSImportRule extends CSSRule {

    private static final long serialVersionUID = -3352769444872087531L;

    private MediaList media_;
    private CSSStyleSheet importedStylesheet_;

    /**
     * Creates a new instance. JavaScript objects must have a default constructor.
     */
    @Deprecated
    public CSSImportRule() {
        // Empty.
    }

    /**
     * Creates a new instance.
     * @param stylesheet the Stylesheet of this rule.
     * @param rule the wrapped rule
     */
    protected CSSImportRule(final CSSStyleSheet stylesheet, final org.w3c.dom.css.CSSRule rule) {
        super(stylesheet, rule);
    }

    /**
     * Returns the URL of the imported style sheet.
     * @return the URL of the imported style sheet
     */
    public String jsxGet_href() {
        return getImportRule().getHref();
    }

    /**
     * Returns the media types that the imported CSS style sheet applies to.
     * @return the media types that the imported CSS style sheet applies to
     */
    public MediaList jsxGet_media() {
        if (media_ == null) {
            final CSSStyleSheet parent = jsxGet_parentStyleSheet();
            final org.w3c.dom.stylesheets.MediaList ml = getImportRule().getMedia();
            media_ = new MediaList(parent, ml);
        }
        return media_;
    }

    /**
     * Returns the style sheet referred to by this rule.
     * @return the style sheet referred to by this rule
     */
    public CSSStyleSheet jsxGet_styleSheet() {
        if (importedStylesheet_ == null) {
            final CSSStyleSheet owningSheet = jsxGet_parentStyleSheet();
            final HTMLElement ownerNode = owningSheet.jsxGet_ownerNode();
            final org.w3c.dom.css.CSSStyleSheet importedStylesheet = getImportRule().getStyleSheet();
            importedStylesheet_ = new CSSStyleSheet(ownerNode, importedStylesheet, owningSheet.getUri());
        }
        return importedStylesheet_;
    }

    /**
     * Returns the wrapped rule, as an import rule.
     * @return the wrapped rule, as an import rule
     */
    private org.w3c.dom.css.CSSImportRule getImportRule() {
        return (org.w3c.dom.css.CSSImportRule) getRule();
    }

}
