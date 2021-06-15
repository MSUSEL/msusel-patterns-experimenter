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

import org.hibernate.internal.jaxb.mapping.hbm.JaxbOneToManyElement;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.metamodel.source.LocalBindingContext;
import org.hibernate.metamodel.source.binder.OneToManyPluralAttributeElementSource;
import org.hibernate.metamodel.source.binder.PluralAttributeElementNature;

/**
 * @author Steve Ebersole
 */
public class OneToManyPluralAttributeElementSourceImpl implements OneToManyPluralAttributeElementSource {
	private final JaxbOneToManyElement oneToManyElement;
	private final LocalBindingContext bindingContext;

	public OneToManyPluralAttributeElementSourceImpl(
			JaxbOneToManyElement oneToManyElement,
			LocalBindingContext bindingContext) {
		this.oneToManyElement = oneToManyElement;
		this.bindingContext = bindingContext;
	}

	@Override
	public PluralAttributeElementNature getNature() {
		return PluralAttributeElementNature.ONE_TO_MANY;
	}

	@Override
	public String getReferencedEntityName() {
		return StringHelper.isNotEmpty( oneToManyElement.getEntityName() )
				? oneToManyElement.getEntityName()
				: bindingContext.qualifyClassName( oneToManyElement.getClazz() );
	}

	@Override
	public boolean isNotFoundAnException() {
		return oneToManyElement.getNotFound() == null
				|| ! "ignore".equals( oneToManyElement.getNotFound().value() );
	}
}
