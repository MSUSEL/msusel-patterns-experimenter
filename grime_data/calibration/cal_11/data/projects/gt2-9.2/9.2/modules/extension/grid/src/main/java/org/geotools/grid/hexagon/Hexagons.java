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
 *    (C) 2010, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.grid.hexagon;

import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.grid.GridFeatureBuilder;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A utilities class with static methods to create and work with hexagonal
 * grid elements.
 *
 * @author mbedward
 * @since 2.7
 *
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class Hexagons {

    private static final double ROOT3 = Math.sqrt(3.0);

    /**
     * Calculates the area of a hexagon with the given side length.
     *
     * @param sideLen side length
     *
     * @return the area
     *
     * @throws IllegalArgumentException if {@code sideLen} is not greater than zero
     */
    public static double sideLengthToArea(double sideLen) {
        if (sideLen <= 0.0) {
            throw new IllegalArgumentException("side length must be > 0");
        }
        return sideLen * sideLen * 1.5 * ROOT3;
    }

    /**
     * Calculates the side length of a hexagon with the given area.
     *
     * @param area the area
     *
     * @return the side length
     *
     * @throws IllegalArgumentException if {@code area} is not greater than zero
     */
    public static double areaToSideLength(double area) {
        if (area <= 0.0) {
            throw new IllegalArgumentException("area must be > 0");
        }
        return Math.sqrt(area * 2.0 / 3.0 / ROOT3);
    }

    /**
     * Creates a new {@code Hexagon} object.
     *
     * @param minX the min X ordinate of the bounding rectangle
     *
     * @param minY the min Y ordinate of the bounding rectangle
     *
     * @param sideLen the side length
     *
     * @param orientation either {@code Hexagon.Orientation.FLAT} or
     *        {@code Hexagon.Orientation.ANGLED}
     *
     * @param crs the coordinate reference system (may be {@code null})
     *
     * @return a new {@code Hexagon} object
     *
     * @throws IllegalArgumentException if {@code sideLen} is {@code <=} 0 or
     *         if {@code orientation} is {@code null}
     */
    public static Hexagon create(double minX, double minY, double sideLen,
            HexagonOrientation orientation, CoordinateReferenceSystem crs) {
        return new HexagonImpl(minX, minY, sideLen, orientation, crs);
    }

    /**
     * Creates a new grid of tesselated hexagons within a bounding rectangle
     * with grid elements represented by simple (ie. undensified) polygons.
     *
     * @param bounds the bounding rectangle
     *
     * @param sideLen hexagon side length
     *
     * @param orientation hexagon orientation
     *
     * @param gridBuilder an instance of {@code GridFeatureBuilder}
     *
     * @return a new grid
     *
     * @throws IllegalArgumentException
     *         if bounds is null or empty; or
     *         if sideLen is {@code <=} 0; or
     *         if the {@code CoordinateReferenceSystems}
     *         set for the bounds and the {@code GridFeatureBuilder} are both
     *         non-null but different
     */
    public static SimpleFeatureSource createGrid(
            ReferencedEnvelope bounds,
            double sideLen,
            HexagonOrientation orientation,
            GridFeatureBuilder gridBuilder) {

        return createGrid(bounds, sideLen, -1, orientation, gridBuilder);
    }
    

    /**
     * Creates a new grid of tesselated hexagons within a bounding rectangle
     * with grid elements represented by densified polygons (ie. additional
     * vertices added to each edge).
     *
     * @param bounds the bounding rectangle
     *
     * @param sideLen hexagon side length
     *
     * @param vertexSpacing maximum distance between adjacent vertices in a grid
     *        element; if {@code <= 0} or {@code >= min(width, height) / 2.0} it
     *        is ignored and the polygons will not be densified
     *
     * @param orientation hexagon orientation
     *
     * @param gridFeatureBuilder an instance of {@code GridFeatureBuilder}
     *
     * @return a new grid
     *
     * @throws IllegalArgumentException
     *         if bounds is null or empty; or
     *         if sideLen is {@code <=} 0; or
     *         if the {@code CoordinateReferenceSystems}
     *         set for the bounds and the {@code GridFeatureBuilder} are both
     *         non-null but different
     */
    public static SimpleFeatureSource createGrid(
            ReferencedEnvelope bounds,
            double sideLen,
            double vertexSpacing,
            HexagonOrientation orientation,
            GridFeatureBuilder gridFeatureBuilder) {
        
        if (bounds == null || bounds.isEmpty() || bounds.isNull()) {
            throw new IllegalArgumentException("bounds should not be null or empty");
        }

        if (sideLen <= 0) {
            throw new IllegalArgumentException("sideLen must be greater than 0");
        }

        if (orientation == null) {
            throw new IllegalArgumentException("orientation should not be null");
        }

        CoordinateReferenceSystem boundsCRS = bounds.getCoordinateReferenceSystem();
        CoordinateReferenceSystem builderCRS = gridFeatureBuilder.getType().getCoordinateReferenceSystem();
        if (boundsCRS != null && builderCRS != null &&
                !CRS.equalsIgnoreMetadata(boundsCRS, builderCRS)) {
            throw new IllegalArgumentException("Different CRS set for bounds and the feature builder");
        }

        final ListFeatureCollection fc = new ListFeatureCollection(gridFeatureBuilder.getType());
        HexagonBuilder gridBuilder = new HexagonBuilder(bounds, sideLen, orientation);
        gridBuilder.buildGrid(gridFeatureBuilder, vertexSpacing, fc);
        return DataUtilities.source(fc);
    }

}
