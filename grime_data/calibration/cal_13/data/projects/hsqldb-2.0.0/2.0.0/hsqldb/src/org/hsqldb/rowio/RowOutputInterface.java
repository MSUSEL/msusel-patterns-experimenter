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

package org.hsqldb.rowio;

import org.hsqldb.Row;
import org.hsqldb.lib.HashMappedList;
import org.hsqldb.lib.HsqlByteArrayOutputStream;
import org.hsqldb.types.Type;

/**
 * Public interface for writing the data for a database row.
 *
 * @author Bob Preston (sqlbob@users dot sourceforge.net)
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.7.0
 */
public interface RowOutputInterface extends Cloneable {

    void writeEnd();

    void writeSize(int size);

    void writeType(int type);

    void writeString(String value);

    void writeByte(int i);

    void writeShort(int i);

    void writeInt(int i);

    void writeIntData(int i, int position);

    void writeLong(long i);

    void writeData(Object[] data, Type[] types);

    void writeData(int l, Type[] types, Object[] data, HashMappedList cols,
                   int[] primarykeys);

    // independent of the this object, calls only a static method
    int getSize(Row row);

    int getStorageSize(int size);

    // returns the underlying HsqlByteArrayOutputStream
    HsqlByteArrayOutputStream getOutputStream();

    // sets the byte[] buffer
    public void setBuffer(byte[] mainBuffer);

    // resets the byte[] buffer, ready for processing new row
    void reset();

    // returns the current size
    int size();

    public RowOutputInterface duplicate();
}
