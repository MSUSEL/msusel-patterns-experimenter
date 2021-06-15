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

import org.hibernate.internal.jaxb.mapping.hbm.JaxbSetElement;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.metamodel.source.binder.AttributeSourceContainer;
import org.hibernate.metamodel.source.binder.Orderable;
import org.hibernate.metamodel.source.binder.PluralAttributeNature;
import org.hibernate.metamodel.source.binder.Sortable;

/**
 * @author Steve Ebersole
 */
public class SetAttributeSourceImpl extends AbstractPluralAttributeSourceImpl implements Orderable, Sortable {
	public SetAttributeSourceImpl(JaxbSetElement setElement, AttributeSourceContainer container) {
		super( setElement, container );
	}

	@Override
	public JaxbSetElement getPluralAttributeElement() {
		return (JaxbSetElement) super.getPluralAttributeElement();
	}

	@Override
	public PluralAttributeNature getPluralAttributeNature() {
		return PluralAttributeNature.SET;
	}

	@Override
	public boolean isSorted() {
		return StringHelper.isNotEmpty( getComparatorName() );
	}

	@Override
	public String getComparatorName() {
		return getPluralAttributeElement().getSort();
	}

	@Override
	public boolean isOrdered() {
		return StringHelper.isNotEmpty( getOrder() );
	}

	@Override
	public String getOrder() {
		return getPluralAttributeElement().getOrderBy();
	}
}
