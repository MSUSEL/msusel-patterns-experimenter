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
package org.geotools.filter.function;

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import java.util.ArrayList;
import java.util.List;

import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateFilter;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPoint;

/**
 * 
 *
 * @source $URL$
 */
public class FilterFunction_vertices extends FunctionExpressionImpl {

    public static FunctionName NAME = new FunctionNameImpl("vertices",
            parameter("vertices", MultiPoint.class),
            parameter("geometry", Geometry.class));

    public FilterFunction_vertices() {
        super(NAME);
    }

    public Object evaluate(Object feature, Class context) {
        Geometry g = getExpression(0).evaluate(feature, Geometry.class);
        if(g == null)
            return null;
        
        MultiPointExtractor filter = new MultiPointExtractor();
        g.apply(filter);
        return filter.getMultiPoint();
    }
    
    static class MultiPointExtractor implements CoordinateFilter {
        List<Coordinate> coordinates = new ArrayList();

        public void filter(Coordinate c) {
            coordinates.add(c);
        }
        
        MultiPoint getMultiPoint() {
            Coordinate[] coorArray = (Coordinate[]) coordinates.toArray(new Coordinate[coordinates.size()]);
            return new GeometryFactory().createMultiPoint(coorArray);
        }
        
    }

}
