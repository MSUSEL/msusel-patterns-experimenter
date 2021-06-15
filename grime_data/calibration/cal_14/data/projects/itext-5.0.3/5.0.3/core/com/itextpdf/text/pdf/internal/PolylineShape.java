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

import java.awt.Shape;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Class that defines a Polyline shape.
 * This class was originally written by wil - amristar.com.au
 * and integrated into iText by Bruno.
 */
public class PolylineShape implements Shape {
	/** All the X-values of the coordinates in the polyline. */
	protected int[] x;
	/** All the Y-values of the coordinates in the polyline. */
	protected int[] y;
	/** The total number of points. */
	protected int np;

	/** Creates a PolylineShape. */
	public PolylineShape(int[] x, int[] y, int nPoints) {
		// Should copy array (as done in Polygon)
		this.np = nPoints;
		// Take a copy.
		this.x = new int[np];
		this.y = new int[np];
		System.arraycopy(x, 0, this.x, 0, np);
		System.arraycopy(y, 0, this.y, 0, np);
	}

	/**
	 * Returns the bounding box of this polyline.
	 *
	 * @return a {@link Rectangle2D} that is the high-precision
	 * 	bounding box of this line.
	 * @see java.awt.Shape#getBounds2D()
	 */
	public Rectangle2D getBounds2D() {
		int[] r = rect();
		return r==null?null:new Rectangle2D.Double(r[0], r[1], r[2], r[3]);
	}
	
	/**
	 * Returns the bounding box of this polyline.
	 * @see java.awt.Shape#getBounds()
	 */
	public Rectangle getBounds() {
		return getBounds2D().getBounds();
	}

	/**
	 * Calculates the origin (X, Y) and the width and height
	 * of a rectangle that contains all the segments of the
	 * polyline.
	 */
	private int[] rect() {
		 if(np==0)return null;
		int xMin = x[0], yMin=y[0], xMax=x[0],yMax=y[0];

		 for(int i=1;i<np;i++) {
			 if(x[i]<xMin)xMin=x[i];
			 else if(x[i]>xMax)xMax=x[i];
			 if(y[i]<yMin)yMin=y[i];
			 else if(y[i]>yMax)yMax=y[i];
		 }

		 return new int[] { xMin, yMin, xMax-xMin, yMax-yMin };
	}

	/**
	 * A polyline can't contain a point.
	 * @see java.awt.Shape#contains(double, double)
	 */
	public boolean contains(double x, double y) { return false; }
	
	/**
	 * A polyline can't contain a point.
	 * @see java.awt.Shape#contains(java.awt.geom.Point2D)
	 */
	public boolean contains(Point2D p) { return false; }
	
	/**
	 * A polyline can't contain a point.
	 * @see java.awt.Shape#contains(double, double, double, double)
	 */
	public boolean contains(double x, double y, double w, double h) { return false; }
	
	/**
	 * A polyline can't contain a point.
	 * @see java.awt.Shape#contains(java.awt.geom.Rectangle2D)
	 */
	public boolean contains(Rectangle2D r) { return false; }

	/**
	 * Checks if one of the lines in the polyline intersects
	 * with a given rectangle.
	 * @see java.awt.Shape#intersects(double, double, double, double)
	 */
	public boolean intersects(double x, double y, double w, double h) {
		return intersects(new Rectangle2D.Double(x, y, w, h));
	}

	/**
	 * Checks if one of the lines in the polyline intersects
	 * with a given rectangle.
	 * @see java.awt.Shape#intersects(java.awt.geom.Rectangle2D)
	 */
	public boolean intersects(Rectangle2D r) {
		if(np==0)return false;
		Line2D line = new Line2D.Double(x[0],y[0],x[0],y[0]);
		for (int i = 1; i < np; i++) {
			line.setLine(x[i-1], y[i-1], x[i], y[i]);
			if(line.intersects(r))return true;
		}
		return false;
	}

	/**
	 * Returns an iteration object that defines the boundary of the polyline.
	 * @param at the specified {@link AffineTransform}
	 * @return a {@link PathIterator} that defines the boundary of this polyline.
	 * @see java.awt.Shape#intersects(java.awt.geom.Rectangle2D)
	 */
	public PathIterator getPathIterator(AffineTransform at) {
		return new PolylineShapeIterator(this, at);
	}

	/**
	 * There's no difference with getPathIterator(AffineTransform at);
	 * we just need this method to implement the Shape interface.
	 */
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		return new PolylineShapeIterator(this, at);
	}

}

