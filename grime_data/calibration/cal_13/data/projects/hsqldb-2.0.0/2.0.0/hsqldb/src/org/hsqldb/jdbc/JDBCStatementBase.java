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

package org.hsqldb.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;

import org.hsqldb.error.ErrorCode;
import org.hsqldb.result.Result;
import org.hsqldb.result.ResultConstants;

/**
 * Base class for HSQLDB's implementations of java.sql.Statement and
 * java.sql.PreparedStatement. Contains common members and methods.
 *
 * @author fredt@usrs
 * @version 2.0
 * @since 1.9.0
 */

/**
 * JDBC specification.
 *
 * Closing the Statement closes the ResultSet instance returned. But:
 *
 * Statement can be executed multiple times and return several results. With
 * normal Statement objects, each execution can be for a completely different
 * query. PreparedStatement instances are specifically for multiple use over
 * multiple transactions.
 *
 * ResultSets may be held over commits and span several transactions.
 *
 * There is no real relation between the current state fo an Statement instance
 * and the various ResultSets that it may have returned for different queries.
 */

/**
 * @todo 1.9.0 - review the following issues:
 *
 * Does not always close ResultSet object directly when closed. Although RS
 * objects will eventually be closed when accessed, the change is not reflected
 * to the server, impacting ResultSets that are held.
 */
class JDBCStatementBase {

    /**
     * Whether this Statement has been explicitly closed.  A JDBCConnection
     * object now explicitly closes all of its open JDBC Statement objects
     * when it is closed.
     */
    volatile boolean isClosed;

    /** Is escape processing enabled? */
    protected boolean isEscapeProcessing = true;

    /** The connection used to execute this statement. */
    protected JDBCConnection connection;

    /** The maximum number of rows to generate when executing this statement. */
    protected int maxRows;

    /** The number of rows returned in a chunk. */
    protected int fetchSize = 0;

    /** Direction of results fetched. */
    protected int fetchDirection = JDBCResultSet.FETCH_FORWARD;

    /** The result of executing this statement. */
    protected Result resultIn;

    /** Any error returned from a batch execute. */
    protected Result errorResult;

    /** The currently existing generated key Result */
    protected Result generatedResult;

    /** The combined result set properties obtained by executing this statement. */
    protected int rsProperties;

    /** Used by this statement to communicate non-batched requests. */
    protected Result resultOut;

    /** Used by this statement to communicate batched execution requests */
    protected Result batchResultOut;

    /** The currently existing ResultSet object */
    protected JDBCResultSet currentResultSet;

    /** The currently existing ResultSet object for generated keys */
    protected JDBCResultSet generatedResultSet;

    /** The first warning in the chain. Null if there are no warnings. */
    protected SQLWarning rootWarning;

    /** Counter for ResultSet in getMoreResults(). */
    protected int resultSetCounter;

    /** Query timeout in seconds */
    protected int queryTimeout;

    /** connection generation */
    int connectionIncarnation;

    /** Implementation in subclasses */
    public synchronized void close() throws SQLException {}

    /**
     * An internal check for closed statements.
     *
     * @throws SQLException when the connection is closed
     */
    void checkClosed() throws SQLException {

        if (isClosed) {
            throw Util.sqlException(ErrorCode.X_07501);
        }

        if (connection.isClosed) {
            close();

            throw Util.sqlException(ErrorCode.X_08503);
        }

        if (connectionIncarnation != connection.incarnation ) {
            throw Util.sqlException(ErrorCode.X_08503);
        }
    }

    /**
     * processes chained warnings and any generated columns result set
     */
    void performPostExecute() throws SQLException {

        resultOut.clearLobResults();

        generatedResult = null;

        if (resultIn == null) {
            return;
        }

        rootWarning = null;

        Result current = resultIn;

        while (current.getChainedResult() != null) {
            current = current.getUnlinkChainedResult();

            if (current.getType() == ResultConstants.WARNING) {
                SQLWarning w = Util.sqlWarning(current);

                if (rootWarning == null) {
                    rootWarning = w;
                } else {
                    rootWarning.setNextWarning(w);
                }
            } else if (current.getType() == ResultConstants.ERROR) {
                errorResult = current;
            } else if (current.getType() == ResultConstants.DATA) {
                generatedResult = current;
            }
        }

        if (rootWarning != null) {
            connection.setWarnings(rootWarning);
        }

        if (resultIn.isData()) {
            currentResultSet = new JDBCResultSet(connection,
                                                 this, resultIn,
                                                 resultIn.metaData);
        }
    }

    int getUpdateCount() throws SQLException {

        checkClosed();

        return (resultIn == null || resultIn.isData()) ? -1
                                                       : resultIn
                                                       .getUpdateCount();
    }

    ResultSet getResultSet() throws SQLException {

        checkClosed();

        ResultSet result = currentResultSet;

        currentResultSet = null;

        return result;
    }

    boolean getMoreResults() throws SQLException {
        return getMoreResults(CLOSE_CURRENT_RESULT);
    }

    /**
     * Not yet correct for multiple ResultSets. Should keep track of all
     * previous ResultSet objects to be able to close them
     */
    boolean getMoreResults(int current) throws SQLException {

        checkClosed();

        if (resultIn == null) {
            return false;
        }

        if (!resultIn.isData()) {
            resultIn = null;

            return false;
        }

        if (currentResultSet != null && current != KEEP_CURRENT_RESULT) {
            currentResultSet.close();
        }

        resultIn = null;

        return false;
    }

    ResultSet getGeneratedResultSet() throws SQLException {

        if (generatedResultSet != null) {
            generatedResultSet.close();
        }

        if (generatedResult == null) {
            generatedResult = Result.emptyGeneratedResult;
        }

        generatedResultSet = new JDBCResultSet(connection, null,
                                               generatedResult,
                                               generatedResult.metaData);

        return generatedResultSet;
    }

    /**
     * See comment for getMoreResults.
     */
    void closeResultData() throws SQLException {

        if (currentResultSet != null) {
            currentResultSet.close();
        }

        if (generatedResultSet != null) {
            generatedResultSet.close();
        }

        generatedResultSet = null;
        generatedResult    = null;
        resultIn           = null;
    }

    /**
     * JDBC 3 constants
     */
    static final int CLOSE_CURRENT_RESULT  = 1;
    static final int KEEP_CURRENT_RESULT   = 2;
    static final int CLOSE_ALL_RESULTS     = 3;
    static final int SUCCESS_NO_INFO       = -2;
    static final int EXECUTE_FAILED        = -3;
    static final int RETURN_GENERATED_KEYS = 1;
    static final int NO_GENERATED_KEYS     = 2;
}
