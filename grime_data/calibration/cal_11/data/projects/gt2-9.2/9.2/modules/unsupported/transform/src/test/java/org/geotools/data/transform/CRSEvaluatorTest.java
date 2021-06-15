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
package org.geotools.data.transform;

import static junit.framework.Assert.*;

import java.io.File;

import org.geotools.data.property.PropertyDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.referencing.CRS;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;

public class CRSEvaluatorTest {
    
    static SimpleFeatureType STATES_SCHEMA;
    private static CoordinateReferenceSystem WGS84;
    WKTReader wkt = new WKTReader();
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
    
    @BeforeClass
    public static void beforeClass() throws Exception {
        PropertyDataStore pds = new PropertyDataStore(new File(
                "./src/test/resources/org/geotools/data/transform"));
        STATES_SCHEMA = pds.getFeatureSource("states").getSchema();
        pds.dispose();
        
        WGS84 = CRS.decode("EPSG:4326");
    }
    
    @Test
    public void testLiteralNull() throws Exception {
        Geometry g = wkt.read("POINT(0 0)");
        Literal literal = ff.literal(g);
        
        CRSEvaluator evaluator = new CRSEvaluator(null);
        CoordinateReferenceSystem crs = (CoordinateReferenceSystem) literal.accept(evaluator, null);
        assertNull(crs);
    }

    @Test
    public void testLiteralSRID() throws Exception {
        Geometry g = wkt.read("POINT(0 0)");
        g.setSRID(4326);
        Literal literal = ff.literal(g);
        
        CRSEvaluator evaluator = new CRSEvaluator(null);
        CoordinateReferenceSystem crs = (CoordinateReferenceSystem) literal.accept(evaluator, null);
        assertEquals(CRS.decode("EPSG:4326"), crs);
    }
    
    @Test
    public void testLiteralCRS() throws Exception {
        Geometry g = wkt.read("POINT(0 0)");
        g.setUserData(WGS84);
        Literal literal = ff.literal(g);
        
        CRSEvaluator evaluator = new CRSEvaluator(null);
        CoordinateReferenceSystem crs = (CoordinateReferenceSystem) literal.accept(evaluator, null);
        assertSame(WGS84, crs);
    }
    
    @Test
    public void testAttribute() throws Exception {
        PropertyName pn = ff.property("the_geom");
        CRSEvaluator evaluator = new CRSEvaluator(STATES_SCHEMA);
        CoordinateReferenceSystem crs = (CoordinateReferenceSystem) pn.accept(evaluator, null);
        assertEquals(WGS84, crs);
    }
    
    @Test
    public void testFunctionLiteral() throws Exception {
        Geometry g = wkt.read("POINT(0 0)");
        g.setUserData(WGS84);
        Literal literal = ff.literal(g);
        Function buffer = ff.function("buffer", literal, ff.literal(10));
        
        CRSEvaluator evaluator = new CRSEvaluator(null);
        CoordinateReferenceSystem crs = (CoordinateReferenceSystem) buffer.accept(evaluator, null);
        assertSame(WGS84, crs);
    }
    
    @Test
    public void testFunctionAttribute() throws Exception {
        Geometry g = wkt.read("POINT(0 0)");
        g.setUserData(WGS84);
        PropertyName pn = ff.property("the_geom");
        Function buffer = ff.function("buffer", pn, ff.literal(10));
        
        CRSEvaluator evaluator = new CRSEvaluator(STATES_SCHEMA);
        CoordinateReferenceSystem crs = (CoordinateReferenceSystem) buffer.accept(evaluator, null);
        assertSame(WGS84, crs);
    }
    
    
}
