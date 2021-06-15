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
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation.builder;

import org.geotools.geometry.DirectPosition2D;
import org.opengis.geometry.DirectPosition;


/**
 * Simple Circle focused on Delaunays triangulation.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Jan Jezek
 */
class Circle {
    /** Coordinates of center. */
    private DirectPosition2D center;

    /** Value of radius */
    private double radius;

    /** Tolerance for the cotains method. */
    private double tolerance = 0.0001;

    /**
     * Creates a circle with center [0,0] and radius = 0.
     *
     */
    protected Circle() {
        this(new DirectPosition2D(0, 0), 0);
    }

    /**
     * Creates a circle using the specified center and radius.
     * @param center of the circle.
     * @param radius of the circle.
     */
    protected Circle(DirectPosition center, double radius) {
        this.center = new DirectPosition2D(center);
        this.radius = radius;
    }

    /**
     * Sets the center.
     *
     * @param center coordinates
     */
    protected void setCenter(DirectPosition center) {
        this.center = new DirectPosition2D(center);
    }

    /**
     * Sets the radius.
     *
     * @param radius value
     */
    protected void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * Returns the coordinate of the center.
     *
     * @return center coordinates
     */
    protected DirectPosition getCenter() {
        return center;
    }

    /**
     * Returns the radius.
     *
     * @return radius value.
     */
    protected double getRadius() {
        return radius;
    }

    /**
     * Sets the tolerance for the contains method.
     *
     * @param tolerance value
     */
    protected void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    /**
     * Raturns the tolerance
     *
     * @return tolerance value
     */
    protected double getTolerance() {
        return tolerance;
    }

    /**
     * The contains test whether the coordinate p is within the circle.
     * Triangle contains coordinate if the distance  between center and p is
     * smaller then the radius that is reduced by tolerance. This is used for
     * triangulation when there are four points on one circle to avoid
     * neverending loop.
     *
     * @param p - the point to be tested
     *
     * @return True if the circle contais p, False if not.
     */
    protected boolean contains(DirectPosition p) {
        if (center.distance(new DirectPosition2D(p)) < (this.radius - tolerance)) {
            return true;
        } else {
            return false;
        }
    }
}
