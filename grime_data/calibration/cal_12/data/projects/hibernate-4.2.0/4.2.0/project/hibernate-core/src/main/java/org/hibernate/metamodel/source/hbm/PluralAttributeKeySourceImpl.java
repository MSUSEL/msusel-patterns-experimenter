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

import org.hibernate.internal.jaxb.mapping.hbm.JaxbKeyElement;
import org.hibernate.metamodel.relational.ForeignKey;
import org.hibernate.metamodel.source.binder.AttributeSourceContainer;
import org.hibernate.metamodel.source.binder.PluralAttributeKeySource;
import org.hibernate.metamodel.source.binder.RelationalValueSource;

/**
 * @author Steve Ebersole
 */
public class PluralAttributeKeySourceImpl implements PluralAttributeKeySource {
	private final JaxbKeyElement keyElement;

	private final List<RelationalValueSource> valueSources;

	public PluralAttributeKeySourceImpl(
			final JaxbKeyElement keyElement,
			final AttributeSourceContainer container) {
		this.keyElement = keyElement;

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
						return Helper.getBooleanValue( keyElement.isUpdate(), true );
					}

					@Override
					public String getColumnAttribute() {
						return keyElement.getColumnAttribute();
					}

					@Override
					public String getFormulaAttribute() {
						return null;
					}

					@Override
					public List getColumnOrFormulaElements() {
						return keyElement.getColumn();
					}
				},
				container.getLocalBindingContext()
		);
	}

	@Override
	public List<RelationalValueSource> getValueSources() {
		return valueSources;
	}

	@Override
	public String getExplicitForeignKeyName() {
		return keyElement.getForeignKey();
	}

	@Override
	public ForeignKey.ReferentialAction getOnDeleteAction() {
		return "cascade".equals( keyElement.getOnDelete() )
				? ForeignKey.ReferentialAction.CASCADE
				: ForeignKey.ReferentialAction.NO_ACTION;
	}

	@Override
	public String getReferencedEntityAttributeName() {
		return keyElement.getPropertyRef();
	}
}
