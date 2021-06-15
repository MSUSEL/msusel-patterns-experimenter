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

/**
 * Type subclass CLOB data.<p>
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public final class ClobType extends CharacterType {

    static final long maxClobPrecision = 1024L * 1024 * 1024 * 1024;
    static final int  defaultClobSize  = 1024 * 1024 * 16;

    public ClobType(long precision) {
        super(Types.SQL_CLOB, precision);
    }

    public int displaySize() {
        return precision > Integer.MAX_VALUE ? Integer.MAX_VALUE
                                             : (int) precision;
    }

    public int getJDBCTypeCode() {
        return Types.CLOB;
    }

    public Class getJDBCClass() {
        return java.sql.Clob.class;
    }

    public String getJDBCClassName() {
        return "java.sql.Clob";
    }

    public int getSQLGenericTypeCode() {
        return typeCode;
    }

    public String getDefinition() {

        long   factor     = precision;
        String multiplier = null;

        if (precision % (1024) == 0) {
            if (precision % (1024 * 1024 * 1024) == 0) {
                factor     = precision / (1024 * 1024 * 1024);
                multiplier = Tokens.T_G_FACTOR;
            } else if (precision % (1024 * 1024) == 0) {
                factor     = precision / (1024 * 1024);
                multiplier = Tokens.T_M_FACTOR;
            } else {
                factor     = precision / (1024);
                multiplier = Tokens.T_K_FACTOR;
            }
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

    public long getMaxPrecision() {
        return maxClobPrecision;
    }

    public boolean isLobType() {
        return true;
    }

    /** @todo - collation comparison */
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

        if (b instanceof String) {
            return session.database.lobManager.compare((ClobData) a,
                    (String) b);
        }

        return session.database.lobManager.compare((ClobData) a, (ClobData) b);
    }

    public Object convertToDefaultType(SessionInterface session, Object a) {

        if (a == null) {
            return null;
        }

        if (a instanceof ClobData) {
            return a;
        }

        if (a instanceof String) {
            ClobData clob = session.createClob(((String) a).length());

            clob.setString(session, 0, (String) a);

            return clob;
        }

        throw Error.error(ErrorCode.X_42561);
    }

    public String convertToString(Object a) {

        if (a == null) {
            return null;
        }

        return ((ClobData) a).toString();
    }

    public String convertToSQLString(Object a) {

        if (a == null) {
            return Tokens.T_NULL;
        }

        String s = convertToString(a);

        return StringConverter.toQuotedString(s, '\'', true);
    }

    public long position(SessionInterface session, Object data,
                         Object otherData, Type otherType, long start) {

        if (otherType.typeCode == Types.SQL_CLOB) {
            return ((ClobData) data).position(session, (ClobData) otherData,
                                              start);
        } else if (otherType.isCharacterType()) {
            return ((ClobData) data).position(session, (String) otherData,
                                              start);
        } else {
            throw Error.runtimeError(ErrorCode.U_S0500, "ClobType");
        }
    }
}
