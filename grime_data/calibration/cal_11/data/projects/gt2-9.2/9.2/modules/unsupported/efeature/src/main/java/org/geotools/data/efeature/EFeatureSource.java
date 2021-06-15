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
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.efeature;

import java.io.IOException;

import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * @author kengu - 7. juli 2011
 *
 *
 * @source $URL$
 */
@SuppressWarnings("unchecked")
public class EFeatureSource extends ContentFeatureSource {

    /**
     * Cached {@link EFeatureInfo} instance
     */
    protected final EFeatureInfo eStructure;
    
    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------

    /**
     * @param entry
     * @param query
     */
    protected EFeatureSource(ContentEntry entry, Query query) {
        //
        // Forward to ContentFeatureSource
        //
        super(entry, query);
        //
        // Get structure instance
        //
        this.eStructure = getDataStore().eStructure().eGetFeatureInfo(entry.getTypeName());
        //
        // Cache schema
        // 
        this.schema = eStructure.getFeatureType();
    }

    // ----------------------------------------------------- 
    //  Overridden methods
    // -----------------------------------------------------

    /**
     * The {@link EFeatureDataStore} that this {@link EFeatureSource} originated from.
     */
    @Override
    public EFeatureDataStore getDataStore() {
        return (EFeatureDataStore)entry.getDataStore();
    }

    // ----------------------------------------------------- 
    //  ContentFeatureSource implementation
    // ----------------------------------------------------- 

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        return eStructure.featureType;
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {        
        return getBounds(getDataStore(), getSchema(), getReader(query));
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        return getCount(getDataStore(), getReader(query));
    }

    @Override
    protected EFeatureReader getReaderInternal(Query query) throws IOException {
        return new EFeatureReader(getDataStore(), query);
    }

    // ----------------------------------------------------- 
    //  Static helper methods
    // -----------------------------------------------------

    protected static ReferencedEnvelope getBounds(
            EFeatureDataStore eDataStore, SimpleFeatureType eType, 
            FeatureReader<SimpleFeatureType, SimpleFeature> eReader)
    throws IOException {
        //
        // Calculate manually TODO: Optimize 
        //
        eDataStore.getLogger().fine("Calculating bounds manually");
        //
        // Grab the 2D-part of the CRS
        //
        CoordinateReferenceSystem flatCRS = CRS.getHorizontalCRS(eType.getCoordinateReferenceSystem());
        //
        // Initialize bounds
        //
        ReferencedEnvelope bounds = new ReferencedEnvelope(flatCRS);
        //
        // Calculate bounds
        //
        try {
            if (eReader.hasNext()) {
                //
                // Initialize bounds
                //
                SimpleFeature f = eReader.next();
                bounds.init(f.getBounds());
                //
                // Make union of all bounds
                //
                while (eReader.hasNext()) {
                    f = eReader.next();
                    bounds.include(f.getBounds());
                }
            }
        } finally {
            //
            // Always ensure that reader is closed to prevent memory leakage
            //
            eReader.close();
        }
        //
        // Calculation finished
        //
        return bounds;
    }

    protected static int getCount(
            EFeatureDataStore eDataStore, 
            FeatureReader<SimpleFeatureType, SimpleFeature> eReader) 
    throws IOException {       
        //
        // Initialize
        //
        int count = 0;
        //
        // Calculate manually TODO: Optimize 
        //
        eDataStore.getLogger().fine("Calculating feature count manually");
        //
        // Calculate feature count
        //
        try {
            //
            // Count all features
            //
            while (eReader.hasNext()) {
                eReader.next();
                count++;
            }
        } finally {
            //
            // Always ensure that reader is closed to prevent memory leakage
            //
            eReader.close();
        }
        //
        // Calculation finished
        //
        return count;
    }    
    
    
}
