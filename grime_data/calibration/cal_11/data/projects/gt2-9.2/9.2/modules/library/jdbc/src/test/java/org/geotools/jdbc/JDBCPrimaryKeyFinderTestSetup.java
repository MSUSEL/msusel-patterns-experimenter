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

import java.sql.SQLException;

/**
 * 
 *
 * @source $URL$
 */
public abstract class JDBCPrimaryKeyFinderTestSetup extends JDBCDelegatingTestSetup {

    protected JDBCPrimaryKeyFinderTestSetup(JDBCTestSetup delegate) {
        super( delegate );
    }
    
    protected final void setUpData() throws Exception {
        //kill all the data
        try {
            dropMetadataTable();
        } catch (SQLException e) {
        }
        try {
            dropSequencedPrimaryKeyTable();
        } catch (SQLException e) {
        }
        try {
            dropAssignedSinglePkView();
        } catch (SQLException e) {
        }
        try {
            dropAssignedMultiPkView();
        } catch (SQLException e) {
        }
        try {
            dropPlainTable();
        } catch (SQLException e) {
        }
        
        //create all the data
        createMetadataTable();
        createSequencedPrimaryKeyTable();
        createPlainTable();
        createAssignedSinglePkView();
        createAssignedMultiPkView();
    }

    /**
     * Drops the metadata table
     */
    protected abstract void dropMetadataTable() throws Exception;

    /**
     * Drops the "assignedsinglepk" view.
     */
    protected abstract void dropAssignedSinglePkView() throws Exception;
    
    /**
     * Drops the "assignedmultipk" view.
     */
    protected abstract void dropAssignedMultiPkView() throws Exception;
    
    /**
     * Drops the "plaintable" table.
     */
    protected abstract void dropPlainTable() throws Exception;
    
    /**
     * Drops the "seqtable" table.
     */
    protected abstract void dropSequencedPrimaryKeyTable() throws Exception;
    

    /**
     * Creates the default geotools metadata table, empty.
     * <p>
     * The table is named "gt_pk_metdata". See {@link MetadataTablePrimaryKeyFinder} javadoc for the 
     * expected table structure
     * </p>
     * 
     */
    protected abstract void createMetadataTable() throws Exception;
    
    /**
     * Creates a table named 'plaintable' with no primary key and the following structure
     * <p>
     * plaintable( key1:Integer, key2: Integer, name:String; geom:Geometry; ) 
     * </p>
     * <p>
     * The table should be populated with the following data:
     *  1, 2, "one" | NULL
     *  2, 3, "two" | NULL
     *  3, 4, "three" | NULL
     * </p>
     */
    protected abstract void createPlainTable() throws Exception;
    
    /**
     * Creates a view named 'assignedsinglepk' that selects all of the contents of 'plaintable' and registers
     * the key1 column as an assigned primary key in the pk metadata table
     */
    protected abstract void createAssignedSinglePkView() throws Exception;
    
    /**
     * Creates a view named 'assignedmultipk' that selects all of the contents of 'plaintable' and registers
     * the key1, key2 columns as an assigned primary key in the pk metadata table
     */
    protected abstract void createAssignedMultiPkView() throws Exception;
    
    /**
     * Creates a table with a primary key column, a sequence name 'pksequence", and
     * links the two using the primary key metadata table
     * <p>
     * seqtable( name:String; geom:Geometry; ) 
     * </p>
     * <p>
     * The table should be populated with the following data:
     *  "one" | NULL ; pkey = 1
     *  "two" | NULL ; pkey = 2
     *  "three" | NULL ; pkey = 3
     * </p>
     */
    protected abstract void createSequencedPrimaryKeyTable() throws Exception;
   
}
