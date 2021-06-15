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
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import junit.framework.TestCase;

import org.geotools.test.TestData;
import org.xml.sax.SAXException;


/**
 * <p>
 * Big dataset tests ... more than you want for regular testing
 * </p>
 * @
 *
 * @author dzwiers www.refractions.net
 *
 *
 * @source $URL$
 */
public class GMLParser2Test extends TestCase {
    public void testBlank(){
        // blank test ... lets it sit in the repository
    }
    
  public void testFMEPostalFeatures() throws SAXException, IOException {
      try {
          SAXParserFactory spf = SAXParserFactory.newInstance();
          spf.setNamespaceAware(true);
          spf.setValidating(false);

          SAXParser parser = spf.newSAXParser();

          String path = "city/dj.xml";
          File f = TestData.file(this,path);
          URI u = f.toURI();

          XMLSAXHandler xmlContentHandler = new XMLSAXHandler(u,null);
          XMLSAXHandler.setLogLevel(Level.WARNING);
          XSISAXHandler.setLogLevel(Level.WARNING);
          XMLElementHandler.setLogLevel(Level.WARNING);
          XSIElementHandler.setLogLevel(Level.WARNING);

          parser.parse(f, xmlContentHandler);

          Object doc = xmlContentHandler.getDocument();
          assertNotNull("Document missing", doc);
          //System.out.println(doc);
      } catch (Throwable e) {
          e.printStackTrace();
          fail(e.toString());
      }
  }
    
//    public void testFMEPostalFeatures() throws SAXException, IOException {
//        try {
//            SAXParserFactory spf = SAXParserFactory.newInstance();
//            spf.setNamespaceAware(true);
//            spf.setValidating(false);
//
//            SAXParser parser = spf.newSAXParser();
//
//            String path = "fme/postal/postal.gml";
//            File f = TestData.file(this,path);
//            URI u = f.toURI();
//
//            XMLSAXHandler xmlContentHandler = new XMLSAXHandler(u,null);
//            XMLSAXHandler.setLogLevel(Level.WARNING);
//            XSISAXHandler.setLogLevel(Level.WARNING);
//            XMLElementHandler.setLogLevel(Level.WARNING);
//            XSIElementHandler.setLogLevel(Level.WARNING);
//
//            parser.parse(f, xmlContentHandler);
//
//            Object doc = xmlContentHandler.getDocument();
//            assertNotNull("Document missing", doc);
//            System.out.println(doc);
//            
//            Object[] objs = (Object[])doc;
//            
//            assertTrue("Should have 95054 features + 1 bbox : "+objs.length,objs.length == 95055);
//                        
//        } catch (Throwable e) {
//            e.printStackTrace();
//            fail(e.toString());
//        }
//    }
//    
//    public void testFMEFedCenFeatures() throws SAXException, IOException {
//        try {
//            SAXParserFactory spf = SAXParserFactory.newInstance();
//            spf.setNamespaceAware(true);
//            spf.setValidating(false);
//
//            SAXParser parser = spf.newSAXParser();
//
//            String path = "fme/fed-cen/fed308_a.gml";
//            File f = TestData.file(this,path);
//            URI u = f.toURI();
//
//            XMLSAXHandler xmlContentHandler = new XMLSAXHandler(u,null);
//            XMLSAXHandler.setLogLevel(Level.WARNING);
//            XSISAXHandler.setLogLevel(Level.WARNING);
//            XMLElementHandler.setLogLevel(Level.WARNING);
//            XSIElementHandler.setLogLevel(Level.WARNING);
//
//            parser.parse(f, xmlContentHandler);
//
//            Object doc = xmlContentHandler.getDocument();
//            assertNotNull("Document missing", doc);
//            System.out.println(doc);
//            
//            Object[] objs = (Object[])doc;
//            
//            assertTrue("Should have N features + 1 bbox : "+objs.length,objs.length > 2);
//            
//        } catch (Throwable e) {
//            e.printStackTrace();
//            fail(e.toString());
//        }
//    }
//    
//    public void testFMEForestFeatures() throws SAXException, IOException {
//        try {
//            SAXParserFactory spf = SAXParserFactory.newInstance();
//            spf.setNamespaceAware(true);
//            spf.setValidating(false);
//
//            SAXParser parser = spf.newSAXParser();
//
//            String path = "fme/forest_districts/forest-districts-2003-04.gml";
//            File f = TestData.file(this,path);
//            URI u = f.toURI();
//
//            XMLSAXHandler xmlContentHandler = new XMLSAXHandler(u,null);
//            XMLSAXHandler.setLogLevel(Level.WARNING);
//            XSISAXHandler.setLogLevel(Level.WARNING);
//            XMLElementHandler.setLogLevel(Level.WARNING);
//            XSIElementHandler.setLogLevel(Level.WARNING);
//
//            parser.parse(f, xmlContentHandler);
//
//            Object doc = xmlContentHandler.getDocument();
//            assertNotNull("Document missing", doc);
//            System.out.println(doc);
//            
//            Object[] objs = (Object[])doc;
//            
//            assertTrue("Should have N features + 1 bbox : "+objs.length,objs.length > 2);
//            
//        } catch (Throwable e) {
//            e.printStackTrace();
//            fail(e.toString());
//        }
//    }
//    
//    public void testFMEVictoriaFeatures() throws SAXException, IOException {
//        try {
//            SAXParserFactory spf = SAXParserFactory.newInstance();
//            spf.setNamespaceAware(true);
//            spf.setValidating(false);
//
//            SAXParser parser = spf.newSAXParser();
//
//            String path = "fme/victoria/victoria.gml";
//            File f = TestData.file(this,path);
//            URI u = f.toURI();
//
//            XMLSAXHandler xmlContentHandler = new XMLSAXHandler(u,null);
//            XMLSAXHandler.setLogLevel(Level.WARNING);
//            XSISAXHandler.setLogLevel(Level.WARNING);
//            XMLElementHandler.setLogLevel(Level.WARNING);
//            XSIElementHandler.setLogLevel(Level.WARNING);
//
//            parser.parse(f, xmlContentHandler);
//
//            Object doc = xmlContentHandler.getDocument();
//            assertNotNull("Document missing", doc);
//            System.out.println(doc);
//            
//            Object[] objs = (Object[])doc;
//            
//            assertTrue("Should have N features + 1 bbox : "+objs.length,objs.length > 2);
//            
//        } catch (Throwable e) {
//            e.printStackTrace();
//            fail(e.toString());
//        }
//    }
}
