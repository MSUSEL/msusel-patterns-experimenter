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

import org.hibernate.internal.jaxb.mapping.hbm.JaxbElementElement;
import org.hibernate.metamodel.source.LocalBindingContext;
import org.hibernate.metamodel.source.binder.BasicPluralAttributeElementSource;
import org.hibernate.metamodel.source.binder.ExplicitHibernateTypeSource;
import org.hibernate.metamodel.source.binder.PluralAttributeElementNature;
import org.hibernate.metamodel.source.binder.RelationalValueSource;

/**
 * @author Steve Ebersole
 */
public class BasicPluralAttributeElementSourceImpl implements BasicPluralAttributeElementSource {
	private final List<RelationalValueSource> valueSources;
	private final ExplicitHibernateTypeSource typeSource;

	public BasicPluralAttributeElementSourceImpl(
			final JaxbElementElement elementElement,
			LocalBindingContext bindingContext) {
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
						return elementElement.getColumn();
					}

					@Override
					public String getFormulaAttribute() {
						return elementElement.getFormula();
					}

					@Override
					public List getColumnOrFormulaElements() {
						return elementElement.getColumnOrFormula();
					}
				},
				bindingContext
		);

		this.typeSource = new ExplicitHibernateTypeSource() {
			@Override
			public String getName() {
				if ( elementElement.getTypeAttribute() != null ) {
					return elementElement.getTypeAttribute();
				}
				else if ( elementElement.getType() != null ) {
					return elementElement.getType().getName();
				}
				else {
					return null;
				}
			}

			@Override
			public Map<String, String> getParameters() {
				return elementElement.getType() != null
						? Helper.extractParameters( elementElement.getType().getParam() )
						: java.util.Collections.<String, String>emptyMap();
			}
		};
	}

	@Override
	public PluralAttributeElementNature getNature() {
		return PluralAttributeElementNature.BASIC;
	}

	@Override
	public List<RelationalValueSource> getValueSources() {
		return valueSources;
	}

	@Override
	public ExplicitHibernateTypeSource getExplicitHibernateTypeSource() {
		return typeSource;
	}
}
