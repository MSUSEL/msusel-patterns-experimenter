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

import org.apache.commons.lang.ArrayUtils;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;

/**
 * The JavaScript object "HTMLBRElement".
 *
 * @version $Revision: 5864 $
 * @author Ahmed Ashour
 */
public class HTMLBRElement extends HTMLElement {

    private static final long serialVersionUID = -3785200238092986918L;

    /** Valid values for the {@link #jsxGet_clear() clear} property. */
    private static final String[] VALID_CLEAR_VALUES = new String[] {"left", "right", "all", "none"};

    /**
     * Creates an instance.
     */
    public HTMLBRElement() {
        // Empty.
    }

    /**
     * Returns the value of the <tt>clear</tt> property.
     * @return the value of the <tt>clear</tt> property
     */
    public String jsxGet_clear() {
        final String clear = getDomNodeOrDie().getAttribute("clear");
        if (!ArrayUtils.contains(VALID_CLEAR_VALUES, clear)
                && getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_42)) {
            return "";
        }
        return clear;
    }

    /**
     * Sets the value of the <tt>clear</tt> property.
     * @param clear the value of the <tt>clear</tt> property
     */
    public void jsxSet_clear(final String clear) {
        if (!ArrayUtils.contains(VALID_CLEAR_VALUES, clear)
                && getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_43)) {
            throw Context.reportRuntimeError("Invalid clear property value: '" + clear + "'.");
        }
        getDomNodeOrDie().setAttribute("clear", clear);
    }

}
