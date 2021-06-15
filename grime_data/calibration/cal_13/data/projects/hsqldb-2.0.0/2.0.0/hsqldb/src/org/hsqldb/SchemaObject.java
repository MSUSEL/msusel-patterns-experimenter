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
import org.hsqldb.rights.Grantee;
import org.hsqldb.lib.OrderedHashSet;

/**
 * SQL schema object interface
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public interface SchemaObject {

    int CATALOG          = 1;
    int SCHEMA           = 2;
    int TABLE            = 3;
    int VIEW             = 4;
    int CONSTRAINT       = 5;
    int ASSERTION        = 6;
    int SEQUENCE         = 7;
    int TRIGGER          = 8;
    int COLUMN           = 9;
    int TRANSITION       = 10;
    int GRANTEE          = 11;
    int TYPE             = 12;
    int DOMAIN           = 13;
    int CHARSET          = 14;
    int COLLATION        = 15;
    int FUNCTION         = 16;
    int PROCEDURE        = 17;
    int ROUTINE          = 18;
    int CURSOR           = 19;
    int INDEX            = 20;
    int LABEL            = 21;
    int VARIABLE         = 22;
    int PARAMETER        = 23;
    int SPECIFIC_ROUTINE = 24;
    int WRAPPER          = 25;
    int SERVER           = 26;
    int SUBQUERY         = 27;

    //
    SchemaObject[] emptyArray = new SchemaObject[]{};

    int getType();

    HsqlName getName();

    HsqlName getSchemaName();

    HsqlName getCatalogName();

    Grantee getOwner();

    OrderedHashSet getReferences();

    OrderedHashSet getComponents();

    void compile(Session session, SchemaObject parentObject);

    String getSQL();

    long getChangeTimestamp();

    interface ConstraintTypes {

        int FOREIGN_KEY = 0;
        int MAIN        = 1;
        int UNIQUE      = 2;
        int CHECK       = 3;
        int PRIMARY_KEY = 4;
        int TEMP        = 5;
    }

    /*
     SQL CLI codes

     Referential Constraint 0 CASCADE
     Referential Constraint 1 RESTRICT
     Referential Constraint 2 SET NULL
     Referential Constraint 3 NO ACTION
     Referential Constraint 4 SET DEFAULT
     */
    interface ReferentialAction {

        int CASCADE     = 0;
        int RESTRICT    = 1;
        int SET_NULL    = 2;
        int NO_ACTION   = 3;
        int SET_DEFAULT = 4;
    }

    interface Deferable {

        int NOT_DEFERRABLE = 0;
        int INIT_DEFERRED  = 1;
        int INIT_IMMEDIATE = 2;
    }

    interface ViewCheckModes {

        int CHECK_NONE    = 0;
        int CHECK_LOCAL   = 1;
        int CHECK_CASCADE = 2;
    }

    interface ParameterModes {

        byte PARAM_UNKNOWN = 0;    // java.sql.ParameterMetaData.parameterModeUnknown
        byte PARAM_IN    = 1;      // java.sql.ParameterMetaData.parameterModeIn
        byte PARAM_OUT   = 4;      // java.sql.ParameterMetaData.parameterModeInOut
        byte PARAM_INOUT = 2;      // java.sql.ParameterMetaData.parameterModeOut
    }

    interface Nullability {

        byte NO_NULLS         = 0;    // java.sql.ResultSetMetaData.columnNoNulls
        byte NULLABLE         = 1;    // java.sql.ResultSetMetaData.columnNullable
        byte NULLABLE_UNKNOWN = 2;    // java.sql.ResultSetMetaData.columnNullableUnknown
    }
}
