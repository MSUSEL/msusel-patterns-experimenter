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
package org.geotools.data.shapefile.prj;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.geotools.data.shapefile.FileReader;
import org.geotools.data.shapefile.ShpFileType;
import org.geotools.data.shapefile.ShpFiles;
import org.geotools.data.shapefile.StreamLogging;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.resources.NIOUtilities;
import org.opengis.referencing.FactoryException;

/**
 * 
 *
 *
 * @source $URL$
 */
public class PrjFileReader implements FileReader {

    ByteBuffer buffer;
    ReadableByteChannel channel;
    CharBuffer charBuffer;
    CharsetDecoder decoder;
    StreamLogging streamLogger = new StreamLogging("PRJ reader");

    org.opengis.referencing.crs.CoordinateReferenceSystem cs;
    private boolean memoryMapped=true;

    // private int[] content;

    /**
     * Load the index file from the given channel.
     * 
     * @param shpFiles
     *                The channel to read from.
     * @throws IOException
     *                 If an error occurs.
     */
    public PrjFileReader(ShpFiles shpFiles) throws IOException {
        Charset chars = Charset.forName("ISO-8859-1");
        decoder = chars.newDecoder();
        this.channel = shpFiles.getReadChannel(ShpFileType.PRJ, this);
        streamLogger.open();
        init();

        // ok, everything is ready...
        decoder.decode(buffer, charBuffer, true);
        buffer.limit(buffer.capacity());
        charBuffer.flip();

        String wkt = charBuffer.toString();

        try {
            cs = ReferencingFactoryFinder.getCRSFactory(null)
                    .createFromWKT(wkt);
        } catch (FactoryException e) {
            cs = null;
        }
    }

    public org.opengis.referencing.crs.CoordinateReferenceSystem getCoodinateSystem() {
        return cs;
    }

    private int fill(ByteBuffer buffer, ReadableByteChannel channel)
            throws IOException {
        int r = buffer.remaining();
        // channel reads return -1 when EOF or other error
        // because they a non-blocking reads, 0 is a valid return value!!
        while (buffer.remaining() > 0 && r != -1) {
            r = channel.read(buffer);
        }
        if (r == -1) {
            buffer.limit(buffer.position());
        }
        return r;
    }

    private void init() throws IOException {
        // create the ByteBuffer
        // if we have a FileChannel, lets map it
        if (channel instanceof FileChannel) {
            FileChannel fc = (FileChannel) channel;
            buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            buffer.position((int) fc.position());
            memoryMapped = true;
        } else {
            // Some other type of channel
            // start with a 8K buffer, should be more than adequate
            int size = 8 * 1024;
            // if for some reason its not, resize it
            // size = header.getRecordLength() > size ? header.getRecordLength()
            // : size;
            buffer = NIOUtilities.allocate(size);
            // fill it and reset
            fill(buffer, channel);
            buffer.flip();
        }

        // The entire file is in little endian
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        charBuffer = CharBuffer.allocate(8 * 1024);
        Charset chars = Charset.forName("ISO-8859-1");
        decoder = chars.newDecoder();

    }

    public void close() throws IOException {
        if (buffer != null) {
            NIOUtilities.clean(buffer, memoryMapped);
            buffer = null;
        }

        if (channel.isOpen()) {
            channel.close();
            streamLogger.close();
        }
    }

    public String id() {
        return getClass().getName();
    }

}
