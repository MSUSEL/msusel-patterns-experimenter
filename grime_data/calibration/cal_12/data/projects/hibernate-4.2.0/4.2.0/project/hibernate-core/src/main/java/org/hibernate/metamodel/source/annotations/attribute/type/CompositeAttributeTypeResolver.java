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
package org.hibernate.metamodel.source.annotations.attribute.type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.AssertionFailure;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.internal.util.collections.CollectionHelper;

/**
 * @author Strong Liu
 */
public class CompositeAttributeTypeResolver implements AttributeTypeResolver {
	private List<AttributeTypeResolver> resolvers = new ArrayList<AttributeTypeResolver>();
	private final AttributeTypeResolverImpl explicitHibernateTypeResolver;

	public CompositeAttributeTypeResolver(AttributeTypeResolverImpl explicitHibernateTypeResolver) {
		if ( explicitHibernateTypeResolver == null ) {
			throw new AssertionFailure( "The Given AttributeTypeResolver is null." );
		}
		this.explicitHibernateTypeResolver = explicitHibernateTypeResolver;
	}

	public void addHibernateTypeResolver(AttributeTypeResolver resolver) {
		if ( resolver == null ) {
			throw new AssertionFailure( "The Given AttributeTypeResolver is null." );
		}
		resolvers.add( resolver );
	}

	@Override
	public String getExplicitHibernateTypeName() {
		String type = explicitHibernateTypeResolver.getExplicitHibernateTypeName();
		if ( StringHelper.isEmpty( type ) ) {
			for ( AttributeTypeResolver resolver : resolvers ) {
				type = resolver.getExplicitHibernateTypeName();
				if ( StringHelper.isNotEmpty( type ) ) {
					break;
				}
			}
		}
		return type;
	}

	@Override
	public Map<String, String> getExplicitHibernateTypeParameters() {
		Map<String, String> parameters = explicitHibernateTypeResolver.getExplicitHibernateTypeParameters();
		if ( CollectionHelper.isEmpty( parameters ) ) {
			for ( AttributeTypeResolver resolver : resolvers ) {
				parameters = resolver.getExplicitHibernateTypeParameters();
				if ( CollectionHelper.isNotEmpty( parameters ) ) {
					break;
				}
			}
		}
		return parameters;
	}
}
