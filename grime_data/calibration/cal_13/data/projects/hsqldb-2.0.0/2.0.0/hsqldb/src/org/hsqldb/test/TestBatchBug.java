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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class TestBatchBug {

    static final int    DATASET_COUNT              = 2;
    static final int    DECIMAL_FIELDS_PER_DATASET = 2;
    static final String TABLE_ATTR_CACHED          = "CACHED";
    static final String IN_PROCESS_FILE_URL =
        "jdbc:hsqldb:/hsql/testbatch/perftest";
    static final String HSQLDB_LOCALHOST_URL =
        "jdbc:hsqldb:hsql://localhost/yourtest";
    ;
    static final String TEST_TABLE_NAME       = "CSBug";
    static String       FIELD_LIST_WITHOUT_ID = "Kennung, Last_Update ";

// wird in static {} erweitert:
    static String FIELD_LIST_WITH_ID    = "ID, ";
    static String SQL_SELECT_ALL_FIELDS = "SELECT ";

// wird in static {} erweitert:
    static {
        for (int i = 1; i <= DECIMAL_FIELDS_PER_DATASET; i++) {
            FIELD_LIST_WITHOUT_ID += ", Field_" + i;
        }

        FIELD_LIST_WITH_ID += FIELD_LIST_WITHOUT_ID;
        SQL_SELECT_ALL_FIELDS += FIELD_LIST_WITH_ID + " FROM "
                                 + TEST_TABLE_NAME;
    }

    static int ldfNrFuerKennung;

    public static void main(String[] arg) {

        try {

// Load the HSQL Database Engine JDBC driver
            Class.forName("org.hsqldb.jdbc.JDBCDriver");

            String[] urls = {
                IN_PROCESS_FILE_URL
            };

            for (int i = 0; i < urls.length; i++) {
                String   url        = urls[i];
                String[] tableAttrs = { TABLE_ATTR_CACHED };

                for (int iAttr = 0; iAttr < tableAttrs.length; iAttr++) {
                    testURL(url, "CACHED");
                }
            }

            System.out.println("bye");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void testURL(String url, String tableAttr) throws SQLException {

        System.out.println(url);

        Connection con = DriverManager.getConnection(url, "sa", "");

        reCreateTable(con, "CACHED");
        populateTable(con);
        con.close();
    }

    static void reCreateTable(Connection con,
                              String tableAttr) throws SQLException {

        String cvsFileName = TEST_TABLE_NAME + ".csv";

// Create a statement object
        Statement stmt = con.createStatement();

// Try to drop the table
        try {

            stmt.executeUpdate("DROP TABLE " + TEST_TABLE_NAME + " IF EXISTS");
        } catch (SQLException e) {    // Ignore Exception, because the table may not yet exist
            System.out.println(e.getMessage());
        }

        StringBuffer sql = new StringBuffer();

        sql.append("CREATE ");
        sql.append(tableAttr);    // z.B. "CACHED"
        sql.append(" TABLE ");
        sql.append(TEST_TABLE_NAME);
        sql.append(" (");
        sql.append("Id integer IDENTITY");
        sql.append(", ");
        sql.append("Kennung varchar(20) NOT NULL");

// etwas andere Schreibweise von CURRENT TIMESTAMP
        sql.append(", last_update TIMESTAMP ");
        sql.append("DEFAULT CURRENT_TIMESTAMP NOT NULL");

        for (int i = 1; i <= DECIMAL_FIELDS_PER_DATASET; i++) {
            sql.append(", Field_" + i + " decimal");
        }

        sql.append(", UNIQUE(Kennung)");
        sql.append(")");
        System.out.println(sql.toString());
        stmt.executeUpdate(sql.toString());

// CLEAR TABLE
        sql = new StringBuffer();

        sql.append("DELETE FROM ");
        sql.append(TEST_TABLE_NAME);
        System.out.println(sql.toString());
        stmt.executeUpdate(sql.toString());
        stmt.close();    // is no longer used
    }

// tries to enter 2 records
    static void populateTable(Connection con) throws SQLException {

        long      startTime = System.currentTimeMillis();
        Timestamp now       = new Timestamp(startTime);

        con.setAutoCommit(false);

        String            sql  = createInsertSQL(true, false);
        PreparedStatement prep = con.prepareStatement(sql);

        prep.clearParameters();
        prep.setString(1, "xxx");
        prep.setTimestamp(2, now);    // last_update

        for (int ii = 0; ii < DECIMAL_FIELDS_PER_DATASET; ii++) {
            prep.setDouble(ii + 3, 0.123456789);    // Wert
        }

        prep.addBatch();
        prep.setString(1, "yyy");
        prep.setTimestamp(2, now);    // last_update

        for (int ii = 0; ii < DECIMAL_FIELDS_PER_DATASET; ii++) {
            prep.setDouble(ii + 3, 0.123456789);    // Wert
        }

        prep.addBatch();

        int[] updateCounts = prep.executeBatch();

        con.setAutoCommit(true);
        prep.close();
    }

    static String createInsertSQL(boolean prepStmt,
                                  boolean getIdAfterInsert) {

        StringBuffer sql = new StringBuffer();

        sql.append("INSERT INTO ");
        sql.append(TEST_TABLE_NAME);
        sql.append(" (");
        sql.append(FIELD_LIST_WITHOUT_ID);
        sql.append(") VALUES (");

        Timestamp now = new Timestamp(System.currentTimeMillis());
        Object    val = "?";

        if (prepStmt) {
            sql.append(val + ", " + val);
        } else {
            long millis = System.currentTimeMillis();

            sql.append("'Ken");
            sql.append((++ldfNrFuerKennung) + "'");

            val = new Double(0.123456789) + "";

            sql.append(", '" + now.toString() + "'");
        }

        for (int i = 1; i <= DECIMAL_FIELDS_PER_DATASET; i++) {
            sql.append(", " + val);
        }

        sql.append(")");

        String ret = sql.toString();

        System.out.println(ret);

        return ret;
    }
}
