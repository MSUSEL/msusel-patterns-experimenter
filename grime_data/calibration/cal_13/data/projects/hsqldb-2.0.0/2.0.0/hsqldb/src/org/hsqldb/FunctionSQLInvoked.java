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
import org.hsqldb.lib.ArrayListIdentity;
import org.hsqldb.lib.HsqlList;
import org.hsqldb.lib.Set;
import org.hsqldb.result.Result;
import org.hsqldb.store.ValuePool;
import org.hsqldb.types.Type;

/**
 * Implementation of SQL-invoked user-defined function calls - PSM and JRT
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class FunctionSQLInvoked extends Expression {

    RoutineSchema routineSchema;
    Routine       routine;

    FunctionSQLInvoked(RoutineSchema routineSchema) {

        super(routineSchema.isAggregate() ? OpTypes.USER_AGGREGATE
                                          : OpTypes.FUNCTION);

        this.routineSchema = routineSchema;
    }

    public void setArguments(Expression[] newNodes) {
        this.nodes = newNodes;
    }

    public HsqlList resolveColumnReferences(RangeVariable[] rangeVarArray,
            int rangeCount, HsqlList unresolvedSet, boolean acceptsSequences) {

        if (isSelfAggregate()) {
            if (unresolvedSet == null) {
                unresolvedSet = new ArrayListIdentity();
            }

            unresolvedSet.add(this);

            return unresolvedSet;
        } else {
            return super.resolveColumnReferences(rangeVarArray, rangeCount,
                                                 unresolvedSet,
                                                 acceptsSequences);
        }
    }

    public void resolveTypes(Session session, Expression parent) {

        Type[] types = new Type[nodes.length];

        for (int i = 0; i < nodes.length; i++) {
            Expression e = nodes[i];

            e.resolveTypes(session, this);

            types[i] = e.dataType;
        }

        routine = routineSchema.getSpecificRoutine(types);

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].dataType == null) {
                nodes[i].dataType = routine.getParameterTypes()[i];
            }
        }

        dataType = routine.getReturnType();
    }

    private Object getValueInternal(Session session, Object[] aggregateData) {

        boolean  isValue       = false;
        int      variableCount = routine.getVariableCount();
        Result   result;
        int      extraArg = routine.javaMethodWithConnection ? 1
                                                             : 0;
        Object[] data     = ValuePool.emptyObjectArray;
        boolean  push     = true;

        if (extraArg + nodes.length > 0) {
            if (opType == OpTypes.USER_AGGREGATE) {
                data = new Object[routine.getParameterCount()];

                for (int i = 0; i < aggregateData.length; i++) {
                    data[i + 1] = aggregateData[i];
                }
            } else {
                data = new Object[nodes.length + extraArg];
            }

            if (extraArg > 0) {
                data[0] = session.getInternalConnection();
            }
        }

        Type[] dataTypes = routine.getParameterTypes();

        for (int i = 0; i < nodes.length; i++) {
            Expression e     = nodes[i];
            Object     value = e.getValue(session, dataTypes[i]);

            if (value == null) {
                if (routine.isNullInputOutput()) {
                    return null;
                }

                if (!routine.getParameter(i).isNullable()) {
                    return Result.newErrorResult(
                        Error.error(ErrorCode.X_39004));
                }
            }

            if (routine.isPSM()) {
                data[i] = value;
            } else {
                data[i + extraArg] = e.dataType.convertSQLToJava(session,
                        value);
            }
        }

        if (push) {
            session.sessionContext.push();
        }

        if (routine.isPSM()) {
            try {
                session.sessionContext.routineArguments = data;
                session.sessionContext.routineVariables =
                    ValuePool.emptyObjectArray;

                if (variableCount > 0) {
                    session.sessionContext.routineVariables =
                        new Object[variableCount];
                }

                result = routine.statement.execute(session);

                if (aggregateData != null) {
                    for (int i = 0; i < aggregateData.length; i++) {
                        aggregateData[i] = data[i + 1];
                    }
                }
            } catch (Throwable e) {
                result = Result.newErrorResult(e);
            }
        } else {
            if (opType == OpTypes.USER_AGGREGATE) {
                data = routine.convertArgsToJava(session, data);
            }

            result = routine.invokeJavaMethod(session, data);

            if (opType == OpTypes.USER_AGGREGATE) {
                Object[] callResult = new Object[data.length];

                routine.convertArgsToSQL(session, callResult, data);

                for (int i = 0; i < aggregateData.length; i++) {
                    aggregateData[i] = callResult[i + 1];
                }
            }
        }

        if (push) {
            session.sessionContext.pop();
        }

        if (result.isError()) {
            throw result.getException();
        }

        if (isValue) {
            return result.valueData;
        } else {
            return result;
        }
    }

    public Object getValue(Session session) {

        if (opType == OpTypes.SIMPLE_COLUMN) {
            Object[] data =
                session.sessionContext.rangeIterators[rangePosition]
                    .getCurrent();

            return data[columnIndex];
        }

        Object returnValue = getValueInternal(session, null);

        if (returnValue instanceof Result) {
            Result result = (Result) returnValue;

            if (result.isError()) {
                throw result.getException();
            } else if (result.isSimpleValue()) {
                returnValue = result.getValueObject();
            } else if (result.isData()) {
                returnValue = result;
            } else {
                throw Error.error(ErrorCode.X_2F005, routine.getName().name);
            }
        }

        return returnValue;
    }

    public Result getResult(Session session) {

        Object value = getValueInternal(session, null);

        if (value instanceof Result) {
            return (Result) value;
        }

        return Result.newPSMResult(value);
    }

    void collectObjectNames(Set set) {
        set.add(routine.getSpecificName());
    }

    public String getSQL() {

        StringBuffer sb = new StringBuffer();

        sb.append(routineSchema.getName().getSchemaQualifiedStatementName());
        sb.append('(');

        int nodeCount = nodes.length;

        if (opType == OpTypes.USER_AGGREGATE) {
            nodeCount = 1;
        }

        for (int i = 0; i < nodeCount; i++) {
            if (i != 0) {
                sb.append(',');
            }

            sb.append(nodes[i].getSQL());
        }

        sb.append(')');

        return sb.toString();
    }

    public String describe(Session session, int blanks) {
        return super.describe(session, blanks);
    }

    boolean isSelfAggregate() {
        return routineSchema.isAggregate();
    }

    public boolean isDeterministic() {
        return routine.isDeterministic();
    }

    public Object updateAggregatingValue(Session session, Object currValue) {

        Object[] array = (Object[]) currValue;

        if (array == null) {
            array = new Object[3];
        }

        array[0] = Boolean.FALSE;

        getValueInternal(session, array);

        return array;
    }

    public Object getAggregatedValue(Session session, Object currValue) {

        Object[] array = (Object[]) currValue;

        if (array == null) {
            array = new Object[3];
        }

        array[0] = Boolean.TRUE;

        Result result = (Result) getValueInternal(session, array);
        Object returnValue;

        if (result.isError()) {
            throw result.getException();
        } else {
            return result.getValueObject();
        }
    }
}
