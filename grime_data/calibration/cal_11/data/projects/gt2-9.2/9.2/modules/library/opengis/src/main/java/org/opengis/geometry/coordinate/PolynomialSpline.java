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
 * A polynimal spline.
 * An "<var>n</var><sup>th</sup> degree" polynomial spline shall be defined piecewise as an
 * <var>n</var>-degree polynomial, with up to C<sup><var>n</var>-1</sup> continuity
 * at the control points where the defining polynomial changes. This level of continuity is
 * controlled by the attribute {@link #getNumDerivativesInterior numDerivativesInterior}.
 * Parameters shall include directions for as many as degree - 2 derivatives of the polynomial
 * at the start and end point of the segment. {@link LineString} is equivalent to a
 * 1<sup>st</sup> degree polynomial spline. It has simple continuity at the
 * {@linkplain #getControlPoints control points} (C</sup>0</sup>), but does
 * not require derivative information (degree - 2 = -1).
 * <p>
 * NOTE: The major difference between the polynomial splines and the b-splines (basis splines)
 * is that polynomial splines pass through their control points, making the control point and
 * sample point array identical.
 *
 *
 *
 * @source $URL$
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
@UML(identifier="GM_PolynomialSpline", specification=ISO_19107)
public interface PolynomialSpline extends SplineCurve {
    /**
     * The interpolation mechanism for a {@code PolynomialSpline}
     * is {@link CurveInterpolation#POLYNOMIAL_SPLINE POLYNOMIAL_SPLINE}.
     */
    @UML(identifier="interpolation", obligation=MANDATORY, specification=ISO_19107)
    CurveInterpolation getInterpolation();

    /**
     * The values used for the initial derivative (up to {@linkplain #getDegree degree} - 2)
     * used for interpolation in this {@code PolynomialSpline} at the start point
     * of the spline.
     *
     * The {@linkplain List#size size} of the returned list is
     * <code>({@linkplain #getDegree degree} - 2)</code>.
     */
    @UML(identifier="vectorAtStart", obligation=MANDATORY, specification=ISO_19107)
    List/*double[]*/ getVectorAtStart();

    /**
     * The values used for the final derivative (up to {@linkplain #getDegree degree} - 2)
     * used for interpolation in this {@code PolynomialSpline} at the end point of
     * the spline.
     *
     * The {@linkplain List#size size} of the returned list is
     * <code>({@linkplain #getDegree degree} - 2)</code>.
     */
    @UML(identifier="vectorAtEnd", obligation=MANDATORY, specification=ISO_19107)
    List/*double[]*/ getVectorAtEnd();
}
