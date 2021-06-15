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

import org.hsqldb.Collation;

/**
 * Test HSQLDBs collation capabilities
 * @author frank.schoenheit@sun.com
 */
public class TestCollation extends TestBase {

    java.sql.Statement      statement;
    java.sql.Connection     connection;
    org.hsqldb.lib.Iterator collIterator;
    org.hsqldb.lib.Iterator localeIterator;

    /** Creates a new instance of TestCollation */
    public TestCollation(String name) {

        super(name);

        super.isNetwork = false;
    }

    protected void setUp() {

        super.setUp();

        try {
            connection = super.newConnection();
            statement  = connection.createStatement();
        } catch (Exception e) {}

        collIterator   = Collation.getCollationsIterator();
        localeIterator = Collation.getLocalesIterator();
    }

    protected void tearDown() {

        try {
            statement = connection.createStatement();

            statement.execute("SHUTDOWN");
        } catch (Exception e) {}

        super.tearDown();
    }

    /**
     * checks whether expected locales are present and selectable
     */
    public void testVerifyAvailability() {

        // let's check whether unknown collation identifiers are rejected
        try {
            statement.execute(
                getSetCollationStmt(
                    "ThisIsDefinitlyNoValidCollationIdentifier"));
            fail("database did not reject invalid collation name");
        } catch (java.sql.SQLException e) {}

        // let's check whether the DB accepts all known collations
        int count = 0;

        while (collIterator.hasNext()) {
            String collationName = (String) collIterator.next();

            try {
                statement.execute(getSetCollationStmt(collationName));
            } catch (java.sql.SQLException e) {
                fail("could not set collation '" + collationName
                     + "'\n  exception message: " + e.getMessage());
            }

            ++count;
        }

        System.out.println("checked " + count
                           + " collations for availability.");

        // even if the above worked, we cannot be sure that all locales are really supported.
        // The fact that SET DATABASE COLLATION succeeeded only means that a Collator could
        // be instantiated with a Locale matching the given collation name. But what if
        // Locale.Instance(...) lied, and returned a fallback Locale instance?
        //
        // Hmm, looking at the documentation of Locale.getAvailableLocales, I'm not sure
        // whether it is really feasible. The doc states "returns a list of all installed Locales".
        // The "installed" puzzles me - maybe this is really different per installation, and not only
        // per JDK version?
        java.util.Locale[] availableLocales =
            java.util.Locale.getAvailableLocales();
        org.hsqldb.lib.Set existenceCheck = new org.hsqldb.lib.HashSet();

        for (int i = 0; i < availableLocales.length; ++i) {
            String availaleName = availableLocales[i].getLanguage();

            if (availableLocales[i].getCountry().length() > 0) {
                availaleName += "-" + availableLocales[i].getCountry();
            }

            existenceCheck.add(availaleName);
        }

        String notInstalled = "";
        int    expected     = 0,
               failed       = 0;

        while (localeIterator.hasNext()) {
            String localeName = (String) localeIterator.next();

            ++expected;

            if (!existenceCheck.contains(localeName)) {
                if (notInstalled.length() > 0) {
                    notInstalled += "; ";
                }

                notInstalled += localeName;

                ++failed;
            }
        }

        if (notInstalled.length() > 0) {
            fail("the following locales are not installed:\n  " + notInstalled
                 + "\n  (" + failed + " out of " + expected + ")");
        }
    }

    /**
     * checks whether sorting via a given collation works as expected
     */
    public void testVerifyCollation() {

        String failedCollations = "";
        String failMessage      = "";

        while (collIterator.hasNext()) {
            String collationName = (String) collIterator.next();
            String message       = checkSorting(collationName);

            if (message.length() > 0) {
                if (failedCollations.length() > 0) {
                    failedCollations += ", ";
                }

                failedCollations += collationName;
                failMessage      += message;
            }
        }

        if (failedCollations.length() > 0) {
            fail("test failed for following collations:\n" + failedCollations
                 + "\n" + failMessage);
        }
    }

    /**
     * returns an SQL statement to set the database collation
     */
    protected final String getSetCollationStmt(String collationName) {

        final String setCollationStmtPre  = "SET DATABASE COLLATION \"";
        final String setCollationStmtPost = "\"";

        return setCollationStmtPre + collationName + setCollationStmtPost;
    }

    /**
     * checks sorting a table with according to a given collation
     */
    protected String checkSorting(String collationName) {

        String stmt1 =
            "DROP TABLE WORDLIST IF EXISTS;";
        String stmt2 =
            "CREATE TEXT TABLE WORDLIST ( ID INTEGER, WORD VARCHAR(50) );";
        String stmt3 =
            "SET TABLE WORDLIST SOURCE \"" + collationName
            + ".csv;encoding=UTF-8\"";
        String selectStmt    = "SELECT ID, WORD FROM WORDLIST ORDER BY WORD";
        String returnMessage = "";

        try {

            // set database collation
            statement.execute(getSetCollationStmt(collationName));
            statement.execute(stmt1);
            statement.execute(stmt2);
            statement.execute(stmt3);

            java.sql.ResultSet results = statement.executeQuery(selectStmt);

            while (results.next()) {
                int expectedPosition = results.getInt(1);
                int foundPosition    = results.getRow();

                if (expectedPosition != foundPosition) {
                    String word = results.getString(2);

                    return "testing collation '" + collationName
                           + "' failed\n" + "  word              : " + word
                           + "\n" + "  expected position : "
                           + expectedPosition + "\n"
                           + "  found position    : " + foundPosition + "\n";
                }
            }
        } catch (java.sql.SQLException e) {
            return "testing collation '" + collationName
                   + "' failed\n  exception message: " + e.getMessage() + "\n";
        }

        return "";
    }

    public static void main(String[] argv) {
        runWithResult(TestCollation.class, "testVerifyAvailability");
        runWithResult(TestCollation.class, "testVerifyCollation");
    }
}
