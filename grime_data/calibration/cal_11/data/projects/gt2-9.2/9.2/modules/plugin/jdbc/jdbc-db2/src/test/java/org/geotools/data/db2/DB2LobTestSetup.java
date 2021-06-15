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
package org.geotools.data.db2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.geotools.jdbc.JDBCLobTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class DB2LobTestSetup extends JDBCLobTestSetup {

    protected DB2LobTestSetup() {
        super(new DB2TestSetup());
    }

    @Override
    protected void createLobTable() throws Exception {
        
        Connection con = getDataSource().getConnection();
        con.prepareStatement("create table "+DB2TestUtil.SCHEMA_QUOTED+
                        ".\"testlob\" (\"fid\" int not null , \"blob_field\" BLOB(32) , \"clob_field\" CLOB(32), \"raw_field\" BLOB(32), PRIMARY KEY(\"fid\"))").execute();
        
        PreparedStatement ps =con.prepareStatement( "INSERT INTO "+DB2TestUtil.SCHEMA_QUOTED+".\"testlob\" (\"fid\",\"blob_field\",\"clob_field\",\"raw_field\")  VALUES (?,?,?,?)");
        ps.setInt(1,0);
        ps.setBytes(2, new byte[] {1,2,3,4,5});
        ps.setString(3, "small clob");
        ps.setBytes(4, new byte[] {6,7,8,9,10});
        ps.execute();
        ps.close();
        con.close();               
    }

    @Override
    protected void dropLobTable() throws Exception {
        Connection con = getDataSource().getConnection();
        try {
                DB2TestUtil.dropTable(DB2TestUtil.SCHEMA, "testlob", con);
        } catch (SQLException e) {              
        }
        
        con.close();
    }

}
