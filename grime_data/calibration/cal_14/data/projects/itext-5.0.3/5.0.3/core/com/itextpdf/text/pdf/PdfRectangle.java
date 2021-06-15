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
package com.itextpdf.text.pdf;

import com.itextpdf.text.Rectangle;

/**
 * <CODE>PdfRectangle</CODE> is the PDF Rectangle object.
 * <P>
 * Rectangles are used to describe locations on the page and bounding boxes for several
 * objects in PDF, such as fonts. A rectangle is represented as an <CODE>array</CODE> of
 * four numbers, specifying the lower left <I>x</I>, lower left <I>y</I>, upper right <I>x</I>,
 * and upper right <I>y</I> coordinates of the rectangle, in that order.<BR>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 7.1 (page 183).
 *
 * @see		com.itextpdf.text.Rectangle
 * @see		PdfArray
 */

public class PdfRectangle extends PdfArray {

    // membervariables

/** lower left x */
    private float llx = 0;

/** lower left y */
    private float lly = 0;

/** upper right x */
    private float urx = 0;

/** upper right y */
    private float ury = 0;

    // constructors

/**
 * Constructs a <CODE>PdfRectangle</CODE>-object.
 *
 * @param		llx			lower left x
 * @param		lly			lower left y
 * @param		urx			upper right x
 * @param		ury			upper right y
 *
 * @since		rugPdf0.10
 */

    public PdfRectangle(float llx, float lly, float urx, float ury, int rotation) {
        super();
        if (rotation == 90 || rotation == 270) {
            this.llx = lly;
            this.lly = llx;
            this.urx = ury;
            this.ury = urx;
        }
        else {
            this.llx = llx;
            this.lly = lly;
            this.urx = urx;
            this.ury = ury;
        }
        super.add(new PdfNumber(this.llx));
        super.add(new PdfNumber(this.lly));
        super.add(new PdfNumber(this.urx));
        super.add(new PdfNumber(this.ury));
    }

    public PdfRectangle(float llx, float lly, float urx, float ury) {
        this(llx, lly, urx, ury, 0);
    }

/**
 * Constructs a <CODE>PdfRectangle</CODE>-object starting from the origin (0, 0).
 *
 * @param		urx			upper right x
 * @param		ury			upper right y
 */

    public PdfRectangle(float urx, float ury, int rotation) {
        this(0, 0, urx, ury, rotation);
    }

    public PdfRectangle(float urx, float ury) {
        this(0, 0, urx, ury, 0);
    }

/**
 * Constructs a <CODE>PdfRectangle</CODE>-object with a <CODE>Rectangle</CODE>-object.
 *
 * @param	rectangle	a <CODE>Rectangle</CODE>
 */

    public PdfRectangle(Rectangle rectangle, int rotation) {
        this(rectangle.getLeft(), rectangle.getBottom(), rectangle.getRight(), rectangle.getTop(), rotation);
    }

    public PdfRectangle(Rectangle rectangle) {
        this(rectangle.getLeft(), rectangle.getBottom(), rectangle.getRight(), rectangle.getTop(), 0);
    }

    // methods
    /**
     * Returns the high level version of this PdfRectangle
     * @return this PdfRectangle translated to class Rectangle
     */
    public Rectangle getRectangle() {
    	return new Rectangle(left(), bottom(), right(), top());
    }

/**
 * Overrides the <CODE>add</CODE>-method in <CODE>PdfArray</CODE> in order to prevent the adding of extra object to the array.
 *
 * @param		object			<CODE>PdfObject</CODE> to add (will not be added here)
 * @return		<CODE>false</CODE>
 */

    public boolean add(PdfObject object) {
        return false;
    }

    /**
     * Block changes to the underlying PdfArray
     * @param values stuff we'll ignore.  Ha!
     * @return false.  You can't add anything to a PdfRectangle
     * @since 2.1.5
     */

    public boolean add( float values[] ) {
        return false;
    }

    /**
     * Block changes to the underlying PdfArray
     * @param values stuff we'll ignore.  Ha!
     * @return false.  You can't add anything to a PdfRectangle
     * @since 2.1.5
     */

    public boolean add( int values[] ) {
        return false;
    }

    /**
     * Block changes to the underlying PdfArray
     * @param object Ignored.
     * @since 2.1.5
     */

    public void addFirst( PdfObject object ) {
    }
/**
 * Returns the lower left x-coordinate.
 *
 * @return		the lower left x-coordinate
 */

    public float left() {
        return llx;
    }

/**
 * Returns the upper right x-coordinate.
 *
 * @return		the upper right x-coordinate
 */

    public float right() {
        return urx;
    }

/**
 * Returns the upper right y-coordinate.
 *
 * @return		the upper right y-coordinate
 */

    public float top() {
        return ury;
    }

/**
 * Returns the lower left y-coordinate.
 *
 * @return		the lower left y-coordinate
 */

    public float bottom() {
        return lly;
    }

/**
 * Returns the lower left x-coordinate, considering a given margin.
 *
 * @param		margin		a margin
 * @return		the lower left x-coordinate
 */

    public float left(int margin) {
        return llx + margin;
    }

/**
 * Returns the upper right x-coordinate, considering a given margin.
 *
 * @param		margin		a margin
 * @return		the upper right x-coordinate
 */

    public float right(int margin) {
        return urx - margin;
    }

/**
 * Returns the upper right y-coordinate, considering a given margin.
 *
 * @param		margin		a margin
 * @return		the upper right y-coordinate
 */

    public float top(int margin) {
        return ury - margin;
    }

/**
 * Returns the lower left y-coordinate, considering a given margin.
 *
 * @param		margin		a margin
 * @return		the lower left y-coordinate
 */

    public float bottom(int margin) {
        return lly + margin;
    }

/**
 * Returns the width of the rectangle.
 *
 * @return		a width
 */

    public float width() {
        return urx - llx;
    }

/**
 * Returns the height of the rectangle.
 *
 * @return		a height
 */

    public float height() {
        return ury - lly;
    }

/**
 * Swaps the values of urx and ury and of lly and llx in order to rotate the rectangle.
 *
 * @return		a <CODE>PdfRectangle</CODE>
 */

    public PdfRectangle rotate() {
        return new PdfRectangle(lly, llx, ury, urx, 0);
    }
}