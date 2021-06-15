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

import static org.geotools.data.shapefile.ShpFileType.FIX;
import static org.geotools.data.shapefile.ShpFileType.SHP;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import org.geotools.data.shapefile.ShpFiles;
import org.geotools.data.shapefile.StorageFile;
import org.geotools.data.shapefile.shp.IndexFile;

/**
 * Creates a .fix file (fid index).
 * 
 * @author Jesse
 *
 *
 *
 * @source $URL$
 */
public class FidIndexer {
    static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.data.shapefile");

    /**
     * Generates the FID index file for the shpFile
     */
    public static synchronized void generate(URL shpURL) throws IOException {
        generate(new ShpFiles(shpURL));
    }

    /**
     * Generates the FID index file for the shpFiles
     */
    public static void generate(ShpFiles shpFiles) throws IOException {
        LOGGER.fine("Generating fids for " + shpFiles.get(SHP));

        
        IndexFile indexFile = null;
        StorageFile file = shpFiles.getStorageFile(FIX);
        IndexedFidWriter writer = null;
        
        try {
            indexFile = new IndexFile(shpFiles, false);

            // writer closes channel for you.
            writer = new IndexedFidWriter(shpFiles, file);

            for (int i = 0, j = indexFile.getRecordCount(); i < j; i++) {
                writer.next();
            }

        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                file.replaceOriginal();
            } finally {
                if (indexFile != null) {
                    indexFile.close();
                }
            }
        }
    }

}
