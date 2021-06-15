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
package com.itextpdf.text.pdf.internal;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.util.NoSuchElementException;
import com.itextpdf.text.error_messages.MessageLocalization;
/**
 * PathIterator for PolylineShape.
 * This class was originally written by wil - amristar.com.au
 * and integrated into iText by Bruno.
 */
public class PolylineShapeIterator implements PathIterator {
	/** The polyline over which we are going to iterate. */
	protected PolylineShape poly;
	/** an affine transformation to be performed on the polyline. */
	protected AffineTransform affine;
	/** the index of the current segment in the iterator. */
	protected int index;
	
	/** Creates a PolylineShapeIterator. */
	PolylineShapeIterator(PolylineShape l, AffineTransform at) {
		this.poly = l;
		this.affine = at;
	}
	
	/**
	 * Returns the coordinates and type of the current path segment in
	 * the iteration. The return value is the path segment type:
	 * SEG_MOVETO, SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE.
	 * A double array of length 6 must be passed in and may be used to
	 * store the coordinates of the point(s).
	 * Each point is stored as a pair of double x,y coordinates.
	 * SEG_MOVETO and SEG_LINETO types will return one point,
	 * SEG_QUADTO will return two points,
	 * SEG_CUBICTO will return 3 points
	 * and SEG_CLOSE will not return any points.
	 * @see #SEG_MOVETO
	 * @see #SEG_LINETO
	 * @see #SEG_QUADTO
	 * @see #SEG_CUBICTO
	 * @see #SEG_CLOSE
	 * @see java.awt.geom.PathIterator#currentSegment(double[])
	 */
	public int currentSegment(double[] coords) {
		if (isDone()) {
			throw new NoSuchElementException(MessageLocalization.getComposedMessage("line.iterator.out.of.bounds"));
		}
		int type = (index==0)?SEG_MOVETO:SEG_LINETO;
		coords[0] = poly.x[index];
		coords[1] = poly.y[index];
		if (affine != null) {
			affine.transform(coords, 0, coords, 0, 1);
		}
		return type;
	}
	
	/**
	 * Returns the coordinates and type of the current path segment in
	 * the iteration. The return value is the path segment type:
	 * SEG_MOVETO, SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE.
	 * A double array of length 6 must be passed in and may be used to
	 * store the coordinates of the point(s).
	 * Each point is stored as a pair of double x,y coordinates.
	 * SEG_MOVETO and SEG_LINETO types will return one point,
	 * SEG_QUADTO will return two points,
	 * SEG_CUBICTO will return 3 points
	 * and SEG_CLOSE will not return any points.
	 * @see #SEG_MOVETO
	 * @see #SEG_LINETO
	 * @see #SEG_QUADTO
	 * @see #SEG_CUBICTO
	 * @see #SEG_CLOSE
	 * @see java.awt.geom.PathIterator#currentSegment(float[])
	 */
	public int currentSegment(float[] coords) {
		if (isDone()) {
			throw new NoSuchElementException(MessageLocalization.getComposedMessage("line.iterator.out.of.bounds"));
		}
		int type = (index==0)?SEG_MOVETO:SEG_LINETO;
		coords[0] = poly.x[index];
		coords[1] = poly.y[index];
		if (affine != null) {
			affine.transform(coords, 0, coords, 0, 1);
		}
		return type;
	}

	/**
	 * Return the winding rule for determining the insideness of the
	 * path.
	 * @see #WIND_EVEN_ODD
	 * @see #WIND_NON_ZERO
	 * @see java.awt.geom.PathIterator#getWindingRule()
	 */
	public int getWindingRule() {
		return WIND_NON_ZERO;
	}

	/**
	 * Tests if there are more points to read.
	 * @return true if there are more points to read
	 * @see java.awt.geom.PathIterator#isDone()
	 */
	public boolean isDone() {
		return (index >= poly.np);
	}

	/**
	 * Moves the iterator to the next segment of the path forwards
	 * along the primary direction of traversal as long as there are
	 * more points in that direction.
	 * @see java.awt.geom.PathIterator#next()
	 */
	public void next() {
		index++;
	}

}
