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

package org.hsqldb.navigator;

import java.io.IOException;

import org.hsqldb.HsqlException;
import org.hsqldb.Row;
import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.result.ResultMetaData;
import org.hsqldb.rowio.RowInputInterface;
import org.hsqldb.rowio.RowOutputInterface;

/*
 * All-in-memory implementation of RowSetNavigator for client side, or for
 * transferring a slice of the result to the client or server using a subset of
 * a server-side row set.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class RowSetNavigatorClient extends RowSetNavigator {

    public static final Object[][] emptyTable = new Object[0][];

    //
    int currentOffset;
    int baseBlockSize;

    //
    Object[][] table;

    //
    public RowSetNavigatorClient() {
        table = emptyTable;
    }

    public RowSetNavigatorClient(int blockSize) {
        table = new Object[blockSize][];
    }

    public RowSetNavigatorClient(RowSetNavigator source, int offset,
                                 int blockSize) {

        this.size          = source.size;
        this.baseBlockSize = blockSize;
        this.currentOffset = offset;
        table              = new Object[blockSize][];

        source.absolute(offset);

        for (int count = 0; count < blockSize; count++) {
            table[count] = source.getCurrent();

            source.next();
        }

        source.beforeFirst();
    }

    /**
     * For communication of small resuls such as BATCHEXECRESPONSE
     */
    public void setData(Object[][] table) {
        this.table = table;
        this.size  = table.length;
    }

    public void setData(int index, Object[] data) {
        table[index] = data;
    }

    public Object[] getData(int index) {
        return table[index];
    }

    /**
     * Returns the current row object. Type of object is implementation defined.
     */
    public Object[] getCurrent() {

        if (currentPos < 0 || currentPos >= size) {
            return null;
        }

        if (currentPos == currentOffset + table.length) {
            getBlock(currentOffset + table.length);
        }

        return table[currentPos - currentOffset];
    }

    public Row getCurrentRow() {
        throw Error.runtimeError(ErrorCode.U_S0500, "RowSetNavigatorClient");
    }

    public void remove() {
        throw Error.runtimeError(ErrorCode.U_S0500, "RowSetNavigatorClient");
    }

    public void add(Object[] data) {

        ensureCapacity();

        table[size] = data;

        size++;
    }

    public boolean addRow(Row row) {
        throw Error.runtimeError(ErrorCode.U_S0500, "RowSetNavigatorClient");
    }

    public void clear() {
        setData(emptyTable);
        reset();
    }

    public boolean absolute(int position) {

        if (position < 0) {
            position += size;
        }

        if (position < 0) {
            beforeFirst();

            return false;
        }

        if (position >= size) {
            afterLast();

            return false;
        }

        if (size == 0) {
            return false;
        }

        currentPos = position;

        return true;
    }

    public void close() {

        if (session != null) {
            if (currentOffset == 0 && table.length == size) {}
            else {
                session.closeNavigator(id);
            }
        }
    }

    public void readSimple(RowInputInterface in,
                           ResultMetaData meta) throws IOException {

        size = in.readInt();

        if (table.length < size) {
            table = new Object[size][];
        }

        for (int i = 0; i < size; i++) {
            table[i] = in.readData(meta.columnTypes);
        }
    }

    public void writeSimple(RowOutputInterface out,
                            ResultMetaData meta) throws IOException {

        out.writeInt(size);

        for (int i = 0; i < size; i++) {
            Object[] data = table[i];

            out.writeData(meta.getColumnCount(), meta.columnTypes, data, null,
                          null);
        }
    }

    public void read(RowInputInterface in,
                     ResultMetaData meta) throws IOException {

        id            = in.readLong();
        size          = in.readInt();
        currentOffset = in.readInt();
        baseBlockSize = in.readInt();

        if (table.length < baseBlockSize) {
            table = new Object[baseBlockSize][];
        }

        for (int i = 0; i < baseBlockSize; i++) {
            table[i] = in.readData(meta.columnTypes);
        }
    }

    public void write(RowOutputInterface out,
                      ResultMetaData meta) throws HsqlException, IOException {

        int limit = size - currentOffset;

        if (limit > table.length) {
            limit = table.length;
        }

        out.writeLong(id);
        out.writeInt(size);
        out.writeInt(currentOffset);
        out.writeInt(limit);

        for (int i = 0; i < limit; i++) {
            Object[] data = table[i];

            out.writeData(meta.getColumnCount(), meta.columnTypes, data, null,
                          null);
        }
    }

    /**
     * baseBlockSize remains unchanged.
     */
    void getBlock(int offset) {

        try {
            RowSetNavigatorClient source = session.getRows(id, offset,
                baseBlockSize);

            table         = source.table;
            currentOffset = source.currentOffset;
        } catch (HsqlException e) {}
    }

    private void ensureCapacity() {

        if (size == table.length) {
            int        newSize  = size == 0 ? 4
                                            : size * 2;
            Object[][] newTable = new Object[newSize][];

            System.arraycopy(table, 0, newTable, 0, size);

            table = newTable;
        }
    }
}
