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
package org.hibernate.internal;
import java.io.Serializable;
import java.util.Properties;

import org.hibernate.TypeHelper;
import org.hibernate.type.BasicType;
import org.hibernate.type.Type;
import org.hibernate.type.TypeResolver;
import org.hibernate.usertype.CompositeUserType;

/**
 * Implementation of {@link org.hibernate.TypeHelper}
 *
 * @todo Do we want to cache the results of {@link #entity}, {@link #custom} and {@link #any} ?
 *
 * @author Steve Ebersole
 */
public class TypeLocatorImpl implements TypeHelper, Serializable {
	private final TypeResolver typeResolver;

	public TypeLocatorImpl(TypeResolver typeResolver) {
		this.typeResolver = typeResolver;
	}

	/**
	 * {@inheritDoc}
	 */
	public BasicType basic(String name) {
		return typeResolver.basic( name );
	}

	/**
	 * {@inheritDoc}
	 */
	public BasicType basic(Class javaType) {
		BasicType type = typeResolver.basic( javaType.getName() );
		if ( type == null ) {
			final Class variant = resolvePrimitiveOrPrimitiveWrapperVariantJavaType( javaType );
			if ( variant != null ) {
				type = typeResolver.basic( variant.getName() );
			}
		}
		return type;
	}

	private Class resolvePrimitiveOrPrimitiveWrapperVariantJavaType(Class javaType) {
		// boolean
		if ( Boolean.TYPE.equals( javaType ) ) {
			return Boolean.class;
		}
		if ( Boolean.class.equals( javaType ) ) {
			return Boolean.TYPE;
		}

		// char
		if ( Character.TYPE.equals( javaType ) ) {
			return Character.class;
		}
		if ( Character.class.equals( javaType ) ) {
			return Character.TYPE;
		}

		// byte
		if ( Byte.TYPE.equals( javaType ) ) {
			return Byte.class;
		}
		if ( Byte.class.equals( javaType ) ) {
			return Byte.TYPE;
		}

		// short
		if ( Short.TYPE.equals( javaType ) ) {
			return Short.class;
		}
		if ( Short.class.equals( javaType ) ) {
			return Short.TYPE;
		}

		// int
		if ( Integer.TYPE.equals( javaType ) ) {
			return Integer.class;
		}
		if ( Integer.class.equals( javaType ) ) {
			return Integer.TYPE;
		}

		// long
		if ( Long.TYPE.equals( javaType ) ) {
			return Long.class;
		}
		if ( Long.class.equals( javaType ) ) {
			return Long.TYPE;
		}

		// float
		if ( Float.TYPE.equals( javaType ) ) {
			return Float.class;
		}
		if ( Float.class.equals( javaType ) ) {
			return Float.TYPE;
		}

		// double
		if ( Double.TYPE.equals( javaType ) ) {
			return Double.class;
		}
		if ( Double.class.equals( javaType ) ) {
			return Double.TYPE;
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Type heuristicType(String name) {
		return typeResolver.heuristicType( name );
	}

	/**
	 * {@inheritDoc}
	 */
	public Type entity(Class entityClass) {
		return entity( entityClass.getName() );
	}

	/**
	 * {@inheritDoc}
	 */
	public Type entity(String entityName) {
		return typeResolver.getTypeFactory().manyToOne( entityName );
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked" })
	public Type custom(Class userTypeClass) {
		return custom( userTypeClass, null );
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked" })
	public Type custom(Class userTypeClass, Properties parameters) {
		if ( CompositeUserType.class.isAssignableFrom( userTypeClass ) ) {
			return typeResolver.getTypeFactory().customComponent( userTypeClass, parameters );
		}
		else {
			return typeResolver.getTypeFactory().custom( userTypeClass, parameters );
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Type any(Type metaType, Type identifierType) {
		return typeResolver.getTypeFactory().any( metaType, identifierType );
	}
}
