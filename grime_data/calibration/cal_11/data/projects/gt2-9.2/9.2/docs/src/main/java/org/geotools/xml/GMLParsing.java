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
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.xml.Parser;
import org.geotools.xml.StreamingParser;
import org.opengis.feature.simple.SimpleFeature;
import org.w3c.dom.Document;

/**
 * GML3 Parsing examples.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 *
 * @source $URL$
 */
public class GMLParsing {

    public static void main(String[] args) throws Exception {
        //parseGML3();
        //streamParseGML3();
        schemaParseGML3();
    }
    
    /**
     * Parses GML3 without specifying a schema location.
     */
    public static void parseGML3() throws Exception {
        InputStream in = GMLParsing.class.getResourceAsStream( "states.xml");
        GMLConfiguration gml = new GMLConfiguration();
        Parser parser = new Parser(gml);
        parser.setStrict(false);
        
        FeatureCollection features = (FeatureCollection) parser.parse(in);
        FeatureIterator i = features.features();
        
        int nfeatures = 0;
        while( i.hasNext() ) {
            SimpleFeature f = (SimpleFeature) i.next();
            System.out.println(f.getID());
            nfeatures++;
        }
        
        System.out.println("Number of features: " + nfeatures);
    }
    
    /**
     * Parses GML3 without specifying a schema location, and illusrates the use
     * of the streaming parser.
     */
    public static void streamParseGML3() throws Exception {
        InputStream in = GMLParsing.class.getResourceAsStream( "states.xml");
        GMLConfiguration gml = new GMLConfiguration();
        StreamingParser parser = new StreamingParser( gml, in, SimpleFeature.class );
        
        int nfeatures = 0;
        SimpleFeature f = null;
        while( ( f = (SimpleFeature) parser.parse() ) != null ) {
            nfeatures++;
            System.out.println(f.getID());
        }
        
        System.out.println("Number of features: " + nfeatures);
    }
    
    /**
     * Parses GML3 by specifying the schema location.
     * <p>
     * This example first transforms the original file states.xml, and sets its
     * schemaLocation to the states.xsd file.
     * </p>
     */
    public static void schemaParseGML3() throws Exception {
        File xml = setSchemaLocation();
        InputStream in = new FileInputStream(xml);
        
        GMLConfiguration gml = new GMLConfiguration();
        Parser parser = new Parser(gml);
        parser.setStrict(false);
        
        FeatureCollection features = (FeatureCollection) parser.parse(in);
        FeatureIterator i = features.features();
        
        int nfeatures = 0;
        while( i.hasNext() ) {
            SimpleFeature f = (SimpleFeature) i.next();
            System.out.println(f.getID());
            nfeatures++;
        }
        
        System.out.println("Number of features: " + nfeatures);
    }

    static File setSchemaLocation() throws Exception {
        File xsd = File.createTempFile("states", "xsd");
        IOUtils.copy(GMLParsing.class.getResourceAsStream("states.xsd"), 
            new FileOutputStream(xsd));
        
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document d = db.parse( GMLParsing.class.getResourceAsStream( "states.xml") );
        d.getDocumentElement().setAttribute( "xsi:schemaLocation", 
            "http://www.openplans.org/topp " + xsd.getCanonicalPath() );
        
        File xml = File.createTempFile("states", "xml");
        TransformerFactory.newInstance().newTransformer().transform( 
            new DOMSource(d), new StreamResult(xml));
        return xml;
    }
}
