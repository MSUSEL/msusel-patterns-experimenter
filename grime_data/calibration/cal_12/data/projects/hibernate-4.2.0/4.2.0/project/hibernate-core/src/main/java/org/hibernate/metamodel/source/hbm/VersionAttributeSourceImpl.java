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

import org.hibernate.internal.jaxb.mapping.hbm.JaxbHibernateMapping;
import org.hibernate.internal.util.ValueHolder;
import org.hibernate.mapping.PropertyGeneration;
import org.hibernate.metamodel.source.LocalBindingContext;
import org.hibernate.metamodel.source.MappingException;
import org.hibernate.metamodel.source.binder.ExplicitHibernateTypeSource;
import org.hibernate.metamodel.source.binder.MetaAttributeSource;
import org.hibernate.metamodel.source.binder.RelationalValueSource;
import org.hibernate.metamodel.source.binder.SingularAttributeNature;
import org.hibernate.metamodel.source.binder.SingularAttributeSource;

/**
 * Implementation for {@code <version/>} mappings
 *
 * @author Steve Ebersole
 */
class VersionAttributeSourceImpl implements SingularAttributeSource {
	private final JaxbHibernateMapping.JaxbClass.JaxbVersion versionElement;
	private final LocalBindingContext bindingContext;
	private final List<RelationalValueSource> valueSources;

	VersionAttributeSourceImpl(
			final JaxbHibernateMapping.JaxbClass.JaxbVersion versionElement,
			LocalBindingContext bindingContext) {
		this.versionElement = versionElement;
		this.bindingContext = bindingContext;
		this.valueSources = Helper.buildValueSources(
				new Helper.ValueSourcesAdapter() {
					@Override
					public String getColumnAttribute() {
						return versionElement.getColumnAttribute();
					}

					@Override
					public String getFormulaAttribute() {
						return null;
					}

					@Override
					public List getColumnOrFormulaElements() {
						return versionElement.getColumn();
					}

					@Override
					public String getContainingTableName() {
						// by definition the version should come from the primary table of the root entity.
						return null;
					}

					@Override
					public boolean isIncludedInInsertByDefault() {
						return Helper.getBooleanValue( versionElement.isInsert(), true );
					}

					@Override
					public boolean isIncludedInUpdateByDefault() {
						return true;
					}
				},
				bindingContext
		);
	}

	private final ExplicitHibernateTypeSource typeSource = new ExplicitHibernateTypeSource() {
		@Override
		public String getName() {
			return versionElement.getType() == null ? "integer" : versionElement.getType();
		}

		@Override
		public Map<String, String> getParameters() {
			return null;
		}
	};

	@Override
	public String getName() {
		return versionElement.getName();
	}

	@Override
	public ExplicitHibernateTypeSource getTypeInformation() {
		return typeSource;
	}

	@Override
	public String getPropertyAccessorName() {
		return versionElement.getAccess();
	}

	@Override
	public boolean isInsertable() {
		return Helper.getBooleanValue( versionElement.isInsert(), true );
	}

	@Override
	public boolean isUpdatable() {
		return true;
	}

	private ValueHolder<PropertyGeneration> propertyGenerationValue = new ValueHolder<PropertyGeneration>(
			new ValueHolder.DeferredInitializer<PropertyGeneration>() {
				@Override
				public PropertyGeneration initialize() {
					final PropertyGeneration propertyGeneration = versionElement.getGenerated() == null
							? PropertyGeneration.NEVER
							: PropertyGeneration.parse( versionElement.getGenerated().value() );
					if ( propertyGeneration == PropertyGeneration.INSERT ) {
						throw new MappingException(
								"'generated' attribute cannot be 'insert' for versioning property",
								bindingContext.getOrigin()
						);
					}
					return propertyGeneration;
				}
			}
	);

	@Override
	public PropertyGeneration getGeneration() {
		return propertyGenerationValue.getValue();
	}

	@Override
	public boolean isLazy() {
		return false;
	}

	@Override
	public boolean isIncludedInOptimisticLocking() {
		return false;
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
		return Helper.buildMetaAttributeSources( versionElement.getMeta() );
	}
}
