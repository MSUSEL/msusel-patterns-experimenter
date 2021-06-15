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

import org.geotools.data.vpf.ifc.VPFRow;


/**
 * VariableIndexRow.java Created: Sun Mar 16 23:28:11 2003
 *
 * @author <a href="mailto:kobit@users.sourceforge.net">Artur Hefczyc</a>
 *
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class VariableIndexRow implements VPFRow {
    /** Describe variable <code>offset</code> here. */
    private int offset = 0;

    /** Describe variable <code>size</code> here. */
    private int size = 0;

    /**
     * Creates a new <code>VariableIndexRow</code> instance.
     *
     * @param offset an <code>int</code> value
     * @param size an <code>int</code> value
     */
    public VariableIndexRow(int offset, int size) {
        this.offset = offset;
        this.size = size;
    }

    // VariableIndexRow constructor

    /**
     * Gets the value of offset
     *
     * @return the value of offset
     */
    public int getOffset() {
        return this.offset;
    }

    //   /**
    //    * Sets the value of offset
    //    *
    //    * @param argOffset Value to assign to this.offset
    //    */
    //   public void setOffset(int argOffset) {
    //     this.offset = argOffset;
    //   }

    /**
     * Gets the value of size
     *
     * @return the value of size
     */
    public int getSize() {
        return this.size;
    }

    //   /**
    //    * Sets the value of size
    //    *
    //    * @param argSize Value to assign to this.size
    //    */
    //   public void setSize(int argSize) {
    //     this.size = argSize;
    //   }
}

// VariableIndexRow
