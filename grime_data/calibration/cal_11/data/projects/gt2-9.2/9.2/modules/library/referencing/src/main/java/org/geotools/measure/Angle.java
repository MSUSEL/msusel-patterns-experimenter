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

import java.io.Serializable;
import java.text.Format;
import java.text.ParseException;
import java.util.Locale;

import org.geotools.resources.ClassChanger;


/**
 * An angle in degrees. An angle is the amount of rotation needed to bring one line or plane
 * into coincidence with another, generally measured in degrees, sexagesimal degrees or grads.
 *
 * @since 2.0
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (PMO, IRD)
 *
 * @see Latitude
 * @see Longitude
 * @see AngleFormat
 */
public class Angle implements Comparable<Angle>, Serializable {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 1158747349433104534L;

    /**
     * A shared instance of {@link AngleFormat}.
     */
    private static Format format;

    /**
     * Define how angle can be converted to {@link Number} objects.
     */
    static {
        ClassChanger.register(new ClassChanger<Angle,Double>(Angle.class, Double.class) {
            protected Double convert(final Angle o) {
                return o.theta;
            }

            protected Angle inverseConvert(final Double value) {
                return new Angle(value);
            }
        });
    }

    /**
     * Angle value in degres.
     */
    private final double theta;

    /**
     * Contructs a new angle with the specified value.
     *
     * @param theta Angle in degrees.
     */
    public Angle(final double theta) {
        this.theta = theta;
    }

    /**
     * Constructs a newly allocated {@code Angle} object that represents the angle value
     * represented by the string. The string should represents an angle in either fractional
     * degrees (e.g. 45.5°) or degrees with minutes and seconds (e.g. 45°30').
     *
     * @param  string A string to be converted to an {@code Angle}.
     * @throws NumberFormatException if the string does not contain a parsable angle.
     */
    public Angle(final String string) throws NumberFormatException {
        final Format format = getAngleFormat();
        final Angle theta;
        try {
            synchronized (Angle.class) {
                theta = (Angle) format.parseObject(string);
            }
        } catch (ParseException exception) {
            NumberFormatException e = new NumberFormatException(exception.getLocalizedMessage());
            e.initCause(exception);
            throw e;
        }
        if (getClass().isAssignableFrom(theta.getClass())) {
            this.theta = theta.theta;
        } else {
            throw new NumberFormatException(string);
        }
    }

    /**
     * Returns the angle value in degrees.
     */
    public double degrees() {
        return theta;
    }

    /**
     * Returns the angle value in radians.
     */
    public double radians() {
        return Math.toRadians(theta);
    }

    /**
     * Returns a hash code for this {@code Angle} object.
     */
    @Override
    public int hashCode() {
        final long code = Double.doubleToLongBits(theta);
        return (int) code ^ (int) (code >>> 32);
    }

    /**
     * Compares the specified object with this angle for equality.
     */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (object!=null && getClass().equals(object.getClass())) {
            final Angle that = (Angle) object;
            return Double.doubleToLongBits(this.theta) ==
                   Double.doubleToLongBits(that.theta);
        }  else {
            return false;
        }
    }

    /**
     * Compares two {@code Angle} objects numerically. The comparaison
     * is done as if by the {@link Double#compare(double,double)} method.
     */
    public int compareTo(final Angle that) {
        return Double.compare(this.theta, that.theta);
    }

    /**
     * Returns a string representation of this {@code Angle} object.
     */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        synchronized (Angle.class) {
            final Format format = getAngleFormat();
            buffer = format.format(this, buffer, null);
        }
        return buffer.toString();
    }

    /**
     * Returns a shared instance of {@link AngleFormat}. The return type is
     * {@link Format} in order to avoid class loading before necessary.
     */
    private static Format getAngleFormat() {
        assert Thread.holdsLock(Angle.class);
        if (format == null) {
            format = new AngleFormat("D°MM.m'", Locale.US);
        }
        return format;
    }
}
