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

package org.hsqldb.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * TestHSQLDB.java
 *
 * Created on June 10, 2004, 10:28 PM
 */

/**
 *
 * @author  Diego Ballve
 */
public class TestHSQLDB {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        java.sql.DatabaseMetaData metaData = null;
        String databaseURL                 = "jdbc:hsqldb:mem:test";
        String                    driver   = "org.hsqldb.jdbc.JDBCDriver";
        String                    user     = "sa";
        String                    password = "";

        //Table creation sql:
        String ddlStr =
            "CREATE TABLE USER_(ID VARCHAR(64) NOT NULL PRIMARY KEY,HOME VARCHAR(128),OBJECTTYPE VARCHAR(64),STATUS VARCHAR(64) NOT NULL,PERSONNAME_FIRSTNAME VARCHAR(64),PERSONNAME_MIDDLENAME VARCHAR(64),PERSONNAME_LASTNAME VARCHAR(64),URL VARCHAR(256))";
        String sqlStr =
            "UPDATE User_ SET  id=\'urn:uuid:921284f0-bbed-4a4c-9342-ecaf0625f9d7\',  home=null, objectType=\'urn:uuid:6d07b299-10e7-408f-843d-bb2bc913bfbb\', status=\'urn:uuid:37d17f1b-3245-425b-988d-e0d98200a146\' , personName_firstName=\'Registry\', personName_middleName=null, personName_lastName=\'Operator\', url=\'http://sourceforge.net/projects/ebxmlrr\' WHERE id = \'urn:uuid:921284f0-bbed-4a4c-9342-ecaf0625f9d7\' ";
        Statement stmt = null;

        try {
            Class.forName(driver);

            Connection connection = DriverManager.getConnection(databaseURL,
                user, password);

            stmt = connection.createStatement();

            stmt.addBatch(ddlStr);
            stmt.addBatch(sqlStr);

            int[] updateCounts = stmt.executeBatch();
        } catch (ClassNotFoundException e) {
            System.err.println(e.getClass().getName() + ": "
                               + e.getMessage());
            e.printStackTrace(System.err);
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": "
                               + e.getMessage());
            e.printStackTrace(System.err);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {}
        }
    }
}
