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
 * Class for ARRAY type objects.<p>
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 2.0.0
 * @since 2.0.0
 */
public class ArrayType extends Type {

    final Type dataType;
    final int  maxCardinality;

    public ArrayType(Type dataType, int cardinality) {

        super(Types.SQL_ARRAY, Types.SQL_ARRAY, 0, 0);

        this.dataType       = dataType;
        this.maxCardinality = cardinality;
    }

    public int displaySize() {
        return 7 + (dataType.displaySize() + 1) * maxCardinality ;
    }

    public int getJDBCTypeCode() {
        return Types.ARRAY;
    }

    public Class getJDBCClass() {
        return java.sql.Array.class;
    }

    public String getJDBCClassName() {
        return "java.sql.Array";
    }

    public Integer getJDBCScale() {
        return ValuePool.INTEGER_0;
    }

    public int getJDBCPrecision() {
        return ValuePool.INTEGER_0;
    }

    public int getSQLGenericTypeCode() {
        return 0;
    }

    public String getNameString() {

        StringBuffer sb = new StringBuffer();

        sb.append(dataType.getNameString()).append(' ');
        sb.append(Tokens.T_ARRAY);

        if (maxCardinality != defaultArrayCardinality) {
            sb.append('[').append(maxCardinality).append(']');
        }

        return sb.toString();
    }

    String getDefinition() {

        StringBuffer sb = new StringBuffer();

        sb.append(dataType.getDefinition()).append(' ');
        sb.append(Tokens.T_ARRAY);

        if (maxCardinality != defaultArrayCardinality) {
            sb.append('[').append(maxCardinality).append(']');
        }

        return sb.toString();
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
            int result = dataType.compare(session, arra[i], arrb[i]);

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

        if (arra.length > maxCardinality) {
            throw Error.error(ErrorCode.X_2202F);
        }

        Object[] arrb = new Object[arra.length];

        for (int i = 0; i < arra.length; i++) {
            arrb[i] = dataType.convertToTypeLimits(session, arra[i]);
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

        if (!otherType.isArrayType()) {
            throw Error.error(ErrorCode.X_42562);
        }

        Object[] arra = (Object[]) a;

        if (arra.length > maxCardinality) {
            throw Error.error(ErrorCode.X_2202F);
        }

        Type otherComponent = otherType.collectionBaseType();

        if (dataType.equals(otherComponent)) {
            return a;
        }

        Object[] arrb = new Object[arra.length];

        for (int i = 0; i < arra.length; i++) {
            arrb[i] = dataType.convertToType(session, arra[i], otherComponent);
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

        sb.append(Tokens.T_ARRAY);
        sb.append('[');

        for (int i = 0; i < arra.length; i++) {
            if (i > 0) {
                sb.append(',');
            }

            sb.append(dataType.convertToSQLString(arra[i]));
        }

        sb.append(']');

        return sb.toString();
    }

    public boolean canConvertFrom(Type otherType) {

        if (otherType == null) {
            return true;
        }

        if (!otherType.isArrayType()) {
            return false;
        }

        Type otherComponent = otherType.collectionBaseType();

        return dataType.canConvertFrom(otherComponent);
    }

    public boolean canBeAssignedFrom(Type otherType) {

        if (otherType == null) {
            return true;
        }

        Type otherComponent = otherType.collectionBaseType();

        return otherComponent != null
               && dataType.canBeAssignedFrom(otherComponent);
    }

    public Type collectionBaseType() {
        return dataType;
    }

    public int arrayLimitCardinality() {
        return maxCardinality;
    }

    public boolean isArrayType() {
        return true;
    }

    public Type getAggregateType(Type otherType) {

        if (otherType == null) {
            return this;
        }

        if (!otherType.isArrayType()) {
            throw Error.error(ErrorCode.X_42562);
        }

        Type otherComponent = otherType.collectionBaseType();

        if (dataType.equals(otherComponent)) {
            return this;
        }

        Type newType = dataType.getAggregateType(otherComponent);

        return new ArrayType(newType, maxCardinality);
    }

    public Type getCombinedType(Type otherType, int operation) {

        if (operation != OpTypes.CONCAT) {
            return getAggregateType(otherType);
        }

        if (otherType == null) {
            return this;
        }

        if (!otherType.isArrayType()) {
            throw Error.error(ErrorCode.X_42562);
        }

        Type otherComponent = otherType.collectionBaseType();
        Type combinedType   = dataType.getAggregateType(otherComponent);

        return new ArrayType(combinedType,
                             maxCardinality
                             + otherType.arrayLimitCardinality());
    }

    public int cardinality(Session session, Object a) {

        if (a == null) {
            return 0;
        }

        return ((Object[]) a).length;
    }

    public Object concat(Session session, Object a, Object b) {

        if (a == null || b == null) {
            return null;
        }

        int      size  = ((Object[]) a).length + ((Object[]) b).length;
        Object[] array = new Object[size];

        System.arraycopy(a, 0, array, 0, ((Object[]) a).length);
        System.arraycopy(b, 0, array, ((Object[]) a).length,
                         ((Object[]) b).length);

        return array;
    }
}
