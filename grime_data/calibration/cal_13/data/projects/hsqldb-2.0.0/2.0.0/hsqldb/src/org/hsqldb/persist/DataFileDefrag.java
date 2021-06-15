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

package org.hsqldb.persist;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.hsqldb.Database;
import org.hsqldb.Session;
import org.hsqldb.Table;
import org.hsqldb.TableBase;
import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.index.IndexAVL;
import org.hsqldb.lib.DoubleIntIndex;
import org.hsqldb.lib.HsqlArrayList;
import org.hsqldb.lib.StopWatch;
import org.hsqldb.lib.Storage;
import org.hsqldb.navigator.RowIterator;
import org.hsqldb.rowio.RowOutputInterface;
import org.hsqldb.store.BitMap;

// oj@openoffice.org - changed to file access api

/**
 *  Routine to defrag the *.data file.
 *
 *  This method iterates over the primary index of a table to find the
 *  disk position for each row and stores it, together with the new position
 *  in an array.
 *
 *  A second pass over the primary index writes each row to the new disk
 *  image after translating the old pointers to the new.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version    1.9.0
 * @since      1.7.2
 */
final class DataFileDefrag {

    BufferedOutputStream fileStreamOut;
    long                 fileOffset;
    StopWatch            stopw = new StopWatch();
    String               dataFileName;
    int[][]              rootsList;
    Database             database;
    DataFileCache        cache;
    int                  scale;
    DoubleIntIndex       transactionRowLookup;

    DataFileDefrag(Database db, DataFileCache cache, String dataFileName) {

        this.database     = db;
        this.cache        = cache;
        this.scale        = cache.cacheFileScale;
        this.dataFileName = dataFileName;
    }

    void process() {

        boolean complete = false;

        Error.printSystemOut("Defrag process begins");

        transactionRowLookup = database.txManager.getTransactionIDList();

        Error.printSystemOut("transaction count: "
                             + transactionRowLookup.size());

        HsqlArrayList allTables = database.schemaManager.getAllTables();

        rootsList = new int[allTables.size()][];

        Storage dest = null;

        try {
            OutputStream fos =
                database.logger.getFileAccess().openOutputStreamElement(
                    dataFileName + ".new");

            fileStreamOut = new BufferedOutputStream(fos, 1 << 12);

            for (int i = 0; i < cache.initialFreePos; i++) {
                fileStreamOut.write(0);
            }

            fileOffset = cache.initialFreePos;

            for (int i = 0, tSize = allTables.size(); i < tSize; i++) {
                Table t = (Table) allTables.get(i);

                if (t.getTableType() == TableBase.CACHED_TABLE) {
                    int[] rootsArray = writeTableToDataFile(t);

                    rootsList[i] = rootsArray;
                } else {
                    rootsList[i] = null;
                }

                Error.printSystemOut("table: " + t.getName().name
                                     + " complete");
            }

            fileStreamOut.flush();
            fileStreamOut.close();

            fileStreamOut = null;

            // write out the end of file position
            int type = database.logger.isStoredFileAccess()
                       ? ScaledRAFile.DATA_FILE_STORED
                       : ScaledRAFile.DATA_FILE_RAF;

            dest = ScaledRAFile.newScaledRAFile(database,
                                                dataFileName + ".new", false,
                                                type);

            dest.seek(DataFileCache.LONG_FREE_POS_POS);
            dest.writeLong(fileOffset);

            // set shadowed flag;
            int flags = 0;

            if (database.logger.propIncrementBackup) {
                flags = BitMap.set(flags, DataFileCache.FLAG_ISSHADOWED);
            }

            flags = BitMap.set(flags, DataFileCache.FLAG_190);
            flags = BitMap.set(flags, DataFileCache.FLAG_ISSAVED);

            dest.seek(DataFileCache.FLAGS_POS);
            dest.writeInt(flags);
            dest.close();

            dest = null;

            for (int i = 0, size = rootsList.length; i < size; i++) {
                int[] roots = rootsList[i];

                if (roots != null) {
                    Error.printSystemOut(
                        "roots: "
                        + org.hsqldb.lib.StringUtil.getList(roots, ",", ""));
                }
            }

            complete = true;
        } catch (IOException e) {
            throw Error.error(ErrorCode.FILE_IO_ERROR, e);
        } catch (OutOfMemoryError e) {
            throw Error.error(ErrorCode.OUT_OF_MEMORY, e);
        } catch (Throwable t) {
            throw Error.error(ErrorCode.GENERAL_ERROR, t);
        } finally {
            try {
                if (fileStreamOut != null) {
                    fileStreamOut.close();
                }

                if (dest != null) {
                    dest.close();
                }
            } catch (Throwable t) {
                database.logger.logSevereEvent("backupFile failed", t);
            }

            if (!complete) {
                database.logger.getFileAccess().removeElement(dataFileName
                        + ".new");
            }
        }

        Error.printSystemOut("Defrag transfer complete: "
                             + stopw.elapsedTime());
    }

