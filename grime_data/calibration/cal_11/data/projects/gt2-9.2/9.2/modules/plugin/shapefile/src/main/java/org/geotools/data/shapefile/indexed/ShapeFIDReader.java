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
package org.geotools.data.shapefile.indexed;

import java.io.IOException;

import org.geotools.data.FIDReader;
import org.geotools.data.shapefile.ShapefileAttributeReader;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Reader that returns FeatureIds in a quick fashion.
 * 
 * @author Tommaso Nolli
 *
 *
 * @source $URL$
 */
public class ShapeFIDReader implements FIDReader {
    protected static final String CLOSE_MESG = "Close has already been called"
            + " on this FIDReader";
    private boolean opened;
    private ShapefileAttributeReader reader;
    private int len;
    protected StringBuffer buffer;

    public ShapeFIDReader(String typeName,
            ShapefileAttributeReader reader) {
        buffer = new StringBuffer(typeName);
        buffer.append('.');
        len = typeName.length() + 1;
        this.opened = true;
        this.reader = reader;
    }

    public ShapeFIDReader(SimpleFeatureType featureType,
            ShapefileAttributeReader reader) {
        this(featureType.getTypeName(), reader);
    }

    /**
     * Release any resources associated with this reader
     */
    public void close() {
        this.opened = false;
    }

    /**
     * This method always returns true, since it is built with a
     * <code>ShapefileDataStore.Reader</code> you have to call
     * <code>ShapefileDataStore.Reader.hasNext()</code>
     * 
     * @return always return <code>true</code>
     * 
     * @throws IOException
     *                 If closed
     */
    public boolean hasNext() throws IOException {
        if (!this.opened) {
            throw new IOException(CLOSE_MESG);
        }

        /*
         * In DefaultFIDReader this is always called after
         * atttributesReader.hasNext so, as we use the same attributeReader,
         * we'll return true
         */
        return true;
    }

    /**
     * Read the feature id.
     * 
     * @return the Feature Id
     * 
     * @throws IOException
     *                 If closed
     */
    public String next() throws IOException {
        if (!this.opened) {
            throw new IOException(CLOSE_MESG);
        }

        buffer.delete(len, buffer.length());
        buffer.append(reader.getRecordNumber());

        return buffer.toString();
    }
}
