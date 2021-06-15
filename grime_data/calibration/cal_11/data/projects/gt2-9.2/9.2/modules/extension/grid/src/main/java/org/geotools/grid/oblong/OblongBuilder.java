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

package org.geotools.grid.oblong;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.grid.Neighbor;
import org.geotools.grid.PolygonBuilder;
import org.geotools.grid.PolygonElement;

/**
 * Used by {@code Oblongs} class to build grids.
 *
 * @author mbedward
 * @since 2.7
 *
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class OblongBuilder extends PolygonBuilder {
    private final double elementWidth;
    private final double elementHeight;

    OblongBuilder(ReferencedEnvelope bounds, double width, double height) {
        super(bounds);
        this.elementWidth = width;
        this.elementHeight = height;
    }

    @Override
    public boolean isValidNeighbor(Neighbor neighbor) {
        return true;
    }

    /**
     * Creates a new {@code Oblong} positioned at the given neighbor position
     * relative to the reference element.
     *
     * @param el the reference oblong
     *
     * @param neighbor a neighbour position
     *
     * @return a new {@code Oblong} object
     *
     * @throws IllegalArgumentException if either argument is {@code null} or
     *         if {@code el} is not an instance of {@code Oblong}
     */
    @Override
    public Oblong createNeighbor(PolygonElement el, Neighbor neighbor) {
        if (el == null || neighbor == null) {
            throw new IllegalArgumentException(
                    "el and neighbour position must both be non-null");
        }

        if (!(el instanceof Oblong)) {
            throw new IllegalArgumentException("el must be an instance of Oblong");
        }

        Oblong oblong = (Oblong) el;
        ReferencedEnvelope bounds = oblong.getBounds();
        double dx, dy;

        switch (neighbor) {
            case LEFT:
                dx = -bounds.getWidth();
                dy = 0.0;
                break;

            case LOWER:
                dx = 0.0;
                dy = -bounds.getHeight();
                break;

            case LOWER_LEFT:
                dx = -bounds.getWidth();
                dy = -bounds.getHeight();
                break;

            case LOWER_RIGHT:
                dx = bounds.getWidth();
                dy = -bounds.getHeight();
                break;

            case RIGHT:
                dx = bounds.getWidth();
                dy = 0.0;
                break;

            case UPPER:
                dx = 0.0;
                dy = bounds.getHeight();
                break;

            case UPPER_LEFT:
                dx = -bounds.getWidth();
                dy = bounds.getHeight();
                break;

            case UPPER_RIGHT:
                dx = bounds.getWidth();
                dy = bounds.getHeight();
                break;

            default:
                throw new IllegalArgumentException("Unrecognized value for neighbor");
        }

        return Oblongs.create(bounds.getMinX() + dx, bounds.getMinY() + dy,
                bounds.getWidth(), bounds.getHeight(),
                bounds.getCoordinateReferenceSystem());
    }

    @Override
    public boolean isValidDenseVertexSpacing(double v) {
        return v > 0 && v < Math.min(elementWidth, elementHeight) / 2.0;
    }

    @Override
    public PolygonElement getFirstElement() {
        return Oblongs.create(
                gridBounds.getMinX(), gridBounds.getMinY(),
                elementWidth, elementHeight, gridBounds.getCoordinateReferenceSystem());
    }

    @Override
    public PolygonElement getNextXElement(PolygonElement el) {
        return createNeighbor(el, Neighbor.RIGHT);
    }

    @Override
    public PolygonElement getNextYElement(PolygonElement el) {
        return createNeighbor(el, Neighbor.UPPER);
    }

}
