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

import org.hibernate.metamodel.source.annotations.entity.EntityClass;
import org.hibernate.metamodel.source.binder.DiscriminatorSource;
import org.hibernate.metamodel.source.binder.RelationalValueSource;

/**
 * @author Hardy Ferentschik
 */
public class DiscriminatorSourceImpl implements DiscriminatorSource {
	private final EntityClass entityClass;

	public DiscriminatorSourceImpl(EntityClass entityClass) {
		this.entityClass = entityClass;
	}

	@Override
	public boolean isForced() {
		return entityClass.isDiscriminatorForced();
	}

	@Override
	public boolean isInserted() {
		return entityClass.isDiscriminatorIncludedInSql();
	}

    @Override
    public RelationalValueSource getDiscriminatorRelationalValueSource() {
        return entityClass.getDiscriminatorFormula() != null ?
                new DerivedValueSourceImpl( entityClass.getDiscriminatorFormula() )
                : new ColumnValuesSourceImpl( entityClass.getDiscriminatorColumnValues() );
    }

	@Override
	public String getExplicitHibernateTypeName() {
		return entityClass.getDiscriminatorType().getName();
	}
}


