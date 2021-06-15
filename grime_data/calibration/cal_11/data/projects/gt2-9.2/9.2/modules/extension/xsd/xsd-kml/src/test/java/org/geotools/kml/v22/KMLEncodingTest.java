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
package org.geotools.kml.v22;

import org.geotools.geometry.jts.GeometryBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class KMLEncodingTest extends KMLTestSupport {

    public void testEncodePoint() throws Exception {
        Point p = new GeometryBuilder().point(1,2);
        Document d = encode(p, KML.Point);
        
        assertEquals("Point", d.getDocumentElement().getLocalName());
        Element e = getElementByQName(d, KML.coordinates);
        assertNotNull(e);

        assertEquals("1.0,2.0", e.getFirstChild().getNodeValue());
    }

    public void testEncodePolygon() throws Exception {
        Polygon p = new GeometryBuilder().polygon(1, 1, 2, 2, 3, 3, 1, 1);
        Document d = encode(p, KML.Polygon);
        assertEquals("Polygon", d.getDocumentElement().getLocalName());

        Element e = getElementByQName(d, KML.outerBoundaryIs);
        assertNotNull(e);

        e = getElementByQName(e, KML.LinearRing);
        assertNotNull(e);

        e = getElementByQName(e, KML.coordinates);
        assertNotNull(e);
    }

    
}