    /**
     * called from outside after the complete end of defrag
     */
    void updateTableIndexRoots() {

        HsqlArrayList allTables = database.schemaManager.getAllTables();

        for (int i = 0, size = allTables.size(); i < size; i++) {
            Table t = (Table) allTables.get(i);

            if (t.getTableType() == TableBase.CACHED_TABLE) {
                int[] rootsArray = rootsList[i];

                t.setIndexRoots(rootsArray);
            }
        }
    }

    /**
     * called from outside after the complete end of defrag
     */
    void updateTransactionRowIDs() {
        database.txManager.convertTransactionIDs(transactionRowLookup);
    }

    int[] writeTableToDataFile(Table table) throws IOException {

        Session session = database.getSessionManager().getSysSession();
        PersistentStore    store  = session.sessionData.getRowStore(table);
        RowOutputInterface rowOut = cache.rowOut.duplicate();
        DoubleIntIndex pointerLookup = new DoubleIntIndex(
            ((IndexAVL) table.getPrimaryIndex()).sizeEstimate(store), false);
        int[] rootsArray = table.getIndexRootsArray();
        long  pos        = fileOffset;
        int   count      = 0;

        pointerLookup.setKeysSearchTarget();
        Error.printSystemOut("lookup begins: " + stopw.elapsedTime());

        // all rows
        RowIterator it = table.rowIterator(store);

        for (; it.hasNext(); count++) {
            CachedObject row = it.getNextRow();

            pointerLookup.addUnsorted(row.getPos(), (int) (pos / scale));

            if (count % 50000 == 0) {
                Error.printSystemOut("pointer pair for row " + count + " "
                                     + row.getPos() + " " + pos);
            }

            pos += row.getStorageSize();
        }

        Error.printSystemOut("table: " + table.getName().name + " list done: "
                             + stopw.elapsedTime());

        count = 0;
        it    = table.rowIterator(store);

        for (; it.hasNext(); count++) {
            CachedObject row = it.getNextRow();

            rowOut.reset();
            row.write(rowOut, pointerLookup);
            fileStreamOut.write(rowOut.getOutputStream().getBuffer(), 0,
                                rowOut.size());

            fileOffset += row.getStorageSize();

            if ((count) % 50000 == 0) {
                Error.printSystemOut(count + " rows " + stopw.elapsedTime());
            }
        }

        for (int i = 0; i < table.getIndexCount(); i++) {
            if (rootsArray[i] == -1) {
                continue;
            }

            int lookupIndex =
                pointerLookup.findFirstEqualKeyIndex(rootsArray[i]);

            if (lookupIndex == -1) {
                throw Error.error(ErrorCode.DATA_FILE_ERROR);
            }

            rootsArray[i] = pointerLookup.getValue(lookupIndex);
        }

        setTransactionRowLookups(pointerLookup);
        Error.printSystemOut("table: " + table.getName().name
                             + " : table converted");

        return rootsArray;
    }

    public int[][] getIndexRoots() {
        return rootsList;
    }

    void setTransactionRowLookups(DoubleIntIndex pointerLookup) {

        for (int i = 0, size = transactionRowLookup.size(); i < size; i++) {
            int key         = transactionRowLookup.getKey(i);
            int lookupIndex = pointerLookup.findFirstEqualKeyIndex(key);

            if (lookupIndex != -1) {
                transactionRowLookup.setValue(
                    i, pointerLookup.getValue(lookupIndex));
            }
        }
    }
}
