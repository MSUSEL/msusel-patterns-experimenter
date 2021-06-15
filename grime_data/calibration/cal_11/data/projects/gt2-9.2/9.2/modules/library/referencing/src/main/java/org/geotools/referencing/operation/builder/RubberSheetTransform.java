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
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.referencing.operation.builder;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Iterator;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.TransformException;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.referencing.operation.transform.AbstractMathTransform;


/**
 * This provides the transformation method based on RubberSheeting (also
 * known as Billinear interpolated transformation) The class is accessed
 * {@linkplain org.geotools.referencing.operation.builder.RubberSheetBuilder
 * RubberSheetBuilder}. More about Rubber Sheet transformation can be seen <a
 * href =
 * "http://planner.t.u-tokyo.ac.jp/member/fuse/rubber_sheeting.pdf">here</a>.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Jan Jezek
 *
 * @todo Consider moving this class to the {@linkplain
 *       org.geotools.referencing.operation.transform} package.
 */
class RubberSheetTransform extends AbstractMathTransform implements MathTransform2D {
    /**
     * Helper variable to hold triangle. It is use for optimalization of searching in TIN for
     * triangle containing points that are transformed.
     */
    private TINTriangle previousTriangle = null;

    /**
     * The HashMap where the keys are the original {@link
     * Polygon} and values are {@link
     * #org.opengis.referencing.operation.MathTransform}.
     */
    private HashMap trianglesToKeysMap;

    /**
     * Constructs the RubberSheetTransform.
     *
     * @param trianglesToAffineTransform The HashMap where the keys are the original
     *        {@linkplain org.geotools.referencing.operation.builder.algorithm.TINTriangle}
     *        and values are {@linkplain org.opengis.referencing.operation.MathTransform}.
     */
    public RubberSheetTransform(HashMap trianglesToAffineTransform) {
        this.trianglesToKeysMap = trianglesToAffineTransform;
    }

    /**
     * Gets the dimension of input points, which is 2.
     *
     * @return dimension of input points
     */
    public final int getSourceDimensions() {
        return 2;
    }

    /**
     * Gets the dimension of output points, which is 2.
     *
     * @return dimension of output points
     */
    public final int getTargetDimensions() {
        return 2;
    }

    /**
     * String representation.
     *
     * @return String expression of the triangle and its affine transform
     *         parameters
     *
     * @todo This method doesn't meet the {@link MathTransform#toString}
     *       constract, which should uses Well Known Text (WKT) format as much
     *       as possible.
     */
    @Override
    public String toString() {
        final String lineSeparator = System.getProperty("line.separator", "\n");
        final StringBuilder buffer = new StringBuilder();

        for (final Iterator i = trianglesToKeysMap.keySet().iterator(); i.hasNext();) {
            TINTriangle trian = (TINTriangle) i.next();
            MathTransform mt = (MathTransform) trianglesToKeysMap.get(trian);
            buffer.append(trian.toString());
            buffer.append(lineSeparator);
            buffer.append(mt.toString());
            buffer.append(lineSeparator);
        }

        return buffer.toString();
    }

    /* (non-Javadoc)
     * @see org.opengis.referencing.operation.MathTransform#transform(double[], int, double[], int, int)
     */
    public void transform(double[] srcPts, int srcOff, final double[] dstPt, int dstOff, int numPts)
        throws TransformException {
        for (int i = srcOff; i < numPts; i++) {
            Point2D pos = (Point2D) (new DirectPosition2D(srcPts[2 * i], srcPts[(2 * i) + 1]));

            TINTriangle triangle = searchTriangle((DirectPosition) pos);

            AffineTransform AT = (AffineTransform) trianglesToKeysMap.get(triangle);

            Point2D dst = AT.transform(pos, null);

            dstPt[2 * i] = dst.getX();
            dstPt[(2 * i) + 1] = dst.getY();
        }
    }

    /**
     * Search the TIN for the triangle that contains p
     * @param p Point of interest
     * @return Triangle containing p
     * @throws TransformException if points are outside the area of TIN.
     */
    private TINTriangle searchTriangle(DirectPosition p)
        throws TransformException {
        /* optimalization for finding triangles.
         * Assuming the point are close to each other -
         * so why not to check if next point is in the same triangle as previous one.
         */
        if ((previousTriangle != null) && previousTriangle.containsOrIsVertex(p)) {
            return previousTriangle;
        }

        for (Iterator i = trianglesToKeysMap.keySet().iterator(); i.hasNext();) {
            TINTriangle triangle = (TINTriangle) i.next();

            if (triangle.containsOrIsVertex(p)) {
                previousTriangle = triangle;

                return triangle;
            }
        }
        throw (new TransformException("Points are outside the scope"));
    }

    /**
     * Returns the inverse transform.
     * @return
     */
    @Override
    public MathTransform2D inverse() throws NoninvertibleTransformException {
        return (MathTransform2D) super.inverse();
    }
}
