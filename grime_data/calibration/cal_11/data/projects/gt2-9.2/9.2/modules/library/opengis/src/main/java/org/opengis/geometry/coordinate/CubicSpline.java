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
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.geometry.coordinate;

import java.util.List;
import org.opengis.geometry.primitive.CurveInterpolation;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Cubic splines.
 * Cubic splines are similar to line strings in that they are a sequence of segments each with
 * its own defining function. A cubic spline uses the control points and a set of derivative
 * parameters to define a piecewise 3rd degree polynomial interpolation. Unlike line-strings,
 * the parameterization by arc length is not necessarily still a polynomial. Splines have two
 * parameterizations that are used in this specification, the defining one (constructive
 * parameter) and the one that has been reparameterized by arc length to satisfy the requirements
 * in {@link GenericCurve}.
 * <p>
 * The function describing the curve must be C<sup>2</sup>, that is, have a continuous
 * 1<sup>st</sup> and 2<sup>nd</sup> derivative at all points, and pass through the
 * {@linkplain #getControlPoints control points} in the order given. Between the control points,
 * the curve segment is defined by a cubic polynomial. At each control point, the polynomial
 * changes in such a manner that the 1<sup>st</sup> and 2<sup>nd</sup> derivative vectors are
 * the same from either side. The control parameters record must contain
 * {@link #getVectorAtStart vectorAtStart}, and {@link #getVectorAtEnd vectorAtEnd}
 * which are the unit tangent vectors at {@code controlPoint[0]} and <code>controlPoint[n]</coded>
 * where <var>n</var> = {@code controlPoint.length-1}.
 *
 *
 *
 * @source $URL$
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
@UML(identifier="GM_CubicSpline", specification=ISO_19107)
public interface CubicSpline extends PolynomialSpline {
    /**
     * The interpolation mechanism for a {@code CubicSpline}
     * is {@link CurveInterpolation#CUBIC_SPLINE CUBIC_SPLINE}.
     */
    @UML(identifier="interpolation", obligation=MANDATORY, specification=ISO_19107)
    CurveInterpolation getInterpolation();

    /**
     * The values used for the initial derivative.
     * The restriction on {@code vectorAtStart} and {@code vectorAtEnd} reduce these
     * sequences to a single tangent vector each. Consequently, the {@linkplain List#size size}
     * of the returned list is 1.
     */
    @UML(identifier="vectorAtStart", obligation=MANDATORY, specification=ISO_19107)
    List/*double[]*/ getVectorAtStart();

    /**
     * The values used for the final derivative.
     * The restriction on {@code vectorAtStart} and {@code vectorAtEnd} reduce these
     * sequences to a single tangent vector each. Consequently, the {@linkplain List#size size}
     * of the returned list is 1.
     */
    @UML(identifier="vectorAtEnd", obligation=MANDATORY, specification=ISO_19107)
    List/*double[]*/ getVectorAtEnd();
}
