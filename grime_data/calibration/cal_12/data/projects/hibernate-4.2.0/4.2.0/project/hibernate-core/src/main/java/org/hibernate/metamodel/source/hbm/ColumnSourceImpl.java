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
package org.hibernate.metamodel.source.hbm;

import org.hibernate.internal.jaxb.mapping.hbm.JaxbColumnElement;
import org.hibernate.metamodel.relational.Datatype;
import org.hibernate.metamodel.relational.Size;
import org.hibernate.metamodel.source.binder.ColumnSource;

/**
* @author Steve Ebersole
*/
class ColumnSourceImpl implements ColumnSource {
	private final String tableName;
	private final JaxbColumnElement columnElement;
	private boolean includedInInsert;
	private boolean includedInUpdate;
    private final boolean isForceNotNull;

	ColumnSourceImpl(
			String tableName,
			JaxbColumnElement columnElement,
			boolean isIncludedInInsert,
			boolean isIncludedInUpdate) {
		this(tableName, columnElement, isIncludedInInsert, isIncludedInUpdate, false);
	}
    ColumnSourceImpl(
            String tableName,
            JaxbColumnElement columnElement,
            boolean isIncludedInInsert,
            boolean isIncludedInUpdate,
            boolean isForceNotNull) {
        this.tableName = tableName;
        this.columnElement = columnElement;
        this.isForceNotNull = isForceNotNull;
        includedInInsert = isIncludedInInsert;
        includedInUpdate = isIncludedInUpdate;
    }

	@Override
	public String getName() {
		return columnElement.getName();
	}

	@Override
	public boolean isNullable() {
        if(isForceNotNull)return false;
		return ! columnElement.isNotNull();
	}

	@Override
	public String getDefaultValue() {
		return columnElement.getDefault();
	}

	@Override
	public String getSqlType() {
		return columnElement.getSqlType();
	}

	@Override
	public Datatype getDatatype() {
		return null;
	}

	@Override
	public Size getSize() {
		return new Size(
				Helper.getIntValue( columnElement.getPrecision(), -1 ),
				Helper.getIntValue( columnElement.getScale(), -1 ),
				Helper.getLongValue( columnElement.getLength(), -1 ),
				Size.LobMultiplier.NONE
		);
	}

	@Override
	public String getReadFragment() {
		return columnElement.getRead();
	}

	@Override
	public String getWriteFragment() {
		return columnElement.getWrite();
	}

	@Override
	public boolean isUnique() {
		return columnElement.isUnique();
	}

	@Override
	public String getCheckCondition() {
		return columnElement.getCheck();
	}

	@Override
	public String getComment() {
		return columnElement.getComment();
	}

	@Override
	public boolean isIncludedInInsert() {
		return includedInInsert;
	}

	@Override
	public boolean isIncludedInUpdate() {
		return includedInUpdate;
	}

	@Override
	public String getContainingTableName() {
		return tableName;
	}
}
