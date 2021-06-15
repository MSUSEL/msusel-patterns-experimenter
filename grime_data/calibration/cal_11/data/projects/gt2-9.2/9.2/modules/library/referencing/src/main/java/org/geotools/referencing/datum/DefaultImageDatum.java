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
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.opengis.referencing.datum.ImageDatum;
import org.opengis.referencing.datum.PixelInCell;

import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.referencing.wkt.Formatter;
import org.geotools.util.Utilities;


/**
 * Defines the origin of an image coordinate reference system. An image datum is used in a local
 * context only. For an image datum, the anchor point is usually either the centre of the image
 * or the corner of the image.
 *
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @since 2.1
 */
public class DefaultImageDatum extends AbstractDatum implements ImageDatum {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -4304193511244150936L;

    /**
     * Specification of the way the image grid is associated with the image data attributes.
     */
    private final PixelInCell pixelInCell;

    /**
     * Constructs a new datum with the same values than the specified one.
     * This copy constructor provides a way to wrap an arbitrary implementation into a
     * Geotools one or a user-defined one (as a subclass), usually in order to leverage
     * some implementation-specific API. This constructor performs a shallow copy,
     * i.e. the properties are not cloned.
     *
     * @param datum The datum to copy.
     *
     * @since 2.2
     */
    public DefaultImageDatum(final ImageDatum datum) {
        super(datum);
        pixelInCell = datum.getPixelInCell();
    }

    /**
     * Constructs an image datum from a name.
     *
     * @param name The datum name.
     * @param pixelInCell the way the image grid is associated with the image data attributes.
     */
    public DefaultImageDatum(final String name, final PixelInCell pixelInCell) {
        this(Collections.singletonMap(NAME_KEY, name), pixelInCell);
    }

    /**
     * Constructs an image datum from a set of properties. The properties map is given
     * unchanged to the {@linkplain AbstractDatum#AbstractDatum(Map) super-class constructor}.
     *
     * @param properties  Set of properties. Should contains at least {@code "name"}.
     * @param pixelInCell the way the image grid is associated with the image data attributes.
     */
    public DefaultImageDatum(final Map<String,?> properties, final PixelInCell pixelInCell) {
        super(properties);
        this.pixelInCell = pixelInCell;
        ensureNonNull("pixelInCell", pixelInCell);
    }

    /**
     * Specification of the way the image grid is associated with the image data attributes.
     *
     * @return The way image grid is associated with image data attributes.
     */
    public PixelInCell getPixelInCell() {
        return pixelInCell;
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
        if (super.equals(object, compareMetadata)) {
            final DefaultImageDatum that = (DefaultImageDatum) object;
            return Utilities.equals(this.pixelInCell, that.pixelInCell);
        }
        return false;
    }

    /**
     * Returns a hash value for this image datum. {@linkplain #getName Name},
     * {@linkplain #getRemarks remarks} and the like are not taken in account. In
     * other words, two image datums will return the same hash value if they
     * are equal in the sense of
     * <code>{@link #equals equals}(AbstractIdentifiedObject, <strong>false</strong>)</code>.
     *
     * @return The hash code value. This value doesn't need to be the same
     *         in past or future versions of this class.
     */
    @Override
    public int hashCode() {
        return super.hashCode() ^ pixelInCell.hashCode();
    }

    /**
     * Format the inner part of a
     * <A HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html"><cite>Well
     * Known Text</cite> (WKT)</A> element.
     *
     * <strong>Note:</strong> WKT of image datum is not yet part of OGC specification.
     *
     * @param  formatter The formatter to use.
     * @return The WKT element name.
     */
    @Override
    protected String formatWKT(final Formatter formatter) {
        super.formatWKT(formatter);
        formatter.append(pixelInCell);
        formatter.setInvalidWKT(ImageDatum.class);
        return "IMAGE_DATUM";
    }
}
