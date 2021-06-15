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
package org.geotools.data.shapefile.ng.shp;

import java.nio.ByteBuffer;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * Wrapper for a Shapefile point.
 * 
 * @author aaime
 * @author Ian Schneider
 *
 *
 * @source $URL$
 * 
 */
public class PointHandler implements ShapeHandler {

    final ShapeType shapeType;
    GeometryFactory geometryFactory;

    public PointHandler(ShapeType type, GeometryFactory gf) throws ShapefileException {
        if ((type != ShapeType.POINT) && (type != ShapeType.POINTM)
                && (type != ShapeType.POINTZ)) { // 2d, 2d+m, 3d+m
            throw new ShapefileException(
                    "PointHandler constructor: expected a type of 1, 11 or 21");
        }

        shapeType = type;
        this.geometryFactory = gf;
    }

    public PointHandler() {
        shapeType = ShapeType.POINT; // 2d
    }
    
    /**
     * Returns the shapefile shape type value for a point
     * 
     * @return int Shapefile.POINT
     */
    public ShapeType getShapeType() {
        return shapeType;
    }

    public int getLength(Object geometry) {
        int length;
        if (shapeType == ShapeType.POINT) {
            length = 20;
        } else if (shapeType == ShapeType.POINTM) {
            length = 28;
        } else if (shapeType == ShapeType.POINTZ) {
            length = 36;
        } else {
            throw new IllegalStateException("Expected ShapeType of Point, got"
                    + shapeType);
        }
        return length;
    }

    public Object read(ByteBuffer buffer, ShapeType type, boolean flatGeometry) {
        if (type == ShapeType.NULL) {
            return createNull();
        }
        
        int dimension = shapeType == ShapeType.POINTZ && !flatGeometry ? 3 : 2;
        CoordinateSequence cs = geometryFactory.getCoordinateSequenceFactory().create(1, dimension);
        cs.setOrdinate(0, 0, buffer.getDouble());
        cs.setOrdinate(0, 1, buffer.getDouble());

        if (shapeType == ShapeType.POINTM) {
            buffer.getDouble();
        }
        
        if (dimension > 2) {
            cs.setOrdinate(0, 2, buffer.getDouble());
        }

        return geometryFactory.createPoint(cs);
    }

    private Object createNull() {
        return geometryFactory.createPoint(new Coordinate(Double.NaN,
                Double.NaN, Double.NaN));
    }

    public void write(ByteBuffer buffer, Object geometry) {
        Coordinate c = ((Point) geometry).getCoordinate();

        buffer.putDouble(c.x);
        buffer.putDouble(c.y);

        if (shapeType == ShapeType.POINTZ) {
            if (Double.isNaN(c.z)) { // nan means not defined
                buffer.putDouble(0.0);
            } else {
                buffer.putDouble(c.z);
            }
        }

        if ((shapeType == ShapeType.POINTZ) || (shapeType == ShapeType.POINTM)) {
            buffer.putDouble(-10E40); // M
        }
    }

}
