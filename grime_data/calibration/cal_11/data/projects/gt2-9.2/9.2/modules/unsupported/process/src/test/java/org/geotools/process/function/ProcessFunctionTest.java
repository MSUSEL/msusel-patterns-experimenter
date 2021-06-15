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
package org.geotools.process.function;

import static org.junit.Assert.*;

import java.net.URL;

import org.geotools.TestData;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.process.feature.BufferFeatureCollectionFactory;
import org.junit.Test;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Function;

import com.vividsolutions.jts.geom.MultiPolygon;

/**
 * 
 *
 * @source $URL$
 */
public class ProcessFunctionTest {

    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    @Test
    public void testBuffer() throws Exception {
        URL url = TestData.getResource(TestData.class, "shapes/archsites.shp");
        ShapefileDataStore store = new ShapefileDataStore(url);
        SimpleFeatureCollection features = store.getFeatureSource().getFeatures();

        // first param, the context feature collection
        Function featuresParam = ff.function("parameter", ff
                .literal(BufferFeatureCollectionFactory.FEATURES.key));
        // second param, the buffer size
        Function bufferParam = ff.function("parameter", ff
                .literal(BufferFeatureCollectionFactory.BUFFER.key), ff.literal(1000));
        // build the function and call it
        Function buffer = ff.function("gt:BufferFeatureCollection", featuresParam, bufferParam);
        SimpleFeatureCollection buffered = (SimpleFeatureCollection) buffer.evaluate(features);
        
        // check the results
        assertEquals(features.size(), buffered.size());
        GeometryDescriptor gd = buffered.getSchema().getGeometryDescriptor();
        // is it actually a buffer?
        assertEquals(MultiPolygon.class, gd.getType().getBinding());
    }
}
