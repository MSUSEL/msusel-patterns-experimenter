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

import net.sourceforge.htmlunit.corejs.javascript.Context;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;

/**
 * The JavaScript object "HTMLTableColElement".
 *
 * @version $Revision: 5864 $
 * @author Ahmed Ashour
 */
public class HTMLTableColElement extends HTMLTableComponent {

    private static final long serialVersionUID = -886020322532732229L;

    /**
     * Creates an instance.
     */
    public HTMLTableColElement() {
        // Empty.
    }

    /**
     * Returns the value of the "span" property.
     * @return the value of the "span" property
     */
    public int jsxGet_span() {
        final String span = getDomNodeOrDie().getAttribute("span");
        int i;
        try {
            i = Integer.parseInt(span);
            if (i < 1) {
                i = 1;
            }
        }
        catch (final NumberFormatException e) {
            i = 1;
        }
        return i;
    }

    /**
     * Sets the value of the "span" property.
     * @param span the value of the "span" property
     */
    public void jsxSet_span(final Object span) {
        final Double d = Context.toNumber(span);
        Integer i = d.intValue();
        if (i < 1) {
            if (getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_102)) {
                final Exception e = new Exception("Cannot set the span property to invalid value: " + span);
                Context.throwAsScriptRuntimeEx(e);
            }
            else {
                i = 1;
            }
        }
        getDomNodeOrDie().setAttribute("span", i.toString());
    }

    /**
     * Returns the value of the "width" property.
     * @return the value of the "width" property
     */
    public String jsxGet_width() {
        final boolean ie = getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_103);
        final Boolean returnNegativeValues = ie ? false : null;
        return getWidthOrHeight("width", returnNegativeValues);
    }

    /**
     * Sets the value of the "width" property.
     * @param width the value of the "width" property
     */
    public void jsxSet_width(final Object width) {
        setWidthOrHeight("width", (width == null ? "" : Context.toString(width)), false);
    }

}
