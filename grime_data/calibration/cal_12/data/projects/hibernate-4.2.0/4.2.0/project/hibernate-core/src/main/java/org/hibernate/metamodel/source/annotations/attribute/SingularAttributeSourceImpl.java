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
package org.hibernate.metamodel.source.annotations.attribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.mapping.PropertyGeneration;
import org.hibernate.metamodel.source.binder.ExplicitHibernateTypeSource;
import org.hibernate.metamodel.source.binder.MetaAttributeSource;
import org.hibernate.metamodel.source.binder.RelationalValueSource;
import org.hibernate.metamodel.source.binder.SingularAttributeNature;
import org.hibernate.metamodel.source.binder.SingularAttributeSource;

/**
 * @author Hardy Ferentschik
 */
public class SingularAttributeSourceImpl implements SingularAttributeSource {
	private final MappedAttribute attribute;
	private final AttributeOverride attributeOverride;

	public SingularAttributeSourceImpl(MappedAttribute attribute) {
		this(attribute, null);
	}

	public SingularAttributeSourceImpl(MappedAttribute attribute, AttributeOverride attributeOverride) {
		this.attribute = attribute;
		this.attributeOverride = attributeOverride;
	}

	@Override
	public ExplicitHibernateTypeSource getTypeInformation() {
		return new ExplicitHibernateTypeSourceImpl( attribute.getHibernateTypeResolver() );
	}

	@Override
	public String getPropertyAccessorName() {
		return attribute.getAccessType();
	}

	@Override
	public boolean isInsertable() {
		return attribute.isInsertable();
	}

	@Override
	public boolean isUpdatable() {
		return attribute.isUpdatable();
	}

	@Override
	public PropertyGeneration getGeneration() {
		return attribute.getPropertyGeneration();
	}

	@Override
	public boolean isLazy() {
		return attribute.isLazy();
	}

	@Override
	public boolean isIncludedInOptimisticLocking() {
		return attribute.isOptimisticLockable();
	}

	@Override
	public String getName() {
		return attribute.getName();
	}

	@Override
	public List<RelationalValueSource> relationalValueSources() {
		List<RelationalValueSource> valueSources = new ArrayList<RelationalValueSource>();
		valueSources.add( new ColumnSourceImpl( attribute, attributeOverride ) );
		return valueSources;
	}

	@Override
	public boolean isVirtualAttribute() {
		return false;
	}

	@Override
	public boolean isSingular() {
		return true;
	}

	@Override
	public SingularAttributeNature getNature() {
		return SingularAttributeNature.BASIC;
	}

	@Override
	public Iterable<MetaAttributeSource> metaAttributes() {
		return Collections.emptySet();
	}

	@Override
	public boolean areValuesIncludedInInsertByDefault() {
		return true;
	}

	@Override
	public boolean areValuesIncludedInUpdateByDefault() {
		return true;
	}

	@Override
	public boolean areValuesNullableByDefault() {
		return true;
	}
}


