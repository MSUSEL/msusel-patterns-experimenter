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
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geojson.geom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.parser.ParseException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

/**
 * 
 *
 * @source $URL$
 */
public class MultiPolygonHandler extends GeometryHandlerBase<MultiPolygon> {

    List<Coordinate> coordinates;
    List<Coordinate[]> rings;
    List<List<Coordinate[]>> polys;
    
    public MultiPolygonHandler(GeometryFactory factory) {
        super(factory);
    }
    
    @Override
    public boolean startObjectEntry(String key) throws ParseException, IOException {
        if ("coordinates".equals(key)) {
            polys = new ArrayList();
        }
        
        return true;
    }
    
    @Override
    public boolean startArray() throws ParseException, IOException {
        if (rings == null) {
            rings = new ArrayList();
        }
        else if (coordinates == null) {
            coordinates = new ArrayList();
        }
        else if (ordinates == null) {
            ordinates = new ArrayList();
        }
        return true;
    }
    
    @Override
    public boolean endArray() throws ParseException, IOException {
        if (ordinates != null) {
            coordinates.add(coordinate(ordinates));
            ordinates = null;
        }
        else if (coordinates != null) {
            rings.add(coordinates(coordinates));
            coordinates = null;
        }
        else if (rings != null) {
            polys.add(rings);
            rings = null;
        }
        
        return true;
    }
    
    @Override
    public boolean endObject() throws ParseException, IOException {
        if (polys != null) {
            Polygon[] polygons = new Polygon[polys.size()];
            for (int i = 0; i < polys.size(); i++) {
                List<Coordinate[]> rings = polys.get(i);
                if (rings.isEmpty()) {
                    continue;
                }
                
                LinearRing outer = factory.createLinearRing(rings.get(0));
                LinearRing[] inner = rings.size() > 1 ? new LinearRing[rings.size()-1] : null;
                for (int j = 1; j < rings.size(); j++) {
                    inner[j-1] = factory.createLinearRing(rings.get(j));
                }
                
                polygons[i] = factory.createPolygon(outer, inner);
            }
            value = factory.createMultiPolygon(polygons);
            polys = null;
        }

        return true;
    }
}
