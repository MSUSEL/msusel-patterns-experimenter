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

import com.gargoylesoftware.htmlunit.html.HtmlFrameSet;

/**
 * Wrapper for the HTML element "frameset".
 *
 * @version $Revision: 5301 $
 * @author Bruce Chapman
 * @author Ahmed Ashour
 */
public class HTMLFrameSetElement extends HTMLElement {

    private static final long serialVersionUID = 5630843390548382869L;

    /**
     * Creates a new frameset instance.
     */
    public HTMLFrameSetElement() {
        // Empty.
    }

    /**
     * Sets the rows property.
     *
     * @param rows the rows attribute value
     */
    public void jsxSet_rows(final String rows) {
        final HtmlFrameSet htmlFrameSet = (HtmlFrameSet) getDomNodeOrNull();
        if (htmlFrameSet != null) {
            htmlFrameSet.setAttribute("rows", rows);
        }
    }

    /**
     * Gets the rows property.
     *
     * @return the rows attribute value
     */

    public String jsxGet_rows() {
        final HtmlFrameSet htmlFrameSet = (HtmlFrameSet) getDomNodeOrNull();
        return htmlFrameSet.getRowsAttribute();
    }

    /**
     * Sets the cols property.
     *
     * @param cols the cols attribute value
     */
    public void jsxSet_cols(final String cols) {
        final HtmlFrameSet htmlFrameSet = (HtmlFrameSet) getDomNodeOrNull();
        if (htmlFrameSet != null) {
            htmlFrameSet.setAttribute("cols", cols);
        }
    }

    /**
     * Gets the cols property.
     *
     * @return the cols attribute value
     */
    public String jsxGet_cols() {
        final HtmlFrameSet htmlFrameSet = (HtmlFrameSet) getDomNodeOrNull();
        return htmlFrameSet.getColsAttribute();
    }

    /**
     * Gets the "border" attribute.
     * @return the "border" attribute
     */
    public String jsxGet_border() {
        String border = getDomNodeOrDie().getAttribute("border");
        if (border == NOT_FOUND) {
            border = "";
        }
        return border;
    }

    /**
     * Sets the "border" attribute.
     * @param border the "border" attribute
     */
    public void jsxSet_border(final String border) {
        getDomNodeOrDie().setAttribute("border", border);
    }
}
