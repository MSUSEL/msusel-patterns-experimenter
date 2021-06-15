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

import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Controls the constructive parameter space for spline curves and surfaces. Each knot sequence
 * is used for a dimension of the spline's parameter space. Thus, in a surface spline, there will
 * be two knot sequences, one for each parameter (<var>u</var>, <var>v</var>).
 * The <var>i</var><sup>th</sup>, <var>j</var><sup>th</sup> would be (<var>u<sub>i</sub></var>,
 * <var>v<sub>j</sub></var>), where the original knot sequences were (<var>u<sub>i</sub></var>)
 * and (<var>v<sub>j</sub></var>). Each knot of a spline curve or surface is described using a
 * {@code Knot}.
 *
 *
 *
 * @source $URL$
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
@UML(identifier="GM_Knot", specification=ISO_19107)
public interface Knot {
    /**
     * The value of the parameter at the knot of the spline. The sequence of knots shall be a
     * nondecreasing sequence. That is, each knot's value in the sequence shall be equal to or
     * greater than the previous knot's value. The use of equal consecutive knots is normally
     * handled using the multiplicity.
     */
    @UML(identifier="value", obligation=MANDATORY, specification=ISO_19107)
    double getValue();

    /**
     * The multiplicity of this knot used in the definition of the spline (with the same weight).
     */
    @UML(identifier="multiplicity", obligation=MANDATORY, specification=ISO_19107)
    int getMultiplicity();

    /**
     * The value of the averaging weight used for this knot of the spline.
     */
    @UML(identifier="weight", obligation=MANDATORY, specification=ISO_19107)
    double getWeight();
}
