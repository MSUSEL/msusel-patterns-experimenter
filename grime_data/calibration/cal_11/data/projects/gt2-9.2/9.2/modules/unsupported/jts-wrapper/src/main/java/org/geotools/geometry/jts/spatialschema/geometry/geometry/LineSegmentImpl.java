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
package org.geotools.geometry.jts.spatialschema.geometry.geometry;

import org.opengis.geometry.coordinate.LineSegment;


/**
 * Two distinct {@linkplain org.opengis.geometry.coordinate.DirectPosition direct positions}
 * (the {@linkplain #getStartPoint start point} and {@linkplain #getEndPoint end point}) joined
 * by a straight line. Thus its interpolation attribute shall be
 * {@link org.opengis.geometry.primitive.CurveInterpolation#LINEAR LINEAR}.
 * The default parameterization is:
 *
 * <blockquote><pre>
 * L = {@linkplain #getEndParam endParam} - {@linkplain #getStartParam startParam}
 * c(s) = ControlPoint[1]+((s-{@linkplain #getStartParam startParam})/L)*(ControlPoint[2]-ControlPoint[1])
 * </pre></blockquote>
 *
 * Any other point in the control point array must fall on this line. The control points of a
 * {@code LineSegment} shall all lie on the straight line between its start point and end
 * point. Between these two points, other positions may be interpolated linearly. The linear
 * interpolation, given using a constructive parameter <var>t</var>, 0 ? <var>t</var> ? 1.0,
 * where c(o) = c.{@linkplain #getStartPoint startPoint} and c(1)=c.{@link #getEndPoint endPoint},
 * is:
 *
 * <blockquote>
 * <var>c</var>(<var>t</var>) = <var>c</var>(0)(1-<var>t</var>) + <var>c</var>(1)<var>t</var>
 * </blockquote>
 *  
 * @UML datatype GM_LineSegment
 * @author ISO/DIS 19107
 * @author <A HREF="http://www.opengis.org">OpenGIS&reg; consortium</A>
 *
 *
 *
 *
 * @source $URL$
 * @version 2.0
 *
 * @see GeometryFactory#createLineSegment
 */
public class LineSegmentImpl extends LineStringImpl implements LineSegment {
    
    //*************************************************************************
    //  Fields
    //*************************************************************************
    
    //*************************************************************************
    //  Constructor
    //*************************************************************************
    
    //*************************************************************************
    //  implement the LineSegment interface
    //*************************************************************************

}
