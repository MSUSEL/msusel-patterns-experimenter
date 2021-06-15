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
import java.util.Map;

import org.hibernate.internal.jaxb.mapping.hbm.JaxbPropertyElement;
import org.hibernate.mapping.PropertyGeneration;
import org.hibernate.metamodel.source.LocalBindingContext;
import org.hibernate.metamodel.source.binder.ExplicitHibernateTypeSource;
import org.hibernate.metamodel.source.binder.MetaAttributeSource;
import org.hibernate.metamodel.source.binder.RelationalValueSource;
import org.hibernate.metamodel.source.binder.SingularAttributeNature;
import org.hibernate.metamodel.source.binder.SingularAttributeSource;

/**
 * Implementation for {@code <property/>} mappings
 *
 * @author Steve Ebersole
 */
class PropertyAttributeSourceImpl implements SingularAttributeSource {
	private final JaxbPropertyElement propertyElement;
	private final ExplicitHibernateTypeSource typeSource;
	private final List<RelationalValueSource> valueSources;

	PropertyAttributeSourceImpl(final JaxbPropertyElement propertyElement, LocalBindingContext bindingContext) {
		this.propertyElement = propertyElement;
		this.typeSource = new ExplicitHibernateTypeSource() {
			private final String name = propertyElement.getTypeAttribute() != null
					? propertyElement.getTypeAttribute()
					: propertyElement.getType() != null
							? propertyElement.getType().getName()
							: null;
			private final Map<String, String> parameters = ( propertyElement.getType() != null )
					? Helper.extractParameters( propertyElement.getType().getParam() )
					: null;

			@Override
			public String getName() {
				return name;
			}

			@Override
			public Map<String, String> getParameters() {
				return parameters;
			}
		};
		this.valueSources = Helper.buildValueSources(
				new Helper.ValueSourcesAdapter() {
					@Override
					public String getColumnAttribute() {
						return propertyElement.getColumn();
					}

					@Override
					public String getFormulaAttribute() {
						return propertyElement.getFormula();
					}

					@Override
					public List getColumnOrFormulaElements() {
						return propertyElement.getColumnOrFormula();
					}

					@Override
					public String getContainingTableName() {
						// todo : need to implement this...
						return null;
					}

					@Override
					public boolean isIncludedInInsertByDefault() {
						return Helper.getBooleanValue( propertyElement.isInsert(), true );
					}

					@Override
					public boolean isIncludedInUpdateByDefault() {
						return Helper.getBooleanValue( propertyElement.isUpdate(), true );
					}
				},
				bindingContext
		);
	}

	@Override
	public String getName() {
		return propertyElement.getName();
	}

	@Override
	public ExplicitHibernateTypeSource getTypeInformation() {
		return typeSource;
	}

	@Override
	public String getPropertyAccessorName() {
		return propertyElement.getAccess();
	}

	@Override
	public boolean isInsertable() {
		return Helper.getBooleanValue( propertyElement.isInsert(), true );
	}

	@Override
	public boolean isUpdatable() {
		return Helper.getBooleanValue( propertyElement.isUpdate(), true );
	}

	@Override
	public PropertyGeneration getGeneration() {
		return PropertyGeneration.parse( propertyElement.getGenerated() );
	}

	@Override
	public boolean isLazy() {
		return Helper.getBooleanValue( propertyElement.isLazy(), false );
	}

	@Override
	public boolean isIncludedInOptimisticLocking() {
		return Helper.getBooleanValue( propertyElement.isOptimisticLock(), true );
	}

	@Override
	public SingularAttributeNature getNature() {
		return SingularAttributeNature.BASIC;
	}

	@Override
	public boolean isVirtualAttribute() {
		return false;
	}

	@Override
	public boolean areValuesIncludedInInsertByDefault() {
		return Helper.getBooleanValue( propertyElement.isInsert(), true );
	}

	@Override
	public boolean areValuesIncludedInUpdateByDefault() {
		return Helper.getBooleanValue( propertyElement.isUpdate(), true );
	}

	@Override
	public boolean areValuesNullableByDefault() {
		return ! Helper.getBooleanValue( propertyElement.isNotNull(), false );
	}

	@Override
	public List<RelationalValueSource> relationalValueSources() {
		return valueSources;
	}

	@Override
	public boolean isSingular() {
		return true;
	}

	@Override
	public Iterable<MetaAttributeSource> metaAttributes() {
		return Helper.buildMetaAttributeSources( propertyElement.getMeta() );
	}
}
