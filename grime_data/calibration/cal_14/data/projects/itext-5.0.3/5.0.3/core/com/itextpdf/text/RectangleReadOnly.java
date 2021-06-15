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
package com.itextpdf.text;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.BaseColor;

/**
 * A <CODE>RectangleReadOnly</CODE> is the representation of a geometric figure.
 * It's the same as a <CODE>Rectangle</CODE> but immutable.
 * Rectangles support constant width borders using
 * {@link #setBorderWidth(float)}and {@link #setBorder(int)}.
 * They also support borders that vary in width/color on each side using
 * methods like {@link #setBorderWidthLeft(float)}or
 * {@link #setBorderColorLeft(BaseColor)}.
 * 
 * @see Element
 * @since 2.1.2
 */

public class RectangleReadOnly extends Rectangle {

	// CONSTRUCTORS

	/**
	 * Constructs a <CODE>RectangleReadOnly</CODE> -object.
	 * 
	 * @param llx	lower left x
	 * @param lly	lower left y
	 * @param urx	upper right x
	 * @param ury	upper right y
	 */
	public RectangleReadOnly(float llx, float lly, float urx, float ury) {
        super(llx, lly, urx, ury);
	}

	/**
	 * Constructs a <CODE>RectangleReadOnly</CODE> -object starting from the origin
	 * (0, 0).
	 * 
	 * @param urx	upper right x
	 * @param ury	upper right y
	 */
	public RectangleReadOnly(float urx, float ury) {
		super(0, 0, urx, ury);
	}

	/**
	 * Constructs a <CODE>RectangleReadOnly</CODE> -object.
	 * 
	 * @param rect	another <CODE>Rectangle</CODE>
	 */
	public RectangleReadOnly(Rectangle rect) {
		super(rect.llx, rect.lly, rect.urx, rect.ury);
		super.cloneNonPositionParameters(rect);
	}

