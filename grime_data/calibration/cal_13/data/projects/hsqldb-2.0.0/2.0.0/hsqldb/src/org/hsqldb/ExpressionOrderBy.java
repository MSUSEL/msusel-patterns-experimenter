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

/**
 * Implementation of ORDER BY operations
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class ExpressionOrderBy extends Expression {

    private boolean isDescending;
    private boolean isNullsLast;

    ExpressionOrderBy(Expression e) {

        super(OpTypes.ORDER_BY);

        nodes       = new Expression[UNARY];
        nodes[LEFT] = e;
    }

    /**
     * Set an ORDER BY column expression DESC
     */
    void setDescending() {
        isDescending = true;
    }

    /**
     * Is an ORDER BY column expression DESC
     */
    boolean isDescending() {
        return isDescending;
    }

    /**
     * Set an ORDER BY column NULL ordering
     */
    void setNullsLast() {
        isNullsLast = true;
    }

    /**
     * Is an ORDER BY column NULL ordering
     */
    boolean isNullsLast() {
        return isNullsLast;
    }

    public Object getValue(Session session) {
        return nodes[LEFT].getValue(session);
    }

    public void resolveTypes(Session session, Expression parent) {

        nodes[LEFT].resolveTypes(session, parent);

        if (nodes[LEFT].isUnresolvedParam()) {
            throw Error.error(ErrorCode.X_42567);
        }

        dataType = nodes[LEFT].dataType;
    }

    public String getSQL() {

        StringBuffer sb = new StringBuffer();

        sb.append(Tokens.T_ORDER).append(' ').append(Tokens.T_BY).append(' ');

        if (nodes[LEFT].alias != null) {
            sb.append(nodes[LEFT].alias.name);
        } else {
            sb.append(nodes[LEFT].getSQL());
        }

        if (isDescending) {
            sb.append(' ').append(Tokens.T_DESC);
        }

        return sb.toString();
    }

    protected String describe(Session session, int blanks) {

        StringBuffer sb = new StringBuffer();

        sb.append('\n');

        for (int i = 0; i < blanks; i++) {
            sb.append(' ');
        }

        sb.append(getLeftNode().describe(session, blanks));

        if (isDescending) {
            sb.append(Tokens.T_DESC).append(' ');
        }

        return sb.toString();
    }
}
