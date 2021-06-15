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

package org.hsqldb.types;

import org.hsqldb.Session;
import org.hsqldb.SessionInterface;
import org.hsqldb.Tokens;
import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.lib.StringConverter;

public final class BlobType extends BinaryType {

    public static final long maxBlobPrecision = 1024L * 1024 * 1024 * 1024;
    public static final int  defaultBlobSize  = 1024 * 1024 * 16;

    public BlobType(long precision) {
        super(Types.SQL_BLOB, precision);
    }

    public int displaySize() {
        return precision > Integer.MAX_VALUE ? Integer.MAX_VALUE
                                             : (int) precision;
    }

    public int getJDBCTypeCode() {
        return Types.BLOB;
    }

    public Class getJDBCClass() {
        return java.sql.Blob.class;
    }

    public String getJDBCClassName() {
        return "java.sql.Blob";
    }

    public String getNameString() {
        return Tokens.T_BLOB;
    }

    public String getFullNameString() {
        return "BINARY LARGE OBJECT";
    }

    public String getDefinition() {

        long   factor     = precision;
        String multiplier = null;

        if (precision % (1024 * 1024 * 1024) == 0) {
            factor     = precision / (1024 * 1024 * 1024);
            multiplier = Tokens.T_G_FACTOR;
        } else if (precision % (1024 * 1024) == 0) {
            factor     = precision / (1024 * 1024);
            multiplier = Tokens.T_M_FACTOR;
        } else if (precision % (1024) == 0) {
            factor     = precision / (1024);
            multiplier = Tokens.T_K_FACTOR;
        }

        StringBuffer sb = new StringBuffer(16);

        sb.append(getNameString());
        sb.append('(');
        sb.append(factor);

        if (multiplier != null) {
            sb.append(multiplier);
        }

        sb.append(')');

        return sb.toString();
    }

    public boolean acceptsPrecision() {
        return true;
    }

    public boolean requiresPrecision() {
        return false;
    }

    public long getMaxPrecision() {
        return maxBlobPrecision;
    }

    public boolean isBinaryType() {
        return true;
    }

    public boolean isLobType() {
        return true;
    }

    public int compare(Session session, Object a, Object b) {

        if (a == b) {
            return 0;
        }

        if (a == null) {
            return -1;
        }

        if (b == null) {
            return 1;
        }

        if (b instanceof BinaryData) {
            return session.database.lobManager.compare((BlobData) a,
                    ((BlobData) b).getBytes());
        }

        return session.database.lobManager.compare((BlobData) a, (BlobData) b);
    }

    /** @todo - implement */
    public Object convertToTypeLimits(SessionInterface session, Object a) {
        return a;
    }

    public Object convertToType(SessionInterface session, Object a,
                                Type otherType) {

        if (a == null) {
            return null;
        }

        if (otherType.typeCode == Types.SQL_BLOB) {
            return a;
        }

        if (otherType.typeCode == Types.SQL_BINARY
                || otherType.typeCode == Types.SQL_VARBINARY) {
            BlobData b    = (BlobData) a;
            BlobData blob = session.createBlob(b.length(session));

            blob.setBytes(session, 0, b.getBytes());

            return blob;
        }

        throw Error.error(ErrorCode.X_42561);
    }

    public Object convertToDefaultType(SessionInterface session, Object a) {

        if (a == null) {
            return a;
        }

        // conversion to Blob via PreparedStatement.setObject();
        if (a instanceof byte[]) {
            return new BinaryData((byte[]) a, false);
        }

        throw Error.error(ErrorCode.X_42561);
    }

    public String convertToString(Object a) {

        if (a == null) {
            return null;
        }

        byte[] bytes = ((BlobData) a).getBytes();

        return StringConverter.byteArrayToHexString(bytes);
    }

    public String convertToSQLString(Object a) {

        if (a == null) {
            return Tokens.T_NULL;
        }

        byte[] bytes = ((BlobData) a).getBytes();

        return StringConverter.byteArrayToSQLHexString(bytes);
    }
}
