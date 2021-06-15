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

import java.io.Serializable;

import org.hsqldb.Session;
import org.hsqldb.SessionInterface;
import org.hsqldb.Tokens;
import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.lib.StringConverter;

/**
 * Type implementation for OTHER type.<p>
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public final class OtherType extends Type {

    static final OtherType otherType = new OtherType();

    private OtherType() {
        super(Types.OTHER, Types.OTHER, 0, 0);
    }

    public int displaySize() {
        return precision > Integer.MAX_VALUE ? Integer.MAX_VALUE
                                             : (int) precision;
    }

    public int getJDBCTypeCode() {
        return typeCode;
    }

    public Class getJDBCClass() {
        return java.lang.Object.class;
    }

    public String getJDBCClassName() {
        return "java.lang.Object";
    }

    public int getSQLGenericTypeCode() {

        // return Types.SQL_UDT;
        return typeCode;
    }

    public int typeCode() {

        // return Types.SQL_UDT;
        return typeCode;
    }

    public String getNameString() {
        return Tokens.T_OTHER;
    }

    public String getDefinition() {
        return Tokens.T_OTHER;
    }

    public Type getAggregateType(Type other) {

        if (typeCode == other.typeCode) {
            return this;
        }

        if (other == SQL_ALL_TYPES) {
            return this;
        }

        throw Error.error(ErrorCode.X_42562);
    }

    public Type getCombinedType(Type other, int operation) {
        return this;
    }

    public int compare(Session session, Object a, Object b) {

        if (a == null) {
            return -1;
        }

        if (b == null) {
            return 1;
        }

        return 0;
    }

    public Object convertToTypeLimits(SessionInterface session, Object a) {
        return a;
    }

    // to review - if conversion is supported, then must be serializable and wappred
    public Object convertToType(SessionInterface session, Object a,
                                Type otherType) {
        return a;
    }

    public Object convertToDefaultType(SessionInterface session, Object a) {

        if (a instanceof Serializable) {
            return a;
        }

        throw Error.error(ErrorCode.X_42561);
    }

    public String convertToString(Object a) {

        if (a == null) {
            return null;
        }

        return StringConverter.byteArrayToHexString(
            ((JavaObjectData) a).getBytes());
    }

    public String convertToSQLString(Object a) {

        if (a == null) {
            return Tokens.T_NULL;
        }

        return StringConverter.byteArrayToSQLHexString(
            ((JavaObjectData) a).getBytes());
    }

    public Object convertSQLToJava(SessionInterface session, Object a) {

        if (a == null) {
            return null;
        }

        return ((JavaObjectData) a).getObject();
    }

    public boolean canConvertFrom(Type otherType) {

        if (otherType.typeCode == typeCode) {
            return true;
        }

        if (otherType.typeCode == Types.SQL_ALL_TYPES) {
            return true;
        }

        return false;
    }

    public boolean isObjectType() {
        return true;
    }

    public static OtherType getOtherType() {
        return otherType;
    }
}
