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
package org.geotools.filter.visitor;

import static org.junit.Assert.*;

import org.geotools.factory.CommonFactoryFinder;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;


public class SpatialFilterVisitorTest {

    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    private SpatialFilterVisitor visitor;
    
    
    @Before
    public void setUp() throws Exception {
        visitor = new SpatialFilterVisitor();
    }

    @Test
    public void testInclude() {
        Filter.INCLUDE.accept(visitor, null);
        assertFalse(visitor.hasSpatialFilter);
    }
    
    @Test
    public void testExclude() {
        Filter.EXCLUDE.accept(visitor, null);
        assertFalse(visitor.hasSpatialFilter);
    }
    
    @Test
    public void testBBOX() {
        ff.bbox("geom", 0, 0, 10, 10, "EPSG:4326").accept(visitor, null);
        assertTrue(visitor.hasSpatialFilter);
    }
    
    @Test
    public void testIntersects() {
        ff.intersects(ff.property("geom"), ff.literal(null)).accept(visitor, null);
        assertTrue(visitor.hasSpatialFilter);
    }
    
    @Test
    public void testOverlaps() {
        ff.overlaps(ff.property("geom"), ff.literal(null)).accept(visitor, null);
        assertTrue(visitor.hasSpatialFilter);
    }
    
}
