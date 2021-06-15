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

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;

/**
 * A JavaScript object for a CSSValue.
 *
 * @see org.w3c.dom.css.CSSValue
 * @version $Revision: 5301 $
 * @author Marc Guillemot
 */
public class CSSValue extends SimpleScriptable {

    private static final long serialVersionUID = 5433037821970827600L;

    /**
     * The value is inherited and the <code>cssText</code> contains "inherit".
     */
    public static final short CSS_INHERIT = org.w3c.dom.css.CSSValue.CSS_INHERIT;

    /**
     * The value is a primitive value and an instance of the
     * <code>CSSPrimitiveValue</code> interface can be obtained by using
     * binding-specific casting methods on this instance of the
     * <code>CSSValue</code> interface.
     */
    public static final short CSS_PRIMITIVE_VALUE = org.w3c.dom.css.CSSValue.CSS_PRIMITIVE_VALUE;

    /**
     * The value is a <code>CSSValue</code> list and an instance of the
     * <code>CSSValueList</code> interface can be obtained by using
     * binding-specific casting methods on this instance of the
     * <code>CSSValue</code> interface.
     */
    public static final short CSS_VALUE_LIST = org.w3c.dom.css.CSSValue.CSS_VALUE_LIST;

    /**
     * The value is a custom value.
     */
    public static final short CSS_CUSTOM = org.w3c.dom.css.CSSValue.CSS_CUSTOM;

    /**
     * The wrapped CSS value.
     */
    private org.w3c.dom.css.CSSValue wrappedCssValue_;

    /**
     * Creates an instance. JavaScript objects must have a default constructor to instantiate prototype.
     */
    public CSSValue() {
        // Empty.
    }

    /**
     * Creates an instance and sets its parent scope to the one of the provided element.
     * @param element the element to which this style is bound
     */
    CSSValue(final HTMLElement element, final org.w3c.dom.css.CSSValue cssValue) {
        setParentScope(element.getParentScope());
        setPrototype(getPrototype(getClass()));
        setDomNode(element.getDomNodeOrNull(), false);
        wrappedCssValue_ = cssValue;
    }

    /**
     * A string representation of the current value.
     * @return the string representation
     */
    public String jsxGet_cssText() {
        return wrappedCssValue_.getCssText();
    }
}
