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

import org.hsqldb.lib.HsqlList;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.lib.Set;
import org.hsqldb.types.Type;

/**
 * Implementation of column used as assignment target.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 2.0.0
 * @since 2.0.0
 */
public class ExpressionColumnAccessor extends Expression {

    ColumnSchema column;

    ExpressionColumnAccessor(ColumnSchema column) {

        super(OpTypes.COLUMN);

        this.column   = column;
        this.dataType = column.getDataType();
    }

    String getAlias() {
        return column.getNameString();
    }

    void collectObjectNames(Set set) {

        set.add(column.getName());

        if (column.getName().parent != null) {
            set.add(column.getName().parent);
        }
    }

    String getColumnName() {
        return column.getNameString();
    }

    ColumnSchema getColumn() {
        return column;
    }

    RangeVariable getRangeVariable() {
        return null;
    }

    public HsqlList resolveColumnReferences(RangeVariable[] rangeVarArray,
            int rangeCount, HsqlList unresolvedSet, boolean acceptsSequences) {
        return unresolvedSet;
    }

    public void resolveTypes(Session session, Expression parent) {}

    public Object getValue(Session session) {
        return null;
    }

    public String getSQL() {
        return column.getName().statementName;
    }

    protected String describe(Session session, int blanks) {
        return column.getName().name;
    }

    public OrderedHashSet getUnkeyedColumns(OrderedHashSet unresolvedSet) {
        return unresolvedSet;
    }

    /**
     * collects all range variables in expression tree
     */
    void collectRangeVariables(RangeVariable[] rangeVariables, Set set) {}

    Expression replaceAliasInOrderBy(Expression[] columns, int length) {
        return this;
    }

    Expression replaceColumnReferences(RangeVariable range,
                                       Expression[] list) {
        return this;
    }

    int findMatchingRangeVariableIndex(RangeVariable[] rangeVarArray) {
        return -1;
    }

    /**
     * return true if given RangeVariable is used in expression tree
     */
    boolean hasReference(RangeVariable range) {
        return false;
    }

    /**
     * SIMPLE_COLUMN expressions can be of different Java types
     */
    public boolean equals(Expression other) {

        if (other == this) {
            return true;
        }

        if (other == null) {
            return false;
        }

        if (opType != ((Expression) other).opType) {
            return false;
        }

        return column == ((Expression) other).getColumn();
    }

    void replaceRangeVariables(RangeVariable[] ranges,
                               RangeVariable[] newRanges) {}

    void resetColumnReferences() {}

    public boolean isIndexable(RangeVariable range) {
        return false;
    }

    public boolean isUnresolvedParam() {
        return false;
    }

    boolean isDynamicParam() {
        return false;
    }

    public Type getDataType() {
        return column.getDataType();
    }
}
