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

import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;

/**
 * A JavaScript object for a CSSPrimitiveValue.
 *
 * @see org.w3c.dom.css.CSSPrimitiveValue
 * @version $Revision: 5301 $
 * @author Marc Guillemot
 */
public class CSSPrimitiveValue extends CSSValue {

    private static final long serialVersionUID = -1742690770021576031L;

    /**
     * The value is not a recognized CSS2 value. The value can only be
     * obtained by using the <code>cssText</code> attribute.
     */
    public static final short CSS_UNKNOWN = org.w3c.dom.css.CSSPrimitiveValue.CSS_UNKNOWN;

    /**
     * The value is a simple number. The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    public static final short CSS_NUMBER = org.w3c.dom.css.CSSPrimitiveValue.CSS_NUMBER;

    /**
     * The value is a percentage. The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    public static final short CSS_PERCENTAGE = org.w3c.dom.css.CSSPrimitiveValue.CSS_PERCENTAGE;

    /**
     * The value is a length (ems). The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    public static final short CSS_EMS = org.w3c.dom.css.CSSPrimitiveValue.CSS_EMS;

    /**
     * The value is a length (exs). The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    public static final short CSS_EXS = org.w3c.dom.css.CSSPrimitiveValue.CSS_EXS;

    /**
     * The value is a length (px). The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    public static final short CSS_PX = org.w3c.dom.css.CSSPrimitiveValue.CSS_PX;

    /**
     * The value is a length (cm). The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    public static final short CSS_CM = org.w3c.dom.css.CSSPrimitiveValue.CSS_CM;

    /**
     * The value is a length (mm). The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    public static final short CSS_MM = org.w3c.dom.css.CSSPrimitiveValue.CSS_MM;

    /**
     * The value is a length (in). The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    public static final short CSS_IN = org.w3c.dom.css.CSSPrimitiveValue.CSS_IN;

    /**
     * The value is a length (pt). The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    public static final short CSS_PT = org.w3c.dom.css.CSSPrimitiveValue.CSS_PT;

    /**
     * The value is a length (pc). The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    public static final short CSS_PC = org.w3c.dom.css.CSSPrimitiveValue.CSS_PC;

    /**
     * The value is an angle (deg). The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    public static final short CSS_DEG = org.w3c.dom.css.CSSPrimitiveValue.CSS_DEG;

    /**
     * The value is an angle (rad). The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    public static final short CSS_RAD = org.w3c.dom.css.CSSPrimitiveValue.CSS_RAD;

    /**
     * The value is an angle (grad). The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    public static final short CSS_GRAD = org.w3c.dom.css.CSSPrimitiveValue.CSS_GRAD;

    /**
     * The value is a time (ms). The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    public static final short CSS_MS = org.w3c.dom.css.CSSPrimitiveValue.CSS_MS;

    /**
     * The value is a time (s). The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    public static final short CSS_S = org.w3c.dom.css.CSSPrimitiveValue.CSS_S;

    /**
     * The value is a frequency (Hz). The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    public static final short CSS_HZ = org.w3c.dom.css.CSSPrimitiveValue.CSS_HZ;

    /**
     * The value is a frequency (kHz). The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    public static final short CSS_KHZ = org.w3c.dom.css.CSSPrimitiveValue.CSS_KHZ;

    /**
     * The value is a number with an unknown dimension. The value can be
     * obtained by using the <code>getFloatValue</code> method.
     */
    public static final short CSS_DIMENSION = org.w3c.dom.css.CSSPrimitiveValue.CSS_DIMENSION;

    /**
     * The value is a STRING. The value can be obtained by using the
     * <code>getStringValue</code> method.
     */
    public static final short CSS_STRING = org.w3c.dom.css.CSSPrimitiveValue.CSS_STRING;

    /**
     * The value is a URI. The value can be obtained by using the
     * <code>getStringValue</code> method.
     */
    public static final short CSS_URI = org.w3c.dom.css.CSSPrimitiveValue.CSS_URI;

    /**
     * The value is an identifier. The value can be obtained by using the
     * <code>getStringValue</code> method.
     */
    public static final short CSS_IDENT = org.w3c.dom.css.CSSPrimitiveValue.CSS_IDENT;

    /**
     * The value is a attribute function. The value can be obtained by using
     * the <code>getStringValue</code> method.
     */
    public static final short CSS_ATTR = org.w3c.dom.css.CSSPrimitiveValue.CSS_ATTR;

    /**
     * The value is a counter or counters function. The value can be obtained
     * by using the <code>getCounterValue</code> method.
     */
    public static final short CSS_COUNTER = org.w3c.dom.css.CSSPrimitiveValue.CSS_COUNTER;

    /**
     * The value is a rect function. The value can be obtained by using the
     * <code>getRectValue</code> method.
     */
    public static final short CSS_RECT = org.w3c.dom.css.CSSPrimitiveValue.CSS_RECT;

    /**
     * The value is a RGB color. The value can be obtained by using the
     * <code>getRGBColorValue</code> method.
     */
    public static final short CSS_RGBCOLOR = org.w3c.dom.css.CSSPrimitiveValue.CSS_RGBCOLOR;

    private org.w3c.dom.css.CSSPrimitiveValue wrappedCssPrimitiveValue_;

    /**
     * Creates an instance. JavaScript objects must have a default constructor to instantiate prototype.
     */
    public CSSPrimitiveValue() {
        // Empty.
    }

    /**
     * Creates an instance and sets its parent scope to the one of the provided element.
     * @param element the element to which this style is bound
     */
    CSSPrimitiveValue(final HTMLElement element, final org.w3c.dom.css.CSSPrimitiveValue cssValue) {
        super(element, cssValue);
        setParentScope(element.getParentScope());
        setPrototype(getPrototype(getClass()));
        setDomNode(element.getDomNodeOrNull(), false);
        wrappedCssPrimitiveValue_ = cssValue;
    }

    /**
     * Gets the float value in the specified unit.
     * @param unitType the type of unit
     * @return the value
     */
    public double jsxFunction_getFloatValue(final int unitType) {
        return wrappedCssPrimitiveValue_.getFloatValue((short) unitType);
    }
}
