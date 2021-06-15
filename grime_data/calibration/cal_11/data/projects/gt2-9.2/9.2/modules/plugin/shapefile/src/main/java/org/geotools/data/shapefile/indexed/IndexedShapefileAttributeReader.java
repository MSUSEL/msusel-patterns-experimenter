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
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.List;

import org.geotools.data.shapefile.ShapefileAttributeReader;
import org.geotools.data.shapefile.dbf.IndexedDbaseFileReader;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.geotools.index.CloseableIterator;
import org.geotools.index.Data;
import org.opengis.feature.type.AttributeDescriptor;

import com.vividsolutions.jts.geom.Envelope;

/**
 * An AttributeReader implementation for shape. Pretty straightforward. <BR/>The
 * default geometry is at position 0, and all dbf columns follow. <BR/>The dbf
 * file may not be necessary, if not, just pass null as the DbaseFileReader
 *
 *
 *
 * @source $URL$
 */
public class IndexedShapefileAttributeReader extends ShapefileAttributeReader {

    protected CloseableIterator<Data> goodRecs;

    private Data next;
    
    public IndexedShapefileAttributeReader(
            List<AttributeDescriptor> attributes, ShapefileReader shp,
            IndexedDbaseFileReader dbf, CloseableIterator<Data> goodRecs) {
        this(attributes.toArray(new AttributeDescriptor[0]), shp, dbf, goodRecs);
    }

    /**
     * Create the shape reader
     * 
     * @param atts -
     *                the attributes that we are going to read.
     * @param shp -
     *                the shape reader, required
     * @param dbf -
     *                the dbf file reader. May be null, in this case no
     *                attributes will be read from the dbf file
     * @param goodRecs
     *                Collection of good indexes that match the query.
     */
    public IndexedShapefileAttributeReader(AttributeDescriptor[] atts,
            ShapefileReader shp, IndexedDbaseFileReader dbf,
            CloseableIterator<Data> goodRecs) {
        super(atts, shp, dbf);
        this.goodRecs = goodRecs;
    }

    public void close() throws IOException {
        try {
            super.close();
        } finally {
            if( goodRecs != null ){
                goodRecs.close();
            }
            goodRecs = null;
        }
    }

    public boolean hasNext() throws IOException {
        if (this.goodRecs != null) {
            while (!featureAvailable && this.goodRecs.hasNext()) {
                next = (Data) goodRecs.next();
                
                Long l = (Long) next.getValue(1);
                shp.goTo((int) l.longValue());
                
                record = shp.nextRecord();
                
                // read the geometry, so that we can decide if this row is to be skipped or not
                Envelope envelope = record.envelope();
                // ... if geometry is out of the target bbox, skip both geom and row
                if (targetBBox != null && !targetBBox.isNull() && !targetBBox.intersects(envelope)) {
                    geometry = null;
                    continue;
                // ... if the geometry is awfully small avoid reading it (unless it's a point)
                } else if (simplificationDistance > 0 && envelope.getWidth() < simplificationDistance
                        && envelope.getHeight() < simplificationDistance) {
                    try {
                        if(screenMap != null && screenMap.checkAndSet(envelope)) {
                            geometry = null;
                            continue;
                        } else {
                            // if we are using the screenmap better provide a slightly modified
                            // version of the geometry bounds or we'll end up with many holes
                            // in the rendering
                            geometry = record.getSimplifiedShape(screenMap);
                        }
                    } catch(Exception e) {
                        geometry = record.getSimplifiedShape();
                    }
                // ... otherwise business as usual
                } else {
                    geometry = record.shape();
                }

                // read the dbf only if the geometry was not skipped
                if (dbf != null) {
                    ((IndexedDbaseFileReader) dbf).goTo(record.number);
                    row = dbf.readRow();
                } else {
                    row = null;
                }
                
                featureAvailable = true;
            }
            
            return featureAvailable;
        } else {
            return super.hasNext();
        }
    }

    public void next() throws IOException {
        if (!hasNext())
            throw new IndexOutOfBoundsException("No more features in reader");
        featureAvailable = false;

        
    }

    

    
}
