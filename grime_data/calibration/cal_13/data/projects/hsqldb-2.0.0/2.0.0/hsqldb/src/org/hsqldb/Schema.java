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
import org.hsqldb.lib.HashMappedList;
import org.hsqldb.lib.HsqlArrayList;
import org.hsqldb.lib.Iterator;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.lib.WrapperIterator;
import org.hsqldb.rights.Grantee;

public final class Schema implements SchemaObject {

    private HsqlName name;
    SchemaObjectSet  triggerLookup;
    SchemaObjectSet  constraintLookup;
    SchemaObjectSet  indexLookup;
    SchemaObjectSet  tableLookup;
    SchemaObjectSet  sequenceLookup;
    SchemaObjectSet  typeLookup;
    SchemaObjectSet  charsetLookup;
    SchemaObjectSet  collationLookup;
    SchemaObjectSet  procedureLookup;
    SchemaObjectSet  functionLookup;
    SchemaObjectSet  specificRoutineLookup;
    SchemaObjectSet  assertionLookup;
    HashMappedList   tableList;
    HashMappedList   sequenceList;
    Grantee          owner;
    long             changeTimestamp;

    public Schema(HsqlName name, Grantee owner) {

        this.name        = name;
        triggerLookup    = new SchemaObjectSet(SchemaObject.TRIGGER);
        indexLookup      = new SchemaObjectSet(SchemaObject.INDEX);
        constraintLookup = new SchemaObjectSet(SchemaObject.CONSTRAINT);
        tableLookup      = new SchemaObjectSet(SchemaObject.TABLE);
        sequenceLookup   = new SchemaObjectSet(SchemaObject.SEQUENCE);
        typeLookup       = new SchemaObjectSet(SchemaObject.TYPE);
        charsetLookup    = new SchemaObjectSet(SchemaObject.CHARSET);
        collationLookup  = new SchemaObjectSet(SchemaObject.COLLATION);
        procedureLookup  = new SchemaObjectSet(SchemaObject.PROCEDURE);
        functionLookup   = new SchemaObjectSet(SchemaObject.FUNCTION);
        specificRoutineLookup =
            new SchemaObjectSet(SchemaObject.SPECIFIC_ROUTINE);
        assertionLookup = new SchemaObjectSet(SchemaObject.ASSERTION);
        tableList       = (HashMappedList) tableLookup.map;
        sequenceList    = (HashMappedList) sequenceLookup.map;
        this.owner      = owner;
        name.owner      = owner;
    }

    public int getType() {
        return SchemaObject.SCHEMA;
    }

    public HsqlName getName() {
        return name;
    }

    public HsqlName getSchemaName() {
        return null;
    }

    public HsqlName getCatalogName() {
        return null;
    }

    public Grantee getOwner() {
        return owner;
    }

    public OrderedHashSet getReferences() {
        return new OrderedHashSet();
    }

    public OrderedHashSet getComponents() {
        return null;
    }

    public void compile(Session session, SchemaObject parentObject) {}

    public long getChangeTimestamp() {
        return changeTimestamp;
    }

    public String getSQL() {

        StringBuffer sb = new StringBuffer(128);

        sb.append(Tokens.T_CREATE).append(' ');
        sb.append(Tokens.T_SCHEMA).append(' ');
        sb.append(getName().statementName).append(' ');
        sb.append(Tokens.T_AUTHORIZATION).append(' ');
        sb.append(getOwner().getStatementName());

        return sb.toString();
    }

    static String getSetSchemaSQL(HsqlName schemaName) {

        StringBuffer sb = new StringBuffer();

        sb.append(Tokens.T_SET).append(' ');
        sb.append(Tokens.T_SCHEMA).append(' ');
        sb.append(schemaName.statementName);

        return sb.toString();
    }

    public String[] getSQLArray(OrderedHashSet resolved,
                                OrderedHashSet unresolved) {

        HsqlArrayList list      = new HsqlArrayList();
        String        setSchema = getSetSchemaSQL(name);

        list.add(setSchema);

        //
        String[] subList;

        subList = sequenceLookup.getSQL(resolved, unresolved);

        list.addAll(subList);

        subList = tableLookup.getSQL(resolved, unresolved);

        list.addAll(subList);

        subList = functionLookup.getSQL(resolved, unresolved);

        list.addAll(subList);

        subList = procedureLookup.getSQL(resolved, unresolved);

        list.addAll(subList);

        subList = assertionLookup.getSQL(resolved, unresolved);

        list.addAll(subList);

//
        if (list.size() == 1) {
            return new String[]{};
        }

        String[] array = new String[list.size()];

        list.toArray(array);

        return array;
    }

