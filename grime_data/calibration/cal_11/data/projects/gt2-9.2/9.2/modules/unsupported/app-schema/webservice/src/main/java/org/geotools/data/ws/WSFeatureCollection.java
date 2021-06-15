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
package org.geotools.data.ws;

import java.io.IOException;

import org.geotools.data.Query;
import org.geotools.data.complex.xml.XmlFeatureCollection;
import org.geotools.data.complex.xml.XmlResponse;
import org.geotools.data.store.DataFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;

import org.opengis.feature.simple.SimpleFeatureType;


/**
 * A {@link FeatureCollection} whose iterators are based on the FeatureReaders returned by a
 * {@link XmlDataStore}.
 * 
 * @author rpetty
 * @version $Id$
 * @since 2.5.x
 *
 *
 *
 * @source $URL$
 *         http://svn.geotools.org/trunk/modules/unsupported/app-schema/webservice/src/main/java/org/geotools/wfs/v_1_1_0
 *         /data/XmlSimpleFeatureParser.java $
 */

public class WSFeatureCollection extends DataFeatureCollection implements XmlFeatureCollection {
    
    private Query query;

    private XmlDataStore dataStore;    

    private XmlResponse xmlResponse;
       
    /**
     * Cached size so multiple calls to {@link #getCount()} does not require multiple server calls
     */
    private int cachedSize = -1;

    /**
     * @param dataStore
     * @param query
     *            properly named query
     * @throws IOException
     */
    public WSFeatureCollection(XmlDataStore dataStore, Query query) throws IOException {
        this.dataStore = dataStore;
        this.query = query;
    }

    @Override
    public SimpleFeatureType getSchema() {
        throw new UnsupportedOperationException("No schema for WS!");
    }

    /**
     * Calculates and returns the aggregated bounds of the collection contents, potentially doing a
     * full scan.
     * <p>
     * As a bonuns, if a full scan needs to be done updates the cached collection size so a future
     * call to {@link #getCount()} does not require an extra server call.
     * </p>
     */
    @Override
    public ReferencedEnvelope getBounds() {
        throw new UnsupportedOperationException("No bounds for WS!");
    }

    /**
     * Calculates the feature collection size, doing a full scan if needed.
     * <p>
     * <b>WARN</b>: this method could be very inefficient if the size cannot be efficiently
     * calculated. That is, it is not cached and {@link XmlDataStore#getCount(Query)} returns
     * {@code -1}.
     * </p>
     * 
     * @return the FeatureCollection<SimpleFeatureType, SimpleFeature> size.
     * @see DataFeatureCollection#getCount()
     */
    @Override
    public int getCount() throws IOException {
        if (cachedSize != -1) {
            return cachedSize;
        }
        cachedSize = dataStore.getCount(query);
        if (cachedSize == -1) {
            // no luck, cache both bounds and count with a full scan
            getBounds();
        }
        return  cachedSize;
    }
    
    public XmlResponse xmlResponse(String xpath, String value) {
        try {
            xmlResponse = dataStore.getXmlReader(query, xpath, value);
        } catch (IOException e) {           
            throw new RuntimeException(e);
        }
        return xmlResponse;
    }
    
    public XmlResponse xmlResponse() {
        try {
            xmlResponse = dataStore.getXmlReader(query);
        } catch (IOException e) {           
            throw new RuntimeException(e);
        }
        return xmlResponse;
    }
}
