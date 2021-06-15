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
public abstract class JDBCLobTestSetup extends JDBCDelegatingTestSetup {

    protected JDBCLobTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }
    
    protected final void setUpData() throws Exception {
        //kill all the data
        try {
            dropLobTable();
        } catch (SQLException e) {
        }

        //create all the data
        createLobTable();
    }

    /**
     * Creates a table with the following schema:
     * <p>
     * testlob( id:Integer; blob_field: blob; clob_field: clob)
     * </p>
     * <p>
     * The table should be populated with the following data
     *  0 | [0,1,2,3,4,5] | "small clob"
     * </p>
     * Where [0,1,2,3,4,5] is a byte[]
     */
    protected abstract void createLobTable() throws Exception;

    /**
     * Drops the "testlob" table
     */
    protected abstract void dropLobTable() throws Exception;

}
