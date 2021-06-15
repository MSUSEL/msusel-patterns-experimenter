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
import org.hsqldb.navigator.RowSetNavigator;
import org.hsqldb.navigator.RowSetNavigatorData;
import org.hsqldb.persist.PersistentStore;
import org.hsqldb.result.Result;
import org.hsqldb.store.ValuePool;
import org.hsqldb.types.RowType;
import org.hsqldb.types.Type;

/**
 * Implementation of table conversion.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 2.0.0
 * @since 2.0.0
 */
public class ExpressionTable extends Expression {

    boolean isTable;
    boolean ordinality = false;

    /**
     * Creates an UNNEST ARRAY or MULTISET expression
     */
    ExpressionTable(Expression e, SubQuery sq, boolean ordinality) {

        super(OpTypes.TABLE);

        nodes           = new Expression[]{ e };
        this.subQuery   = sq;
        this.ordinality = ordinality;
    }

    public String getSQL() {

        if (isTable) {
            return Tokens.T_TABLE;
        } else {
            return Tokens.T_UNNEST;
        }
    }

    protected String describe(Session session, int blanks) {

        StringBuffer sb = new StringBuffer(64);

        sb.append('\n');

        for (int i = 0; i < blanks; i++) {
            sb.append(' ');
        }

        if (isTable) {
            sb.append(Tokens.T_TABLE).append(' ');
        } else {
            sb.append(Tokens.T_UNNEST).append(' ');
        }

        sb.append(nodes[LEFT].describe(session, blanks));

        return sb.toString();
    }

    public void resolveTypes(Session session, Expression parent) {

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                nodes[i].resolveTypes(session, this);
            }
        }

        if (nodes[LEFT].dataType.isRowType()) {
            isTable       = true;
            nodeDataTypes = ((RowType) nodes[LEFT].dataType).getTypesArray();

            subQuery.prepareTable(session);

            subQuery.getTable().columnList =
                ((FunctionSQLInvoked) nodes[LEFT]).routine.getTable()
                    .columnList;
        } else {
            isTable = false;

            int columnCount = ordinality ? 2
                                         : 1;

            nodeDataTypes       = new Type[columnCount];
            nodeDataTypes[LEFT] = nodes[LEFT].dataType.collectionBaseType();

            if (ordinality) {
                nodeDataTypes[RIGHT] = Type.SQL_INTEGER;
            }

            subQuery.prepareTable(session);
        }
    }

    public Result getResult(Session session) {

        switch (opType) {

            case OpTypes.TABLE : {
                RowSetNavigatorData navigator = subQuery.getNavigator(session);
                Result              result    = Result.newResult(navigator);

                result.metaData = subQuery.queryExpression.getMetaData();

                return result;
            }
            default : {
                throw Error.runtimeError(ErrorCode.U_S0500, "ExpressionTable");
            }
        }
    }

    public Object[] getRowValue(Session session) {

        switch (opType) {

            case OpTypes.TABLE : {
                return subQuery.queryExpression.getValues(session);
            }
            default :
                throw Error.runtimeError(ErrorCode.U_S0500, "Expression");
        }
    }

    Object getValue(Session session, Type type) {

        switch (opType) {

            case OpTypes.TABLE : {
                materialise(session);

                Object[] value = subQuery.getValues(session);

                if (value.length == 1) {
                    return ((Object[]) value)[0];
                }

                return value;
            }
            default :
                throw Error.runtimeError(ErrorCode.U_S0500, "Expression");
        }
    }

    public Object getValue(Session session) {
        return valueData;
    }

    void insertValuesIntoSubqueryTable(Session session,
                                       PersistentStore store) {

        if (isTable) {
            Result          result = nodes[LEFT].getResult(session);
            RowSetNavigator nav    = result.navigator;
            int             size   = nav.getSize();

            while (nav.hasNext()) {
                Object[] data = nav.getNext();
                Row      row  = (Row) store.getNewCachedObject(session, data);

                try {
                    store.indexRow(session, row);
                } catch (HsqlException e) {}
            }
        } else {
            Object[] array = (Object[]) nodes[LEFT].getValue(session);

            for (int i = 0; i < array.length; i++) {
                Object[] data;

                if (ordinality) {
                    data = new Object[] {
                        array[i], ValuePool.getInt(i)
                    };
                } else {
                    data = new Object[]{ array[i] };
                }

                Row row = (Row) store.getNewCachedObject(session, data);

                try {
                    store.indexRow(session, row);
                } catch (HsqlException e) {}
            }
        }
    }
}
