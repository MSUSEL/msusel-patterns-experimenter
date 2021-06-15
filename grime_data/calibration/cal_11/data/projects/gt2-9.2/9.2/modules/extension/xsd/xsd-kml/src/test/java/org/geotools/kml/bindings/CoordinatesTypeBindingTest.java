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
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.kml.bindings;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequenceFactory;

import org.geotools.kml.KML;
import org.geotools.kml.KMLTestSupport;
import org.geotools.xml.Binding;
import org.w3c.dom.Document;


/**
 * 
 *
 * @source $URL$
 */
public class CoordinatesTypeBindingTest extends KMLTestSupport {
    public void testType() {
        assertEquals(CoordinateSequence.class, binding(KML.CoordinatesType).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(KML.CoordinatesType).getExecutionMode());
    }

    public void testParse() throws Exception {
        buildDocument("<coordinates>1,1 2,2</coordinates>");

        CoordinateSequence cs = (CoordinateSequence) parse();

        assertEquals(2, cs.size());
        assertEquals(new Coordinate(1, 1), cs.getCoordinate(0));
        assertEquals(new Coordinate(2, 2), cs.getCoordinate(1));

        buildDocument("<coordinates>1,1,1" + " 2,2,2" + " </coordinates>");
        cs = (CoordinateSequence) parse();

        assertEquals(2, cs.size());
        assertEquals(new Coordinate(1, 1), cs.getCoordinate(0));
        assertEquals(1d, cs.getCoordinate(0).z, 0.1);
        assertEquals(new Coordinate(2, 2), cs.getCoordinate(1));
        assertEquals(2d, cs.getCoordinate(1).z, 0.1);
    }
    
    public void testEncode() throws Exception {
        CoordinateSequence cs = CoordinateArraySequenceFactory.instance().create(
            new Coordinate[]{ new Coordinate(1,1), new Coordinate(2,2) } 
        );
        Document dom = encode( cs, KML.coordinates );
        assertEquals( "1.0,1.0 2.0,2.0", dom.getDocumentElement().getFirstChild().getTextContent() );
    }
}
