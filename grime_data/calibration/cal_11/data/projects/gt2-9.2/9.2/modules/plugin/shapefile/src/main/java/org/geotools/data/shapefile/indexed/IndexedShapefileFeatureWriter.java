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

import static org.geotools.data.shapefile.ShpFileType.FIX;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.TimeZone;
import java.util.logging.Level;

import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.shapefile.FileWriter;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.shapefile.ShapefileFeatureWriter;
import org.geotools.data.shapefile.ShpFileType;
import org.geotools.data.shapefile.ShpFiles;
import org.geotools.data.shapefile.StorageFile;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * A FeatureWriter for ShapefileDataStore. Uses a write and annotate technique
 * to avoid buffering attributes and geometries. Because the shape and dbf
 * require header information which can only be obtained by reading the entire
 * series of Features, the headers are updated after the initial write
 * completes.
 */
class IndexedShapefileFeatureWriter extends ShapefileFeatureWriter implements
        FileWriter {

    private IndexedShapefileDataStore indexedShapefileDataStore;
    private IndexedFidWriter fidWriter;

    private String currentFid;

    public IndexedShapefileFeatureWriter(String typeName, ShpFiles shpFiles,
            IndexedShapefileAttributeReader attsReader,
             FeatureReader<SimpleFeatureType, SimpleFeature> featureReader, IndexedShapefileDataStore datastore,
             Charset charset, TimeZone timeZone)
            throws IOException {
        super(typeName, shpFiles, attsReader, featureReader, charset, timeZone);
        this.indexedShapefileDataStore = datastore;
        if (!datastore.isLocal()) {
            this.fidWriter = IndexedFidWriter.EMPTY_WRITER;
        } else {
            StorageFile storageFile = shpFiles.getStorageFile(FIX);
            storageFiles.put(FIX, storageFile);
            this.fidWriter = new IndexedFidWriter(shpFiles, storageFile);
        }
    }

    @Override
    public SimpleFeature next() throws IOException {
        // closed already, error!
        if (featureReader == null) {
            throw new IOException("Writer closed");
        }

        // we have to write the current feature back into the stream
        if (currentFeature != null) {
            write();
        }

        long next = fidWriter.next();
        currentFid = getFeatureType().getTypeName() + "." + next;
        SimpleFeature feature = super.next();
        return feature;
    }

    @Override
    protected String nextFeatureId() {
        return currentFid;
    }

    @Override
    public void remove() throws IOException {
        fidWriter.remove();
        super.remove();
    }

    @Override
    public void write() throws IOException {
        fidWriter.write();
        super.write();
    }

    /**
     * Release resources and flush the header information.
     */
    public void close() throws IOException {
        super.close();
        fidWriter.close();

        try {
            if (shpFiles.isLocal()) {
                if (indexedShapefileDataStore.needsGeneration(ShpFileType.FIX)) {
                    FidIndexer.generate(shpFiles);
                }

                deleteFile(ShpFileType.QIX);

                if (indexedShapefileDataStore.treeType == IndexType.QIX) {
                    indexedShapefileDataStore.buildQuadTree();
                }
            }
        } catch (Throwable e) {
            indexedShapefileDataStore.treeType = IndexType.NONE;
            ShapefileDataStoreFactory.LOGGER.log(Level.WARNING,
                    "Error creating Spatial index", e);
        }
    }

    @Override
    protected void doClose() throws IOException {
        super.doClose();
        try{
            fidWriter.close();
        }catch(Throwable e){
            indexedShapefileDataStore.treeType = IndexType.NONE;
            ShapefileDataStoreFactory.LOGGER.log(Level.WARNING,
                    "Error creating Feature ID index", e);
        }
    }
    
    private void deleteFile(ShpFileType shpFileType) {
        URL url = shpFiles.acquireWrite(shpFileType, this);
        try {
            File toDelete = DataUtilities.urlToFile(url);

            if (toDelete.exists()) {
                toDelete.delete();
            }
        } finally {
            shpFiles.unlockWrite(url, this);
        }
    }

    public String id() {
        return getClass().getName();
    }
}
