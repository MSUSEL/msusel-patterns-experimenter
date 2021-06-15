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
package org.geotools.geometry.jts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;
/**
 * 
 *
 * @source $URL$
 */
public class WKTReader2Test {

    @Test
    public void verifyWKT() throws Exception {
        String WKT = "LINESTRING (60 380, 60 20, 200 400, 280 20, 360 400, 420 20, 500 400, 580 20, 620 400)";
        WKTReader reader = new WKTReader2();

        Geometry geometry = reader.read(WKT);
        assertNotNull(geometry);

    }

    /**
     * Draw a circle between the start and end point; or each group of three their after.
     * @throws Exception
     */
    @Test
    public void circularString() throws Exception {
        String WKT = "CIRCULARSTRING(220268.439465645 150415.359530563,220227.333322076 150505.561285879,220227.353105332 150406.434743975)";
        WKTReader reader = new WKTReader2();

        Geometry geometry = reader.read(WKT);
        assertNotNull("parsed circularstring", geometry);
        assertEquals("segmentized as expected", 112, geometry.getNumPoints() );
        
        WKT = "CIRCULARSTRING(143.62025166838282 -30.037497356076827, 142.92857147299705 -32.75101196874403, 145.96132309891922 -34.985671061528784, 149.57565307617188 -33.41153335571289, 149.41972407584802 -29.824672680573517, 146.1209416055467 -30.19711586270431, 143.62025166838282 -30.037497356076827)";
        geometry = reader.read(WKT);
        assertNotNull("parsed circularstring ring",geometry);
        Coordinate[] array = geometry.getCoordinates();
        assertEquals( "forms a ring", array[0], array[ array.length-1]);
        
        WKT = "CIRCULARSTRING(143.62025166838282 -30.037497356076827, 142.92857147299705 -32.75101196874403, 143.62025166838282 -30.037497356076827)";
        geometry = reader.read(WKT);
        assertNotNull("parsed perfect circle",geometry);
        assertEquals( 129, geometry.getNumPoints() );
        
        WKT = "CIRCULARSTRING EMPTY";
        geometry = reader.read(WKT);
        assertNotNull(geometry);
        assertTrue( geometry.isEmpty() );
    }

    @Test
    public void compoundCurve() throws Exception {
        String WKT = "COMPOUNDCURVE((153.72942375 -27.21757040, 152.29285719 -29.23940482, 154.74034096 -30.51635287),CIRCULARSTRING(154.74034096 -30.51635287, 154.74034096 -30.51635287, 152.39926953 -32.16574411, 155.11278414 -34.08116619, 151.86720784 -35.62414508))";
        WKTReader reader = new WKTReader2();

        Geometry geometry = reader.read(WKT);
        assertNotNull(geometry);
        
        WKT = "COMPOUNDCURVE((153.72942375 -27.21757040, 152.29285719 -29.23940482, 154.74034096 -30.51635287))";
        geometry = reader.read(WKT);
        assertNotNull(geometry);
        
        WKT = "COMPOUNDCURVE(CIRCULARSTRING(154.74034096 -30.51635287, 154.74034096 -30.51635287, 152.39926953 -32.16574411, 155.11278414 -34.08116619, 151.86720784 -35.62414508))";
        geometry = reader.read(WKT);
        assertNotNull(geometry);
        
        WKT = "COMPOUNDCURVE EMPTY";
        geometry = reader.read(WKT);
        assertNotNull(geometry);
        assertTrue( geometry.isEmpty() );        
    }

    @Test
    public void curvePolygon() throws Exception {
        // perfect circle!
        WKTReader reader = new WKTReader2();
        String WKT;
        Polygon polygon;
        Geometry geometry;
        
        WKT = "CURVEPOLYGON(CIRCULARSTRING(143.62025166838282 -30.037497356076827, 142.92857147299705 -32.75101196874403, 143.62025166838282 -30.037497356076827))";
        geometry = reader.read(WKT);
        assertNotNull("read curvepolygon", geometry);
        
        assertTrue( geometry instanceof Polygon );
        polygon = (Polygon) geometry;
        assertTrue( "ring", polygon.getExteriorRing().isClosed() );
        assertEquals( "segmented ring", 129, polygon.getExteriorRing().getNumPoints() );
        assertEquals( "no holes", 0, polygon.getNumInteriorRing() );
        
        WKT = "CURVEPOLYGON((144.84399355252685 -31.26123924022086, 144.20551952601693 -32.27215644886158, 145.55230712890625 -33.49203872680664, 147.97080993652344 -32.03618621826172, 146.38697244992585 -31.47406391572417, 144.84399355252685 -31.26123924022086))";
        polygon = (Polygon) reader.read( WKT );
        assertTrue( "ring", polygon.getExteriorRing().isClosed() );
        assertEquals( "no holes", 0, polygon.getNumInteriorRing() );        
        
        WKT = "CURVEPOLYGON("+
              "CIRCULARSTRING(143.62025166838282 -30.037497356076827, 142.92857147299705 -32.75101196874403, 145.96132309891922 -34.985671061528784, 149.57565307617188 -33.41153335571289, 149.41972407584802 -29.824672680573517, 146.1209416055467 -30.19711586270431, 143.62025166838282 -30.037497356076827),"+
              "(144.84399355252685 -31.26123924022086, 144.20551952601693 -32.27215644886158, 145.55230712890625 -33.49203872680664, 147.97080993652344 -32.03618621826172, 146.38697244992585 -31.47406391572417, 144.84399355252685 -31.26123924022086))";
        
        polygon = (Polygon) reader.read( WKT );
        assertTrue( "ring", polygon.getExteriorRing().isClosed() );
        assertEquals( "one holes", 1, polygon.getNumInteriorRing() );        
        
    }

}
