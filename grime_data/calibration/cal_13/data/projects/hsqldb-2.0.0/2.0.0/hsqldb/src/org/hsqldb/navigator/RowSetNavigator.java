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

import org.hsqldb.RangeVariable;
import org.hsqldb.Row;
import org.hsqldb.SessionInterface;
import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.result.ResultMetaData;
import org.hsqldb.rowio.RowInputInterface;
import org.hsqldb.rowio.RowOutputInterface;

/**
 * Encapsulates navigation functionality for lists of objects. The base class
 * provides positional navigation and checking, while the subclasses provide
 * object retreival.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public abstract class RowSetNavigator implements RangeIterator {

    public RowSetNavigator() {}

    SessionInterface session;
    long             id;
    int              size;
    int              mode;
    boolean          isIterator;
    int              currentPos = -1;
    int              rangePosition;

    /**
     * Sets the id;
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Returns the id
     */
    public long getId() {
        return id;
    }

    /**
     * Returns the current row object. Type of object is implementation defined.
     */
    public abstract Object[] getCurrent();

    public void setCurrent(Object[] data) {}

    public long getRowid() {
        return 0;
    }

    public Object getRowidObject() {
        return null;
    }

    public abstract Row getCurrentRow();

    /**
     * Add data to the end
     */
    public abstract void add(Object[] data);

    /**
     * Add row to the end
     */
    public abstract boolean addRow(Row row);

    /**
     * Remove current row
     */
    public abstract void remove();

    /**
     * Clear all rows
     */
    public abstract void clear();

    /**
     * Reset to initial state
     */
    public void reset() {
        currentPos = -1;
    }

    /**
     * Remove any resourses and invalidate
     */
    public void release() {
        reset();
    }

    public void setSession(SessionInterface session) {
        this.session = session;
    }

    public SessionInterface getSession() {
        return session;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Object[] getNext() {
        return next() ? getCurrent()
                      : null;
    }

    public boolean next() {

        if (hasNext()) {
            currentPos++;

            return true;
        } else if (size != 0) {
            currentPos = size;
        }

        return false;
    }

    public boolean hasNext() {
        return currentPos < size - 1;
    }

    public Row getNextRow() {
        throw Error.runtimeError(ErrorCode.U_S0500, "RowSetNavigator");
    }

    public boolean setRowColumns(boolean[] columns) {
        throw Error.runtimeError(ErrorCode.U_S0500, "RowSetNavigator");
    }

    public long getRowId() {
        throw Error.runtimeError(ErrorCode.U_S0500, "RowSetNavigator");
    }

    public boolean beforeFirst() {

        reset();

        currentPos = -1;

        return true;
    }

    public boolean afterLast() {

        if (size == 0) {
            return false;
        }

        reset();

        currentPos = size;

        return true;
    }

    public boolean first() {

        beforeFirst();

        return next();
    }

    public boolean last() {

        if (size == 0) {
            return false;
        }

        if (isAfterLast()) {
            beforeFirst();
        }

        while (hasNext()) {
            next();
        }

        return true;
    }

    public int getRowNumber() {
        return currentPos;
    }

    /**
     * Uses similar semantics to java.sql.ResultSet except this is 0 based.
     * When position is 0 or positive, it is from the start; when negative,
     * it is from end
     */
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

        if (position < currentPos) {
            beforeFirst();
        }

        // go to the tagget row;
        while (position > currentPos) {
            next();
        }

        return true;
    }

    public boolean relative(int rows) {

        int position = currentPos + rows;

        if (position < 0) {
            beforeFirst();

            return false;
        }

        return absolute(position);
    }

    public boolean previous() {
        return relative(-1);
    }

    public boolean isFirst() {
        return size > 0 && currentPos == 0;
    }

    public boolean isLast() {
        return size > 0 && currentPos == size - 1;
    }

    public boolean isBeforeFirst() {
        return size > 0 && currentPos == -1;
    }

    public boolean isAfterLast() {
        return size > 0 && currentPos == size;
    }

    public void close() {}

    public void writeSimple(RowOutputInterface out,
                            ResultMetaData meta) throws IOException {
        throw Error.runtimeError(ErrorCode.U_S0500, "RowSetNavigator");
    }

    public void readSimple(RowInputInterface in,
                           ResultMetaData meta) throws IOException {
        throw Error.runtimeError(ErrorCode.U_S0500, "RowSetNavigator");
    }

    public abstract void write(RowOutputInterface out,
                               ResultMetaData meta) throws IOException;

    public abstract void read(RowInputInterface in,
                              ResultMetaData meta) throws IOException;

    public boolean isMemory() {
        return true;
    }

    public int getRangePosition() {
        return rangePosition;
    }

    public RangeVariable getRange() {
        return null;
    }
}
