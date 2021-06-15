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
package org.geotools.data.vpf.io;

import java.io.IOException;

import org.geotools.data.vpf.ifc.VPFHeader;
import org.geotools.data.vpf.ifc.VPFRow;


/**
 * SpatialIndexInputStream.java Created: Mon Feb 24 22:25:15 2003
 *
 * @author <a href="mailto:kobit@users.sourceforge.net">Artur Hefczyc</a>
 *
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class SpatialIndexInputStream extends VPFInputStream {
    /** Variable constant <code>SPATIAL_INDEX_ROW_SIZE</code> keeps value of */
    public static final long SPATIAL_INDEX_ROW_SIZE = 8;

    /**
     * Creates a new <code>SpatialIndexInputStream</code> instance.
     *
     * @param file a <code>String</code> value
     * @param byteOrder a <code>char</code> value
     *
     * @exception IOException if an error occurs
     */
    public SpatialIndexInputStream(String file, char byteOrder)
                            throws IOException {
        super(file, byteOrder);
    }

    /**
     * Describe <code>tableSize</code> method here.
     *
     * @return an <code>int</code> value
     */
    public int tableSize() {
        return -1;
    }

    /**
     * Describe <code>readHeader</code> method here.
     *
     * @return a <code>VPFHeader</code> value
     *
     * @exception IOException if an error occurs
     */
    public VPFHeader readHeader() throws IOException {
        return new SpatialIndexHeader(readInteger(), readFloat(), readFloat(), 
                                      readFloat(), readFloat(), readInteger());
    }

    /**
     * Describe <code>readRow</code> method here.
     *
     * @return a <code>VPFRow</code> value
     *
     * @exception IOException if an error occurs
     */
    public VPFRow readRow() throws IOException {
        return null;
    }

    /**
     * Describe <code>setPosition</code> method here.
     *
     * @param pos a <code>long</code> value
     *
     * @exception IOException if an error occurs
     */
    public void setPosition(long pos) throws IOException {
        seek(SPATIAL_INDEX_ROW_SIZE * pos);
    }
}