    public String[] getSequenceRestartSQL() {

        HsqlArrayList list = new HsqlArrayList();
        Iterator      it   = sequenceLookup.map.values().iterator();

        while (it.hasNext()) {
            NumberSequence sequence = (NumberSequence) it.next();
            String         ddl      = sequence.getRestartSQL();

            list.add(ddl);
        }

        String[] array = new String[list.size()];

        list.toArray(array);

        return array;
    }

    public String[] getTriggerSQL() {

        HsqlArrayList list = new HsqlArrayList();
        Iterator      it   = tableLookup.map.values().iterator();

        while (it.hasNext()) {
            Table    table = (Table) it.next();
            String[] ddl   = table.getTriggerSQL();

            list.addAll(ddl);
        }

        String[] array = new String[list.size()];

        list.toArray(array);

        return array;
    }

    public void addSimpleObjects(OrderedHashSet unresolved) {

        Iterator it = specificRoutineLookup.map.values().iterator();

        while (it.hasNext()) {
            Routine routine = (Routine) it.next();

            if (routine.dataImpact == Routine.NO_SQL
                    || routine.dataImpact == Routine.CONTAINS_SQL) {
                unresolved.add(routine);
            }
        }

        unresolved.addAll(typeLookup.map.values());
        unresolved.addAll(charsetLookup.map.values());
        unresolved.addAll(collationLookup.map.values());
    }

    boolean isEmpty() {

        return sequenceLookup.isEmpty() && tableLookup.isEmpty()
               && typeLookup.isEmpty() && charsetLookup.isEmpty()
               && collationLookup.isEmpty() && specificRoutineLookup.isEmpty();
    }

    public SchemaObjectSet getObjectSet(int type) {

        switch (type) {

            case SchemaObject.SEQUENCE :
                return sequenceLookup;

            case SchemaObject.TABLE :
            case SchemaObject.VIEW :
                return tableLookup;

            case SchemaObject.CHARSET :
                return charsetLookup;

            case SchemaObject.COLLATION :
                return collationLookup;

            case SchemaObject.PROCEDURE :
                return procedureLookup;

            case SchemaObject.FUNCTION :
                return functionLookup;

            case SchemaObject.ROUTINE :
                return functionLookup;

            case SchemaObject.SPECIFIC_ROUTINE :
                return specificRoutineLookup;

            case SchemaObject.DOMAIN :
            case SchemaObject.TYPE :
                return typeLookup;

            case SchemaObject.ASSERTION :
                return assertionLookup;

            case SchemaObject.TRIGGER :
                return triggerLookup;

            case SchemaObject.INDEX :
                return indexLookup;

            case SchemaObject.CONSTRAINT :
                return constraintLookup;

            default :
                throw Error.runtimeError(ErrorCode.U_S0500, "Schema");
        }
    }

    Iterator schemaObjectIterator(int type) {

        switch (type) {

            case SchemaObject.SEQUENCE :
                return sequenceLookup.map.values().iterator();

            case SchemaObject.TABLE :
            case SchemaObject.VIEW :
                return tableLookup.map.values().iterator();

            case SchemaObject.CHARSET :
                return charsetLookup.map.values().iterator();

            case SchemaObject.COLLATION :
                return collationLookup.map.values().iterator();

            case SchemaObject.PROCEDURE :
                return procedureLookup.map.values().iterator();

            case SchemaObject.FUNCTION :
                return functionLookup.map.values().iterator();

            case SchemaObject.ROUTINE :
                Iterator functions = functionLookup.map.values().iterator();

                return new WrapperIterator(
                    functions, procedureLookup.map.values().iterator());

            case SchemaObject.SPECIFIC_ROUTINE :
                return specificRoutineLookup.map.values().iterator();

            case SchemaObject.DOMAIN :
            case SchemaObject.TYPE :
                return typeLookup.map.values().iterator();

            case SchemaObject.ASSERTION :
                return assertionLookup.map.values().iterator();

            case SchemaObject.TRIGGER :
                return triggerLookup.map.values().iterator();

            case SchemaObject.INDEX :
                return indexLookup.map.values().iterator();

            case SchemaObject.CONSTRAINT :
                return constraintLookup.map.values().iterator();

            default :
                throw Error.runtimeError(ErrorCode.U_S0500, "Schema");
        }
    }

    void clearStructures() {

        tableList.clear();
        sequenceList.clear();

        triggerLookup    = null;
        indexLookup      = null;
        constraintLookup = null;
        procedureLookup  = null;
        functionLookup   = null;
        sequenceLookup   = null;
        tableLookup      = null;
        typeLookup       = null;
    }
}
