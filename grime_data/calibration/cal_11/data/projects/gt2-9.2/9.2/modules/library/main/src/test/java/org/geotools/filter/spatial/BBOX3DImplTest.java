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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.geometry.jts.ReferencedEnvelope3D;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.geometry.BoundingBox3D;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * A test for the 3D BBOX Filter.
 *
 * @source $URL$
 * @author Niels Charlier
 */
public class BBOX3DImplTest extends TestCase {

    public static Test suite() {
        return new TestSuite(BBOX3DImplTest.class);
    }

    public void testBbox3D() {
        FilterFactoryImpl ff = new FilterFactoryImpl();
        GeometryFactory gf = new GeometryFactory(new PrecisionModel());
                
        ;
        SimpleFeatureType type = null;
        try {
            type = DataUtilities.createType("testSchema", "name:String,*geom:Geometry");
        } catch (SchemaException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }        
       
        
        Feature f1 = SimpleFeatureBuilder.build(type, new Object[] { "testFeature1", gf.createPoint(new Coordinate(10, 20, 30)) }, null);
        Feature f2 = SimpleFeatureBuilder.build(type, new Object[] { "testFeature2", gf.createPoint(new Coordinate(10, 10, 60)) }, null);
        
        BoundingBox3D envelope1 = new ReferencedEnvelope3D( 0, 50, 0, 50, 0, 50, null);
        Filter bbox1 = ff.bbox(ff.createAttributeExpression("geom"), envelope1);
        BoundingBox3D envelope2 = new ReferencedEnvelope3D( 0, 50, 0, 50, 50, 100, null);
        Filter bbox2 = ff.bbox(ff.createAttributeExpression("geom"), envelope2);
        
        assertTrue(bbox1.evaluate(f1));
        assertFalse(bbox1.evaluate(f2));
        assertFalse(bbox2.evaluate(f1));
        assertTrue(bbox2.evaluate(f2));
    }

}
