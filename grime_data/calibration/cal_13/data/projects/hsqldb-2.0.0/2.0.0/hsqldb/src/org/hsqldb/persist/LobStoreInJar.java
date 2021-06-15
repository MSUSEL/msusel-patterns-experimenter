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

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.hsqldb.Database;
import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;

/**
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class LobStoreInJar implements LobStore {

    final int       lobBlockSize;
    Database        database;
    DataInputStream file;
    final String    fileName;

    //
    long realPosition;

    public LobStoreInJar(Database database, int lobBlockSize) {

        this.lobBlockSize = lobBlockSize;
        this.database     = database;

        try {
            fileName = database.getPath() + ".lobs";
        } catch (Throwable t) {
            throw Error.error(ErrorCode.DATA_FILE_ERROR, t);
        }
    }

    public byte[] getBlockBytes(int blockAddress, int blockCount) {

        if (file == null) {
            throw Error.error(ErrorCode.FILE_IO_ERROR);
        }

        try {
            long   address   = (long) blockAddress * lobBlockSize;
            int    count     = blockCount * lobBlockSize;
            byte[] dataBytes = new byte[count];

            fileSeek(address);
            file.readFully(dataBytes, 0, count);

            realPosition = address + count;

            return dataBytes;
        } catch (Throwable t) {
            throw Error.error(ErrorCode.DATA_FILE_ERROR, t);
        }
    }

    public void setBlockBytes(byte[] dataBytes, int blockAddress,
                              int blockCount) {}

    public int getBlockSize() {
        return lobBlockSize;
    }

    public void close() {

        try {
            if (file != null) {
                file.close();
            }
        } catch (Throwable t) {
            throw Error.error(ErrorCode.DATA_FILE_ERROR, t);
        }
    }

    private void resetStream() throws IOException {

        if (file != null) {
            file.close();
        }

        InputStream fis;

        try {
            fis = getClass().getResourceAsStream(fileName);

            if (fis == null) {
                fis = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(fileName);
            }
        } catch (Throwable t) {
            throw Error.error(ErrorCode.DATABASE_NOT_EXISTS, t);
        }

        file         = new DataInputStream(fis);
        realPosition = 0;
    }

    private void fileSeek(long position) throws IOException {

        if (file == null) {
            resetStream();
        }

        long skipPosition = realPosition;

        if (position < skipPosition) {
            resetStream();

            skipPosition = 0;
        }

        while (position > skipPosition) {
            skipPosition += file.skip(position - skipPosition);
        }

        realPosition = position;
    }
}
