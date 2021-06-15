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

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.jboss.jandex.AnnotationInstance;

import org.hibernate.AnnotationException;
import org.hibernate.AssertionFailure;
import org.hibernate.metamodel.source.annotations.JPADotNames;
import org.hibernate.metamodel.source.annotations.JandexHelper;
import org.hibernate.metamodel.source.annotations.attribute.MappedAttribute;
import org.hibernate.type.EnumType;

/**
 * @author Strong Liu
 */
public class EnumeratedTypeResolver extends AbstractAttributeTypeResolver {
	private final MappedAttribute mappedAttribute;
	private final boolean isMapKey;

	public EnumeratedTypeResolver(MappedAttribute mappedAttribute) {
		if ( mappedAttribute == null ) {
			throw new AssertionFailure( "MappedAttribute is null" );
		}
		this.mappedAttribute = mappedAttribute;
		this.isMapKey = false;//todo
	}

	@Override
	protected AnnotationInstance getTypeDeterminingAnnotationInstance() {
		return JandexHelper.getSingleAnnotation(
				mappedAttribute.annotations(),
				JPADotNames.ENUMERATED
		);
	}

	@Override
	public String resolveHibernateTypeName(AnnotationInstance enumeratedAnnotation) {
		boolean isEnum = mappedAttribute.getAttributeType().isEnum();
		if ( !isEnum ) {
			if ( enumeratedAnnotation != null ) {
				throw new AnnotationException( "Attribute " + mappedAttribute.getName() + " is not a Enumerated type, but has a @Enumerated annotation." );
			}
			else {
				return null;
			}
		}
		return EnumType.class.getName();
	}

	@Override
	protected Map<String, String> resolveHibernateTypeParameters(AnnotationInstance annotationInstance) {
		HashMap<String, String> typeParameters = new HashMap<String, String>();
		typeParameters.put( EnumType.ENUM, mappedAttribute.getAttributeType().getName() );
		if ( annotationInstance != null ) {
			javax.persistence.EnumType enumType = JandexHelper.getEnumValue(
					annotationInstance,
					"value",
					javax.persistence.EnumType.class
			);
			if ( javax.persistence.EnumType.ORDINAL.equals( enumType ) ) {
				typeParameters.put( EnumType.TYPE, String.valueOf( Types.INTEGER ) );
			}
			else if ( javax.persistence.EnumType.STRING.equals( enumType ) ) {
				typeParameters.put( EnumType.TYPE, String.valueOf( Types.VARCHAR ) );
			}
			else {
				throw new AssertionFailure( "Unknown EnumType: " + enumType );
			}
		}
		else {
			typeParameters.put( EnumType.TYPE, String.valueOf( Types.INTEGER ) );
		}
		return typeParameters;
	}
}