	/**
	 * Throws an error because of the read only nature of this object. 
	 */
    private void throwReadOnlyError() {
        throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("rectanglereadonly.this.rectangle.is.read.only"));
    }
    
	// OVERWRITE METHODS SETTING THE DIMENSIONS:

	/**
	 * Sets the lower left x-coordinate.
	 * 
	 * @param llx	the new value
	 */
	public void setLeft(float llx) {
		throwReadOnlyError();
	}

	/**
	 * Sets the upper right x-coordinate.
	 * 
	 * @param urx	the new value
	 */

	public void setRight(float urx) {
		throwReadOnlyError();
	}

	/**
	 * Sets the upper right y-coordinate.
	 * 
	 * @param ury	the new value
	 */
	public void setTop(float ury) {
		throwReadOnlyError();
	}

	/**
	 * Sets the lower left y-coordinate.
	 * 
	 * @param lly	the new value
	 */
	public void setBottom(float lly) {
		throwReadOnlyError();
	}

	/**
	 * Normalizes the rectangle.
     * Switches lower left with upper right if necessary.
	 */
	public void normalize() {
		throwReadOnlyError();
	}

	// OVERWRITE METHODS SETTING THE BACKGROUND COLOR:

	/**
	 * Sets the backgroundcolor of the rectangle.
	 * 
	 * @param value	the new value
	 */
	public void setBackgroundColor(BaseColor value) {
		throwReadOnlyError();
	}

	/**
	 * Sets the grayscale of the rectangle.
	 * 
	 * @param value	the new value
	 */
	public void setGrayFill(float value) {
		throwReadOnlyError();
	}
	
	// OVERWRITE METHODS SETTING THE BORDER:

	/**
	 * Enables/Disables the border on the specified sides.
	 * The border is specified as an integer bitwise combination of
	 * the constants: <CODE>LEFT, RIGHT, TOP, BOTTOM</CODE>.
	 * 
	 * @see #enableBorderSide(int)
	 * @see #disableBorderSide(int)
	 * @param border	the new value
	 */
	public void setBorder(int border) {
		throwReadOnlyError();
	}
	
	/**
	 * Sets a parameter indicating if the rectangle has variable borders
	 * 
	 * @param useVariableBorders	indication if the rectangle has variable borders
	 */
	public void setUseVariableBorders(boolean useVariableBorders) {
		throwReadOnlyError();
	}

	/**
	 * Enables the border on the specified side.
	 * 
	 * @param side	the side to enable.
	 * One of <CODE>LEFT, RIGHT, TOP, BOTTOM</CODE>
	 */
	public void enableBorderSide(int side) {
		throwReadOnlyError();
	}

	/**
	 * Disables the border on the specified side.
	 * 
	 * @param side	the side to disable.
	 * One of <CODE>LEFT, RIGHT, TOP, BOTTOM</CODE>
	 */
	public void disableBorderSide(int side) {
		throwReadOnlyError();
	}

	// OVERWRITE METHODS SETTING THE BORDER WIDTH:

	/**
	 * Sets the borderwidth of the table.
	 * 
	 * @param borderWidth	the new value
	 */

	public void setBorderWidth(float borderWidth) {
		throwReadOnlyError();
	}

	/**
	 * Sets the width of the left border
	 * 
	 * @param borderWidthLeft	a width
	 */
	public void setBorderWidthLeft(float borderWidthLeft) {
		throwReadOnlyError();
	}

	/**
	 * Sets the width of the right border
	 * 
	 * @param borderWidthRight	a width
	 */
	public void setBorderWidthRight(float borderWidthRight) {
		throwReadOnlyError();
	}

	/**
	 * Sets the width of the top border
	 * 
	 * @param borderWidthTop	a width
	 */
	public void setBorderWidthTop(float borderWidthTop) {
		throwReadOnlyError();
	}

	/**
	 * Sets the width of the bottom border
	 * 
	 * @param borderWidthBottom	a width
	 */
	public void setBorderWidthBottom(float borderWidthBottom) {
		throwReadOnlyError();
	}

	// METHODS TO GET/SET THE BORDER COLOR:

	/**
	 * Sets the color of the border.
	 * 
	 * @param borderColor	a <CODE>BaseColor</CODE>
	 */

	public void setBorderColor(BaseColor borderColor) {
		throwReadOnlyError();
	}
	
	/**
	 * Sets the color of the left border.
	 * 
	 * @param borderColorLeft	a <CODE>BaseColor</CODE>
	 */
	public void setBorderColorLeft(BaseColor borderColorLeft) {
		throwReadOnlyError();
	}

	/**
	 * Sets the color of the right border
	 * 
	 * @param borderColorRight	a <CODE>BaseColor</CODE>
	 */
	public void setBorderColorRight(BaseColor borderColorRight) {
		throwReadOnlyError();
	}

	/**
	 * Sets the color of the top border.
	 * 
	 * @param borderColorTop	a <CODE>BaseColor</CODE>
	 */
	public void setBorderColorTop(BaseColor borderColorTop) {
		throwReadOnlyError();
	}

	/**
	 * Sets the color of the bottom border.
	 * 
	 * @param borderColorBottom	a <CODE>BaseColor</CODE>
	 */
	public void setBorderColorBottom(BaseColor borderColorBottom) {
		throwReadOnlyError();
	}

	// SPECIAL METHODS:

	/**
	 * Copies each of the parameters, except the position, from a
     * <CODE>Rectangle</CODE> object
	 * 
	 * @param rect	<CODE>Rectangle</CODE> to copy from
	 */
	public void cloneNonPositionParameters(Rectangle rect) {
		throwReadOnlyError();
	}

	/**
	 * Copies each of the parameters, except the position, from a
     * <CODE>Rectangle</CODE> object if the value is set there.
	 * 
	 * @param rect	<CODE>Rectangle</CODE> to copy from
	 */
	public void softCloneNonPositionParameters(Rectangle rect) {
		throwReadOnlyError();
	}
	
	/**
	 * @return	String version of the most important rectangle properties
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer("RectangleReadOnly: ");
		buf.append(getWidth());
		buf.append('x');
		buf.append(getHeight());
		buf.append(" (rot: ");
		buf.append(rotation);
		buf.append(" degrees)");
		return buf.toString();
	}
}
