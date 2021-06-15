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

package org.geotools.grid;

import java.util.Collection;

import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.grid.ortholine.OrthoLineBuilder;
import org.geotools.grid.ortholine.OrthoLineDef;
import org.geotools.grid.ortholine.OrthoLineFeatureBuilder;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


/**
 * A utility class to create line grids with basic attributes. 
 * @author mbedward
 * @since 8.0
 *
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class Lines {

    /**
     * Creates a grid of ortho-lines. Lines are parallel to the bounding envelope's
     * X-axis, Y-axis or both according to the provided line definitions.
     * 
     * @param bounds the bounding envelope
     * @param lineDefs one or more ortho-line definitions
     * @return the vector grid of lines
     * 
     * @see OrthoLineDef
     */
    public static SimpleFeatureSource createOrthoLines(ReferencedEnvelope bounds,
            Collection<OrthoLineDef> lineDefs) {

        return createOrthoLines(bounds, lineDefs, 0.0);
    }

    /**
     * Creates a grid of ortho-lines. Lines are parallel to the bounding envelope's
     * X-axis, Y-axis or both according to the provided line definitions.
     * Densified lines (lines strings with additional vertices along their length) can be
     * created by setting the value of {@code vertexSpacing} greater than zero; if so, any
     * lines more than twice as long as this value will be densified.
     * 
     * @param bounds the bounding envelope
     * @param lineDefs one or more ortho-line definitions
     * @param vertexSpacing maximum distance between adjacent vertices along a line
     * @return the vector grid of lines
     */
    public static SimpleFeatureSource createOrthoLines(ReferencedEnvelope bounds,
            Collection<OrthoLineDef> lineDefs, double vertexSpacing) {

        return createOrthoLines(bounds, lineDefs, vertexSpacing,
                new OrthoLineFeatureBuilder(bounds.getCoordinateReferenceSystem()));
    }

    /**
     * Creates a grid of ortho-lines. Lines are parallel to the bounding envelope's
     * X-axis, Y-axis or both according to the provided line definitions. 
     * Line features will be created using the supplied feature builder.
     * Densified lines (lines strings with additional vertices along their length) can be
     * created by setting the value of {@code vertexSpacing} greater than zero; if so, any
     * lines more than twice as long as this value will be densified.
     * 
     * @param bounds the bounding envelope
     * @param lineDefs one or more ortho-line definitions
     * @param vertexSpacing maximum distance between adjacent vertices along a line
     * @param lineFeatureBuilder feature build to create line features
     * @return the vector grid of lines
     */
    public static SimpleFeatureSource createOrthoLines(ReferencedEnvelope bounds, 
            Collection<OrthoLineDef> lineDefs,
            double vertexSpacing,
            GridFeatureBuilder lineFeatureBuilder) {

        if (bounds == null || bounds.isEmpty() || bounds.isNull()) {
            throw new IllegalArgumentException("The bounds should not be null or empty");
        }

        if (lineDefs == null || lineDefs.isEmpty()) {
            throw new IllegalArgumentException("One or more line controls must be provided");
        }

        CoordinateReferenceSystem boundsCRS = bounds.getCoordinateReferenceSystem();
        CoordinateReferenceSystem builderCRS = 
                lineFeatureBuilder.getType().getCoordinateReferenceSystem();
        if (boundsCRS != null && builderCRS != null &&
                !CRS.equalsIgnoreMetadata(boundsCRS, builderCRS)) {
            throw new IllegalArgumentException("Different CRS set for bounds and the feature builder");
        }

        final ListFeatureCollection fc = new ListFeatureCollection(lineFeatureBuilder.getType());
        OrthoLineBuilder lineBuilder = new OrthoLineBuilder(bounds);
        lineBuilder.buildGrid(lineDefs, lineFeatureBuilder, vertexSpacing, fc);
        return DataUtilities.source(fc);
    }

}
