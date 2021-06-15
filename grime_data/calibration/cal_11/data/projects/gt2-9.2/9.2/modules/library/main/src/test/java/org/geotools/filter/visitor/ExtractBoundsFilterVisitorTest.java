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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

/**
 * 
 *
 * @source $URL$
 */
public class ExtractBoundsFilterVisitorTest {

    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    ExtractBoundsFilterVisitor visitor = new ExtractBoundsFilterVisitor();

    Envelope infinity = visitor.infinity();

    @Test
    public void testInclude() {
        Envelope env = (Envelope) Filter.INCLUDE.accept(visitor, null);
        assertEquals(infinity, env);
    }

    @Test
    public void testExclude() {
        Envelope env = (Envelope) Filter.EXCLUDE.accept(visitor, null);
        assertTrue(env.isNull());
    }

    @Test
    public void testNonSpatial() {
        Filter f = ff.less(ff.property("att"), ff.literal(10));
        Envelope env = (Envelope) f.accept(visitor, null);
        assertEquals(infinity, env);
    }

    @Test
    public void testBbox() {
        Filter f = ff.bbox("geom", -10, -10, 10, 10, null);
        Envelope env = (Envelope) f.accept(visitor, null);
        assertEquals(new Envelope(-10, 10, -10, 10), env);
    }

    @Test
    public void testAnd() {
        Filter f = ff.and(ff.bbox("geom", -10, -10, 10, 10, null), ff.equals(ff.property("att"), ff
                .literal("10")));
        Envelope env = (Envelope) f.accept(visitor, null);
        assertEquals(new Envelope(-10, 10, -10, 10), env);
    }

    @Test
    public void testOr() {
        Filter f = ff.or(ff.bbox("geom", -10, -10, 10, 10, null), ff.equals(ff.property("att"), ff
                .literal("10")));
        Envelope env = (Envelope) f.accept(visitor, null);
        assertEquals(infinity, env);
    }

    @Test
    public void testTouches() {
        Coordinate[] coords = new Coordinate[] { new Coordinate(0, 0), new Coordinate(10, 10) };
        LineString lineString = new GeometryFactory().createLineString(coords);
        Filter filter = ff.touches(ff.property("name"), ff.literal(lineString));
        Envelope env = (Envelope) filter.accept(visitor, null);
        assertEquals(new Envelope(0, 10, 0, 10), env);
    }

    @Test
    public void testBeyond() {
        Coordinate[] coords = new Coordinate[] { new Coordinate(0, 0), new Coordinate(10, 10) };
        LineString lineString = new GeometryFactory().createLineString(coords);
        Filter filter = ff.beyond(ff.property("name"), ff.literal(lineString), 100, "m");
        Envelope env = (Envelope) filter.accept(visitor, null);
        assertEquals(infinity, env);
    }

    @Test
    public void testNotBeyond() {
        Coordinate[] coords = new Coordinate[] { new Coordinate(0, 0), new Coordinate(10, 10) };
        LineString lineString = new GeometryFactory().createLineString(coords);
        Filter filter = ff.beyond(ff.property("name"), ff.literal(lineString), 100, "m");
        Envelope env = (Envelope) filter.accept(visitor, null);
        // the thing is not so smart to assess that not(beyond) -> within, but we have to make
        // sure that at least the returned envelope contains the real one
        assertEquals(infinity, env);
    }

    @Test
    public void testNull() {
        Filter filter = ff.isNull(ff.property("name"));
        Envelope env = (Envelope) filter.accept(visitor, null);
        assertEquals(infinity, env);
    }
    
    @Test
    public void testDWithin() {
        Point geom = new GeometryFactory().createPoint(new Coordinate(0,0));
        Filter filter = ff.dwithin(ff.property("name"), ff.literal(geom), 100, "metre");
        Envelope env = (Envelope) filter.accept(visitor, null);
        
        assertEquals(new Envelope(-100, 100, -100, 100), env);
    }
    
    @Test
    public void testAndDWithin() {
        Point geom = new GeometryFactory().createPoint(new Coordinate(0,0));
        Filter filter = ff.dwithin(ff.property("geom"), ff.literal(geom), 100, "metre");
        filter = ff.and(filter, ff.bbox(ff.property("geom"), 50, 50, 150, 150, null));
        Envelope env = (Envelope) filter.accept(visitor, null);
        
        assertEquals(new Envelope(50, 100, 50, 100), env);
    }
    
    @Test
    public void testDisjoint() {
        Coordinate[] coords = new Coordinate[] { new Coordinate(0, 0), new Coordinate(10, 10) };
        LineString lineString = new GeometryFactory().createLineString(coords);
        Filter filter = ff.disjoint(ff.property("name"), ff.literal(lineString));
        Envelope env = (Envelope) filter.accept(visitor, null);
        
        assertEquals(infinity, env);
    }
    
    @Test
    public void testAndDisjoint() {
        Coordinate[] coords = new Coordinate[] { new Coordinate(0, 0), new Coordinate(10, 10) };
        LineString lineString = new GeometryFactory().createLineString(coords);
        Filter filter = ff.disjoint(ff.property("name"), ff.literal(lineString));
        filter = ff.and(filter, ff.bbox(ff.property("geom"), 50, 50, 150, 150, null));
        Envelope env = (Envelope) filter.accept(visitor, null);
        
        assertEquals(new Envelope(50, 150, 50, 150), env);
    }

}
