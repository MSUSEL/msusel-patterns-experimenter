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
package org.geotools.filter;

import java.util.logging.Logger;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * Uses SAX to extact a GetFeature query from and incoming GetFeature request 
 * XML stream.
 *
 * <p>Note that this Handler extension ignores Filters completely and must be 
 * chained as a parent to the PredicateFilter method in order to recognize them.  
 * If it is not chained, it will still generate valid queries, but with no 
 * filtering whatsoever.</p>
 * 
 * @author Rob Hranac, Vision for New York
 *
 *
 * @source $URL$
 * @version 0.9 beta, 11/01/01
 */
public class TestFilterHandler 
    implements ContentHandler, FilterHandler {


    /** Standard logging class */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.defaultcore");
    
    
    /********************************************************
     Local tracking methods to deal with incoming XML stream
    ********************************************************/

    /** Tracks tag we are currently inside */
    private org.opengis.filter.Filter filter = null;
    
    public TestFilterHandler () {
    }
    
    
    /********************************************
     Start of SAX Content Handler Methods
     most of these are unused at the moment
     no namespace awareness, yet
    ********************************************/

    
    /** Notes the document locator. */ 
    public void setDocumentLocator (Locator locator) {
    }
    
    
    /** Notes the start of the document.    */ 
    public void startDocument()
        throws SAXException {
        //_log.info("start of document");
    }
    
    
    /** Notes the start of the document.    */ 
    public void endDocument()
        throws SAXException {
        //_log.info( "at end of document");
    }
    

    /** Notes processing instructions.  */ 
    public void processingInstruction(String target, String data)
        throws SAXException {
    }
    
    
    /** Notes start of prefix mappings. */ 
    public void startPrefixMapping(String prefix, String uri) {
    }
    
    
    /** Notes end of prefix mappings. */ 
    public void endPrefixMapping(String prefix) {
    }
    
    
    /**
     * Notes the start of the element and sets type names and query attributes.
     *
     * @param namespaceURI URI for namespace appended to element.
     * @param localName Local name of element.
     * @param rawName Raw name of element.
     * @param atts Element attributes.
     */ 
    public void startElement(String namespaceURI, 
                             String localName, 
                             String rawName, 
                             Attributes atts)
        throws SAXException {
    }
    
    
    /**
     * Notes the end of the element exists query or bounding box.
     *
     * @param namespaceURI URI for namespace appended to element.
     * @param localName Local name of element.
     * @param rawName Raw name of element.
     */ 
    public void endElement(String namespaceURI, String localName, String rawName)
        throws SAXException {
        
    }
    
    
    /**
     * Checks if inside parsed element and adds its contents to the appropriate 
     * variable.
     *
     * @param ch URI for namespace appended to element.
     * @param start Local name of element.
     * @param length Raw name of element.
     */ 
    public void characters(char[] ch, int start, int length)
        throws SAXException {
    }
    

    /**
     * Notes ignorable whitespace.
     *
     * @param ch URI for namespace appended to element.
     * @param start Local name of element.
     * @param length Raw name of element.
     */ 
    public void ignorableWhitespace(char[] ch, int start, int length)
                throws SAXException {
    }
    
    
    /**
     * Notes skipped entity.
     *
     * @param name Name of skipped entity.
     *
     */ 
    public void skippedEntity(String name)
        throws SAXException {
    }


    /**
     * Gets filter.
     *
     * @param filter (OGC WFS) Filter from (SAX) filter..
     */ 
    public void filter(org.opengis.filter.Filter filter) {
        
        LOGGER.finer("found filter: " + filter.toString());
        this.filter = filter;
    }    

    /**
     * Gets filter.
     *
     * @return (OGC WFS) Filter from (SAX) filter..
     */ 
    public org.opengis.filter.Filter getFilter() {
        return this.filter;
    }    
}
