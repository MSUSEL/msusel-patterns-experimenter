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
package org.geotools.renderer.lite;

import static org.geotools.filter.capability.FunctionNameImpl.*;

import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.capability.FunctionName;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

/**
 * A test rendering transformation that buffers a feature collection by a given amount
 * 
 * @author Andrea Aime - GeoSolutions
 */
public class BufferTestFunction extends FunctionExpressionImpl {

    public static FunctionName NAME = new FunctionNameImpl("BufferTest", parameter("distance", Double.class));

    public BufferTestFunction() {
        super(NAME);
    }

    public Object evaluate(Object object) {
    
        SimpleFeatureCollection fc = (SimpleFeatureCollection) object;
        Double distance = null;

        try { 
            distance = getExpression(0).evaluate(null, Double.class);
        } catch (Exception e) // probably a type error
        {
            throw new IllegalArgumentException(
                    "Filter Function problem for test buffer argument #0 - expected type Double");
        }
        
        // compute the output schema
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        GeometryDescriptor gd = fc.getSchema().getGeometryDescriptor();
        tb.add(gd.getName().getLocalPart(), Polygon.class, gd.getCoordinateReferenceSystem());
        tb.setName("bufferedCollection");
        SimpleFeatureType schema = tb.buildFeatureType();
        
        // compute the output features
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(schema);
        ListFeatureCollection result = new ListFeatureCollection(schema);
        SimpleFeatureIterator fi = fc.features();
        while(fi.hasNext()) {
            SimpleFeature f = fi.next();
            fb.add(((Geometry) f.getDefaultGeometry()).buffer(distance));
            result.add(fb.buildFeature(null));
        }
        
        return result;
    }
}
