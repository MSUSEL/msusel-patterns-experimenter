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
package org.hibernate.envers.query.criteria;

import org.hibernate.envers.query.projection.AuditProjection;
import org.hibernate.envers.query.projection.PropertyAuditProjection;
import org.hibernate.envers.query.property.EntityPropertyName;
import org.hibernate.envers.query.property.OriginalIdPropertyName;
import org.hibernate.envers.query.property.PropertyNameGetter;

/**
 * Create restrictions and projections for the id of an audited entity.
 *
 * @author Adam Warski (adam at warski dot org)
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@SuppressWarnings({ "JavaDoc" })
public class AuditId<T> extends AuditProperty<T> {
	public static final String IDENTIFIER_PLACEHOLDER = "$$id$$";
	private static final PropertyNameGetter identifierPropertyGetter = new EntityPropertyName( IDENTIFIER_PLACEHOLDER );

	public AuditId() {
		super( identifierPropertyGetter );
	}

	/**
	 * Apply an "equal" constraint
	 */
	public AuditCriterion eq(Object id) {
		return new IdentifierEqAuditExpression( id, true );
	}

	/**
	 * Apply a "not equal" constraint
	 */
	public AuditCriterion ne(Object id) {
		return new IdentifierEqAuditExpression( id, false );
	}

	// Projections

	/**
	 * Projection counting the values
	 *
	 * @param idPropertyName Name of the identifier property
	 *
	 * @deprecated Use {@link #count()}.
	 */
	public AuditProjection count(String idPropertyName) {
		return new PropertyAuditProjection( new OriginalIdPropertyName( idPropertyName ), "count", false );
	}

	@Override
	public AuditCriterion hasChanged() {
		throw new UnsupportedOperationException();
	}

	@Override
	public AuditCriterion hasNotChanged() {
		throw new UnsupportedOperationException();
	}
}