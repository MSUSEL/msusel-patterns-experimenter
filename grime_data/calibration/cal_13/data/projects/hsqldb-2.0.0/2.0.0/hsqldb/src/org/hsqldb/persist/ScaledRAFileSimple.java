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

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.hsqldb.Database;

/**
 * This class is a simple wapper for a random access file such as used
 * for backup and lobs.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version  1.9.0
 * @since  1.9.0
 */
final class ScaledRAFileSimple implements ScaledRAInterface {

    final RandomAccessFile file;
    final boolean          readOnly;

    ScaledRAFileSimple(String name,
                       String openMode)
                       throws FileNotFoundException, IOException {
        this.file = new RandomAccessFile(name, openMode);
        readOnly  = openMode.equals("r");
    }

    public long length() throws IOException {
        return file.length();
    }

    /**
     * Some JVM's do not allow seek beyond end of file, so zeros are written
     * first in that case. Reported by bohgammer@users in Open Disucssion
     * Forum.
     */
    public void seek(long position) throws IOException {
        file.seek(position);
    }

    public long getFilePointer() throws IOException {
        return file.getFilePointer();
    }

    public int read() throws IOException {
        return file.read();
    }

    public long readLong() throws IOException {
        return file.readLong();
    }

    public int readInt() throws IOException {
        return file.readInt();
    }

    public void read(byte[] b, int offset, int length) throws IOException {
        file.readFully(b, offset, length);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        file.write(b, off, len);
    }

    public void writeInt(int i) throws IOException {
        file.writeInt(i);
    }

    public void writeLong(long i) throws IOException {
        file.writeLong(i);
    }

    public void close() throws IOException {
        file.close();
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public boolean wasNio() {
        return false;
    }

    public boolean canAccess(int length) {
        return true;
    }

    public boolean canSeek(long position) {
        return true;
    }

    public Database getDatabase() {
        return null;
    }

    public void synch() {

        try {
            file.getFD().sync();
        } catch (IOException e) {}
    }
}
