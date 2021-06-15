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

/** - Added hasNext to support the FeatureWriter API.
 *  - Changed order of writer parameters to match Collections, JDBC API.
 *  - Added IOExceptions on all methods.
 *  - Do we want AttributeWriters to know about the schema and perform
 *    validation??
 *  
 *
 *
 *
 * @source $URL$
 * @version $Id$
 * @author  Ian Schneider
 * @author Sean Geoghegan, Defence Science and Technology Organisation.
 */
public interface AttributeWriter {
    /**
     * The number of attributes this reader can read, i.e the length of a row.
     */
    int getAttributeCount();

    /**
     * Retrieve the AttributeType at the given index.
     */
    AttributeDescriptor getAttributeType(int i) throws ArrayIndexOutOfBoundsException;
        
    /**
     * Advance the AttributeWriter, all calls to write will correspond to the
     * same set of attributes until next is called again.
     */
    void next() throws IOException;
    
    /**
     * Write the given attribute value at the position indicated.
     * Implementations can choose to immediately flush the write or buffer it.
     */
    void write(int position, Object attribute) throws IOException;
    
    void close() throws IOException;
    
    /** Query whether there are other rows in the attribute writer.
     * 
     * @throws IOException
     * @see FeatureWriter#hasNext()
     */
    boolean hasNext() throws IOException;
    
}
