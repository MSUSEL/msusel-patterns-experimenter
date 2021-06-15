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

import java.io.EOFException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.hsqldb.lib.StringConverter;
import org.hsqldb.store.ValuePool;
import org.hsqldb.types.BinaryData;
import org.hsqldb.types.BlobData;
import org.hsqldb.types.BlobDataID;
import org.hsqldb.types.ClobData;
import org.hsqldb.types.ClobDataID;
import org.hsqldb.types.IntervalMonthData;
import org.hsqldb.types.IntervalSecondData;
import org.hsqldb.types.IntervalType;
import org.hsqldb.types.JavaObjectData;
import org.hsqldb.types.TimeData;
import org.hsqldb.types.TimestampData;
import org.hsqldb.types.Type;
import org.hsqldb.types.Types;

/**
 *  Provides methods for reading the data for a row from a
 *  byte array. The format of data is that used for storage of cached
 *  tables by v.1.6.x databases, apart from strings.
 *
 * @author Bob Preston (sqlbob@users dot sourceforge.net)
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.7.0
 */
public class RowInputBinary extends RowInputBase
implements org.hsqldb.rowio.RowInputInterface {

    private RowOutputBinary out;

    public RowInputBinary(byte[] buf) {
        super(buf);
    }

    /**
     * uses the byte[] buffer from out. At each reset, the buffer is set
     * to the current one for out.
     */
    public RowInputBinary(RowOutputBinary out) {

        super(out.getBuffer());

        this.out = out;
    }

    public int readType() throws IOException {
        return readShort();
    }

    public String readString() throws IOException {

        int    length = readInt();
        String s      = StringConverter.readUTF(buffer, pos, length);

        s   = ValuePool.getString(s);
        pos += length;

        return s;
    }

    protected boolean readNull() throws IOException {

        int b = readByte();

        return b == 0 ? true
                      : false;
    }

    protected String readChar(Type type) throws IOException {
        return readString();
    }

    protected Integer readSmallint() throws IOException {
        return ValuePool.getInt(readShort());
    }

    protected Integer readInteger() throws IOException {
        return ValuePool.getInt(readInt());
    }

    protected Long readBigint() throws IOException {
        return ValuePool.getLong(readLong());
    }

    protected Double readReal() throws IOException {
        return ValuePool.getDouble(readLong());
    }

    protected BigDecimal readDecimal(Type type) throws IOException {

        byte[]     bytes  = readByteArray();
        int        scale  = readInt();
        BigInteger bigint = new BigInteger(bytes);

        return ValuePool.getBigDecimal(new BigDecimal(bigint, scale));
    }

    protected Boolean readBoole() throws IOException {
        return readBoolean() ? Boolean.TRUE
                             : Boolean.FALSE;
    }

    protected TimeData readTime(Type type) throws IOException {

        if (type.typeCode == Types.SQL_TIME) {
            return new TimeData(readInt(), readInt(), 0);
        } else {
            return new TimeData(readInt(), readInt(), readInt());
        }
    }

    protected TimestampData readDate(Type type) throws IOException {

        long date = readLong();

        return new TimestampData(date);
    }

    protected TimestampData readTimestamp(Type type) throws IOException {

        if (type.typeCode == Types.SQL_TIMESTAMP) {
            return new TimestampData(readLong(), readInt());
        } else {
            return new TimestampData(readLong(), readInt(), readInt());
        }
    }

    protected IntervalMonthData readYearMonthInterval(Type type)
    throws IOException {

        long months = readLong();

        return new IntervalMonthData(months, (IntervalType) type);
    }

    protected IntervalSecondData readDaySecondInterval(Type type)
    throws IOException {

        long seconds = readLong();
        int  nanos   = readInt();

        return new IntervalSecondData(seconds, nanos, (IntervalType) type);
    }

    protected Object readOther() throws IOException {
        return new JavaObjectData(readByteArray());
    }

    protected BinaryData readBit() throws IOException {

        int    length = readInt();
        byte[] b      = new byte[(length + 7) / 8];

        readFully(b);

        return BinaryData.getBitData(b, length);
    }

    protected BinaryData readBinary() throws IOException {
        return new BinaryData(readByteArray(), false);
    }

    protected ClobData readClob() throws IOException {

        long id = super.readLong();

        return new ClobDataID(id);
    }

    protected BlobData readBlob() throws IOException {

        long id = super.readLong();

        return new BlobDataID(id);
    }

    protected Object[] readArray(Type type) throws IOException {
        type = type.collectionBaseType();
        int size = readInt();

        Object[] data = new Object[size];

        for (int i = 0; i < size; i++) {
            data[i] = readData(type);
        }

        return data;
    }

    public Object[] readData(Type[] colTypes) throws IOException {
        return super.readData(colTypes);
    }

    // helper methods
    public byte[] readByteArray() throws IOException {

        byte[] b = new byte[readInt()];

        readFully(b);

        return b;
    }

    public char[] readCharArray() throws IOException {

        char[] c = new char[readInt()];

        if (count - pos < c.length) {
            pos = count;

            throw new EOFException();
        }

        for (int i = 0; i < c.length; i++) {
            int ch1 = buffer[pos++] & 0xff;
            int ch2 = buffer[pos++] & 0xff;

            c[i] = (char) ((ch1 << 8) + (ch2));
        }

        return c;
    }

    /**
     *  Used to reset the row, ready for Result data to be written into the
     *  byte[] buffer by an external routine.
     *
     */
    public void resetRow(int rowsize) {

        if (out != null) {
            out.reset(rowsize);

            buffer = out.getBuffer();
        }

        super.reset();
    }

    /**
     *  Used to reset the row, ready for a new db row to be written into the
     *  byte[] buffer by an external routine.
     *
     */
    public void resetRow(int filepos, int rowsize) throws IOException {

        if (out != null) {
            out.reset(rowsize);

            buffer = out.getBuffer();
        }

        super.resetRow(filepos, rowsize);
    }
}
