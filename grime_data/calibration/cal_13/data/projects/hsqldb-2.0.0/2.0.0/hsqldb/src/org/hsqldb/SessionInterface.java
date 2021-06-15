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

package org.hsqldb;

import java.io.InputStream;
import java.util.Calendar;

import org.hsqldb.navigator.RowSetNavigatorClient;
import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.result.Result;
import org.hsqldb.result.ResultLob;
import org.hsqldb.types.BlobDataID;
import org.hsqldb.types.ClobDataID;
import org.hsqldb.types.TimestampData;

/**
 * Interface to Session and its remote proxy objects. Used by the
 * implementations of JDBC interfaces to communicate with the database at
 * the session level.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.7.2
 */
public interface SessionInterface {

    int INFO_ID      = 0;                // used
    int INFO_INTEGER = 1;                // used
    int INFO_BOOLEAN = 2;                // used
    int INFO_VARCHAR = 3;                // used
    int INFO_LIMIT   = 4;

    //
    int INFO_ISOLATION           = 0;    // used
    int INFO_AUTOCOMMIT          = 1;    // used
    int INFO_CONNECTION_READONLY = 2;    // used
    int INFO_CATALOG             = 3;    // used

    //
    int TX_READ_UNCOMMITTED = 1;
    int TX_READ_COMMITTED   = 2;
    int TX_REPEATABLE_READ  = 4;
    int TX_SERIALIZABLE     = 8;

    Result execute(Result r);

    RowSetNavigatorClient getRows(long navigatorId, int offset, int size);

    void closeNavigator(long id);

    void close();

    boolean isClosed();

    boolean isReadOnlyDefault();

    void setReadOnlyDefault(boolean readonly);

    boolean isAutoCommit();

    void setAutoCommit(boolean autoCommit);

    int getIsolation();

    void setIsolationDefault(int level);

    void startPhasedTransaction();

    void prepareCommit();

    void commit(boolean chain);

    void rollback(boolean chain);

    void rollbackToSavepoint(String name);

    void savepoint(String name);

    void releaseSavepoint(String name);

    void addWarning(HsqlException warning);

    Object getAttribute(int id);

    void setAttribute(int id, Object value);

    long getId();

    void resetSession();

    String getInternalConnectionURL();

    BlobDataID createBlob(long length);

    ClobDataID createClob(long length);

    void allocateResultLob(ResultLob result, InputStream dataInput);

    Scanner getScanner();

    Calendar getCalendar();

    TimestampData getCurrentDate();

    int getZoneSeconds();

    int getStreamBlockSize();

    HsqlProperties getClientProperties();
}
