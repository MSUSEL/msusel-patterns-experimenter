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
package org.osgeo.geometry.primitive;

import java.util.List;

import org.osgeo.geometry.composite.CompositeCurve;
import org.osgeo.geometry.points.Points;
import org.osgeo.geometry.primitive.segments.CurveSegment;
import org.osgeo.geometry.primitive.segments.LineStringSegment;

/**
 * <code>Curve</code> instances are 1D-geometries that consist of an arbitrary number of curve segments.
 * 
 * @see CompositeCurve
 * @see LineString
 * @see OrientableCurve
 * @see Ring
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author$
 * 
 * @version. $Revision$, $Date$
 */
public interface Curve extends GeometricPrimitive {

    /**
     * Convenience enum type for discriminating the different curve variants.
     */
    public enum CurveType {
        /** Generic curve that consists of an arbitrary number of segments. */
        Curve,
        /** Curve that consists of a single segment with linear interpolation. */
        LineString,
        /** Curve that wraps a base curve with additional orientation flag. */
        OrientableCurve,
        /** Curve composited from multiple base curves. */
        CompositeCurve,
        /** A Ring consists of a sequence of curves connected in a cycle */
        Ring
    }

    /**
     * Must always return {@link GeometricPrimitive.PrimitiveType#Curve}.
     * 
     * @return {@link GeometricPrimitive.PrimitiveType#Curve}
     */
    public PrimitiveType getPrimitiveType();

    /**
     * Returns the type of curve.
     * 
     * @return the type of curve
     */
    public CurveType getCurveType();

    /**
     * Returns whether the curve forms a closed loop.
     * 
     * @return true, if the curve forms a closed loop, false otherwise
     */
    public boolean isClosed();

    /**
     * Returns the start point of the curve.
     * 
     * @return the start point of the curve
     */
    public Point getStartPoint();

    /**
     * Returns the end point of the curve.
     * 
     * @return the end point of the curve
     */
    public Point getEndPoint();

    /**
     * Returns the segments that constitute this curve.
     * 
     * @return the segments that constitute this curve
     */
    public List<CurveSegment> getCurveSegments();

    /**
     * Convenience method for accessing the control points of linear interpolated curves.
     * <p>
     * NOTE: This method is only safe to use when the curve is a {@link LineString} or {@link LinearRing} or it only
     * consists of {@link LineStringSegment}s. In any other case it will fail.
     * </p>
     * 
     * @return the control points
     * @throws IllegalArgumentException
     *             if the curve is not linear interpolated
     */
    public Points getControlPoints();

    /**
     * Returns a linear interpolated representation of the curve.
     * <p>
     * Please note that this operation returns an approximated version if the curve uses non-linear curve segments.
     * 
     * @return a linear interpolated representation of the curve
     */
    public LineString getAsLineString();
}
