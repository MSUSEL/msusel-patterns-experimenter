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

package org.geotools.filter.text.ecql;

import java.util.LinkedList;
import java.util.List;

import org.geotools.filter.text.commons.BuildResultStack;
import org.geotools.filter.text.commons.Result;
import org.geotools.filter.text.cql2.CQLException;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Builds a Polygon using the lines (shell and Holes) made in the parsing process.
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.6
 */
class PolygonBuilder extends GeometryBuilder {

    /**
     * @param statement
     * @param resultStack
     */
    public PolygonBuilder(String statement, BuildResultStack resultStack) {
        super(statement, resultStack);
    }

    /**
     * Builds the a polygon using the linestring geometries (Sell and holes).
     * @param linestringNode LineString identifier defined in the grammar file
     */
    @Override
    public Geometry build(final int linestringNode) throws CQLException {

        Result result = getResultStack().peek();
        try{
            // Retrieve the liner ring for shell and holes
            final List<Geometry> geometryList= popGeometry(linestringNode);
            
            assert geometryList.size() >= 1;
            
            // retrieves the shell
            LineString line = (LineString)geometryList.get(0);
            final LinearRing shell = getGeometryFactory().createLinearRing(line.getCoordinates());

            // if it has holes, creates a ring for each linestring
            LinearRing[] holes = new LinearRing[0]; 
            if(geometryList.size() > 1){
                
                List<LinearRing> holeList = new LinkedList<LinearRing>();
                for( int i = 1;i < geometryList.size(); i++) {
                    
                    LineString holeLines = (LineString) geometryList.get(i);
                    LinearRing ring = getGeometryFactory().createLinearRing(holeLines.getCoordinates());
                    holeList.add(ring);
                }
                int holesSize = holeList.size();
                holes = holeList.toArray(new LinearRing[holesSize]) ;
            } 
            // creates the polygon
            Polygon polygon= getGeometryFactory().createPolygon(shell, holes);
            
            return polygon;
            
        }catch(Exception e){

            throw new CQLException(e.getMessage(),  result.getToken(), getStatemet());
        }
    
    
    }
}
