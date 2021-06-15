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

package org.hsqldb.lib;

import java.io.IOException;

/**
 * An interface that supports the commonly used methods of java.io.RandomAccessFile.
 *
 * @author Ocke Janssen oj@openoffice.org
 * @version 1.9.0
 * @since 1.8.0
 */
public interface Storage {

    long length() throws IOException;

    void seek(long position) throws IOException;

    long getFilePointer() throws IOException;

    int read() throws IOException;

    void read(byte[] b, int offset, int length) throws IOException;

    void write(byte[] b, int offset, int length) throws IOException;

    int readInt() throws IOException;

    void writeInt(int i) throws IOException;

    long readLong() throws IOException;

    void writeLong(long i) throws IOException;

    void close() throws IOException;

    boolean isReadOnly();

    boolean wasNio();

    void synch();
}
