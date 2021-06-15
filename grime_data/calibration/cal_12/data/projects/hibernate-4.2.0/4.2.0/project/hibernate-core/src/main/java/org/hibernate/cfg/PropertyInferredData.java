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
package org.hibernate.cfg;
import javax.persistence.Access;

import org.hibernate.MappingException;
import org.hibernate.annotations.Target;
import org.hibernate.annotations.common.reflection.ReflectionManager;
import org.hibernate.annotations.common.reflection.XClass;
import org.hibernate.annotations.common.reflection.XProperty;

/**
 * Retrieve all inferred data from an annnoted element
 *
 * @author Emmanuel Bernard
 * @author Paolo Perrotta
 */
public class PropertyInferredData implements PropertyData {
	private final AccessType defaultAccess;

	private final XProperty property;
	private final ReflectionManager reflectionManager;
	private final XClass declaringClass;

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append( "PropertyInferredData" );
		sb.append( "{property=" ).append( property );
		sb.append( ", declaringClass=" ).append( declaringClass );
		sb.append( '}' );
		return sb.toString();
	}

	/**
	 * Take the annoted element for lazy process
	 */
	public PropertyInferredData(XClass declaringClass, XProperty property, String propertyAccessor, ReflectionManager reflectionManager) {
		this.declaringClass = declaringClass;
		this.property = property;
		this.defaultAccess = AccessType.getAccessStrategy( propertyAccessor );
		this.reflectionManager = reflectionManager;
	}

	public AccessType getDefaultAccess() throws MappingException {
		AccessType accessType = defaultAccess;

		AccessType hibernateAccessType = AccessType.DEFAULT;
		AccessType jpaAccessType = AccessType.DEFAULT;

		org.hibernate.annotations.AccessType accessTypeAnnotation = property.getAnnotation( org.hibernate.annotations.AccessType.class );
		if ( accessTypeAnnotation != null ) {
			hibernateAccessType = AccessType.getAccessStrategy( accessTypeAnnotation.value() );
		}

		Access access = property.getAnnotation( Access.class );
		if ( access != null ) {
			jpaAccessType = AccessType.getAccessStrategy( access.value() );
		}

		if ( hibernateAccessType != AccessType.DEFAULT
				&& jpaAccessType != AccessType.DEFAULT
				&& hibernateAccessType != jpaAccessType ) {

			StringBuilder builder = new StringBuilder();
			builder.append( property.toString() );
			builder.append(
					" defines @AccessType and @Access with contradicting values. Use of @Access only is recommended."
			);
			throw new MappingException( builder.toString() );
		}

		if ( hibernateAccessType != AccessType.DEFAULT ) {
			accessType = hibernateAccessType;
		}
		else if ( jpaAccessType != AccessType.DEFAULT ) {
			accessType = jpaAccessType;
		}
		return accessType;
	}

	public String getPropertyName() throws MappingException {
		return property.getName();
	}

	public XClass getPropertyClass() throws MappingException {
		if ( property.isAnnotationPresent( Target.class ) ) {
			return reflectionManager.toXClass( property.getAnnotation( Target.class ).value() );
		}
		else {
			return property.getType();
		}
	}

	public XClass getClassOrElement() throws MappingException {
		if ( property.isAnnotationPresent( Target.class ) ) {
			return reflectionManager.toXClass( property.getAnnotation( Target.class ).value() );
		}
		else {
			return property.getClassOrElementClass();
		}
	}

	public String getClassOrElementName() throws MappingException {
		return getClassOrElement().getName();
	}

	public String getTypeName() throws MappingException {
		return getPropertyClass().getName();
	}

	public XProperty getProperty() {
		return property;
	}

	public XClass getDeclaringClass() {
		return declaringClass;
	}
}
