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

import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.lib.FileUtil;
import org.hsqldb.lib.StringConverter;
import org.hsqldb.persist.DataFileCache;
import org.hsqldb.persist.PersistentStore;
import org.hsqldb.persist.TextCache;
import org.hsqldb.navigator.RowIterator;

// tony_lai@users 20020820 - patch 595099 - user define PK name

/**
 * Subclass of Table to handle TEXT data source. <p>
 *
 * Extends Table to provide the notion of an SQL base table object whose
 * data is read from and written to a text format data file.
 *
 * @author Bob Preston (sqlbob@users dot sourceforge.net)
 * @version 1.9.0
 */
public class TextTable extends org.hsqldb.Table {

    String  dataSource  = "";
    boolean isReversed  = false;
    boolean isConnected = false;

//    TextCache cache;

    /**
     * Constructs a new TextTable from the given arguments.
     *
     * @param db the owning database
     * @param name the table's HsqlName
     * @param type code (normal or temp text table)
     */
    TextTable(Database db, HsqlNameManager.HsqlName name, int type) {
        super(db, name, type);
    }

    public boolean isConnected() {
        return isConnected;
    }

    /**
     * connects to the data source
     */
    public void connect(Session session) {
        connect(session, isReadOnly);
    }

    /**
     * connects to the data source
     */
    private void connect(Session session, boolean withReadOnlyData) {

        // Open new cache:
        if ((dataSource.length() == 0) || isConnected) {

            // nothing to do
            return;
        }

        PersistentStore store =
            database.persistentStoreCollection.getStore(this);

        this.store = store;

        DataFileCache cache = null;

        try {
            cache = (TextCache) database.logger.openTextFilePersistence(this,
                    dataSource, withReadOnlyData, isReversed);

            store.setCache(cache);

            // read and insert all the rows from the source file
            Row row     = null;
            int nextpos = 0;

            if (((TextCache) cache).ignoreFirst) {
                nextpos += ((TextCache) cache).readHeaderLine();
            }

            while (true) {
                row = (Row) store.get(nextpos, false);

                if (row == null) {
                    break;
                }

                Object[] data = row.getData();

                nextpos = row.getPos() + row.getStorageSize();

                systemUpdateIdentityValue(data);
                enforceRowConstraints(session, data);

                for (int i = 0; i < indexList.length; i++) {
                    indexList[i].insert(null, store, row);
                }
            }
        } catch (Throwable t) {
            int linenumber = cache == null ? 0
                                           : ((TextCache) cache)
                                               .getLineNumber();

            clearAllData(session);

            if (cache != null) {
                database.logger.closeTextCache(this);
                store.release();
            }

            // everything is in order here.
            // At this point table should either have a valid (old) data
            // source and cache or have an empty source and null cache.
            throw Error.error(t, ErrorCode.TEXT_FILE, 0, new Object[] {
                t.getMessage(), new Integer(linenumber)
            });
        }

        isConnected = true;
        isReadOnly  = withReadOnlyData;
    }

    /**
     * disconnects from the data source
     */
    public void disconnect() {

        this.store = null;

        PersistentStore store =
            database.persistentStoreCollection.getStore(this);

        store.release();

        isConnected = false;
    }

    /**
     * This method does some of the work involved with managing the creation
     * and openning of the cache, the rest is done in Log.java and
     * TextCache.java.
     *
     * Better clarification of the role of the methods is needed.
     */
    private void openCache(Session session, String dataSourceNew,
                           boolean isReversedNew, boolean isReadOnlyNew) {

        String  dataSourceOld = dataSource;
        boolean isReversedOld = isReversed;
        boolean isReadOnlyOld = isReadOnly;

        if (dataSourceNew == null) {
            dataSourceNew = "";
        }

        disconnect();

        dataSource = dataSourceNew;
        isReversed = (isReversedNew && dataSource.length() > 0);

        try {
            connect(session, isReadOnlyNew);
        } catch (HsqlException e) {
            dataSource = dataSourceOld;
            isReversed = isReversedOld;

            connect(session, isReadOnlyOld);

            throw e;
        }
    }

