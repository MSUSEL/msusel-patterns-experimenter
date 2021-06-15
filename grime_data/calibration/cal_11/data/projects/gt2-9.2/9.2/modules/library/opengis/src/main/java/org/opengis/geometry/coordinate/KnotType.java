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
import java.util.ArrayList;

import org.opengis.util.CodeList;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * The type of a B-spline.
 * A B-spline is uniform if and only if all knots are of {@linkplain Knot#getMultiplicity
 * multiplicity 1} and they differ by a positive constant from the preceding knot. A B-spline
 * is quasi-uniform if and only if the knots are of multiplicity (degree+1) at the ends, of
 * multiplicity 1 elsewhere, and they differ by a positive constant from the preceding knot.
 * This code list is used to describe the distribution of knots in the parameter space of
 * various splines.
 *
 *
 *
 * @source $URL$
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
@UML(identifier="GM_KnotType", specification=ISO_19107)
public class KnotType extends CodeList<KnotType> {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = -431722533158166557L;

    /**
     * List of all enumerations of this type.
     * Must be declared before any enum declaration.
     */
    private static final List<KnotType> VALUES = new ArrayList<KnotType>(3);

    /**
     * The form of knots is appropriate for a uniform B-spline.
     */
    @UML(identifier="uniform", obligation=CONDITIONAL, specification=ISO_19107)
    public static final KnotType UNIFORM = new KnotType("UNIFORM");

    /**
     * The form of knots is appropriate for a quasi-uniform B-spline.
     */
    @UML(identifier="quasiUniform", obligation=CONDITIONAL, specification=ISO_19107)
    public static final KnotType QUASI_UNIFORM = new KnotType("QUASI_UNIFORM");

    /**
     * The form of knots is appropriate for a piecewise Bezier curve.
     */
    @UML(identifier="piecewiseBezier", obligation=CONDITIONAL, specification=ISO_19107)
    public static final KnotType PIECEWISE_BEZIER = new KnotType("PIECEWISE_BEZIER");

    /**
     * Constructs an enum with the given name. The new enum is
     * automatically added to the list returned by {@link #values}.
     *
     * @param name The enum name. This name must not be in use by an other enum of this type.
     */
    private KnotType(final String name) {
        super(name, VALUES);
    }

    /**
     * Returns the list of {@code KnotType}s.
     *
     * @return The list of codes declared in the current JVM.
     */
    public static KnotType[] values() {
        synchronized (VALUES) {
            return VALUES.toArray(new KnotType[VALUES.size()]);
        }
    }

    /**
     * Returns the list of enumerations of the same kind than this enum.
     */
    public KnotType[] family() {
        return values();
    }

    /**
     * Returns the KnotType that matches the given string, or returns a
     * new one if none match it.
     *
     * @param code The name of the code to fetch or to create.
     * @return A code matching the given name.
     */
    public static KnotType valueOf(String code) {
        return valueOf(KnotType.class, code);
    }
}
