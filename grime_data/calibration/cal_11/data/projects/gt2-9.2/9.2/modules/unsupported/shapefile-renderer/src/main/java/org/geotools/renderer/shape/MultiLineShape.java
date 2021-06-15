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
package org.geotools.renderer.shape;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;


/**
 * A shape for drawing on a graphics2d
 *
 * @author jeichar
 *
 * @since 2.1.x
 *
 *
 *
 * @source $URL$
 */
public class MultiLineShape extends AbstractShape implements Shape {
    public MultiLineShape(SimpleGeometry geom) {
        super(geom);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.awt.Shape#getPathIterator(java.awt.geom.AffineTransform)
     */
    public PathIterator getPathIterator(final AffineTransform at) {
        return new PathIterator() {
                int currentPart = 0;
                int currentIndex = 0;

                public int getWindingRule() {
                    return WIND_NON_ZERO;
                }

                public boolean isDone() {
                    // TODO Auto-generated method stub
                    return currentPart == geom.coords.length;
                }

                public void next() {
                    if (isDone()) {
                        return;
                    }

                    if ((currentIndex + 1) >= geom.coords[currentPart].length) {
                        currentIndex = 0;
                        currentPart++;
                    } else {
                        currentIndex++;
                    }
                }

                public int currentSegment(float[] coords) {
                    if (currentIndex == 0) {
                        coords[0] = (float) geom.coords[currentPart][currentIndex];
                        currentIndex++;
                        coords[1] = (float) geom.coords[currentPart][currentIndex];

                        if (at != null) {
                            at.transform(coords, 0, coords, 0, 1);
                        }

                        return SEG_MOVETO;
                    } else {
                        coords[0] = (float) geom.coords[currentPart][currentIndex];
                        currentIndex++;
                        coords[1] = (float) geom.coords[currentPart][currentIndex];

                        if (at != null) {
                            at.transform(coords, 0, coords, 0, 1);
                        }

                        return SEG_LINETO;
                    }
                }

                public int currentSegment(double[] coords) {
                    if (currentIndex == 0) {
                        coords[0] = (float) geom.coords[currentPart][currentIndex];
                        currentIndex++;
                        coords[1] = (float) geom.coords[currentPart][currentIndex];

                        if (at != null) {
                            at.transform(coords, 0, coords, 0, 1);
                        }

                        return SEG_MOVETO;
                    } else {
                        coords[0] = (float) geom.coords[currentPart][currentIndex];
                        currentIndex++;
                        coords[1] = (float) geom.coords[currentPart][currentIndex];

                        if (at != null) {
                            at.transform(coords, 0, coords, 0, 1);
                        }

                        return SEG_LINETO;
                    }
                }
            };
    }

    /*
     * (non-Javadoc)
     *
     * @see java.awt.Shape#getPathIterator(java.awt.geom.AffineTransform,
     *      double)
     */
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return getPathIterator(at);
    }
}
