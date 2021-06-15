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

import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.DefaultCoordinateSequenceFactory;

/**
 * This coordinate sequence transformer will take a Geometry and transform in a set
 * of curved lines that will be "flattened" in order to get back a set of straight segments.
 * The error in the transform is linked to the "flattening", the higher the flattening,
 * the bigger the error, but also, the lesser the number of points that will be used
 * to represent the resulting coordinate sequence.
 *
 * @todo Not yet implemented.
 *
 *
 *
 * @source $URL$
 */
public class PreciseCoordinateSequenceTransformer implements CoordinateSequenceTransformer {
    CoordinateSequenceFactory csFactory;
    double flatness;
    
    public PreciseCoordinateSequenceTransformer() {
        csFactory = DefaultCoordinateSequenceFactory.instance();
    }
    
    /**
     * @see org.geotools.geometry.jts.CoordinateSequenceTransformer#transform(com.vividsolutions.jts.geom.CoordinateSequence, org.geotools.ct.MathTransform2D)
     */
    public CoordinateSequence transform(CoordinateSequence cs, MathTransform transform)
            throws TransformException {
//        Coordinate[] scs = cs.toCoordinateArray();
//        GeneralPath path = new GeneralPath();
//        path.moveTo(scs[0].x, scs[0].y);
//        for (int i = 0; i < scs.length; i++) {
//            path.moveTo(scs[0].x, scs[0].y);
//        }
//        Shape transformed = transform.createTransformedShape(path);
//        PathIterator iterator = transformed.getPathIterator(new AffineTransform(), flatness);
//        ArrayList coords = new ArrayList(scs.length);
//        double[] point = new double[6];
//        while(!iterator.isDone()) {
//            iterator.next();
//            iterator.currentSegment(point);
//            coords.add(new Coordinate(point[0], point[1]));
//        }
//        return csFactory.create(coords);
    	return null;
    }

    public double getFlatness() {
        return flatness;
    }
    public void setFlatness(double flatness) {
        this.flatness = flatness;
    }
}
