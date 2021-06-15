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
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.store;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.geotools.data.FeatureWriter;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * An iterator wrapper for a FeatureWriter - for use with
 * an AbstractFeatureCollection.
 * <p>
 * There is no reason modify this class, subclasses that wish
 * to work with a custom iterator need just that - a custom iterator.
 * <p>
 * <p>
 * The use of this class against a SimpleFeatureSource not backed by
 * a Transaction may *really* cut into performance. Consider if
 * you will the overhead involved in writing out each feature into
 * a temporary file (when the user may not even modify anything).
 * </p>
 * @author jgarnett
 * @since 2.1.RC0
 * @source $URL$
 */
final class FeatureWriterIterator implements Iterator<SimpleFeature> {
    FeatureWriter<SimpleFeatureType, SimpleFeature> writer;    
    public FeatureWriterIterator( FeatureWriter<SimpleFeatureType, SimpleFeature> writer ){
        this.writer = writer;
    }
    public boolean hasNext() {
        try {
            if( writer == null ) {
                return false;
            }
            writer.write(); // write out any "changes" made
            if( writer.hasNext() ){
                return true;
            }
            else {
                close();
                return false;
                // auto close because we don't trust client
                // code to call close 
            }
        } catch (IOException e) {
            close();
            return false; // failure sounds like lack of next to me
        }        
    }

    public SimpleFeature next() {
        if( writer == null ) {
            throw new NoSuchElementException( "Iterator has been closed" );            
        }
        try {
            return writer.next();
        } catch (IOException io) {
            NoSuchElementException problem = new NoSuchElementException( "Could not obtain the next feature:"+io );
            problem.initCause( io );
            throw problem;
        }       
    }
    public void remove() {
        try {
            writer.remove();
        } catch (IOException problem) {
            throw (IllegalStateException) new IllegalStateException( "Could not remove feature" ).initCause( problem ); 
        }        
    }
    /**
     * This method only needs package visability as only AbstractFeatureCollection
     * is trusted enough to call it.
     */
    void close(){
        if( writer != null){
            try {
                writer.close();
            } catch (IOException e) {
                // sorry but iterators die quitely in the night
            }
            writer = null;
        }
    }
};
