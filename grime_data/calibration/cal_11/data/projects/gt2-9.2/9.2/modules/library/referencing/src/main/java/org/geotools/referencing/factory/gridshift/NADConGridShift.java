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
 *    (C) 2002-2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory.gridshift;

import org.geotools.referencing.operation.builder.LocalizationGrid;

/**
 * A NADCON localization grid
 * 
 * @author Andrea Aime - GeoSolutions
 * @author Rueben Schulz
 */
public class NADConGridShift extends LocalizationGrid {
    
    /**
     * The minimum longitude value covered by this grid (decimal degrees)
     */
    private double minX;

    /**
     * The minimum latitude value covered by this grid (decimal degrees)
     */
    private double minY;

    /**
     * The maximum longitude value covered by this grid (decimal degrees)
     */
    private double maxX;

    /**
     * The maximum latitude value covered by this grid (decimal degrees)
     */
    private double maxY;

    /**
     * The difference between longitude grid points (decimal degrees)
     */
    private double dx;

    /**
     * The difference between latitude grid points (decimal degrees)
     */
    private double dy;

    public NADConGridShift(double xmin, double ymin, double xmax, double ymax, double dx, double dy, int width, int height) {
        super(width, height);
        this.minX = xmin;
        this.maxX = xmax;
        this.minY = ymin;
        this.maxY = ymax;
        this.dx = dx;
        this.dy = dy;
    }
    
    /**
     * Returns a hash value for this transform. To make this faster it does not
     * check the grid values.
     *
     * @return a hash value for this transform.
     */
    @Override
    public final int hashCode() {
        final long code = Double.doubleToLongBits(minX)
            + (37 * (Double.doubleToLongBits(minY)
            + (37 * (Double.doubleToLongBits(maxX)
            + (37 * (Double.doubleToLongBits(maxY)
            + (37 * (Double.doubleToLongBits(dx)
            + (37 * (Double.doubleToLongBits(dy)))))))))));

        return (int) code ^ (int) (code >>> 32);
    }

    /**
     * Compares the specified object with this math transform for equality.
     *
     * @param object the object to compare to
     * @return {@code true} if the objects are equal.
     */
    @Override
    public final boolean equals(final Object object) {
        if (object == this) {
            // Slight optimization
            return true;
        }

        if (super.equals(object)) {
            final NADConGridShift that = (NADConGridShift) object;

            return (Double.doubleToLongBits(this.minX) == Double.doubleToLongBits(that.minX))
                && (Double.doubleToLongBits(this.minY) == Double.doubleToLongBits(that.minY))
                && (Double.doubleToLongBits(this.maxX) == Double.doubleToLongBits(that.maxX))
                && (Double.doubleToLongBits(this.maxY) == Double.doubleToLongBits(that.maxY))
                && (Double.doubleToLongBits(this.dx)   == Double.doubleToLongBits(that.dx))
                && (Double.doubleToLongBits(this.dy)   == Double.doubleToLongBits(that.dy));
        }

        return false;
    }

    /**
     * The minimum longitude value covered by this grid (decimal degrees)
     */
    public double getMinX() {
        return minX;
    }

    /**
     * The minimum latitude value covered by this grid (decimal degrees)
     */
    public double getMinY() {
        return minY;
    }

    /**
     * The maximum longitude value covered by this grid (decimal degrees)
     */
    public double getMaxX() {
        return maxX;
    }

    /**
     * The maximum latitude value covered by this grid (decimal degrees)
     */
    public double getMaxY() {
        return maxY;
    }

    /**
     * The difference between longitude grid points (decimal degrees)
     */
    public double getDx() {
        return dx;
    }

    /**
     * The difference between latitude grid points (decimal degrees)
     */
    public double getDy() {
        return dy;
    }


}
