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
package org.geotools.xml.ogc;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.logging.Level;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import junit.framework.TestCase;

import org.geotools.TestData;
import org.geotools.xml.PrintHandler;
import org.geotools.xml.XSISAXHandler;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.Schema;
import org.xml.sax.Attributes;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

/**
 * @author Jesse
 *
 *
 *
 *
 * @source $URL$
 */
public class GeometryEncoderTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }
    
    public void testEncodeChoiceGeometryType() throws Exception {
        File f = TestData.copy(this,"xml/feature-type-choice.xsd");
        URI u = f.toURI();
        XSISAXHandler contentHandler = new XSISAXHandler(u);
        XSISAXHandler.setLogLevel(Level.WARNING);

        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        spf.setValidating(false);
        SAXParser parser = spf.newSAXParser();
        parser.parse(f, contentHandler);

        Schema schema = contentHandler.getSchema();
        Element geomElement = schema.getElements()[0].findChildElement("GEOM");
        GeometryFactory factory=new GeometryFactory();
        LinearRing ring = factory.createLinearRing(new Coordinate[]{
                new Coordinate(0,0),
                new Coordinate(10,0),
                new Coordinate(0,10),
                new Coordinate(0,0)
            });
        Polygon polygon = factory.createPolygon(ring, new LinearRing[0]);
        polygon.setUserData("EPSG:4326");
        MultiPolygon geom = factory.createMultiPolygon(new Polygon[]{polygon});
        geom.setUserData("EPSG:4326");
        final StringWriter writer = new StringWriter();
//        DocumentWriter.writeDocument(geom, schema, writer, new HashMap());
        
        PrintHandler output=new PrintHandler(){

            public void characters(char[] arg0, int arg1, int arg2) throws IOException {
                writer.write(arg0, arg1, arg2);
            }

            public void characters(String s) throws IOException {
                writer.write(s);
            }

            public void element(URI namespaceURI, String localName, Attributes attributes) throws IOException {
            }

            public void endDocument() throws IOException {
            }

            public void endElement(URI namespaceURI, String localName) throws IOException {
                writer.write("</"+localName+">");
            }

            public Element findElement(Object value) {
                return null;
            }

            public Element findElement(String name) {
                return null;
            }

            public Schema getDocumentSchema() {
                return null;
            }

            public Object getHint(Object key) {
                return null;
            }

            public void ignorableWhitespace(char[] arg0, int arg1, int arg2) throws IOException {
            }

            public void startDocument() throws IOException {
            }

            public void startElement(URI namespaceURI, String localName, Attributes attributes) throws IOException {
                writer.write("<"+localName);
                if( attributes!=null ){
                    for( int i=0; i<attributes.getLength(); i++){
                        writer.write(" "+attributes.getLocalName(i)+"="+attributes.getValue(i));
                    }
                }
                writer.write(">");
            }
            
        };
        geomElement.getType().encode(geomElement, geom, output, new HashMap());
        String expected="<GEOM><MultiPolygon srsName=EPSG:4326><polygonMember><Polygon srsName=EPSG:4326><outerBoundaryIs><LinearRing><coordinates decimal=. cs=, ts= >0.0,0.0 10.0,0.0 0.0,10.0 0.0,0.0</coordinates></LinearRing></outerBoundaryIs></Polygon></polygonMember></MultiPolygon></GEOM>";
        assertEquals(expected, writer.toString());
    }

}
