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
import org.hsqldb.lib.HashMap;
import org.hsqldb.lib.HashMappedList;
import org.hsqldb.lib.HsqlArrayList;
import org.hsqldb.lib.Iterator;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.store.ValuePool;

/**
 * Collection of SQL schema objects of a specific type in a schema
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class SchemaObjectSet {

    HashMap       map;
    int           type;

    SchemaObjectSet(int type) {

        this.type = type;

        switch (type) {

            case SchemaObject.VIEW :
            case SchemaObject.TABLE :
            case SchemaObject.SEQUENCE :
            case SchemaObject.CHARSET :
            case SchemaObject.DOMAIN :
            case SchemaObject.TYPE :
            case SchemaObject.COLLATION :
            case SchemaObject.PROCEDURE :
            case SchemaObject.FUNCTION :
            case SchemaObject.SPECIFIC_ROUTINE :
            case SchemaObject.ASSERTION :
            case SchemaObject.TRIGGER :
                map = new HashMappedList();
                break;

            case SchemaObject.COLUMN :
            case SchemaObject.CONSTRAINT :
            case SchemaObject.INDEX :
                map = new HashMap();
                break;
        }
    }

    HsqlName getName(String name) {

        switch (type) {

            case SchemaObject.VIEW :
            case SchemaObject.TABLE :
            case SchemaObject.SEQUENCE :
            case SchemaObject.CHARSET :
            case SchemaObject.DOMAIN :
            case SchemaObject.TYPE :
            case SchemaObject.COLLATION :
            case SchemaObject.PROCEDURE :
            case SchemaObject.SPECIFIC_ROUTINE :
            case SchemaObject.FUNCTION :
            case SchemaObject.ASSERTION :
            case SchemaObject.TRIGGER :
                SchemaObject object = ((SchemaObject) map.get(name));

                return object == null ? null
                                      : object.getName();

            case SchemaObject.COLUMN :
            case SchemaObject.CONSTRAINT :
            case SchemaObject.INDEX : {
                return (HsqlName) map.get(name);
            }
            default :
                return (HsqlName) map.get(name);
        }
    }

    public SchemaObject getObject(String name) {

        switch (type) {

            case SchemaObject.VIEW :
            case SchemaObject.TABLE :
            case SchemaObject.SEQUENCE :
            case SchemaObject.CHARSET :
            case SchemaObject.DOMAIN :
            case SchemaObject.TYPE :
            case SchemaObject.COLLATION :
            case SchemaObject.PROCEDURE :
            case SchemaObject.SPECIFIC_ROUTINE :
            case SchemaObject.FUNCTION :
            case SchemaObject.ASSERTION :
            case SchemaObject.TRIGGER :
            case SchemaObject.COLUMN :
                return (SchemaObject) map.get(name);

            default :
                throw Error.runtimeError(ErrorCode.U_S0500, "SchemaObjectSet");
        }
    }

    public boolean contains(String name) {
        return map.containsKey(name);
    }

    void checkAdd(HsqlName name) {

        if (map.containsKey(name.name)) {
            int code = getAddErrorCode(name.type);

            throw Error.error(code, name.name);
        }
    }

    boolean isEmpty() {
        return map.isEmpty();
    }

    void checkExists(String name) {

        if (!map.containsKey(name)) {
            int code = getGetErrorCode(type);

            throw Error.error(code, name);
        }
    }

    public void add(SchemaObject object) {

        HsqlName name = object.getName();

        if (type == SchemaObject.SPECIFIC_ROUTINE) {
            name = ((Routine) object).getSpecificName();
        }

        if (map.containsKey(name.name)) {
            int code = getAddErrorCode(name.type);

            throw Error.error(code, name.name);
        }

        Object value = object;

        switch (name.type) {

            case SchemaObject.CONSTRAINT :
            case SchemaObject.INDEX :
                value = name;
        }

        map.put(name.name, value);
    }

    void remove(String name) {
        map.remove(name);
    }

    void removeParent(HsqlName parent) {

        Iterator it = map.values().iterator();

        while (it.hasNext()) {
            if (type == SchemaObject.TRIGGER
                    || type == SchemaObject.SPECIFIC_ROUTINE) {
                SchemaObject object = (SchemaObject) it.next();

                if (object.getName().parent == parent) {
                    it.remove();
                }
            } else {
                HsqlName name = (HsqlName) it.next();

                if (name.parent == parent) {
                    it.remove();
                }
            }
        }
    }

    void rename(HsqlName name, HsqlName newName) {

        if (map.containsKey(newName.name)) {
            int code = getAddErrorCode(name.type);

            throw Error.error(code, newName.name);
        }

        switch (newName.type) {

            case SchemaObject.VIEW :
            case SchemaObject.TABLE :
            case SchemaObject.SEQUENCE :
            case SchemaObject.CHARSET :
            case SchemaObject.COLLATION :
            case SchemaObject.PROCEDURE :
            case SchemaObject.FUNCTION :
            case SchemaObject.DOMAIN :
            case SchemaObject.TYPE :
            case SchemaObject.ASSERTION :
            case SchemaObject.TRIGGER : {
                int i = ((HashMappedList) map).getIndex(name.name);

                if (i == -1) {
                    int code = getGetErrorCode(name.type);

                    throw Error.error(code, name.name);
                }

                SchemaObject object =
                    (SchemaObject) ((HashMappedList) map).get(i);

                object.getName().rename(newName);
                ((HashMappedList) map).setKey(i, name.name);

                break;
            }
            case SchemaObject.COLUMN :
            case SchemaObject.CONSTRAINT :
            case SchemaObject.INDEX : {
                map.remove(name.name);
                name.rename(newName);
                map.put(name.name, name);

                break;
            }
        }
    }

    static int getAddErrorCode(int type) {

        int code;

        switch (type) {

            case SchemaObject.VIEW :
            case SchemaObject.TABLE :
            case SchemaObject.COLUMN :
            case SchemaObject.SEQUENCE :
            case SchemaObject.CHARSET :
            case SchemaObject.DOMAIN :
            case SchemaObject.TYPE :
            case SchemaObject.COLLATION :
            case SchemaObject.PROCEDURE :
            case SchemaObject.FUNCTION :
            case SchemaObject.SPECIFIC_ROUTINE :
            case SchemaObject.CONSTRAINT :
            case SchemaObject.ASSERTION :
            case SchemaObject.INDEX :
            case SchemaObject.TRIGGER :
                code = ErrorCode.X_42504;
                break;

            default :
                throw Error.runtimeError(ErrorCode.U_S0500, "SchemaObjectSet");
        }

        return code;
    }

    static int getGetErrorCode(int type) {

        int code;

        switch (type) {

            case SchemaObject.VIEW :
            case SchemaObject.TABLE :
            case SchemaObject.COLUMN :
            case SchemaObject.SEQUENCE :
            case SchemaObject.CHARSET :
            case SchemaObject.DOMAIN :
            case SchemaObject.TYPE :
            case SchemaObject.CONSTRAINT :
            case SchemaObject.COLLATION :
            case SchemaObject.PROCEDURE :
            case SchemaObject.FUNCTION :
            case SchemaObject.SPECIFIC_ROUTINE :
            case SchemaObject.ASSERTION :
            case SchemaObject.INDEX :
            case SchemaObject.TRIGGER :
                code = ErrorCode.X_42501;
                break;

            default :
                throw Error.runtimeError(ErrorCode.U_S0500, "SchemaObjectSet");
        }

        return code;
    }

    public static String getName(int type) {

        switch (type) {

            case SchemaObject.VIEW :
                return Tokens.T_VIEW;

            case SchemaObject.COLUMN :
                return Tokens.T_COLUMN;

            case SchemaObject.TABLE :
                return Tokens.T_TABLE;

            case SchemaObject.SEQUENCE :
                return Tokens.T_SEQUENCE;

            case SchemaObject.CHARSET :
                return Tokens.T_CHARACTER + ' ' + Tokens.T_SET;

            case SchemaObject.DOMAIN :
                return Tokens.T_DOMAIN;

            case SchemaObject.TYPE :
                return Tokens.T_TYPE;

            case SchemaObject.CONSTRAINT :
                return Tokens.T_CONSTRAINT;

            case SchemaObject.COLLATION :
                return Tokens.T_COLLATION;

            case SchemaObject.PROCEDURE :
                return Tokens.T_PROCEDURE;

            case SchemaObject.FUNCTION :
                return Tokens.T_FUNCTION;

            case SchemaObject.ASSERTION :
                return Tokens.T_ASSERTION;

            case SchemaObject.INDEX :
                return Tokens.T_INDEX;

            case SchemaObject.TRIGGER :
                return Tokens.T_TRIGGER;

            default :
                throw Error.runtimeError(ErrorCode.U_S0500, "SchemaObjectSet");
        }
    }

    String[] getSQL(OrderedHashSet resolved, OrderedHashSet unresolved) {

        HsqlArrayList list = new HsqlArrayList();

        if (!(map instanceof HashMappedList)) {
            return null;
        }

        if (map.isEmpty()) {
            return ValuePool.emptyStringArray;
        }

        Iterator it = map.values().iterator();

        if (type == SchemaObject.FUNCTION || type == SchemaObject.PROCEDURE) {
            OrderedHashSet set = new OrderedHashSet();

            while (it.hasNext()) {
                RoutineSchema routineSchema = (RoutineSchema) it.next();

                for (int i = 0; i < routineSchema.routines.length; i++) {
                    Routine routine = routineSchema.routines[i];

                    if (routine.dataImpact == Routine.NO_SQL
                            || routine.dataImpact == Routine.CONTAINS_SQL) {}
                    else {
                        set.add(routine);
                    }
                }
            }

            it = set.iterator();
        }

        addAllSQL(resolved, unresolved, list, it, null);

        String[] array = new String[list.size()];

        list.toArray(array);

        return array;
    }

    static void addAllSQL(OrderedHashSet resolved, OrderedHashSet unresolved,
                          HsqlArrayList list, Iterator it,
                          OrderedHashSet newResolved) {

        while (it.hasNext()) {
            SchemaObject   object     = (SchemaObject) it.next();
            OrderedHashSet references = object.getReferences();
            boolean        isResolved = true;

            for (int j = 0; j < references.size(); j++) {
                HsqlName name = (HsqlName) references.get(j);

                if (SqlInvariants.isSchemaNameSystem(name)) {
                    continue;
                }

                switch (name.type) {

                    case SchemaObject.TABLE :
                        if (!resolved.contains(name)) {
                            isResolved = false;
                        }
                        break;

                    case SchemaObject.COLUMN : {
                        if (object.getType() == SchemaObject.TABLE) {
                            int index = ((Table) object).findColumn(name.name);
                            ColumnSchema column =
                                ((Table) object).getColumn(index);

                            if (!isChildObjectResolved(column, resolved)) {
                                isResolved = false;
                            }

                            break;
                        }

                        if (!resolved.contains(name.parent)) {
                            isResolved = false;
                        }

                        break;
                    }
                    case SchemaObject.CONSTRAINT : {
                        if (name.parent == object.getName()) {
                            Constraint constraint =
                                ((Table) object).getConstraint(name.name);

                            if (constraint.getConstraintType()
                                    == SchemaObject.ConstraintTypes.CHECK) {
                                if (!isChildObjectResolved(constraint,
                                                           resolved)) {
                                    isResolved = false;
                                }
                            }
                        }

                        // only UNIQUE constraint referenced by FK in table
                        break;
                    }
                    case SchemaObject.CHARSET :
                        if (name.schema == null) {
                            continue;
                        }
                    case SchemaObject.TYPE :
                    case SchemaObject.DOMAIN :
                    case SchemaObject.FUNCTION :
                    case SchemaObject.PROCEDURE :
                    case SchemaObject.SPECIFIC_ROUTINE :
                        if (!resolved.contains(name)) {
                            isResolved = false;
                        }
                    default :
                }
            }

            if (!isResolved) {
                unresolved.add(object);

                continue;
            }

            HsqlName name;

            if (object.getType() == SchemaObject.FUNCTION) {
                name = ((Routine) object).getSpecificName();
            } else {
                name = object.getName();
            }

            resolved.add(name);

            if (newResolved != null) {
                newResolved.add(object);
            }

            if (object.getType() == SchemaObject.TABLE) {
                list.addAll(((Table) object).getSQL(resolved, unresolved));
            } else {
                list.add(object.getSQL());
            }
        }
    }

    static boolean isChildObjectResolved(SchemaObject object,
                                         OrderedHashSet resolved) {

        OrderedHashSet refs = object.getReferences();

        for (int i = 0; i < refs.size(); i++) {
            HsqlName name = (HsqlName) refs.get(i);

            if (SqlInvariants.isSchemaNameSystem(name)) {
                continue;
            }

            if (!resolved.contains(name)) {
                return false;
            }
        }

        return true;
    }
}
