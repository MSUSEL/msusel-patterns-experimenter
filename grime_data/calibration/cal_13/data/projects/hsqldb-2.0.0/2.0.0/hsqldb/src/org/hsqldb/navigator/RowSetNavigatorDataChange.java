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
import org.hsqldb.Session;
import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.lib.OrderedLongKeyHashMap;
import org.hsqldb.result.ResultMetaData;
import org.hsqldb.rowio.RowInputInterface;
import org.hsqldb.rowio.RowOutputInterface;
import org.hsqldb.types.Type;

/*
 * All-in-memory implementation of RowSetNavigator for delete and update
 * operations.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 2.0.0
 * @since 1.9.0
 */
public class RowSetNavigatorDataChange extends RowSetNavigator {

    OrderedLongKeyHashMap list;

    public RowSetNavigatorDataChange() {
        list = new OrderedLongKeyHashMap(8, true);
    }

    /**
     * Returns the current row object.
     */
    public Object[] getCurrent() {
        return getCurrentRow().getData();
    }

    public Row getCurrentRow() {
        return (Row) list.getValueByIndex(super.currentPos);
    }

    public Object[] getCurrentChangedData() {
        return (Object[]) list.getSecondValueByIndex(super.currentPos);
    }

    public int[] getCurrentChangedColumns() {
        return (int[]) list.getThirdValueByIndex(super.currentPos);
    }

    public Row getNextRow() {
        return next() ? getCurrentRow()
                      : null;
    }

    public void remove() {
        throw new NoSuchElementException();
    }

    public boolean next() {
        return super.next();
    }

    public void reset() {
        super.reset();
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

    public void add(Object[] d) {
        throw Error.runtimeError(ErrorCode.U_S0500,
                                 "RowSetNavigatorDataChange");
    }

    public boolean addRow(Row row) {

        int lookup = list.getLookup(row.getId());

        if (lookup == -1) {
            list.put(row.getId(), row, null);

            size++;

            return true;
        } else {
            if (list.getSecondValueByIndex(lookup) != null) {
                throw Error.error(ErrorCode.X_27000);
            }

            return false;
        }
    }

    public Object[] addRow(Session session, Row row, Object[] data,
                           Type[] types, int[] columnMap) {

        long rowId  = row.getId();
        int  lookup = list.getLookup(rowId);

        if (lookup == -1) {
            list.put(rowId, row, data);
            list.setThirdValueByIndex(size, columnMap);

            size++;

            return data;
        } else {
            Object[] rowData = ((Row) list.getFirstByLookup(lookup)).getData();
            Object[] currentData =
                (Object[]) list.getSecondValueByIndex(lookup);

            if (currentData == null) {
                throw Error.error(ErrorCode.X_27000);
            }

            for (int i = 0; i < columnMap.length; i++) {
                int j = columnMap[i];

                if (types[j].compare(session, data[j], currentData[j]) != 0) {
                    if (types[j].compare(session, rowData[j], currentData[j])
                            != 0) {
                        throw Error.error(ErrorCode.X_27000);
                    } else {
                        currentData[j] = data[j];
                    }
                }
            }

            int[] currentMap = (int[]) list.getThirdValueByIndex(lookup);

            currentMap = ArrayUtil.union(currentMap, columnMap);

            list.setThirdValueByIndex(lookup, currentMap);

            return currentData;
        }
    }

    public boolean containsDeletedRow(Row row) {

        int lookup = list.getLookup(row.getId());

        if (lookup == -1) {
            return false;
        }

        Object[] currentData = (Object[]) list.getSecondValueByIndex(lookup);

        return currentData == null;
    }
}
