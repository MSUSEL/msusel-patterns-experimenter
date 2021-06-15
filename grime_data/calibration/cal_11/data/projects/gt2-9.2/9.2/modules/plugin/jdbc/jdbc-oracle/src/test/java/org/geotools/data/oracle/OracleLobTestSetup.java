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
package org.geotools.data.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.geotools.jdbc.JDBCLobTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class OracleLobTestSetup extends JDBCLobTestSetup {

    protected OracleLobTestSetup() {
        super(new OracleTestSetup());
    }

    @Override
    protected void createLobTable() throws Exception {
        run("CREATE TABLE testlob (fid int, blob_field BLOB, clob_field CLOB, raw_field RAW(50), PRIMARY KEY (fid) )");
        run("CREATE SEQUENCE testlob_fid_seq START WITH 0 MINVALUE 0");

        // insert data. We need to use prepared statements in order to insert blobs
        Connection conn = getConnection();
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO testlob(fid, blob_field, clob_field, raw_field) VALUES(testlob_fid_seq.nextval, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setBytes(1, new byte[] {1,2,3,4,5});
            ps.setString(2, "small clob");
            ps.setBytes(3, new byte[] {6,7,8,9,10});
            ps.execute();
        } finally {
            if(ps != null) ps.close();
            conn.close();
        }
    }

    @Override
    protected void dropLobTable() throws Exception {
        runSafe("DROP SEQUENCE testlob_fid_seq");
        runSafe("DROP TABLE testlob");
    }

}
