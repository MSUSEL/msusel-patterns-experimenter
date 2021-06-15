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
 *    OSGeom -- Geometry Collab
 *
 *    (C) 2009, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2009 Department of Geography, University of Bonn
 *    (C) 2001-2009 lat/lon GmbH
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
package org.osgeo.geometry.primitive.segments;

import java.util.List;

import org.osgeo.geometry.points.Points;

/**
 * {@link CurveSegment} that uses either polynomial or rational interpolation.
 * <p>
 * Description from the GML 3.1.1 schema:
 * <p>
 * A B-Spline is a piecewise parametric polynomial or rational curve described in terms of control points and basis
 * functions. Knots are breakpoints on the curve that connect its pieces. They are given as a non-decreasing sequence of
 * real numbers. If the weights in the knots are equal then it is a polynomial spline. The degree is the algebraic
 * degree of the basis functions.
 * </p>
 *
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author$
 *
 * @version $Revision$, $Date$
 */
public interface BSpline extends CurveSegment {

    /**
     * Returns the control points of the segment.
     *
     * @return the control points of the segment
     */
    public Points getControlPoints();

    /**
     * Returns the degree of the polynomial used for interpolation in this spline.
     *
     * @return the degree of the polynomial
     */
    public int getPolynomialDegree();

    /**
     * Returns whether the interpolation is polynomial or rational.
     *
     * @return true, if the interpolation is polynomial, false if it's rational 
     */
    public boolean isPolynomial();

    /**
     * Returns the knots that define the spline basis functions.
     *
     * @return list of distinctive knots
     */
    public List<Knot> getKnots();
}
