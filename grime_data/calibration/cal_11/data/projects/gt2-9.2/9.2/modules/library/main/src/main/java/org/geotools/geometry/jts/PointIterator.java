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
package org.geotools.geometry.jts;

import java.awt.geom.AffineTransform;



import com.vividsolutions.jts.geom.Point;


/**
 * A path iterator for the LiteShape class, specialized to iterate over Point objects.
 *
 * @author Andrea Aime
 *
 *
 * @source $URL$
 */
public final class PointIterator extends AbstractLiteIterator {
    /** Transform applied on the coordinates during iteration */
    private AffineTransform at;
    
    /** The point we are going to provide when asked for coordinates */
    private Point point;
    
    /** True when the point has been read once */
    private boolean done;
    
    /** Current coordinate */
    private boolean moved;

    /**
     * Creates a new PointIterator object.
     *
     * @param point The point
     * @param at The affine transform applied to coordinates during iteration
     */
    public PointIterator(Point point, AffineTransform at) {
        if (at == null) {
            at = new AffineTransform();
        }
        
        this.at = at;
        this.point = point;
        done = false;
        moved = false;
    }

    /**
     * Return the winding rule for determining the interior of the path.
     *
     * @return <code>WIND_EVEN_ODD</code> by default.
     */
    public int getWindingRule() {
        return WIND_EVEN_ODD;
    }

    /**
     * @see java.awt.geom.PathIterator#next()
     */
    public void next() {
        done = true;
    }

    /**
     * @see java.awt.geom.PathIterator#isDone()
     */
    public boolean isDone() {
        return done && moved;
    }

    /**
     * @see java.awt.geom.PathIterator#currentSegment(double[])
     */
    public int currentSegment(double[] coords) {
        if (!done && !moved) {
            coords[0] = point.getX();
            coords[1] = point.getY();
            at.transform(coords, 0, coords, 0, 1);

            return SEG_MOVETO;
        } else {
            coords[0] = point.getX();
            coords[1] = point.getY();
            at.transform(coords, 0, coords, 0, 1);

            moved = true;
            return SEG_LINETO;
        }
    }

}
