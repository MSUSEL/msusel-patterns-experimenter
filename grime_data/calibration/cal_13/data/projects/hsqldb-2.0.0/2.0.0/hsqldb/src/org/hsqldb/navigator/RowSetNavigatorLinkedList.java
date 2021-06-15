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
import java.util.NoSuchElementException;

import org.hsqldb.Row;
import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.lib.HsqlLinkedList;
import org.hsqldb.lib.HsqlLinkedList.Node;
import org.hsqldb.result.ResultMetaData;
import org.hsqldb.rowio.RowInputInterface;
import org.hsqldb.rowio.RowOutputInterface;

/*
 * All-in-memory implementation of RowSetNavigator for simple client or server
 * side result sets. These are the result sets used for batch or other internal
 * operations.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class RowSetNavigatorLinkedList extends RowSetNavigator {

    HsqlLinkedList list;
    final Node     root;
    Node           previous;
    Node           current;

    public RowSetNavigatorLinkedList() {

        list    = new HsqlLinkedList();
        root    = list.getHeadNode();
        current = root;
    }

    /**
     * Returns the current row object.
     */
    public Object[] getCurrent() {
        return ((Row) current.data).getData();
    }

    public Row getCurrentRow() {
        return (Row) current.data;
    }

    public Row getNextRow() {
        return next() ? (Row) current.data
                      : null;
    }

    public void remove() {

        // avoid consecutive removes without next()
        if (previous == null) {
            throw new NoSuchElementException();
        }

        if (currentPos < size && currentPos != -1) {
            list.removeAfter(previous);

            current = previous;

            size--;
            currentPos--;

            return;
        }

        throw new NoSuchElementException();
    }

    public boolean next() {

        boolean result = super.next();

        if (result) {
            previous = current;
            current  = current.next;
        }

        return result;
    }

    public void reset() {

        super.reset();

        current  = root;
        previous = null;
    }

    // reading and writing
    public void write(RowOutputInterface out,
                      ResultMetaData meta) throws IOException {

        beforeFirst();
        out.writeLong(id);
        out.writeInt(size);
        out.writeInt(0);    // offset
        out.writeInt(size);

        while (hasNext()) {
            Object[] data = getNext();

            out.writeData(meta.getColumnCount(), meta.columnTypes, data, null,
                          null);
        }

        beforeFirst();
    }

    public void read(RowInputInterface in,
                     ResultMetaData meta) throws IOException {

        id = in.readLong();

        int count = in.readInt();

        in.readInt();    // offset
        in.readInt();    // size again

        while (count-- > 0) {
            add(in.readData(meta.columnTypes));
        }
    }

    public void clear() {

        reset();
        list.clear();

        size = 0;
    }

    /**
     *  Method declaration
     *
     * @param  d
     */
    public void add(Object[] d) {
        throw Error.runtimeError(ErrorCode.U_S0500, "RowSetNavigatorClient");
    }

    public boolean addRow(Row row) {

        list.add(row);

        size++;

        return true;
    }
}
