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
package org.geotools.gml3.v3_2.bindings;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.gml3.v3_2.GML;
import org.geotools.gml3.v3_2.GML32TestSupport;
import org.geotools.referencing.CRS;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.vividsolutions.jts.geom.Envelope;

/**
 * 
 *
 * @source $URL$
 */
public class BoundingShapeTypeBindingTest extends GML32TestSupport {

    public void testEncode() throws Exception {
        Envelope e  = new Envelope(-180,-90,180,90);
        
        Document dom = encode( e , GML.boundedBy );
        
        assertEquals( "gml:Envelope", dom.getDocumentElement().getFirstChild().getNodeName());
    }
    
    public void testEncodeWithCRS() throws Exception {
        Envelope e  = new ReferencedEnvelope(-180,-90,180,90,CRS.decode( "EPSG:4326"));
        Document dom = encode( e , GML.boundedBy );
        assertEquals( "gml:Envelope", dom.getDocumentElement().getFirstChild().getNodeName());
        assertTrue( ((Element)dom.getDocumentElement().getFirstChild()).getAttribute( "srsName").endsWith( "4326") );
    }
    
    public void testEncodeAsNull() throws Exception {
        Envelope e  = new Envelope();
        e.setToNull();
        
        Document dom = encode( e , GML.boundedBy );
        assertEquals( "gml:Null", dom.getDocumentElement().getFirstChild().getNodeName());
    }
}
