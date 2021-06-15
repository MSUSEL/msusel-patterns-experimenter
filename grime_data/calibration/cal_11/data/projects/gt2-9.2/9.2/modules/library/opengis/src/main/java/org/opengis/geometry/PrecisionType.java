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
package org.opengis.geometry;

import java.util.List;
import java.util.ArrayList;

import org.opengis.util.CodeList;


/**
 * The rounding policy used for a {@linkplain Precision precision model}.
 *
 * @author Jody Garnett
 * @since GeoAPI 2.1
 *
 *
 * @source $URL$
 */
public final class PrecisionType extends CodeList<PrecisionType> {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = -2771887290382853282L;

    /**
     * Indicates Precision Model uses floating point math (rather then a grid).
     */
    private final boolean isFloating;

    /**
     * List of all enumerations of this type.
     * Must be declared before any enum declaration.
     */
    private static final List<PrecisionType> VALUES = new ArrayList<PrecisionType>(3);

    /**
     * Fixed precision indicates that coordinates have a fixed number of decimal places.
     */
    public static final PrecisionType FIXED = new PrecisionType("FIXED", false);

    /**
     * Floating precision corresponds to the standard Java double-precision floating-point
     * representation, which is based on the IEEE-754 standard.
     */
    public static final PrecisionType DOUBLE = new PrecisionType("DOUBLE", true);

    /**
     * Floating single precision corresponds to the standard Java single-precision
     * floating-point representation, which is based on the IEEE-754 standard.
     */
    public static final PrecisionType FLOAT  = new PrecisionType("FLOAT", true);

    /**
     * Constructs an enum with the given name. The new enum is
     * automatically added to the list returned by {@link #values}.
     *
     * @param name The enum name. This name must not be in use by an other enum of this type.
     * @param isFloating {@code true} if the precision model uses floating point math
     *        (rather then a grid).
     */
    private PrecisionType(final String name, final boolean isFloating) {
        super(name, VALUES);
        this.isFloating = isFloating;
    }

    /**
     * Constructs an enum with a default {@code isFloating} value.
     * This is needed for {@link CodeList#valueOf} reflection.
     */
    private PrecisionType(final String name) {
        this(name, true);
    }

    /**
     * Returns {@code true} if {@code PrecisionModelType} is a represented using floating point
     * arithmatic (rather then a grid).
     *
     * @return true if floating point arithmatic is used.
     */
    public boolean isFloating(){
        return isFloating;
    }

    /**
     * Returns the list of {@code PrecisionModelType}s.
     *
     * @return The list of codes declared in the current JVM.
     */
    public static PrecisionType[] values() {
        synchronized (VALUES) {
            return VALUES.toArray(new PrecisionType[VALUES.size()]);
        }
    }

    /**
     * Returns the list of enumerations of the same kind than this enum.
     */
    public PrecisionType[] family() {
        return values();
    }

    /**
     * Returns the precision type that matches the given string, or returns a
     * new one if none match it.
     *
     * @param code The name of the code to fetch or to create.
     * @return A code matching the given name.
     */
    public static PrecisionType valueOf(String code) {
        return valueOf(PrecisionType.class, code);
    }
}
