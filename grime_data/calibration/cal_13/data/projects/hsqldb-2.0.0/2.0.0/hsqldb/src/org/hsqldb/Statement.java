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
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.result.Result;
import org.hsqldb.result.ResultMetaData;
import org.hsqldb.result.ResultProperties;

/**
 * Base class for compiled statement objects.<p>
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public abstract class Statement {

    final static int META_RESET_VIEWS      = 1;
    final static int META_RESET_STATEMENTS = 2;

    //
    final static Statement[] emptyArray = new Statement[]{};

    //
    final int type;
    int       group;
    boolean   isLogged = true;
    boolean   isValid  = true;

    /** the default schema name used to resolve names in the sql */
    HsqlName schemaName;

    /** root in PSM */
    Routine root;

    /** parent in PSM */
    StatementCompound parent;
    boolean           isError;
    boolean           isTransactionStatement;
    boolean           isExplain;
    int               metaDataImpact;

    /** SQL string for the statement */
    String sql;

    /** id in StatementManager */
    long id;

    /** compileTimestamp */
    long compileTimestamp;

    /** table names read - for concurrency control */
    HsqlName[] readTableNames = HsqlName.emptyArray;

    /** table names written - for concurrency control */
    HsqlName[] writeTableNames = HsqlName.emptyArray;;

    //
    OrderedHashSet references;

    public abstract Result execute(Session session);

    public void setParameters(ExpressionColumn[] params) {}

    Statement(int type) {
        this.type = type;
    }

    Statement(int type, int group) {
        this.type  = type;
        this.group = group;
    }

    public final boolean isError() {
        return isError;
    }

    public boolean isTransactionStatement() {
        return isTransactionStatement;
    }

    public boolean isAutoCommitStatement() {
        return false;
    }

    public void setCompileTimestamp(long ts) {
        compileTimestamp = ts;
    }

    public long getCompileTimestamp() {
        return compileTimestamp;
    }

    public final void setSQL(String sql) {
        this.sql = sql;
    }

    public String getSQL() {
        return sql;
    }

    public OrderedHashSet getReferences() {
        return references;
    }

    public final void setDescribe() {
        isExplain = true;
    }

    public abstract String describe(Session session);

    public HsqlName getSchemaName() {
        return schemaName;
    }

    public final void setSchemaHsqlName(HsqlName name) {
        schemaName = name;
    }

    public final void setID(long csid) {
        id = csid;
    }

    public final long getID() {
        return id;
    }

    public final int getType() {
        return type;
    }

    public final int getGroup() {
        return group;
    }

    public final boolean isValid() {
        return isValid;
    }

    public final boolean isLogged() {
        return isLogged;
    }

    public void clearVariables() {}

    public void resolve(Session session) {}

    public RangeVariable[] getRangeVariables() {
        return RangeVariable.emptyArray;
    }

    public final HsqlName[] getTableNamesForRead() {
        return readTableNames;
    }

    public final HsqlName[] getTableNamesForWrite() {
        return writeTableNames;
    }

    public boolean isCatalogChange() {

        switch (group) {

            case StatementTypes.X_SQL_SCHEMA_DEFINITION :
            case StatementTypes.X_SQL_SCHEMA_MANIPULATION :
            case StatementTypes.X_HSQLDB_SCHEMA_MANIPULATION :
                return true;

            case StatementTypes.X_HSQLDB_DATABASE_OPERATION :
                return true;

            default :
                return false;
        }
    }

    public void setParent(StatementCompound statement) {
        this.parent = statement;
    }

    public void setRoot(Routine root) {
        this.root = root;
    }

    public boolean hasGeneratedColumns() {
        return false;
    }

    public ResultMetaData generatedResultMetaData() {
        return null;
    }

    public void setGeneratedColumnInfo(int mode, ResultMetaData meta) {}

    public ResultMetaData getResultMetaData() {
        return ResultMetaData.emptyResultMetaData;
    }

    public ResultMetaData getParametersMetaData() {
        return ResultMetaData.emptyParamMetaData;
    }

    public int getResultProperties() {
        return ResultProperties.defaultPropsValue;
    }
}
