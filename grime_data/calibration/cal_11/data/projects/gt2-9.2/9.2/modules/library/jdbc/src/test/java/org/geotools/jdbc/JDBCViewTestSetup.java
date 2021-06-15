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
package org.geotools.jdbc;

import java.sql.SQLException;

/**
 * 
 *
 * @source $URL$
 */
public abstract class JDBCViewTestSetup extends JDBCDelegatingTestSetup {

    protected JDBCViewTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void setUpData() throws Exception {
        try {
            dropLakesView();
        } catch (SQLException e) {
        }
        
        try {
            dropLakesViewPk();
        } catch (SQLException e) {
        }
        
        try {
            dropLakesTable();
        } catch (SQLException e) {
        }
        
        //create all the data
        createLakesTable();
        createLakesView();
        createLakesViewPk();
    }
    
    /**
     * Creates a table with the following schema:
     * <p>
     * lakes(fid: Integer (pk), id:Integer; geom:Polygon; name:String )
     * </p>
     * <p>
     * The table should be populated with the following data:
     * <pre>
     * 0 | 0 | POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6));srid=4326 | "muddy"
     * </pre>
     * </p>
     */
    protected abstract void createLakesTable() throws Exception;
    
    /**
     * Creates a "lakesview" view that simply returns the lake table fully.
     * The table should be registered in the geometry metadata tables 
     */
    protected abstract void createLakesView() throws Exception;
    
    /**
     * If the database supports views with primary keys, creates a "lakesviewpk" view 
     * that simply returns the lake table fully and makes sure the original pk
     * is registered as a pk in the view as well.
     * The table should be registered in the geometry metadata tables.
     * If no primary key on views support is available, the method can be empty
     */
    protected abstract void createLakesViewPk() throws Exception;
    
    /**
     * Drops the "lakes" table.
     */
    protected abstract void dropLakesTable() throws Exception;
    
    /**
     * Drops the "lakesview" view.
     */
    protected abstract void dropLakesView() throws Exception;
    
    /**
     * Drops the "lakesviewpk" view
     */
    protected abstract void dropLakesViewPk() throws Exception;

}
