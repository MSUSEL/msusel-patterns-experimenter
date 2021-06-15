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

import org.hsqldb.Database;
import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.lib.FileUtil;

/**
 * A file-based row store for temporary CACHED table persistence.<p>
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class DataFileCacheSession extends DataFileCache {

    // We are using persist.Logger-instance-specific FrameworkLogger
    // because it is Database-instance specific.
    // If add any static level logging, should instantiate a standard,
    // context-agnostic FrameworkLogger for that purpose.
    public int storeCount;

    public DataFileCacheSession(Database db, String baseFileName) {
        super(db, baseFileName);
    }

    /**
     * Initial external parameters are set here. The size if fixed.
     */
    protected void initParams(Database database, String baseFileName) {

        this.dataFileName = baseFileName + ".data.tmp";
        this.database     = database;
        fa                = FileUtil.getFileUtil();

        int cacheSizeScale = 10;

        cacheFileScale = 8;
        maxCacheRows   = 2048;

        int avgRowBytes = 1 << cacheSizeScale;

        maxCacheBytes   = maxCacheRows * avgRowBytes;
        maxDataFileSize = (long) Integer.MAX_VALUE * 4;
        dataFile        = null;
    }

    /**
     * Opens the *.data file for this cache.
     */
    public void open(boolean readonly) {

        try {
            dataFile = ScaledRAFile.newScaledRAFile(database, dataFileName,
                    false, ScaledRAFile.DATA_FILE_RAF);
            fileFreePosition = MIN_INITIAL_FREE_POS;

            initBuffers();

            freeBlocks = new DataFileBlockManager(0, cacheFileScale, 0);
        } catch (Throwable t) {
            database.logger.logWarningEvent("Failed to open RA file", t);
            close(false);

            throw Error.error(t, ErrorCode.FILE_IO_ERROR,
                              ErrorCode.M_DataFileCache_open, new Object[] {
                t.getMessage(), dataFileName
            });
        }
    }

    public synchronized void add(CachedObject object) {
        super.add(object);
    }

    /**
     *  Parameter write is always false. The backing file is simply closed and
     *  deleted.
     */
    public synchronized void close(boolean write) {

        try {
            if (dataFile != null) {
                dataFile.close();

                dataFile = null;

                fa.removeElement(dataFileName);
            }
        } catch (Throwable t) {
            database.logger.logWarningEvent("Failed to close RA file", t);

            throw Error.error(t, ErrorCode.FILE_IO_ERROR,
                              ErrorCode.M_DataFileCache_close, new Object[] {
                t.getMessage(), dataFileName
            });
        }
    }

    void postClose(boolean keep) {}

    public void clear() {

        cache.clear();

        fileFreePosition = MIN_INITIAL_FREE_POS;
    }

    public void deleteAll() {

        cache.clear();

        fileFreePosition = MIN_INITIAL_FREE_POS;

        initBuffers();
    }
}
