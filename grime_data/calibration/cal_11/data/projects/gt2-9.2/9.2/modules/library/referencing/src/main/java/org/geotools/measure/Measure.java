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
 *    (C) 1999-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.measure;

import javax.measure.unit.Unit;
import org.geotools.util.Utilities;


/**
 * A scalar with an unit.
 *
 * @since 2.1
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (PMO, IRD)
 */
public final class Measure extends Number {
    /**
     * For compatibility with different versions.
     */
    private static final long serialVersionUID = 6917234039472328164L;

    /**
     * The scalar value.
     */
    private final double value;

    /**
     * The unit.
     */
    private final Unit<?> unit;

    /**
     * Creates a new measure with the specified value and unit.
     *
     * @param value The value.
     * @param unit The unit of measurement for the given value.
     */
    public Measure(final double value, final Unit<?> unit) {
        this.value = value;
        this.unit  = unit;
    }

    /** Returns the scalar value. */           public double doubleValue() {return         value;}
    /** Returns the scalar value. */           public float   floatValue() {return (float) value;}
    /** Returns the scalar value. */           public long     longValue() {return (long)  value;}
    /** Returns the scalar value. */           public int       intValue() {return (int)   value;}
    /** Returns the scalar value. */ @Override public short   shortValue() {return (short) value;}
    /** Returns the scalar value. */ @Override public byte     byteValue() {return (byte)  value;}

    /**
     * Returns the unit.
     *
     * @return The unit of measurement.
     */
    public Unit<?> getUnit() {
        return unit;
    }

    /**
     * Returns a hash code value for this measure.
     */
    @Override
    public int hashCode() {
        long code = Double.doubleToLongBits(value);
        return (int) code ^ (int)(code >>> 32) ^ unit.hashCode();
    }

    /**
     * Compares this measure with the specified object for equality.
     *
     * @param object The object to compare with this measure.
     * @return {@code true} if the given object is equals to this measure.
     */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof Measure) {
            final Measure that = (Measure) object;
            return Utilities.equals(value, that.value) &&
                   Utilities.equals(unit,  that.unit);
        }
        return false;
    }

    /**
     * Returns a string representation of this measure.
     */
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append(value);
        buffer.append(' ');
        buffer.append(unit);
        return buffer.toString();
    }
}