    /**
     * High level command to assign a data source to the table definition.
     * Reassigns only if the data source or direction has changed.
     */
    protected void setDataSource(Session session, String dataSourceNew,
                                 boolean isReversedNew, boolean createFile) {

        if (getTableType() == Table.TEMP_TEXT_TABLE) {
            ;
        } else {
            session.getGrantee().checkSchemaUpdateOrGrantRights(
                getSchemaName().name);
        }

        dataSourceNew = dataSourceNew.trim();

        if (createFile && FileUtil.getFileUtil().exists(dataSourceNew)) {
            throw Error.error(ErrorCode.TEXT_SOURCE_EXISTS, dataSourceNew);
        }

        //-- Open if descending, direction changed, file changed, or not connected currently
        if (isReversedNew || (isReversedNew != isReversed)
                || !dataSource.equals(dataSourceNew) || !isConnected) {
            openCache(session, dataSourceNew, isReversedNew, isReadOnly);
        }

        if (isReversed) {
            isReadOnly = true;
        }
    }

    public String getDataSource() {
        return dataSource;
    }

    public boolean isDescDataSource() {
        return isReversed;
    }

    public void setHeader(String header) {

        PersistentStore store =
            database.persistentStoreCollection.getStore(this);
        TextCache cache = (TextCache) store.getCache();

        if (cache != null && cache.ignoreFirst) {
            cache.setHeader(header);

            return;
        }

        throw Error.error(ErrorCode.TEXT_TABLE_HEADER);
    }

    public String getHeader() {

        PersistentStore store =
            database.persistentStoreCollection.getStore(this);
        TextCache cache  = (TextCache) store.getCache();
        String    header = cache == null ? null
                                         : cache.getHeader();

        return header == null ? null
                              : StringConverter.toQuotedString(header, '\'',
                              true);
    }

    /**
     * Used by INSERT, DELETE, UPDATE operations. This class will return
     * a more appropriate message when there is no data source.
     */
    void checkDataReadOnly() {

        if (dataSource.length() == 0) {
            throw Error.error(ErrorCode.TEXT_TABLE_UNKNOWN_DATA_SOURCE);
        }

        if (isReadOnly) {
            throw Error.error(ErrorCode.DATA_IS_READONLY);
        }
    }

    public boolean isDataReadOnly() {
        return !isConnected() || super.isDataReadOnly()
               || store.getCache().isDataReadOnly();
    }

    public void setDataReadOnly(boolean value) {

        if (!value) {
            if (isReversed) {
                throw Error.error(ErrorCode.DATA_IS_READONLY);
            }

            if (database.isFilesReadOnly()) {
                throw Error.error(ErrorCode.DATABASE_IS_READONLY);
            }

            if (isConnected()) {
                store.getCache().close(true);
                store.getCache().open(value);
            }
        }

        isReadOnly = value;
    }

    boolean isIndexCached() {
        return false;
    }

    void setIndexRoots(String s) {

        // do nothing
    }

    String getDataSourceDDL() {

        String dataSource = getDataSource();

        if (dataSource == null) {
            return null;
        }

        boolean      isDesc = isDescDataSource();
        StringBuffer sb     = new StringBuffer(128);

        sb.append(Tokens.T_SET).append(' ').append(Tokens.T_TABLE).append(' ');
        sb.append(getName().getSchemaQualifiedStatementName());
        sb.append(' ').append(Tokens.T_SOURCE).append(' ').append('\'');
        sb.append(dataSource);
        sb.append('\'');

        if (isDesc) {
            sb.append(' ').append(Tokens.T_DESC);
        }

        return sb.toString();
    }

    /**
     * Generates the SET TABLE <tablename> SOURCE HEADER <string> statement for a
     * text table;
     */
    String getDataSourceHeader() {

        String header = getHeader();

        if (header == null) {
            return null;
        }

        StringBuffer sb = new StringBuffer(128);

        sb.append(Tokens.T_SET).append(' ').append(Tokens.T_TABLE).append(' ');
        sb.append(getName().getSchemaQualifiedStatementName());
        sb.append(' ').append(Tokens.T_SOURCE).append(' ');
        sb.append(Tokens.T_HEADER).append(' ');
        sb.append(header);

        return sb.toString();
    }

    void moveData(Session session, Table from, int colindex, int adjust) {

        PersistentStore store = session.sessionData.getRowStore(this);

        store.setCache(from.store.getCache());

        RowIterator it = from.rowIterator(session);

        try {
            while (it.hasNext()) {
                Row row = it.getNextRow();

                store.indexRow(session, row);
            }
        } catch (Throwable t) {
            store.release();

            if (t instanceof HsqlException) {
                throw (HsqlException) t;
            }

            throw new HsqlException(t, "", 0);
        }
    }

    /**
     * Adds commitPersistence() call
     */
    public void insertData(Session session, PersistentStore store,
                           Object[] data) {

        Row row = (Row) store.getNewCachedObject(null, data);

        store.indexRow(null, row);
        store.commitPersistence(row);
    }
}
