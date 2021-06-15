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
package org.geotools.data.shapefile.ng.files;

import java.io.File;
import java.net.URL;

/**
 * Enumerates the known types of files associated with a shapefile.
 * 
 * @author jesse
 *
 *
 * @source $URL$
 */
public enum ShpFileType {

    /**
     * The .shp file. It contains the geometries of the shapefile
     */
    SHP("shp"),
    /**
     * the .dbf file, it contains the attribute information of the shapefile
     */
    DBF("dbf"),
    /**
     * the .shx file, it contains index information of the existing features
     */
    SHX("shx"),
    /**
     * the .prj file, it contains the projection information of the shapefile
     */
    PRJ("prj"),
    /**
     * the .qix file, A quad tree spatial index of the shapefile. It is the same
     * format the mapservers shptree tool generates
     */
    QIX("qix"),
    /**
     * the .fix file, it contains all the Feature IDs for constant time lookup
     * by fid also so that the fids stay consistent across deletes and adds
     */
    FIX("fix"),
    /**
     * the .shp.xml file, it contains the metadata about the shapefile
     */
    SHP_XML("shp.xml");

    public final String extension;
    public final String extensionWithPeriod;

    private ShpFileType(String extension) {
        this.extension = extension.toLowerCase();
        this.extensionWithPeriod = "." + this.extension;
    }

    /**
     * Returns the base of the file or null if the file passed in is not of the
     * correct type (has the correct extension.)
     * <p>
     * For example if the file is c:\shapefiles\file1.dbf. The DBF type will
     * return c:\shapefiles\file1 but all other will return null.
     */
    public String toBase(File file) {
        String path = file.getPath();
        return toBase(path);
    }

    /**
     * Returns the base of the file or null if the file passed in is not of the
     * correct type (has the correct extension.)
     * <p>
     * For example if the file is c:\shapefiles\file1.dbf. The DBF type will
     * return c:\shapefiles\file1 but all other will return null.
     */
    public String toBase(String path) {
        if (!path.toLowerCase().endsWith(extensionWithPeriod)
                || path.equalsIgnoreCase(extensionWithPeriod)) {
            return null;
        }

        int indexOfExtension = path.toLowerCase().lastIndexOf(
                extensionWithPeriod);
        return path.substring(0, indexOfExtension);
    }

    /**
     * Returns the base of the file or null if the file passed in is not of the
     * correct type (has the correct extension.)
     * <p>
     * For example if the file is c:\shapefiles\file1.dbf. The DBF type will
     * return c:\shapefiles\file1 but all other will return null.
     */
    public String toBase(URL url) {
        return toBase( url.toExternalForm() );        
    }
}
