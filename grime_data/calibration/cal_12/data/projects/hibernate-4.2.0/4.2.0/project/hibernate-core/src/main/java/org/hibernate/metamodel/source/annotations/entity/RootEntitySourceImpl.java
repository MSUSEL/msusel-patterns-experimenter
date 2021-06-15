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
package org.hibernate.metamodel.source.annotations.entity;

import org.hibernate.AssertionFailure;
import org.hibernate.EntityMode;
import org.hibernate.cfg.NotYetImplementedException;
import org.hibernate.engine.OptimisticLockStyle;
import org.hibernate.metamodel.binding.Caching;
import org.hibernate.metamodel.source.annotations.attribute.BasicAttribute;
import org.hibernate.metamodel.source.annotations.attribute.DiscriminatorSourceImpl;
import org.hibernate.metamodel.source.annotations.attribute.SimpleIdentifierSourceImpl;
import org.hibernate.metamodel.source.annotations.attribute.SingularAttributeSourceImpl;
import org.hibernate.metamodel.source.binder.DiscriminatorSource;
import org.hibernate.metamodel.source.binder.IdentifierSource;
import org.hibernate.metamodel.source.binder.RootEntitySource;
import org.hibernate.metamodel.source.binder.SingularAttributeSource;

/**
 * @author Hardy Ferentschik
 */
public class RootEntitySourceImpl extends EntitySourceImpl implements RootEntitySource {
	public RootEntitySourceImpl(EntityClass entityClass) {
		super( entityClass );
	}

	@Override
	public IdentifierSource getIdentifierSource() {
		IdType idType = getEntityClass().getIdType();
		switch ( idType ) {
			case SIMPLE: {
				BasicAttribute attribute = getEntityClass().getIdAttributes().iterator().next();
				return new SimpleIdentifierSourceImpl( attribute, getEntityClass().getAttributeOverrideMap() );
			}
			case COMPOSED: {
				throw new NotYetImplementedException( "Composed ids must still be implemented." );
			}
			case EMBEDDED: {
				throw new NotYetImplementedException( "Embedded ids must still be implemented." );
			}
			default: {
				throw new AssertionFailure( "The root entity needs to specify an identifier" );
			}
		}
	}

	@Override
	public SingularAttributeSource getVersioningAttributeSource() {
		SingularAttributeSource attributeSource = null;
		EntityClass entityClass = getEntityClass();
		if ( entityClass.getVersionAttribute() != null ) {
			attributeSource = new SingularAttributeSourceImpl( entityClass.getVersionAttribute() );
		}
		return attributeSource;
	}

	@Override
	public DiscriminatorSource getDiscriminatorSource() {
		DiscriminatorSource discriminatorSource = null;
		if ( getEntityClass().getDiscriminatorColumnValues() != null ) {
			discriminatorSource = new DiscriminatorSourceImpl( getEntityClass() );
		}
		return discriminatorSource;
	}

	@Override
	public EntityMode getEntityMode() {
		return EntityMode.POJO;
	}

	@Override
	public boolean isMutable() {
		return getEntityClass().isMutable();
	}

	@Override
	public boolean isExplicitPolymorphism() {
		return getEntityClass().isExplicitPolymorphism();
	}

	@Override
	public String getWhere() {
		return getEntityClass().getWhereClause();
	}

	@Override
	public String getRowId() {
		return getEntityClass().getRowId();
	}

	@Override
	public OptimisticLockStyle getOptimisticLockStyle() {
		return getEntityClass().getOptimisticLockStyle();
	}

	@Override
	public Caching getCaching() {
		return getEntityClass().getCaching();
	}
}


