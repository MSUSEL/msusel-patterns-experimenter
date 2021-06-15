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

import javax.xml.namespace.QName;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;

/**
 * Interface for objects which need to take over parsing control from the main 
 * parsing driver.
 * <p>
 * An example of such a case is when a schema dynamically imports content from 
 * other schemas. 
 * </p>
 * <p>
 * Instances of these objects are declared in the {@link Configuration#getContext()}. Example:
 * <pre>
 * MyParserDelegate delegate = new MyParserDelegate();
 * Configuration configuration = ...;
 * 
 * configuration.getContext().registerComponentInstance( delegate );
 * </pre>
 * </p>
 * @author Justin Deoliveira, OpenGEO
 *
 * @see ParserDelegate2
 * @source $URL$
 */
public interface ParserDelegate extends ContentHandler {

    /**
     * Determines if this delegate can handle the specified element name.
     * <p>
     * A common check in this method would be to check the namespace of the element.
     * </p>
     * @param elementName The name of the element to potentially handle.
     * 
     * @return True if this delegate handles elements of the specified name and should
     * take over parsing.
     * 
     * @deprecated This method is deprecated and 
     * {@link ParserDelegate2#canHandle(QName, Attributes, Handler)} should be used. After one major 
     * release cycle ParserDelegate2 methods will be pulled into this interface and this method will 
     * be removed. In preparation implementations should implement both interfaces.
     */
    boolean canHandle( QName elementName );
    
    /**
     * Gets the final parsed object from the delegate.
     * <p>
     * This method is called after parsing control returns to the main parsing 
     * driver.
     * </p> 
     */
    Object getParsedObject();
}
