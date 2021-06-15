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

import static org.junit.Assert.*;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Function;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;

public class FilterFunction_setCRSTest {
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
    Geometry g;
    
    @Before
    public void setup() throws Exception {
        g = new WKTReader().read("POINT(0 0)");
    }
    
    @Test
    public void setCRSObject() {
        Function f = ff.function("setCRS", ff.literal(g), ff.literal(DefaultGeographicCRS.WGS84));
        Geometry sg = (Geometry) f.evaluate(null);
        assertEquals(DefaultGeographicCRS.WGS84, sg.getUserData());
    }
    
    @Test
    public void setCRSCode() throws Exception {
        Function f = ff.function("setCRS", ff.literal(g), ff.literal("EPSG:4326"));
        Geometry sg = (Geometry) f.evaluate(null);
        assertEquals(CRS.decode("EPSG:4326"), sg.getUserData());
    }
    
    @Test
    public void setCRSWkt() {
        Function f = ff.function("setCRS", ff.literal(g), ff.literal(DefaultGeographicCRS.WGS84.toWKT()));
        Geometry sg = (Geometry) f.evaluate(null);
        assertTrue(CRS.equalsIgnoreMetadata(DefaultGeographicCRS.WGS84, sg.getUserData()));
    }
}
