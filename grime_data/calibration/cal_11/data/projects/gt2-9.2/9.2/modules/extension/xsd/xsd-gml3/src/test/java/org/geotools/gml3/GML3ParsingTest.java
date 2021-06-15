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
package org.geotools.gml3;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import junit.framework.TestCase;

import org.geotools.xml.StreamingParser;
import org.opengis.feature.simple.SimpleFeature;
import org.w3c.dom.Document;

/**
 * 
 *
 * @source $URL$
 */
public class GML3ParsingTest extends TestCase {

    public void testWithoutSchema() throws Exception {
        InputStream in = getClass().getResourceAsStream( "states.xml");
        GMLConfiguration gml = new GMLConfiguration();
        StreamingParser parser = new StreamingParser( gml, in, SimpleFeature.class );
        
        int nfeatures = 0;
        SimpleFeature f = null;
        while( ( f = (SimpleFeature) parser.parse() ) != null ) {
            nfeatures++;
            assertNotNull( f.getAttribute( "STATE_NAME"));
            assertNotNull( f.getAttribute( "STATE_ABBR"));
            assertTrue( f.getAttribute( "SAMP_POP") instanceof String );
        }
        
        assertEquals( 49, nfeatures );
    }
    
    public void testWithSchema() throws Exception {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        
        //copy the schema to a temporary file
        File xsd = new File( "target/states.xsd");
        //xsd.deleteOnExit();
        
        Document schema = db.parse( getClass().getResourceAsStream( "states.xsd" ));
        Transformer tx = TransformerFactory.newInstance().newTransformer();
        tx.transform( new DOMSource( schema ) , new StreamResult( xsd ) );
        
        //update the schemaLocation to point at the schema
        Document instance = db.parse( getClass().getResourceAsStream( "states.xml"));
        instance.getDocumentElement().setAttribute( "schemaLocation", 
            "http://www.openplans.org/topp target/states.xsd");
        
        File xml = new File( "target/states.xml");
        //xml.deleteOnExit();
        tx.transform( new DOMSource( instance ), new StreamResult( xml ) );
        
        InputStream in = new FileInputStream( xml );
        GMLConfiguration gml = new GMLConfiguration();
        StreamingParser parser = new StreamingParser( gml, in, SimpleFeature.class );
        
        int nfeatures = 0;
        SimpleFeature f = null;
        while( ( f = (SimpleFeature) parser.parse() ) != null ) {
            nfeatures++;
            assertNotNull( f.getAttribute( "STATE_NAME"));
            assertNotNull( f.getAttribute( "STATE_ABBR"));
            assertTrue( f.getAttribute( "SAMP_POP") instanceof Double );
        }
        
        assertEquals( 49, nfeatures );
    }
}
