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

import org.geotools.jdbc.JDBCDateTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class DB2DateTestSetup extends JDBCDateTestSetup {

    public DB2DateTestSetup() {
        super(new DB2TestSetup());
    }

    @Override
    protected void createDateTable() throws Exception {
        Connection con = getDataSource().getConnection();
        con.prepareStatement("CREATE TABLE "+DB2TestUtil.SCHEMA_QUOTED+
             ".\"dates\"(\"d\" DATE, \"dt\" TIMESTAMP, \"t\" TIME)").execute();
        
        String insertClause = "INSERT INTO "+DB2TestUtil.SCHEMA_QUOTED+".\"dates\"(\"d\",\"dt\",\"t\")";
        
        con.prepareStatement(insertClause+" VALUES (" +
              "DATE('2009-06-28'), " +
              "TIMESTAMP('2009-06-28', '15:12:41')," +
              "TIME('15:12:41')  )")
              .execute();
        
        con.prepareStatement(insertClause+" VALUES (" +
              "DATE('2009-01-15'), " +
              "TIMESTAMP('2009-01-15','13:10:12')," +
              "TIME('13:10:12')  )")
              .execute();

        con.prepareStatement(insertClause+" VALUES (" +
              "DATE('2009-09-29'), " +
              "TIMESTAMP('2009-09-29', '17:54:23')," +
              "TIME('17:54:23') )")
               .execute();
        
        
        con.close();
        
    }

    @Override
    protected void dropDateTable() throws Exception {
        Connection con = getDataSource().getConnection();               
        DB2TestUtil.dropTable(DB2TestUtil.SCHEMA, "dates", con);
        con.close();
    }

}
