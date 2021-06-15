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

import org.hsqldb.Constraint;
import org.hsqldb.Expression;
import org.hsqldb.HsqlNameManager;
import org.hsqldb.HsqlNameManager.HsqlName;
import org.hsqldb.SchemaObject;
import org.hsqldb.Session;
import org.hsqldb.Tokens;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.rights.Grantee;

/**
 * Class for DOMAIN and DISTINCT objects.<p>
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class UserTypeModifier {

    final HsqlName name;
    final int      schemaObjectType;
    final Type     dataType;
    Constraint[]   constraints = Constraint.emptyArray;
    Expression     defaultExpression;
    boolean        isNullable = true;

    public UserTypeModifier(HsqlName name, int type, Type dataType) {

        this.name             = name;
        this.schemaObjectType = type;
        this.dataType         = dataType;
    }

    public int schemaObjectType() {
        return schemaObjectType;
    }

    public void addConstraint(Constraint c) {

        int position = constraints.length;

        constraints = (Constraint[]) ArrayUtil.resizeArray(constraints,
                position + 1);
        constraints[position] = c;

        setNotNull();
    }

    public void removeConstraint(String name) {

        for (int i = 0; i < constraints.length; i++) {
            if (constraints[i].getName().name.equals(name)) {
                constraints =
                    (Constraint[]) ArrayUtil.toAdjustedArray(constraints,
                        null, i, -1);

                break;
            }
        }

        setNotNull();
    }

    public Constraint getConstraint(String name) {

        for (int i = 0; i < constraints.length; i++) {
            if (constraints[i].getName().name.equals(name)) {
                return constraints[i];
            }
        }

        return null;
    }

    public Constraint[] getConstraints() {
        return constraints;
    }

    public boolean isNullable() {
        return isNullable;
    }

    public Expression getDefaultClause() {
        return defaultExpression;
    }

    public void setDefaultClause(Expression defaultExpression) {
        this.defaultExpression = defaultExpression;
    }

    public void removeDefaultClause() {
        defaultExpression = null;
    }

    private void setNotNull() {

        isNullable = true;

        for (int i = 0; i < constraints.length; i++) {
            if (constraints[i].isNotNull()) {
                isNullable = false;
            }
        }
    }

    // interface specific methods
    public int getType() {
        return schemaObjectType;
    }

    public HsqlName getName() {
        return name;
    }

    public HsqlName getSchemaName() {
        return name.schema;
    }

    public Grantee getOwner() {
        return name.schema.owner;
    }

    public OrderedHashSet getReferences() {

        OrderedHashSet set = new OrderedHashSet();

        for (int i = 0; i < constraints.length; i++) {
            OrderedHashSet subSet = constraints[i].getReferences();

            if (subSet != null) {
                set.addAll(subSet);
            }
        }

        return set;
    }

    public final OrderedHashSet getComponents() {

        if (constraints == null) {
            return null;
        }

        OrderedHashSet set = new OrderedHashSet();

        set.addAll(constraints);

        return set;
    }

    public void compile(Session session) {

        for (int i = 0; i < constraints.length; i++) {
            constraints[i].compile(session, null);
        }
    }

    public String getSQL() {

        StringBuffer sb = new StringBuffer();

        if (schemaObjectType == SchemaObject.TYPE) {
            sb.append(Tokens.T_CREATE).append(' ').append(
                Tokens.T_TYPE).append(' ');
            sb.append(name.getSchemaQualifiedStatementName());
            sb.append(' ').append(Tokens.T_AS).append(' ');
            sb.append(dataType.getDefinition());
        } else {
            sb.append(Tokens.T_CREATE).append(' ').append(
                Tokens.T_DOMAIN).append(' ');
            sb.append(name.getSchemaQualifiedStatementName());
            sb.append(' ').append(Tokens.T_AS).append(' ');
            sb.append(dataType.getDefinition());

            if (defaultExpression != null) {
                sb.append(' ').append(Tokens.T_DEFAULT).append(' ');
                sb.append(defaultExpression.getSQL());
            }

            for (int i = 0; i < constraints.length; i++) {
                sb.append(' ').append(Tokens.T_CONSTRAINT).append(' ');
                sb.append(constraints[i].getName().statementName).append(' ');
                sb.append(Tokens.T_CHECK).append('(').append(
                    constraints[i].getCheckSQL()).append(')');
            }
        }

        return sb.toString();
    }
}
