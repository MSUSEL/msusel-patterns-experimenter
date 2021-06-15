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
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data;

import java.io.IOException;

import org.opengis.feature.type.AttributeDescriptor;


/**
 * The low-level attribute reading API.  An AttributeReader is responsible for
 * reading a finite set of attributes from an underlying storage format. It
 * provides meta-data regarding the data it can provide, and an iterative,
 * row-based approach for accessing the data.
 *
 * @author Ian Schneider
 *
 *
 * @source $URL$
 * @version $Id$
 *
 * @see AttributeAcceptor
 */
public interface AttributeReader {
    /**
     * The number of attributes this reader can read, i.e the length of a row.
     *
     * @return Number of attribtues this reader can read
     */
    int getAttributeCount();

    /**
     * Retrieve the AttributeType at the given index.
     *
     * @return AttributeType at given index
     */
    AttributeDescriptor getAttributeType(int index)
        throws ArrayIndexOutOfBoundsException;

    /**
     * Release any resources associated with this reader
     */
    void close() throws IOException;

    /**
     * Does another set of attributes exist in this reader?
     *
     * @return <code>true</code> if additional content exists for
     *         AttributeReader
     */
    boolean hasNext() throws IOException;

    /**
     * Advance the reader to the next set of attributes.
     */
    void next() throws IOException;

    /**
     * Read the attribute at the given index.
     *
     * @return Object Attribute at given index
     */
    Object read(int index) throws IOException, ArrayIndexOutOfBoundsException;
}
