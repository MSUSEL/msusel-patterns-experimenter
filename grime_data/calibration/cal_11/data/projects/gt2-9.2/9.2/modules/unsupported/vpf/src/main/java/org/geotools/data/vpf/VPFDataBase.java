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
package org.geotools.data.vpf;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.geotools.data.vpf.file.VPFFile;
import org.geotools.data.vpf.file.VPFFileFactory;
import org.geotools.data.vpf.ifc.FileConstants;

import org.geotools.feature.SchemaException;
import org.opengis.feature.simple.SimpleFeature;


/**
 * This class is not completely implemented due to a decision that the 
 * VPFDataStore shall correspond to the VPFLibrary class, not this class
 *
 * @author <a href="mailto:kobit@users.sourceforge.net">Artur Hefczyc</a>
 * @author <a href="mailto:knuterik@onemap.org">Knut-Erik Johnsen</a>, Project
 *         OneMap
 *
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class VPFDataBase implements FileConstants {
    /**
     * The libraries in the database
     */
    private final List libraries = new Vector();
    /**
     * Constructor
     * @param directory A <code>File</code> representing the base directory of the database
     * @throws IOException
     * @throws SchemaException
     */
    public VPFDataBase(File directory) throws IOException, SchemaException {
        VPFFile vpfTable;
        String vpfTableName;
        SimpleFeature feature;
        VPFLibrary library;

        // read libraries info
        vpfTableName = new File(directory, LIBRARY_ATTTIBUTE_TABLE).toString();
        vpfTable = VPFFileFactory.getInstance().getFile(vpfTableName);

        Iterator iter = vpfTable.readAllRows().iterator();

        while (iter.hasNext()) {
            feature = (SimpleFeature) iter.next();

            try {
              library = new VPFLibrary(feature, directory);
              libraries.add(library);
            }
            catch ( java.io.FileNotFoundException ex ) {
                // This must be a partial data set - the library wasn't found so just ignore it
            }
        }

        //        // read data base header info
        //        //this.directory = directory;
        //        String vpfTableName = new File(directory, DATABASE_HEADER_TABLE).toString();
        //        VPFFile vpfTable = VPFFileFactory.getInstance().getFile(vpfTableName);
        //        vpfTable.reset();
        //        Feature dataBaseInfo = (Feature) vpfTable.readFeature();
        ////        vpfTable.close();
        //
        //        // read libraries info
        //        vpfTableName = new File(directory, LIBRARY_ATTTIBUTE_TABLE).toString();
        //        vpfTable = VPFFileFactory.getInstance().getFile(vpfTableName);
        //
        //        Iterator iter = vpfTable.readAllRows().iterator();
        //        while(iter.hasNext()){
        //            
        //        }
        ////        vpfTable.close();
        //
        //        TableRow[] libraries_tmp = (TableRow[]) list.toArray(new TableRow[list.size()]);
        //        libraries = new VPFLibrary[libraries_tmp.length];
        //
        //        for (int i = 0; i < libraries_tmp.length; i++) {
        //            libraries[i] = new VPFLibrary(libraries_tmp[i], directory, this);
        //        }
    }
    /**
     * Returns the libraries that are in the database
     * @return a <code>List</code> containing <code>VPFLibrary</code> objects
     */
    public List getLibraries() {
        return libraries;
    }
    /**
     * Returns the minimum X value of the database
     * @return a <code>double</code> value
     */
    public double getMinX() {
        double result = Double.NaN;
        Iterator iter = libraries.iterator();
        VPFLibrary library;

        if (iter.hasNext()) {
            library = (VPFLibrary) iter.next();
            result = library.getXmin();
        }

        while (iter.hasNext()) {
            library = (VPFLibrary) iter.next();
            result = Math.min(result, library.getXmin());
        }

        return result;
    }

    /**
     * Returns the minimum X value of the database
     * @return a <code>double</code> value
     */
    public double getMinY() {
        double result = Double.NaN;
        Iterator iter = libraries.iterator();
        VPFLibrary library;

        if (iter.hasNext()) {
            library = (VPFLibrary) iter.next();
            result = library.getYmin();
        }

        while (iter.hasNext()) {
            library = (VPFLibrary) iter.next();
            result = Math.min(result, library.getYmin());
        }

        return result;
    }

    /**
     * Returns the minimum X value of the database
     * @return a <code>double</code> value
     */
    public double getMaxX() {
        double result = Double.NaN;
        Iterator iter = libraries.iterator();
        VPFLibrary library;

        if (iter.hasNext()) {
            library = (VPFLibrary) iter.next();
            result = library.getXmax();
        }

        while (iter.hasNext()) {
            library = (VPFLibrary) iter.next();
            result = Math.max(result, library.getXmax());
        }

        return result;
    }

    /**
     * Returns the minimum X value of the database
     * @return a <code>double</code> value
     */
    public double getMaxY() {
        double result = Double.NaN;
        Iterator iter = libraries.iterator();
        VPFLibrary library;

        if (iter.hasNext()) {
            library = (VPFLibrary) iter.next();
            result = library.getYmax();
        }

        while (iter.hasNext()) {
            library = (VPFLibrary) iter.next();
            result = Math.max(result, library.getYmax());
        }

        return result;
    }
    /*
     *  (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "VPF database with the following extents: \n" + getMinX() + " "
        + getMinY() + " - " + getMaxX() + " " + getMinY() + "\n";
    }

    //    public VPFFeatureClass getFeatureClass(String typename) {
    //        VPFFeatureClass tmp = null;
    //
    //        for (int i = 0; i < libraries.length; i++) {
    //            tmp = libraries[i].getFeatureClass(typename);
    //
    //            if (tmp != null) {
    //                return tmp;
    //            }
    //        }
    //
    //        return null;
    //    }
}
