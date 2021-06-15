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
package org.geotools.jdbc;

import java.io.IOException;
import java.sql.Connection;

import org.geotools.data.DelegatingFeatureWriter;
import org.geotools.data.FeatureWriter;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * 
 *
 * @source $URL$
 */
public class JDBCClosingFeatureWriter implements FeatureWriter<SimpleFeatureType,SimpleFeature> {

    FeatureWriter writer;
    
    public JDBCClosingFeatureWriter(FeatureWriter writer) {
        this.writer = writer;
    }

   public SimpleFeatureType getFeatureType() {
        return (SimpleFeatureType) writer.getFeatureType();
    }

    public boolean hasNext() throws IOException {
        return writer.hasNext();
    }


    public SimpleFeature next() throws IOException {
        return (SimpleFeature) writer.next();
    }

    
    public void remove() throws IOException {
        writer.remove();
    }

    public void write() throws IOException {
        writer.write();
    }
    
    public void close() throws IOException {
        FeatureWriter w = writer;
        while( w instanceof DelegatingFeatureWriter ) {
            if ( w instanceof JDBCFeatureReader ) {
                break;
            }
            
            w = ((DelegatingFeatureWriter)w).getDelegate();
        }
        
        if ( w instanceof JDBCFeatureReader ) {
            JDBCFeatureReader jdbcReader = (JDBCFeatureReader) w;
            JDBCFeatureSource fs = jdbcReader.featureSource;
            Connection cx = jdbcReader.cx;

            try {
                writer.close();
            }
            finally {
                fs.getDataStore().releaseConnection( cx, fs.getState() );
            }
        }
    }
}
