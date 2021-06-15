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
package org.geotools.geometry;

import org.geotools.geometry.jts.Geometries;
import org.geotools.geometry.jts.JTSFactoryFinder;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class GeometryExamples {

public void createPoint() {
    // createPoint start
    GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
    
    Coordinate coord = new Coordinate(1, 1);
    Point point = geometryFactory.createPoint(coord);
    // createPoint end
}

public void createPointWKT() throws ParseException {
    // createPointWKT start
    GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
    
    WKTReader reader = new WKTReader(geometryFactory);
    Point point = (Point) reader.read("POINT (1 1)");
    // createPointWKT end
}

// geometries start
public boolean hit(Point point, Geometry geometry) {
    final double MAX_DISTANCE = 0.001;
    
    switch (Geometries.get(geometry)) {
    case POINT:
    case MULTIPOINT:
    case LINESTRING:
    case MULTILINESTRING:
        // Test if p is within a threshold distance
        return geometry.isWithinDistance(point, MAX_DISTANCE);
        
    case POLYGON:
    case MULTIPOLYGON:
        // Test if the polygonal geometry contains p
        return geometry.contains(point);
        
    default:
        // For simplicity we just assume distance check will work for other
        // types (e.g. GeometryCollection) in this example
        return geometry.isWithinDistance(point, MAX_DISTANCE);
    }
}
// geometries end

}
