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
 * Attribute Reader that joins.
 *
 * @author Ian Schneider
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class JoiningAttributeReader implements AttributeReader {
    private AttributeReader[] readers;
    private int[] index;
    private AttributeDescriptor[] metaData;

    /**
     * Creates a new instance of JoiningAttributeReader
     *
     * @param readers Readers to join
     */
    public JoiningAttributeReader(AttributeReader[] readers) {
        this.readers = readers;

        this.metaData = joinMetaData(readers);
    }

    private AttributeDescriptor[] joinMetaData(AttributeReader[] readers) {
        int total = 0;
        index = new int[readers.length];

        for (int i = 0, ii = readers.length; i < ii; i++) {
            index[i] = total;
            total += readers[i].getAttributeCount();
        }

        AttributeDescriptor[] md = new AttributeDescriptor[total];
        int idx = 0;

        for (int i = 0, ii = readers.length; i < ii; i++) {
            for (int j = 0, jj = readers[i].getAttributeCount(); j < jj; j++) {
                md[idx] = readers[i].getAttributeType(j);
                idx++;
            }
        }

        return md;
    }

    public void close() throws IOException {
        IOException dse = null;

        for (int i = 0, ii = readers.length; i < ii; i++) {
            try {
                readers[i].close();
            } catch (IOException e) {
                dse = e;
            }
        }

        if (dse != null) {
            throw dse;
        }
    }

    public boolean hasNext() throws IOException {
        for (int i = 0, ii = readers.length; i < ii; i++) {
            if (readers[i].hasNext()) {
                return true;
            }
        }

        return false;
    }

    public void next() throws IOException {
        for (int i = 0, ii = readers.length; i < ii; i++) {
            if (readers[i].hasNext()) {
                readers[i].next();
            }
        }
    }

    public Object read(int idx) throws IOException {
        AttributeReader reader = null;

        for (int i = index.length - 1; i >= 0; i--) {
            if (idx >= index[i]) {
                idx -= index[i];
                reader = readers[i];

                break;
            }
        }

        if (reader == null) {
            throw new ArrayIndexOutOfBoundsException(idx);
        }

        return reader.read(idx);
    }

    public int getAttributeCount() {
        return metaData.length;
    }

    public AttributeDescriptor getAttributeType(int i) {
        return metaData[i];
    }
}
