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

import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.internal.jaxb.mapping.hbm.JaxbManyToManyElement;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.metamodel.source.LocalBindingContext;
import org.hibernate.metamodel.source.binder.ManyToManyPluralAttributeElementSource;
import org.hibernate.metamodel.source.binder.PluralAttributeElementNature;
import org.hibernate.metamodel.source.binder.RelationalValueSource;

/**
 * @author Steve Ebersole
 */
public class ManyToManyPluralAttributeElementSourceImpl implements ManyToManyPluralAttributeElementSource {
	private final JaxbManyToManyElement manyToManyElement;
	private final LocalBindingContext bindingContext;

	private final List<RelationalValueSource> valueSources;

	public ManyToManyPluralAttributeElementSourceImpl(
			final JaxbManyToManyElement manyToManyElement,
			final LocalBindingContext bindingContext) {
		this.manyToManyElement = manyToManyElement;
		this.bindingContext = bindingContext;

		this.valueSources = Helper.buildValueSources(
				new Helper.ValueSourcesAdapter() {
					@Override
					public String getContainingTableName() {
						return null;
					}

					@Override
					public boolean isIncludedInInsertByDefault() {
						return true;
					}

					@Override
					public boolean isIncludedInUpdateByDefault() {
						return true;
					}

					@Override
					public String getColumnAttribute() {
						return manyToManyElement.getColumn();
					}

					@Override
					public String getFormulaAttribute() {
						return manyToManyElement.getFormula();
					}

					@Override
					public List getColumnOrFormulaElements() {
						return manyToManyElement.getColumnOrFormula();
					}
				},
				bindingContext
		);
	}

	@Override
	public PluralAttributeElementNature getNature() {
		return PluralAttributeElementNature.MANY_TO_MANY;
	}

	@Override
	public String getReferencedEntityName() {
		return StringHelper.isNotEmpty( manyToManyElement.getEntityName() )
				? manyToManyElement.getEntityName()
				: bindingContext.qualifyClassName( manyToManyElement.getClazz() );
	}

	@Override
	public String getReferencedEntityAttributeName() {
		return manyToManyElement.getPropertyRef();
	}

	@Override
	public List<RelationalValueSource> getValueSources() {
		return valueSources;
	}

	@Override
	public boolean isNotFoundAnException() {
		return manyToManyElement.getNotFound() == null
				|| ! "ignore".equals( manyToManyElement.getNotFound().value() );
	}

	@Override
	public String getExplicitForeignKeyName() {
		return manyToManyElement.getForeignKey();
	}

	@Override
	public boolean isUnique() {
		return manyToManyElement.isUnique();
	}

	@Override
	public String getOrderBy() {
		return manyToManyElement.getOrderBy();
	}

	@Override
	public String getWhere() {
		return manyToManyElement.getWhere();
	}

	@Override
	public FetchMode getFetchMode() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public boolean fetchImmediately() {
		if ( manyToManyElement.getLazy() != null ) {
			if ( "false".equals( manyToManyElement.getLazy().value() ) ) {
				return true;
			}
		}

		if ( manyToManyElement.getOuterJoin() == null ) {
			return ! bindingContext.getMappingDefaults().areAssociationsLazy();
		}
		else {
			final String value = manyToManyElement.getOuterJoin().value();
			if ( "auto".equals( value ) ) {
				return ! bindingContext.getMappingDefaults().areAssociationsLazy();
			}
			return "true".equals( value );
		}
	}
}
