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

import org.hsqldb.HsqlNameManager.HsqlName;
import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.lib.HsqlArrayList;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.rights.Grantee;
import org.hsqldb.types.NumberType;
import org.hsqldb.types.Type;

/**
 * Implementation of SQL procedure and functions
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 *
 * @version 1.9.0
 * @since 1.9.0
 */
public class RoutineSchema implements SchemaObject {

    Routine[]        routines = Routine.emptyArray;
    int              routineType;
    private HsqlName name;

    public RoutineSchema(int type, HsqlName name) {
        routineType = type;
        this.name   = name;
    }

    public int getType() {
        return routineType;
    }

    public HsqlName getCatalogName() {
        return name.schema.schema;
    }

    public HsqlName getSchemaName() {
        return name.schema;
    }

    public HsqlName getName() {
        return name;
    }

    public Grantee getOwner() {
        return name.schema.owner;
    }

    public OrderedHashSet getReferences() {

        OrderedHashSet set = new OrderedHashSet();

        for (int i = 0; i < routines.length; i++) {
            set.addAll(routines[i].getReferences());
        }

        return set;
    }

    public OrderedHashSet getComponents() {

        OrderedHashSet set = new OrderedHashSet();

        set.addAll(routines);

        return set;
    }

    public void compile(Session session, SchemaObject parentObject) {}

    public String getSQL() {
        return null;
    }

    public long getChangeTimestamp() {
        return 0;
    }

    public String[] getSQLArray() {

        HsqlArrayList list = new HsqlArrayList();

        for (int i = 0; i < routines.length; i++) {
            list.add(routines[i].getSQL());
        }

        String[] array = new String[list.size()];

        list.toArray(array);

        return array;
    }

    public void addSpecificRoutine(Database database, Routine routine) {

        int    signature = routine.getParameterSignature();
        Type[] types     = routine.getParameterTypes();

        for (int i = 0; i < this.routines.length; i++) {
            if (routines[i].parameterTypes.length == types.length) {
                if (routineType == SchemaObject.PROCEDURE) {
                    throw Error.error(ErrorCode.X_42605);
                }

                if (routines[i].isAggregate() != routine.isAggregate()) {
                    throw Error.error(ErrorCode.X_42605);
                }

                boolean match = true;

                for (int j = 0; j < types.length; j++) {
                    if (!routines[i].parameterTypes[j].equals(types[j])) {
                        match = false;

                        break;
                    }
                }

                if (match) {
                    throw Error.error(ErrorCode.X_42605);
                }
            }
        }

        if (routine.getSpecificName() == null) {
            HsqlName specificName =
                database.nameManager.newSpecificRoutineName(name);

            routine.setSpecificName(specificName);
        } else {
            routine.getSpecificName().parent = name;
            routine.getSpecificName().schema = name.schema;
        }

        routine.setName(name);

        routine.routineSchema = this;
        routines = (Routine[]) ArrayUtil.resizeArray(routines,
                routines.length + 1);
        routines[routines.length - 1] = routine;
    }

    public void removeSpecificRoutine(Routine routine) {

        for (int i = 0; i < this.routines.length; i++) {
            if (routines[i] == routine) {
                routines = (Routine[]) ArrayUtil.toAdjustedArray(routines,
                        null, i, -1);

                break;
            }
        }
    }

    public Routine[] getSpecificRoutines() {
        return routines;
    }

    public Routine getSpecificRoutine(Type[] types) {

        int matchIndex = -1;

        outerLoop:
        for (int i = 0; i < this.routines.length; i++) {
            int matchCount = 0;

            if (routines[i].isAggregate()) {
                if (types.length == 1) {
                    if (types[0] == null) {
                        return routines[i];
                    }

                    int typeDifference = types[0].precedenceDegree(
                        routines[i].parameterTypes[0]);

                    if (typeDifference < -NumberType.DOUBLE_WIDTH) {
                        if (matchIndex == -1) {
                            continue;
                        }

                        int oldDiff = types[0].precedenceDegree(
                            routines[matchIndex].parameterTypes[0]);
                        int newDiff = types[0].precedenceDegree(
                            routines[i].parameterTypes[0]);

                        if (oldDiff == newDiff) {
                            continue outerLoop;
                        }

                        if (newDiff < oldDiff) {
                            matchIndex = i;
                        }

                        continue outerLoop;
                    } else if (typeDifference == 0) {
                        return routines[i];
                    } else {
                        matchIndex = i;

                        continue outerLoop;
                    }
                }

                // treat routine as non-aggregate
            }

            if (routines[i].parameterTypes.length != types.length) {
                continue;
            }

            if (types.length == 0) {
                return this.routines[i];
            }

            for (int j = 0; j < types.length; j++) {
                int typeDifference;

                // parameters
                if (types[j] == null) {
                    continue;
                }

                typeDifference =
                    types[j].precedenceDegree(routines[i].parameterTypes[j]);

                if (typeDifference < -NumberType.DOUBLE_WIDTH) {

                    // accept numeric type narrowing
                    continue outerLoop;
                } else if (typeDifference == 0) {
                    if (matchCount == j) {
                        matchCount = j + 1;
                    }
                }
            }

            if (matchCount == types.length) {
                return routines[i];
            }

            if (matchIndex == -1) {
                matchIndex = i;

                continue;
            }

            for (int j = 0; j < types.length; j++) {
                if (types[j] == null) {
                    continue;
                }

                int oldDiff = types[j].precedenceDegree(
                    routines[matchIndex].parameterTypes[j]);
                int newDiff =
                    types[j].precedenceDegree(routines[i].parameterTypes[j]);

                if (oldDiff == newDiff) {
                    continue;
                }

                if (newDiff < oldDiff) {
                    matchIndex = i;
                }

                continue outerLoop;
            }
        }

        if (matchIndex < 0) {
            StringBuffer sb = new StringBuffer();

            sb.append(name.getSchemaQualifiedStatementName());
            sb.append(Tokens.T_OPENBRACKET);

            for (int i = 0; i < types.length; i++) {
                if (i != 0) {
                    sb.append(Tokens.T_COMMA);
                }

                sb.append(types[i].getNameString());
            }

            sb.append(Tokens.T_CLOSEBRACKET);

            throw Error.error(ErrorCode.X_42609, sb.toString());
        }

        return routines[matchIndex];
    }

    public Routine getSpecificRoutine(int paramCount) {

        for (int i = 0; i < this.routines.length; i++) {
            if (routines[i].parameterTypes.length == paramCount) {
                return routines[i];
            }
        }

        throw Error.error(ErrorCode.X_42501);
    }

    public boolean isAggregate() {
        return routines[0].isAggregate;
    }
}
