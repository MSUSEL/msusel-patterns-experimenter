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

import org.hsqldb.OpTypes;
import org.hsqldb.Session;
import org.hsqldb.SessionInterface;
import org.hsqldb.Tokens;
import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.store.ValuePool;

/**
 * Class for ROW type objects.<p>
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 2.0.0
 * @since 2.0.0
 */
public class RowType extends Type {

    final Type[] dataTypes;

    public RowType(Type[] dataTypes) {

        super(Types.SQL_ROW, Types.SQL_ROW, 0, 0);

        this.dataTypes = dataTypes;
    }

    public int displaySize() {
        return 0;
    }

    public int getJDBCTypeCode() {
        return Types.NULL;
    }

    public Class getJDBCClass() {
        return java.sql.ResultSet.class;
    }

    public String getJDBCClassName() {
        return "java.sql.ResultSet";
    }

    public Integer getJDBCScale() {
        return ValuePool.INTEGER_0;
    }

    public int getJDBCPrecision() {
        return ValuePool.INTEGER_0;
    }

    public int getSQLGenericTypeCode() {
        return Types.SQL_ROW;
    }

    public boolean isRowType() {
        return true;
    }

    public String getNameString() {

        StringBuffer sb = new StringBuffer();

        sb.append(Tokens.T_ROW);
        sb.append('(');

        for (int i = 0; i < dataTypes.length; i++) {
            if (i > 0) {
                sb.append(',');
            }

            sb.append(dataTypes[i].getNameString());
        }

        sb.append(')');

        return sb.toString();
    }

    String getDefinition() {
        return getNameString();
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

        Object[] arra   = (Object[]) a;
        Object[] arrb   = (Object[]) b;
        int      length = arra.length;

        if (arrb.length < length) {
            length = arrb.length;
        }

        for (int i = 0; i < length; i++) {
            int result = dataTypes[i].compare(session, arra[i], arrb[i]);

            if (result != 0) {
                return result;
            }
        }

        if (arra.length > arrb.length) {
            return 1;
        } else if (arra.length < arrb.length) {
            return -1;
        }

        return 0;
    }

    public Object convertToTypeLimits(SessionInterface session, Object a) {

        if (a == null) {
            return null;
        }

        Object[] arra = (Object[]) a;
        Object[] arrb = new Object[arra.length];

        for (int i = 0; i < arra.length; i++) {
            arrb[i] = dataTypes[i].convertToTypeLimits(session, arra[i]);
        }

        return arrb;
    }

    public Object convertToType(SessionInterface session, Object a,
                                Type otherType) {

        if (a == null) {
            return null;
        }

        if (otherType == null) {
            return a;
        }

        if (!otherType.isRowType()) {
            throw Error.error(ErrorCode.X_42562);
        }

        Type[]   otherTypes = ((RowType) otherType).getTypesArray();

        if (dataTypes.length != otherTypes.length) {
            throw Error.error(ErrorCode.X_42564);
        }

        Object[] arra       = (Object[]) a;
        Object[] arrb       = new Object[arra.length];

        for (int i = 0; i < arra.length; i++) {
            arrb[i] = dataTypes[i].convertToType(session, arra[i],
                                                 otherTypes[i]);
        }

        return arrb;
    }

    public Object convertToDefaultType(SessionInterface sessionInterface,
                                       Object o) {
        return o;
    }

    public String convertToString(Object a) {

        if (a == null) {
            return null;
        }

        return convertToSQLString(a);
    }

    public String convertToSQLString(Object a) {

        if (a == null) {
            return Tokens.T_NULL;
        }

        Object[]     arra = (Object[]) a;
        StringBuffer sb   = new StringBuffer();

        sb.append(Tokens.T_ROW);
        sb.append('(');

        for (int i = 0; i < arra.length; i++) {
            if (i > 0) {
                sb.append(',');
            }

            sb.append(dataTypes[i].convertToSQLString(arra[i]));
        }

        sb.append(')');

        return sb.toString();
    }

    public boolean canConvertFrom(Type otherType) {

        if (otherType == null) {
            return true;
        }

        if (!otherType.isRowType()) {
            return false;
        }

        Type[] otherTypes = ((RowType) otherType).getTypesArray();

        if (dataTypes.length != otherTypes.length) {
            return false;
        }

        for (int i = 0; i < dataTypes.length; i++) {
            if (!dataTypes[i].canConvertFrom(otherTypes[i])) {
                return false;
            }
        }

        return true;
    }

    public boolean canBeAssignedFrom(Type otherType) {

        if (otherType == null) {
            return true;
        }

        if (!otherType.isRowType()) {
            return false;
        }

        Type[] otherTypes = ((RowType) otherType).getTypesArray();

        if (dataTypes.length != otherTypes.length) {
            return false;
        }

        for (int i = 0; i < dataTypes.length; i++) {
            if (!dataTypes[i].canBeAssignedFrom(otherTypes[i])) {
                return false;
            }
        }

        return true;
    }

    public Type getAggregateType(Type otherType) {

        if (otherType == null) {
            return this;
        }

        if (otherType == this) {
            return this;
        }

        if (!otherType.isRowType()) {
            throw Error.error(ErrorCode.X_42562);
        }

        Type[] newTypes   = new Type[dataTypes.length];
        Type[] otherTypes = ((RowType) otherType).getTypesArray();

        if (dataTypes.length != otherTypes.length) {
            throw Error.error(ErrorCode.X_42564);
        }

        for (int i = 0; i < dataTypes.length; i++) {
            newTypes[i] = dataTypes[i].getAggregateType(otherTypes[i]);
        }

        return new RowType(newTypes);
    }

    public Type getCombinedType(Type otherType, int operation) {

        if (operation != OpTypes.CONCAT) {
            return getAggregateType(otherType);
        }

        if (otherType == null) {
            return this;
        }

        if (!otherType.isRowType()) {
            throw Error.error(ErrorCode.X_42562);
        }

        Type[] newTypes   = new Type[dataTypes.length];
        Type[] otherTypes = ((RowType) otherType).getTypesArray();

        if (dataTypes.length != otherTypes.length) {
            throw Error.error(ErrorCode.X_42564);
        }

        for (int i = 0; i < dataTypes.length; i++) {
            newTypes[i] = dataTypes[i].getAggregateType(otherTypes[i]);
        }

        return new RowType(newTypes);
    }

    public Type[] getTypesArray() {
        return dataTypes;
    }
}
