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

import java.util.ArrayList;
import java.util.List;

import org.hibernate.EntityMode;
import org.hibernate.internal.jaxb.mapping.hbm.JaxbCompositeElementElement;
import org.hibernate.internal.jaxb.mapping.hbm.JaxbTuplizerElement;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.internal.util.ValueHolder;
import org.hibernate.metamodel.source.LocalBindingContext;
import org.hibernate.metamodel.source.binder.AttributeSource;
import org.hibernate.metamodel.source.binder.CompositePluralAttributeElementSource;
import org.hibernate.metamodel.source.binder.PluralAttributeElementNature;

/**
 * @author Steve Ebersole
 */
public class CompositePluralAttributeElementSourceImpl implements CompositePluralAttributeElementSource {
	private final JaxbCompositeElementElement compositeElement;
	private final LocalBindingContext bindingContext;

	public CompositePluralAttributeElementSourceImpl(
			JaxbCompositeElementElement compositeElement,
			LocalBindingContext bindingContext) {
		this.compositeElement = compositeElement;
		this.bindingContext = bindingContext;
	}

	@Override
	public PluralAttributeElementNature getNature() {
		return PluralAttributeElementNature.COMPONENT;
	}

	@Override
	public String getClassName() {
		return bindingContext.qualifyClassName( compositeElement.getClazz() );
	}

	@Override
	public ValueHolder<Class<?>> getClassReference() {
		return bindingContext.makeClassReference( getClassName() );
	}

	@Override
	public String getParentReferenceAttributeName() {
		return compositeElement.getParent() != null
				? compositeElement.getParent().getName()
				: null;
	}

	@Override
	public String getExplicitTuplizerClassName() {
		if ( compositeElement.getTuplizer() == null ) {
			return null;
		}
		final EntityMode entityMode = StringHelper.isEmpty( compositeElement.getClazz() ) ? EntityMode.MAP : EntityMode.POJO;
		for ( JaxbTuplizerElement tuplizerElement : compositeElement.getTuplizer() ) {
			if ( entityMode == EntityMode.parse( tuplizerElement.getEntityMode() ) ) {
				return tuplizerElement.getClazz();
			}
		}
		return null;
	}

	@Override
	public String getPath() {
		// todo : implementing this requires passing in the collection source and being able to resolve the collection's role
		return null;
	}

	@Override
	public Iterable<AttributeSource> attributeSources() {
		List<AttributeSource> attributeSources = new ArrayList<AttributeSource>();
		for ( Object attribute : compositeElement.getPropertyOrManyToOneOrAny() ) {

		}
		return attributeSources;
	}

	@Override
	public LocalBindingContext getLocalBindingContext() {
		return bindingContext;
	}
}
