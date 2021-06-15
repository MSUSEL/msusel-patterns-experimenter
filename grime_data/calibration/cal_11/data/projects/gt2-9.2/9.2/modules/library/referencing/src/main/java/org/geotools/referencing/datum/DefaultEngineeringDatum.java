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
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.referencing.datum;

import java.util.Collections;
import java.util.Map;

import org.opengis.referencing.datum.EngineeringDatum;
import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.referencing.wkt.Formatter;
import org.geotools.resources.i18n.VocabularyKeys;


/**
 * Defines the origin of an engineering coordinate reference system. An engineering datum is used
 * in a region around that origin. This origin can be fixed with respect to the earth (such as a
 * defined point at a construction site), or be a defined point on a moving vehicle (such as on a
 * ship or satellite).
 *
 * @since 2.1
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class DefaultEngineeringDatum extends AbstractDatum implements EngineeringDatum {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 1498304918725248637L;

    /**
     * An engineering datum for unknown coordinate reference system. Such CRS are usually
     * assumed cartesian, but will not have any transformation path to other CRS.
     *
     * @see org.geotools.referencing.crs.DefaultEngineeringCRS#CARTESIAN_2D
     * @see org.geotools.referencing.crs.DefaultEngineeringCRS#CARTESIAN_3D
     * 
     * @deprecated Use UKNONWN instead
     */
    public static final DefaultEngineeringDatum UNKNOW =
            new DefaultEngineeringDatum(name(VocabularyKeys.UNKNOW));
    
    /**
     * An engineering datum for unknown coordinate reference system. Such CRS are usually
     * assumed cartesian, but will not have any transformation path to other CRS.
     *
     * @see org.geotools.referencing.crs.DefaultEngineeringCRS#CARTESIAN_2D
     * @see org.geotools.referencing.crs.DefaultEngineeringCRS#CARTESIAN_3D
     */
    public static final DefaultEngineeringDatum UNKNOWN =
            new DefaultEngineeringDatum(name(VocabularyKeys.UNKNOWN));

    /**
     * Constructs a new datum with the same values than the specified one.
     * This copy constructor provides a way to wrap an arbitrary implementation into a
     * Geotools one or a user-defined one (as a subclass), usually in order to leverage
     * some implementation-specific API. This constructor performs a shallow copy,
     * i.e. the properties are not cloned.
     *
     * @since 2.2
     */
    public DefaultEngineeringDatum(final EngineeringDatum datum) {
        super(datum);
    }

    /**
     * Constructs an engineering datum from a name.
     *
     * @param name The datum name.
     */
    public DefaultEngineeringDatum(final String name) {
        this(Collections.singletonMap(NAME_KEY, name));
    }

    /**
     * Constructs an engineering datum from a set of properties. The properties map is given
     * unchanged to the {@linkplain AbstractDatum#AbstractDatum(Map) super-class constructor}.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     */
    public DefaultEngineeringDatum(final Map<String,?> properties) {
        super(properties);
    }

    /**
     * Compare this datum with the specified object for equality.
     *
     * @param  object The object to compare to {@code this}.
     * @param  compareMetadata {@code true} for performing a strict comparaison, or
     *         {@code false} for comparing only properties relevant to transformations.
     * @return {@code true} if both objects are equal.
     */
    @Override
    public boolean equals(final AbstractIdentifiedObject object, final boolean compareMetadata) {
        if (object == this) {
            return true; // Slight optimization.
        }
        return super.equals(object, compareMetadata);
    }

    /**
     * Format the inner part of a
     * <A HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html"><cite>Well
     * Known Text</cite> (WKT)</A> element.
     *
     * @param  formatter The formatter to use.
     * @return The WKT element name, which is "LOCAL_DATUM"
     */
    @Override
    protected String formatWKT(final Formatter formatter) {
        super.formatWKT(formatter);
        return "LOCAL_DATUM";
    }
}
