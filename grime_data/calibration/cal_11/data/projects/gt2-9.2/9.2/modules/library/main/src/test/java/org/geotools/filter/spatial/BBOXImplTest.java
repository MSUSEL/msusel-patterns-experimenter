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
package org.geotools.filter.spatial;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.FilterFactoryImpl;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * 
 *
 * @source $URL$
 */
public class BBOXImplTest extends TestCase {

    public static Test suite() {
        return new TestSuite(BBOXImplTest.class);
    }

    public void testBbox() {
        FilterFactoryImpl ff = new FilterFactoryImpl();
        GeometryFactory gf = new GeometryFactory(new PrecisionModel());
        Coordinate coords[] = new Coordinate[6];
        coords[0] = new Coordinate(0, 1.5);
        coords[1] = new Coordinate(0, 2.5);
        coords[2] = new Coordinate(1.5, 3);
        coords[3] = new Coordinate(4, 2.5);
        coords[4] = new Coordinate(0.5, 1);
        coords[5] = coords[0];
        Polygon p = gf.createPolygon(gf.createLinearRing(coords), null);
        SimpleFeatureType type = null;
        try {
            type = DataUtilities.createType("testSchema", "name:String,*geom:Geometry");
        } catch (SchemaException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ArrayList<Object> attributes = new ArrayList<Object>();
        attributes.add("testFeature");
        attributes.add(p);
        Feature f = null;
        f = SimpleFeatureBuilder.build(type, new Object[] { "testFeature", p }, null);
        Envelope e1 = new Envelope(3, 6, 0, 2);
        Envelope e2 = new Envelope(3.25, 3.75, 1.25, 1.75);
        assertTrue(e1.contains(e2));
        assertTrue(p.getEnvelopeInternal().contains(e2));
        assertTrue(p.getEnvelopeInternal().intersects(e1));
        BBOXImpl bbox1 = (BBOXImpl) ff.bbox(ff.createAttributeExpression("geom"), e1.getMinX(),
                e1.getMinY(), e1.getMaxX(), e1.getMaxY(), "");
        BBOXImpl bbox2 = (BBOXImpl) ff.bbox(ff.createAttributeExpression("geom"), e2.getMinX(),
                e2.getMinY(), e2.getMaxX(), e2.getMaxY(), "");
        assertFalse(bbox2.evaluate(f));
        assertFalse(bbox1.evaluate(f));
    }

}
