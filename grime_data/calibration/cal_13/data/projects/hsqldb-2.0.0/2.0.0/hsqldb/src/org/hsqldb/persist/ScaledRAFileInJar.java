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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.hsqldb.Database;
import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.lib.HsqlByteArrayInputStream;

/**
 * This class is a random access wrapper around a DataInputStream object and
 * enables access to cached tables when a database is included in a jar.
 *
 * A proof-of-concept prototype was first contributed by winfriedthom@users.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version  1.9.0
 * @since  1.8.0
 */
final class ScaledRAFileInJar implements ScaledRAInterface {

    DataInputStream          file;
    final String             fileName;
    long                     fileLength;
    boolean                  bufferDirty = true;
    byte[]                   buffer      = new byte[4096];
    HsqlByteArrayInputStream ba = new HsqlByteArrayInputStream(buffer);
    long                     bufferOffset;

    //
    long seekPosition;
    long realPosition;

    ScaledRAFileInJar(String name) throws FileNotFoundException, IOException {

        fileName   = name;
        fileLength = getLength();

        resetStream();
    }

    public long length() throws IOException {
        return fileLength;
    }

    /**
     * Some JVM's do not allow seek beyond end of file, so zeros are written
     * first in that case. Reported by bohgammer@users in Open Disucssion
     * Forum.
     */
    public void seek(long position) throws IOException {
        seekPosition = position;
    }

    public long getFilePointer() throws IOException {
        return seekPosition;
    }

    private void readIntoBuffer() throws IOException {

        long filePos = seekPosition;

        bufferDirty = false;

        long subOffset  = filePos % buffer.length;
        long readLength = fileLength - (filePos - subOffset);

        if (readLength <= 0) {
            throw new IOException("read beyond end of file");
        }

        if (readLength > buffer.length) {
            readLength = buffer.length;
        }

        fileSeek(filePos - subOffset);
        file.readFully(buffer, 0, (int) readLength);

        bufferOffset = filePos - subOffset;
        realPosition = bufferOffset + readLength;
    }

    public int read() throws IOException {

        if (seekPosition >= fileLength) {
            return -1;
        }

        if (bufferDirty || seekPosition < bufferOffset
                || seekPosition >= bufferOffset + buffer.length) {
            readIntoBuffer();
        }

        ba.reset();
        ba.skip(seekPosition - bufferOffset);

        int val = ba.read();

        seekPosition++;

        return val;
    }

    public long readLong() throws IOException {

        long hi = readInt();
        long lo = readInt();

        return (hi << 32) + (lo & 0xffffffffL);
    }

    public int readInt() throws IOException {

        if (bufferDirty || seekPosition < bufferOffset
                || seekPosition >= bufferOffset + buffer.length) {
            readIntoBuffer();
        }

        ba.reset();
        ba.skip(seekPosition - bufferOffset);

        int val = ba.readInt();

        seekPosition += 4;

        return val;
    }

    public void read(byte[] b, int offset, int length) throws IOException {

        if (bufferDirty || seekPosition < bufferOffset
                || seekPosition >= bufferOffset + buffer.length) {
            readIntoBuffer();
        }

        ba.reset();
        ba.skip(seekPosition - bufferOffset);

        int bytesRead = ba.read(b, offset, length);

        seekPosition += bytesRead;

        if (bytesRead < length) {
            if (seekPosition != realPosition) {
                fileSeek(seekPosition);
            }

            file.readFully(b, offset + bytesRead, length - bytesRead);

            seekPosition += (length - bytesRead);
            realPosition = seekPosition;
        }
    }

    public void write(byte[] b, int off, int len) throws IOException {}

    public void writeInt(int i) throws IOException {}

    public void writeLong(long i) throws IOException {}

    public void close() throws IOException {
        file.close();
    }

    public boolean isReadOnly() {
        return true;
    }

    public boolean wasNio() {
        return false;
    }

    private long getLength() throws IOException {

        int count = 0;

        resetStream();

        while (true) {
            if (file.read() < 1) {
                break;
            }

            count++;
        }

        return count;
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

        file = new DataInputStream(fis);
    }

    private void fileSeek(long position) throws IOException {

        long skipPosition = realPosition;

        if (position < skipPosition) {
            resetStream();

            skipPosition = 0;
        }

        while (position > skipPosition) {
            skipPosition += file.skip(position - skipPosition);
        }
    }

    public boolean canAccess(int length) {
        return false;
    }

    public boolean canSeek(long position) {
        return false;
    }

    public Database getDatabase() {
        return null;
    }

    public void synch() {}
}
