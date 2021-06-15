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

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import org.hsqldb.Row;
import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.lib.StringConverter;
import org.hsqldb.persist.TextCache;
import org.hsqldb.types.BinaryData;
import org.hsqldb.types.BlobData;
import org.hsqldb.types.ClobData;
import org.hsqldb.types.IntervalMonthData;
import org.hsqldb.types.IntervalSecondData;
import org.hsqldb.types.JavaObjectData;
import org.hsqldb.types.TimeData;
import org.hsqldb.types.TimestampData;
import org.hsqldb.types.Type;
import org.hsqldb.types.Types;

/**
 *  Class for writing the data for a database row in text table format.
 *
 * @author Bob Preston (sqlbob@users dot sourceforge.net)
 * @version 2.0.0
 * @since 1.7.0
 */
public class RowOutputText extends RowOutputBase {

    protected String  fieldSep;
    protected String  varSep;
    protected String  longvarSep;
    private boolean   fieldSepEnd;
    private boolean   varSepEnd;
    private boolean   longvarSepEnd;
    private String    nextSep = "";
    private boolean   nextSepEnd;
    protected boolean allQuoted;
    private String    encoding;

    public RowOutputText(String fieldSep, String varSep, String longvarSep,
                         boolean allQuoted, String encoding) {

        super();

        initTextDatabaseRowOutput(fieldSep, varSep, longvarSep, allQuoted,
                                  encoding);
    }

    private void initTextDatabaseRowOutput(String fieldSep, String varSep,
                                           String longvarSep,
                                           boolean allQuoted,
                                           String encoding) {

        //-- Newline indicates that field should match to end of line.
        if (fieldSep.endsWith("\n")) {
            fieldSepEnd = true;
            fieldSep    = fieldSep.substring(0, fieldSep.length() - 1);
        }

        if (varSep.endsWith("\n")) {
            varSepEnd = true;
            varSep    = varSep.substring(0, varSep.length() - 1);
        }

        if (longvarSep.endsWith("\n")) {
            longvarSepEnd = true;
            longvarSep    = longvarSep.substring(0, longvarSep.length() - 1);
        }

        this.fieldSep   = fieldSep;
        this.varSep     = varSep;
        this.longvarSep = longvarSep;
        this.allQuoted  = allQuoted;
        this.encoding   = encoding;
    }

    public void writeEnd() {

        // terminate at the end of row
        if (nextSepEnd) {
            writeBytes(nextSep);
        }

        writeBytes(TextCache.NL);
    }

    public void writeSize(int size) {

        // initialise at the start of row
        nextSep    = "";
        nextSepEnd = false;
    }

    public void writeType(int type) {

        //--do Nothing
    }

    public void writeString(String s) {

        s = checkConvertString(s, fieldSep);

        // error
        if (s == null) {
            return;
        }

        // writeBytes(s);
        byte[] bytes = getBytes(s);

        write(bytes, 0, bytes.length);

        nextSep    = fieldSep;
        nextSepEnd = fieldSepEnd;
    }

    protected void writeVarString(String s) {

        s = checkConvertString(s, varSep);

        if (s == null) {
            return;
        }

        // writeBytes(s);
        byte[] bytes = getBytes(s);

        write(bytes, 0, bytes.length);

        nextSep    = varSep;
        nextSepEnd = varSepEnd;
    }

    protected void writeLongVarString(String s) {

        s = checkConvertString(s, longvarSep);

        if (s == null) {
            return;
        }

        // writeBytes(s);
        byte[] bytes = getBytes(s);

        write(bytes, 0, bytes.length);

        nextSep    = longvarSep;
        nextSepEnd = longvarSepEnd;
    }

    protected String checkConvertString(String s, String sep) {

        if (s.indexOf('\n') != -1 || s.indexOf('\r') != -1) {
            throw new IllegalArgumentException(
                Error.getMessage(ErrorCode.TEXT_STRING_HAS_NEWLINE));
        } else if (s.indexOf(sep) != -1) {
            return null;
        }

        return s;
    }

