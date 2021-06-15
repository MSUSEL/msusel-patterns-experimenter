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

import org.hsqldb.types.Type;

/**
 * Base implementation of variables, columns of result or table.<p>
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class ColumnBase {

    private String    name;
    private String    table;
    private String    schema;
    private String    catalog;
    private boolean   isWriteable;
    private boolean   isSearchable;
    protected byte     parameterMode;
    protected boolean isIdentity;
    protected byte     nullability;
    protected Type    dataType;

    ColumnBase() {}

    public ColumnBase(String catalog, String schema, String table,
                      String name) {

        this.catalog = catalog;
        this.schema  = schema;
        this.table   = table;
        this.name    = name;
    }

    public String getNameString() {
        return name;
    }

    public String getTableNameString() {
        return table;
    }

    public String getSchemaNameString() {
        return schema;
    }

    public String getCatalogNameString() {
        return catalog;
    }

    public void setIdentity(boolean value) {
        isIdentity = value;
    }

    public boolean isIdentity() {
        return isIdentity;
    }

    protected void setType(ColumnBase other) {
        nullability = other.nullability;
        dataType    = other.dataType;
    }

    public void setType(Type type) {
        this.dataType = type;
    }

    public boolean isNullable() {
        return !isIdentity && nullability == SchemaObject.Nullability.NULLABLE;
    }

    protected void setNullable(boolean value) {
        nullability = value ? SchemaObject.Nullability.NULLABLE
                            : SchemaObject.Nullability.NO_NULLS;
    }

    public byte getNullability() {
        return isIdentity ? SchemaObject.Nullability.NO_NULLS
                          : nullability;
    }

    public void setNullability(byte value) {
        nullability = value;
    }

    public boolean isWriteable() {
        return isWriteable;
    }

    public boolean isSearchable() {
        return isSearchable;
    }

    public void setWriteable(boolean value) {
        isWriteable = value;
    }

    public Type getDataType() {
        return dataType;
    }

    public byte getParameterMode() {
        return parameterMode;
    }

    public void setParameterMode(byte mode) {
        this.parameterMode = mode;
    }
}
