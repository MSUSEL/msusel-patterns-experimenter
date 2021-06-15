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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

/**
 * This is the main entry point into the XSI parsing routines.
 * <p>
 * Example Use:
 * 
 * <pre>
 *     Object x = DocumentFactory.getInstance(new URI(&quot;MyInstanceDocumentURI&quot;);
 * </pre>
 * 
 * </p>
 * 
 * @author dzwiers, Refractions Research, Inc. http://www.refractions.net
 * @author $Author:$ (last modification)
 *
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/library/xml/src/main/java/org/geotools/xml
 *         /DocumentFactory.java $
 * @version $Id$
 */
public class DocumentFactory {

    /**
     * When this hint is contained and set to Boolean.FALSE, element ordering will not be validated.
     * This key may also affect data validation within the parse routines. The inherent safety of
     * the resulting objects is weekend by turning this param to false.
     */
    public static final String VALIDATION_HINT = "DocumentFactory_VALIDATION_HINT";

    /**
     * <p>
     * calls getInstance(URI,Level) with Level.WARNING
     * </p>
     * 
     * @param desiredDocument
     * @param hints
     *            May be null.
     * 
     * @return Object
     * 
     * @throws SAXException
     * 
     * @see DocumentFactory#getInstance(URI, Map, Level)
     */
    public static Object getInstance(URI desiredDocument, Map hints) throws SAXException {
        return getInstance(desiredDocument, hints, Level.WARNING);
    }

    /**
     * <p>
     * Parses the instance data provided. This method assumes that the XML document is fully
     * described using XML Schemas. Failure to be fully described as Schemas will result in errors,
     * as opposed to a vid parse.
     * </p>
     * 
     * @param desiredDocument
     * @param hints
     *            May be null.
     * @param level
     * 
     * @return Object
     * 
     * @throws SAXException
     */
    public static Object getInstance(URI desiredDocument, Map hints, Level level)
            throws SAXException {
        SAXParser parser = getParser();

        XMLSAXHandler xmlContentHandler = new XMLSAXHandler(desiredDocument, hints);
        XMLSAXHandler.setLogLevel(level);

        try {
            parser.parse(desiredDocument.toString(), xmlContentHandler);
        } catch (IOException e) {
            throw new SAXException(e);
        }

        return xmlContentHandler.getDocument();
    }

    /**
     * <p>
     * Parses the instance data provided. This method assumes that the XML document is fully
     * described using XML Schemas. Failure to be fully described as Schemas will result in errors,
     * as opposed to a vid parse.
     * </p>
     * 
     * @param is
     * @param hints
     *            May be null.
     * @param level
     * 
     * @return Object
     * 
     * @throws SAXException
     */
    public static Object getInstance(InputStream is, Map hints, Level level) throws SAXException {
        SAXParser parser = getParser();

        XMLSAXHandler xmlContentHandler = new XMLSAXHandler(hints);
        XMLSAXHandler.setLogLevel(level);

        try {
            parser.parse(is, xmlContentHandler);
        } catch (IOException e) {
            XMLSAXHandler.logger.warning(e.toString());
            throw new SAXException(e);
        }

        return xmlContentHandler.getDocument();
    }

    /*
     * convinience method to create an instance of a SAXParser if it is null.
     */
    private static SAXParser getParser() throws SAXException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        spf.setValidating(false);

        try {
            // spf.setFeature("http://xml.org/sax/features/external-general-entities", false);
            // spf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

            SAXParser sp = spf.newSAXParser();
            return sp;
        } catch (ParserConfigurationException e) {
            throw new SAXException(e);
        }
    }
}
