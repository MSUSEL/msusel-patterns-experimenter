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
 */
package org.geotools.resources;

import java.util.Locale;
import java.text.FieldPosition;

import org.opengis.metadata.extent.GeographicBoundingBox;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.CoordinateOperationFactory;
import org.opengis.referencing.operation.TransformException;
import org.opengis.geometry.Envelope;

import org.geotools.factory.Hints;
import org.geotools.measure.Latitude;
import org.geotools.measure.Longitude;
import org.geotools.measure.AngleFormat;
import org.geotools.metadata.iso.extent.GeographicBoundingBoxImpl;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.TransformPathNotFoundException;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;


/**
 * Provides convenience methods for {@linkplain GeographicBoundingBox geographic bounding boxes}.
 * This is mostly a helper class for {@link GeographicBoundingBoxImpl}; users should not use this
 * class directly.
 *
 * @since 2.4
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 */
public final class BoundingBoxes {
    /**
     * A set of hints used in order to fetch lenient coordinate operation factory. We accept
     * lenient transforms because {@link GeographicBoundingBox} are usually for approximative
     * bounds (e.g. the area of validity of some CRS). If a user wants accurate bounds, he
     * should probably use an {@link Envelope} with the appropriate CRS.
     */
    private static final Hints LENIENT = new Hints(Hints.LENIENT_DATUM_SHIFT, Boolean.TRUE);

    /**
     * Prevents the creation of instances of this class.
     */
    private BoundingBoxes() {
    }

    /**
     * Initializes a geographic bounding box from the specified envelope. If the envelope contains
     * a CRS, then the bounding box will be projected to a geographic CRS. Otherwise, the envelope
     * is assumed already in appropriate CRS.
     *
     * @param envelope The source envelope.
     * @param box The target bounding box.
     */
    public static void copy(Envelope envelope, final GeographicBoundingBoxImpl box)
            throws TransformException
    {
        final CoordinateReferenceSystem crs = envelope.getCoordinateReferenceSystem();
        if (crs != null) {
            final GeographicCRS standardCRS = CRSUtilities.getStandardGeographicCRS2D(crs);
            if (!startsWith(crs, standardCRS) &&
                !startsWith(crs, DefaultGeographicCRS.WGS84) &&
                !startsWith(crs, DefaultGeographicCRS.WGS84_3D))
            {
                final CoordinateOperation operation;
                final CoordinateOperationFactory factory;
                factory = ReferencingFactoryFinder.getCoordinateOperationFactory(LENIENT);
                try {
                    operation = factory.createOperation(crs, standardCRS);
                } catch (FactoryException exception) {
                    throw new TransformPathNotFoundException(Errors.format(
                              ErrorKeys.CANT_TRANSFORM_ENVELOPE, exception));
                }
                envelope = CRS.transform(operation, envelope);
            }
        }
        box.setWestBoundLongitude(envelope.getMinimum(0));
        box.setEastBoundLongitude(envelope.getMaximum(0));
        box.setSouthBoundLatitude(envelope.getMinimum(1));
        box.setNorthBoundLatitude(envelope.getMaximum(1));
    }

    /**
     * Returns {@code true} if the specified {@code crs} starts with the specified {@code head}.
     */
    private static final boolean startsWith(final CoordinateReferenceSystem crs,
                                            final CoordinateReferenceSystem head)
    {
        final int dimension = head.getCoordinateSystem().getDimension();
        return crs.getCoordinateSystem().getDimension() >= dimension &&
               CRS.equalsIgnoreMetadata(CRSUtilities.getSubCRS(crs, 0, dimension), head);
    }

    /**
     * Returns a string representation of the specified extent using the specified angle
     * pattern and locale. See {@link AngleFormat} for a description of angle patterns.
     *
     * @param box     The bounding box to format.
     * @param pattern The angle pattern (e.g. {@code DD°MM'SS.s"}.
     * @param locale  The locale, or {@code null} for the default one.
     */
    public static String toString(final GeographicBoundingBox box,
                                  final String pattern, final Locale locale)
    {
        final AngleFormat format;
        format = (locale!=null) ? new AngleFormat(pattern, locale) : new AngleFormat(pattern);
        final FieldPosition pos = new FieldPosition(0);
        final StringBuffer buffer = new StringBuffer();
        format.format(new  Latitude(box.getNorthBoundLatitude()), buffer, pos).append(", ");
        format.format(new Longitude(box.getWestBoundLongitude()), buffer, pos).append(" - ");
        format.format(new  Latitude(box.getSouthBoundLatitude()), buffer, pos).append(", ");
        format.format(new Longitude(box.getEastBoundLongitude()), buffer, pos);
        return buffer.toString();
    }
}
