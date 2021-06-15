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

package org.hsqldb;

import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.lib.HsqlList;
import org.hsqldb.types.Type;

/**
 * database object component access
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 2.0.0
 * @since 2.0.0
 */
public class ExpressionAccessor extends Expression {

    ExpressionAccessor(Expression left, Expression right) {

        super(OpTypes.ARRAY_ACCESS);

        nodes = new Expression[] {
            left, right
        };
    }

    public ColumnSchema getColumn() {
        return nodes[LEFT].getColumn();
    }

    public HsqlList resolveColumnReferences(RangeVariable[] rangeVarArray,
            int rangeCount, HsqlList unresolvedSet, boolean acceptsSequences) {

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] == null) {
                continue;
            }

            unresolvedSet = nodes[i].resolveColumnReferences(rangeVarArray,
                    rangeCount, unresolvedSet, acceptsSequences);
        }

        return unresolvedSet;
    }

    public void resolveTypes(Session session, Expression parent) {

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                nodes[i].resolveTypes(session, this);
            }
        }

        if (nodes[LEFT].dataType == null) {
            throw Error.error(ErrorCode.X_42567);
        }

        if (!nodes[LEFT].dataType.isArrayType()) {
            throw Error.error(ErrorCode.X_42563);
        }

        dataType = nodes[LEFT].dataType.collectionBaseType();

        if (nodes[RIGHT].opType == OpTypes.DYNAMIC_PARAM) {
            nodes[RIGHT].dataType = Type.SQL_INTEGER;
        }
    }

    public Object getValue(Session session) {

        Object[] array = (Object[]) nodes[LEFT].getValue(session);

        if (array == null) {
            return null;
        }

        Number index = (Number) nodes[RIGHT].getValue(session);

        if (index == null) {
            return null;
        }

        if (index.intValue() < 1 || index.intValue() > array.length) {
            throw Error.error(ErrorCode.X_2202E);
        }

        return array[index.intValue() - 1];
    }

    /**
     * Assignment result
     */
    public Object[] getUpdatedArray(Session session, Object[] array,
                                    Object value, boolean copy) {

        if (array == null) {
            throw Error.error(ErrorCode.X_2200E);
        }

        Number index = (Number) nodes[RIGHT].getValue(session);

        if (index == null) {
            throw Error.error(ErrorCode.X_2202E);
        }

        int i = index.intValue() - 1;

        if (i < 0) {
            throw Error.error(ErrorCode.X_2202E);
        }

        if (i >= nodes[LEFT].dataType.arrayLimitCardinality()) {
            throw Error.error(ErrorCode.X_2202E);
        }

        Object[] newArray = array;

        if (i >= array.length) {
            newArray = new Object[i + 1];

            System.arraycopy(array, 0, newArray, 0, array.length);
        } else if (copy) {
            newArray = new Object[array.length];

            System.arraycopy(array, 0, newArray, 0, array.length);
        }

        newArray[i] = value;

        return newArray;
    }

    public String getSQL() {

        StringBuffer sb   = new StringBuffer(64);
        String       left = getContextSQL(nodes[LEFT]);

        sb.append(left).append('[');
        sb.append(nodes[RIGHT].getSQL()).append(']');

        return sb.toString();
    }

    protected String describe(Session session, int blanks) {

        StringBuffer sb = new StringBuffer(64);

        sb.append('\n');

        for (int i = 0; i < blanks; i++) {
            sb.append(' ');
        }

        sb.append("ARRAY ACCESS");

        if (getLeftNode() != null) {
            sb.append(" array=[");
            sb.append(nodes[LEFT].describe(session, blanks + 1));
            sb.append(']');
        }

        if (getRightNode() != null) {
            sb.append(" array_index=[");
            sb.append(nodes[RIGHT].describe(session, blanks + 1));
            sb.append(']');
        }

        return sb.toString();
    }
}
