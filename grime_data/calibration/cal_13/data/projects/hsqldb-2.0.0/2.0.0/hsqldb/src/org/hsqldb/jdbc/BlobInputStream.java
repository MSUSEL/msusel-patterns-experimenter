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

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.lib.java.JavaSystem;

/**
 * This class is used as an InputStream to retrieve data from a Blob.
 * mark() and reset() are not supported.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class BlobInputStream extends InputStream {

    final JDBCBlobClient blob;
    final long availableLength;
    long       bufferOffset;
    long       currentPosition;
    byte[]     buffer;
    boolean    isClosed;
    int        streamBlockSize;

    public BlobInputStream(JDBCBlobClient blob, long offset, long length,
                           int blockSize) throws SQLException {

        if (!JDBCBlobClient.isInLimits(blob.length(), offset, length)) {
            throw Util.outOfRangeArgument();
        }

        this.blob            = blob;
        this.availableLength = offset + length;
        this.currentPosition = offset;
        this.streamBlockSize = blockSize;
    }

    public int read() throws IOException {

        checkClosed();

        if (currentPosition >= availableLength) {
            return -1;
        }

        if (buffer == null
                || currentPosition >= bufferOffset + buffer.length) {
            try {
                readIntoBuffer();
            } catch (SQLException e) {
                throw JavaSystem.toIOException(e);
            }
        }

        int val = buffer[(int) (currentPosition - bufferOffset)] & 0xff;

        currentPosition++;

        return val;
    }

    public long skip(long n) throws IOException {

        checkClosed();

        if (n <= 0) {
            return 0;
        }

        if (currentPosition + n > availableLength) {
            n = availableLength - currentPosition;
        }

        currentPosition += n;

        return n;
    }

    public int available() throws IOException {

        checkClosed();

        return (int) (bufferOffset + buffer.length - currentPosition);
    }

    public void close() throws IOException {
        isClosed = true;
    }

    private void checkClosed() throws IOException {

        if (isClosed || blob.isClosed()) {
            throw new IOException(
                Error.getMessage(ErrorCode.X_0F503));
        }
    }

    private void readIntoBuffer() throws SQLException {

        long readLength = availableLength - currentPosition;

        if (readLength <= 0) {}

        if (readLength > streamBlockSize) {
            readLength = streamBlockSize;
        }

        buffer       = blob.getBytes(currentPosition + 1, (int) readLength);
        bufferOffset = currentPosition;
    }
}
