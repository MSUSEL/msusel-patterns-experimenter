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

import java.io.IOException;
import java.io.RandomAccessFile;

import org.hsqldb.Database;
import org.hsqldb.lib.HsqlByteArrayOutputStream;
import org.hsqldb.lib.Storage;
import org.hsqldb.lib.java.JavaSystem;
import org.hsqldb.store.BitMap;

/*
 * Wrapper for random access file for incremental backup of the .data file.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class RAShadowFile {

    final Database database;
    final String   pathName;
    final Storage  source;
    Storage        dest;
    final int      pageSize;
    final long     maxSize;
    final BitMap   bitMap;
    boolean        zeroPageSet;
    HsqlByteArrayOutputStream byteArrayOutputStream =
        new HsqlByteArrayOutputStream(new byte[]{});

    RAShadowFile(Database database, Storage source, String pathName,
                 long maxSize, int pageSize) {

        this.database = database;
        this.pathName = pathName;
        this.source   = source;
        this.pageSize = pageSize;
        this.maxSize  = maxSize;

        int bitSize = (int) (maxSize / pageSize);

        if (maxSize % pageSize != 0) {
            bitSize++;
        }

        bitMap = new BitMap(bitSize);
    }

    void copy(long fileOffset, int size) throws IOException {

        // always copy the first page
        if (!zeroPageSet) {
            copy(0);
            bitMap.set(0);

            zeroPageSet = true;
        }

        if (fileOffset >= maxSize) {
            return;
        }

        long endOffset       = fileOffset + size;
        int  startPageOffset = (int) (fileOffset / pageSize);
        int  endPageOffset   = (int) (endOffset / pageSize);

        if (endOffset % pageSize == 0) {
            endPageOffset--;
        }

        for (; startPageOffset <= endPageOffset; startPageOffset++) {
            copy(startPageOffset);
        }
    }

    private void copy(int pageOffset) throws IOException {

        if (bitMap.set(pageOffset) == 1) {
            return;
        }

        long position = (long) pageOffset * pageSize;
        int  readSize = pageSize;

        if (maxSize - position < pageSize) {
            readSize = (int) (maxSize - position);
        }

        try {
            if (dest == null) {
                open();
            }

            long   writePos = dest.length();
            byte[] buffer   = new byte[pageSize + 12];

            byteArrayOutputStream.setBuffer(buffer);
            byteArrayOutputStream.writeInt(pageSize);
            byteArrayOutputStream.writeLong(position);
            source.seek(position);
            source.read(buffer, 12, readSize);
            dest.seek(writePos);
            dest.write(buffer, 0, buffer.length);
        } catch (Throwable t) {
            bitMap.unset(pageOffset);
            close();
            database.logger.logWarningEvent("pos" + position + " " + readSize,
                                            t);

            throw JavaSystem.toIOException(t);
        } finally {}
    }

    private void open() throws IOException {

        if (database.logger.isStoredFileAccess()) {
            dest = ScaledRAFile.newScaledRAFile(database, pathName, false,
                                                ScaledRAFile.DATA_FILE_STORED);
        } else {
            dest = new ScaledRAFileSimple(pathName, "rwd");
        }
    }

    /**
     * Called externally after a series of copy() calls.
     * Called internally after a restore or when error in writing
     */
    void close() throws IOException {

        if (dest != null) {
            dest.synch();
            dest.close();

            dest = null;
        }
    }

    private static Storage getStorage(Database database, String pathName,
                                      String openMode) throws IOException {

        if (database.logger.isStoredFileAccess()) {
            return ScaledRAFile.newScaledRAFile(database, pathName,
                                                openMode.equals("r"),
                                                ScaledRAFile.DATA_FILE_STORED);
        } else {
            return new ScaledRAFileSimple(pathName, openMode);
        }
    }

    /** todo - take account of incomplete addition of block due to lack of disk */

    // buggy database files had size == position == 0 at the end
    public static void restoreFile(Database database, String sourceName,
                                   String destName) throws IOException {

        Storage source = getStorage(database, sourceName, "r");
        Storage dest   = getStorage(database, destName, "rw");

        while (source.getFilePointer() != source.length()) {
            int    size     = source.readInt();
            long   position = source.readLong();
            byte[] buffer   = new byte[size];

            source.read(buffer, 0, buffer.length);
            dest.seek(position);
            dest.write(buffer, 0, buffer.length);
        }

        source.close();
        dest.close();
    }
}
