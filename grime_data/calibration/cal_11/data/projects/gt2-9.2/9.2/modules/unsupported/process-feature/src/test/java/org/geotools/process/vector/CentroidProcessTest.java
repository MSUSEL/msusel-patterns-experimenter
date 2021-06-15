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
package org.geotools.process.vector;

import static junit.framework.Assert.*;

import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.process.vector.CentroidProcess;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;

public class CentroidProcessTest {

    WKTReader reader = new WKTReader();
    private ListFeatureCollection fc;

    @Before
    public void setup() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.add("geom", Polygon.class, "EPSG:4326");
        tb.add("name", String.class);
        tb.setName("circles");
        SimpleFeatureType ft = tb.buildFeatureType();
        
        fc = new ListFeatureCollection(ft);
        
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(ft);
        fb.add(reader.read("POINT(0 0)").buffer(10));
        fb.add("one");
        fc.add(fb.buildFeature(null));
        fb.add(reader.read("POINT(10 0)").buffer(10));
        fb.add("two");
        fc.add(fb.buildFeature(null));
    }
    
    @Test
    public void testSchema() {
        CentroidProcess cp = new CentroidProcess();
        SimpleFeatureCollection result = cp.execute(fc);
        SimpleFeatureType ft = result.getSchema();
        assertEquals(2, ft.getAttributeCount());
        assertEquals(Point.class, ft.getGeometryDescriptor().getType().getBinding());
        assertEquals(String.class, ft.getDescriptor("name").getType().getBinding());
    }

    
    @Test
    public void testResults() throws Exception {
        CentroidProcess cp = new CentroidProcess();
        SimpleFeatureCollection result = cp.execute(fc);

        SimpleFeatureIterator it = result.features();
        assertTrue(it.hasNext());
        SimpleFeature f = it.next();
        assertEquals(0, ((Point) f.getDefaultGeometry()).getX(), 1e-6);
        assertEquals(0, ((Point) f.getDefaultGeometry()).getY(), 1e-6);
        assertEquals("one", f.getAttribute("name"));
        f = it.next();
        assertEquals(10, ((Point) f.getDefaultGeometry()).getX(), 1e-6);
        assertEquals(0, ((Point) f.getDefaultGeometry()).getY(), 1e-6);
        assertEquals("two", f.getAttribute("name"));
    }
}
