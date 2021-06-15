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


/**
 * Specifies the precision model of the {@linkplain DirectPosition direct positions}
 * in a {@linkplain Geometry geometry}.
 * <p>
 * A precision model defines a grid of allowable points. The {@link #round} method
 * allows to round a direct position to the nearest allowed point. The {@link #getType}
 * method describes the collapsing behavior of a direct position.
 * <p>
 * {@code Precision} instances can be sorted by their {@linkplain #getScale scale}.
 * </p>
 *
 * @author Jody Garnett
 * @since GeoAPI 2.1
 *
 *
 * @source $URL$
 */
public interface Precision extends Comparable<Precision> {
    /**
     * Compares this precision model with the specified one. Returns -1 is this model is
     * less accurate than the other one, +1 if it is more accurate, or 0 if they have the
     * same accuracy.
     *
     * @param other Other precision model to compare against.
     * @return a negative integer, zero, or a positive integer as this object
     *      is less than, equal to, or greater than the other.
     */
    int compareTo(Precision other);

    /**
     * Multiplying factor used to obtain a precise ordinate.
     * <p>
     * Multiply by this value and then divide by this value to round correctly:
     *
     * <blockquote><pre>
     * double scale = pm.getScale();
     * return Math.round(value * scale) / scale;
     * </pre></blockquote>
     *
     * So to round to {@code 3} significant digits we would have a scale of {@code 1000}.
     * Tip: the number of significant digits can be computed as below:
     *
     * <blockquote><pre>
     * int significantDigits = (int) Math.ceil(Math.log10(pm.getScale()));
     * </pre></blockquote>
     *
     * @return Multiplying factor used before rounding.
     */
    double getScale();

    /**
     * Returns the type of this precision model.
     */
    PrecisionType getType();

    /**
     * Rounds a direct position to this precision model in place.
     * <p>
     * It is likely that a {@code Precision} instance will keep different rounding rules for different
     * axis (example <var>x</var> & <var>y</var> ordinates may be handled differently then height),
     * by always rounding a direct position as a whole we will enable this functionality.
     */
    void round(DirectPosition position);
}
