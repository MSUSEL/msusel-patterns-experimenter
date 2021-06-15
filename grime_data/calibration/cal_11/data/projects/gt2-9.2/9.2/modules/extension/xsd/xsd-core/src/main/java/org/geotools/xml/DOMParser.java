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
package org.geotools.xml;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import org.geotools.xml.impl.ParserHandler;


/**
 * Parses a DOM (Document Object Model) using the geotools xml binding system.
 * <p>
 * This parser should be used if the source xml being parsed has already been parsed into a 
 * {@link Document}. If not use one of {@link Parser} or {@link Parser}.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 *
 * @source $URL$
 */
public class DOMParser {
    Configuration configuration;
    Document document;
    ParserHandler handler;

    /**
     * Creates a new instance of the parser.
     *
     * @param configuration Object representing the configuration of the parser.
     * @param document An xml document.
     *
     */
    public DOMParser(Configuration configuration, Document document) {
        this.configuration = configuration;
        this.document = document;
    }

    /**
     * Parses the supplied DOM returning a single object respresenting the
     * result of the parse.
     *
     * @return The object representation of the root element of the document.
     *
     */
    public Object parse() throws IOException, SAXException, ParserConfigurationException {
        //Prepare the DOM source
        Source source = new DOMSource(document);

        Parser fake = new Parser(configuration);
        
        // Create the handler to handle the SAX events
        handler = fake.getParserHandler();

        try {
            // Prepare the result
            SAXResult result = new SAXResult(handler);

            TransformerFactory xformerFactory = TransformerFactory.newInstance();

            // Create a transformer
            Transformer xformer = xformerFactory.newTransformer();

            // Traverse the DOM tree
            xformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            new ParserConfigurationException().initCause(e);
        } catch (TransformerException e) {
            throw (IOException) new IOException().initCause(e);
        }

        return handler.getValue();
    }
}
