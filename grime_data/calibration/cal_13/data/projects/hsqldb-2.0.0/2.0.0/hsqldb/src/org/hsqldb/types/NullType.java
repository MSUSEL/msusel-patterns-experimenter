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

/**
 * Type subclass for untyped NULL values.<p>
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public final class NullType extends Type {

    static final NullType nullType = new NullType();

    private NullType() {
        super(Types.SQL_ALL_TYPES, Types.SQL_ALL_TYPES, 0, 0);
    }

    public int displaySize() {
        return 4;
    }

    public int getJDBCTypeCode() {
        return typeCode;
    }

    public Class getJDBCClass() {
        return java.lang.Void.class;
    }

    public String getJDBCClassName() {
        return "java.lang.Void";
    }

    public String getNameString() {
        return Tokens.T_NULL;
    }

    public String getDefinition() {
        return Tokens.T_NULL;
    }

    public Type getAggregateType(Type other) {
        return other;
    }

    public Type getCombinedType(Type other, int operation) {
        return other;
    }

    public int compare(Session session, Object a, Object b) {
        throw Error.runtimeError(ErrorCode.U_S0500, "NullType");
    }

    public Object convertToTypeLimits(SessionInterface session, Object a) {
        return null;
    }

    public Object convertToType(SessionInterface session, Object a,
                                Type otherType) {
        return null;
    }

    public Object convertToDefaultType(SessionInterface session, Object a) {
        return null;
    }

    public String convertToString(Object a) {
        throw Error.runtimeError(ErrorCode.U_S0500, "NullType");
    }

    public String convertToSQLString(Object a) {
        throw Error.runtimeError(ErrorCode.U_S0500, "NullType");
    }

    public boolean canConvertFrom(Type otherType) {
        return true;
    }

    public static Type getNullType() {
        return nullType;
    }
}
