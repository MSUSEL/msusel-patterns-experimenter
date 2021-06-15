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
package org.hibernate.metamodel.relational;

import org.jboss.logging.Logger;

import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.metamodel.ValidationException;

/**
 * Basic support for {@link SimpleValue} implementations.
 *
 * @author Steve Ebersole
 */
public abstract class AbstractSimpleValue implements SimpleValue {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, AbstractSimpleValue.class.getName());

	private final TableSpecification table;
	private final int position;
	private Datatype datatype;

	protected AbstractSimpleValue(TableSpecification table, int position) {
		this.table = table;
		this.position = position;
	}

	@Override
	public TableSpecification getTable() {
		return table;
	}

	public int getPosition() {
		return position;
	}

	@Override
	public Datatype getDatatype() {
		return datatype;
	}

	@Override
	public void setDatatype(Datatype datatype) {
		LOG.debugf( "setting datatype for column %s : %s", toLoggableString(), datatype );
		if ( this.datatype != null && ! this.datatype.equals( datatype ) ) {
			LOG.debugf( "overriding previous datatype : %s", this.datatype );
		}
		this.datatype = datatype;
	}

	@Override
	public void validateJdbcTypes(JdbcCodes typeCodes) {
		// todo : better compatibility testing...
		if ( datatype.getTypeCode() != typeCodes.nextJdbcCde() ) {
			throw new ValidationException( "Mismatched types" );
		}
	}
}
