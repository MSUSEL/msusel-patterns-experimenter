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
import org.hsqldb.store.ValuePool;

/**
 * Implementation of aggregate operations
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class ExpressionAggregate extends Expression {

    boolean isDistinctAggregate;

    ExpressionAggregate(int type, boolean distinct, Expression e) {

        super(type);

        nodes               = new Expression[UNARY];
        isDistinctAggregate = distinct;
        nodes[LEFT]         = e;
    }

    boolean isSelfAggregate() {
        return true;
    }

    public String getSQL() {

        StringBuffer sb   = new StringBuffer(64);
        String       left = getContextSQL(nodes.length > 0 ? nodes[LEFT]
                                                           : null);

        switch (opType) {

            case OpTypes.COUNT :
                sb.append(' ').append(Tokens.T_COUNT).append('(');
                break;

            case OpTypes.SUM :
                sb.append(' ').append(Tokens.T_SUM).append('(');
                sb.append(left).append(')');
                break;

            case OpTypes.MIN :
                sb.append(' ').append(Tokens.T_MIN).append('(');
                sb.append(left).append(')');
                break;

            case OpTypes.MAX :
                sb.append(' ').append(Tokens.T_MAX).append('(');
                sb.append(left).append(')');
                break;

            case OpTypes.AVG :
                sb.append(' ').append(Tokens.T_AVG).append('(');
                sb.append(left).append(')');
                break;

            case OpTypes.EVERY :
                sb.append(' ').append(Tokens.T_EVERY).append('(');
                sb.append(left).append(')');
                break;

            case OpTypes.SOME :
                sb.append(' ').append(Tokens.T_SOME).append('(');
                sb.append(left).append(')');
                break;

            case OpTypes.STDDEV_POP :
                sb.append(' ').append(Tokens.T_STDDEV_POP).append('(');
                sb.append(left).append(')');
                break;

            case OpTypes.STDDEV_SAMP :
                sb.append(' ').append(Tokens.T_STDDEV_SAMP).append('(');
                sb.append(left).append(')');
                break;

            case OpTypes.VAR_POP :
                sb.append(' ').append(Tokens.T_VAR_POP).append('(');
                sb.append(left).append(')');
                break;

            case OpTypes.VAR_SAMP :
                sb.append(' ').append(Tokens.T_VAR_SAMP).append('(');
                sb.append(left).append(')');
                break;

            default :
                throw Error.runtimeError(ErrorCode.U_S0500,
                                         "ExpressionAggregate");
        }

        return sb.toString();
    }

    protected String describe(Session session, int blanks) {

        StringBuffer sb = new StringBuffer(64);

        sb.append('\n');

        for (int i = 0; i < blanks; i++) {
            sb.append(' ');
        }

        switch (opType) {

            case OpTypes.COUNT :
                sb.append("COUNT ");
                break;

            case OpTypes.SUM :
                sb.append("SUM ");
                break;

            case OpTypes.MIN :
                sb.append("MIN ");
                break;

            case OpTypes.MAX :
                sb.append("MAX ");
                break;

            case OpTypes.AVG :
                sb.append("AVG ");
                break;

            case OpTypes.EVERY :
                sb.append(Tokens.T_EVERY).append(' ');
                break;

            case OpTypes.SOME :
                sb.append(Tokens.T_SOME).append(' ');
                break;

            case OpTypes.STDDEV_POP :
                sb.append(Tokens.T_STDDEV_POP).append(' ');
                break;

            case OpTypes.STDDEV_SAMP :
                sb.append(Tokens.T_STDDEV_SAMP).append(' ');
                break;

            case OpTypes.VAR_POP :
                sb.append(Tokens.T_VAR_POP).append(' ');
                break;

            case OpTypes.VAR_SAMP :
                sb.append(Tokens.T_VAR_SAMP).append(' ');
                break;
        }

        if (getLeftNode() != null) {
            sb.append(" arg=[");
            sb.append(nodes[LEFT].describe(session, blanks + 1));
            sb.append(']');
        }

        return sb.toString();
    }

    public HsqlList resolveColumnReferences(RangeVariable[] rangeVarArray,
            int rangeCount, HsqlList unresolvedSet, boolean acceptsSequences) {

        if (unresolvedSet == null) {
            unresolvedSet = new ArrayListIdentity();
        }

        unresolvedSet.add(this);

        return unresolvedSet;
    }

    public void resolveTypes(Session session, Expression parent) {

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                nodes[i].resolveTypes(session, this);
            }
        }

        if (nodes[LEFT].isUnresolvedParam()) {
            throw Error.error(ErrorCode.X_42567);
        }

        if (isDistinctAggregate) {
            if (nodes[LEFT].dataType.isLobType()) {
                throw Error.error(ErrorCode.X_42534);
            }
        }

        dataType = SetFunction.getType(opType, nodes[LEFT].dataType);
    }

    public boolean equals(Object other) {

        if (!(other instanceof ExpressionAggregate)) {
            return false;
        }

        return opType == ((ExpressionAggregate) other).opType
               && exprSubType == ((ExpressionAggregate) other).exprSubType
               && isDistinctAggregate
                  == ((ExpressionAggregate) other)
                      .isDistinctAggregate && equals(nodes,
                          ((ExpressionAggregate) other).nodes);
    }

    public Object updateAggregatingValue(Session session, Object currValue) {

        if (currValue == null) {
            currValue = new SetFunction(opType, nodes[LEFT].dataType,
                                        isDistinctAggregate);
        }

        Object newValue = nodes[LEFT].opType == OpTypes.ASTERISK
                          ? ValuePool.INTEGER_1
                          : nodes[LEFT].getValue(session);

        ((SetFunction) currValue).add(session, newValue);

        return currValue;
    }

    /**
     * Get the result of a SetFunction or an ordinary value
     *
     * @param currValue instance of set function or value
     * @param session context
     * @return object
     */
    public Object getAggregatedValue(Session session, Object currValue) {

        if (currValue == null) {
            return opType == OpTypes.COUNT ? ValuePool.INTEGER_0
                                           : null;
        }

        return ((SetFunction) currValue).getValue(session);
    }
}