    private byte[] getBytes(String s) {

        byte[] bytes = null;

        try {
            bytes = s.getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            bytes = s.getBytes();
        }

        return bytes;
    }

    protected void writeByteArray(byte[] b) {

        ensureRoom(b.length * 2);
        StringConverter.writeHexBytes(this.getBuffer(), count, b);

        count += b.length * 2;
    }

    public void writeShort(int i) {
        writeInt(i);
    }

    public void writeInt(int i) {

        writeBytes(Integer.toString(i));

        nextSep    = fieldSep;
        nextSepEnd = fieldSepEnd;
    }

    public void writeIntData(int i, int position) {
        throw Error.runtimeError(ErrorCode.U_S0500, "RowOutputText");
    }

    public void writeLong(long i) {
        throw Error.runtimeError(ErrorCode.U_S0500, "RowOutputText");
    }

// fredt@users - comment - methods used for writing each SQL type
    protected void writeFieldType(Type type) {

        writeBytes(nextSep);

        switch (type.typeCode) {

            case Types.SQL_VARCHAR :
            case Types.VARCHAR_IGNORECASE :
                nextSep    = varSep;
                nextSepEnd = varSepEnd;
                break;

            default :
                nextSep    = fieldSep;
                nextSepEnd = fieldSepEnd;
                break;
        }
    }

    protected void writeNull(Type type) {
        writeFieldType(type);
    }

    protected void writeChar(String s, Type t) {

        switch (t.typeCode) {

            case Types.SQL_CHAR :
                writeString(s);

                return;

            case Types.SQL_VARCHAR :
            case Types.VARCHAR_IGNORECASE :
                writeVarString(s);

                return;

            default :
                writeLongVarString(s);

                return;
        }
    }

    protected void writeSmallint(Number o) {
        writeString(o.toString());
    }

    protected void writeInteger(Number o) {
        writeString(o.toString());
    }

    protected void writeBigint(Number o) {
        writeString(o.toString());
    }

    protected void writeReal(Double o) {
        writeString(o.toString());
    }

    protected void writeDecimal(BigDecimal o, Type type) {
        writeString(type.convertToString(o));
    }

    protected void writeBoolean(Boolean o) {
        writeString(o.toString());
    }

    protected void writeDate(TimestampData o, Type type) {
        writeString(type.convertToString(o));
    }

    protected void writeTime(TimeData o, Type type) {
        writeString(type.convertToString(o));
    }

    protected void writeTimestamp(TimestampData o, Type type) {
        writeString(type.convertToString(o));
    }

    protected void writeYearMonthInterval(IntervalMonthData o, Type type) {
        this.writeBytes(type.convertToString(o));
    }

    protected void writeDaySecondInterval(IntervalSecondData o, Type type) {
        this.writeBytes(type.convertToString(o));
    }

    protected void writeOther(JavaObjectData o) {

        byte[] ba = o.getBytes();

        writeByteArray(ba);
    }

    protected void writeBit(BinaryData o) {

        String s = StringConverter.byteArrayToBitString(o.getBytes(),
            (int) o.bitLength(null));

        writeString(s);
    }

    protected void writeBinary(BinaryData o) {
        writeByteArray(o.getBytes());
    }

    protected void writeClob(ClobData o, Type type) {
        writeString(Long.toString(o.getId()));
    }

    protected void writeBlob(BlobData o, Type type) {
        writeString(Long.toString(o.getId()));
    }

    protected void writeArray(Object[] o, Type type) {
        throw Error.runtimeError(ErrorCode.U_S0500, "RowOutputText");
    }

    public int getSize(Row r) {

        reset();

        try {
            writeSize(0);
            writeData(r.getData(), r.getTable().getColumnTypes());
            writeEnd();
        } catch (Exception e) {
            reset();

//            throw Error.error(ErrorCode.FILE_IO_ERROR, e.toString());
        }

        int rowsize = size();

        reset();

        return rowsize;
    }

    public int getStorageSize(int size) {
        return size;
    }

    public RowOutputInterface duplicate() {
        throw Error.runtimeError(ErrorCode.U_S0500, "RowOutputText");
    }
}
