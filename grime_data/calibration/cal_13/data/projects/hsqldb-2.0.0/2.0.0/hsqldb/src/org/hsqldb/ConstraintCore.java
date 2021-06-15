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
import org.hsqldb.index.Index;

/**
 * This class consists of the data structure for a Constraint. This
 * structure is shared between two Constraint Objects that together form a
 * foreign key constraint. This simplifies structural modifications to a
 * table. When changes to the column indexes are applied to the table's
 * Constraint Objects, they are reflected in the Constraint Objects of any
 * other table that shares a foreign key constraint with the modified
 * table.
 *
 * New class partly based on Hypersonic code
 *
 * @author Thomas Mueller (Hypersonic SQL Group)
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.7.1
 */
class ConstraintCore {

    // refName and mainName are for foreign keys only
    HsqlName refName;
    HsqlName mainName;
    HsqlName uniqueName;
    HsqlName refTableName;
    HsqlName mainTableName;

    // Main is the sole table in a UNIQUE or PRIMARY constraint
    // Or the table that is referenced by FOREIGN KEY ... REFERENCES
    Table mainTable;
    int[] mainCols;
    Index mainIndex;

    // Ref is the table that has a reference to the main table
    Table   refTable;
    int[]   refCols;
    Index   refIndex;
    int     deleteAction;
    int     updateAction;
    boolean hasUpdateAction;
    boolean hasDeleteAction;
    int     matchType;

    //
    ConstraintCore duplicate() {

        ConstraintCore copy = new ConstraintCore();

        copy.refName      = refName;
        copy.mainName     = mainName;
        copy.uniqueName   = uniqueName;
        copy.mainTable    = mainTable;
        copy.mainCols     = mainCols;
        copy.mainIndex    = mainIndex;
        copy.refTable     = refTable;
        copy.refCols      = refCols;
        copy.refIndex     = refIndex;
        copy.deleteAction = deleteAction;
        copy.updateAction = updateAction;
        copy.matchType    = matchType;

        return copy;
    }
}
