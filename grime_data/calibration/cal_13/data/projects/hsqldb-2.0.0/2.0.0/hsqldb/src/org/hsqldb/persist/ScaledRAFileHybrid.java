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

import org.hsqldb.Database;

/**
 * Mixe NIO / non-NIO version of ScaledRAFile.
 * This class is used only for storing a CACHED
 * TABLE .data file and cannot be used for TEXT TABLE source files.
 *
 * Due to various issues with java.nio classes, this class will use a mapped
 * channel of fixed size. After reaching this size, the file and channel are
 * closed and a new one opened, up to the maximum size.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version  1.8.0.5
 * @since 1.7.2
 */
public final class ScaledRAFileHybrid implements ScaledRAInterface {

    final Database    database;
    final String      fileName;
    final boolean     isReadOnly;
    final boolean     wasNio;
    long              maxLength;
    ScaledRAInterface store;

    public ScaledRAFileHybrid(Database database, String name,
                              boolean readOnly) throws IOException {

        this.database   = database;
        this.fileName   = name;
        this.isReadOnly = readOnly;

        newStore(0);

        this.wasNio = store.wasNio();
    }

    public long length() throws IOException {
        return store.length();
    }

    public void seek(long position) throws IOException {
        checkSeek(position);
        store.seek(position);
    }

    public long getFilePointer() throws IOException {
        return store.getFilePointer();
    }

    public int read() throws IOException {

        checkLength(1);

        return store.read();
    }

    public void read(byte[] b, int offset, int length) throws IOException {
        checkLength(length);
        store.read(b, offset, length);
    }

    public void write(byte[] b, int offset, int length) throws IOException {
        checkLength(length);
        store.write(b, offset, length);
    }

    public int readInt() throws IOException {

        checkLength(4);

        return store.readInt();
    }

    public void writeInt(int i) throws IOException {
        checkLength(4);
        store.writeInt(i);
    }

    public long readLong() throws IOException {

        checkLength(8);

        return store.readLong();
    }

    public void writeLong(long i) throws IOException {
        checkLength(8);
        store.writeLong(i);
    }

    public void close() throws IOException {
        store.close();
    }

    public boolean isReadOnly() {
        return store.isReadOnly();
    }

    public boolean wasNio() {
        return wasNio;
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
        store.synch();
    }

    private void checkLength(int length) throws IOException {

        if (store.canAccess(length)) {
            return;
        }

        newStore(store.getFilePointer() + length);
    }

    private void checkSeek(long position) throws IOException {

        if (store.canSeek(position)) {
            return;
        }

        newStore(position);
    }

    void newStore(long requiredPosition) throws IOException {

        long currentPosition = 0;

        if (store != null) {
            currentPosition = store.getFilePointer();

            store.close();
        }

        if (requiredPosition <= ScaledRAFile.MAX_NIO_LENGTH) {
            try {
                store = new ScaledRAFileNIO(database, fileName, isReadOnly,
                                            (int) requiredPosition);

                store.seek(currentPosition);

                return;
            } catch (Throwable e) {}
        }

        store = new ScaledRAFile(database, fileName, isReadOnly);

        store.seek(currentPosition);
    }
}
