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

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Clob;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.AssertionFailure;
import org.hibernate.metamodel.source.annotations.JPADotNames;
import org.hibernate.metamodel.source.annotations.JandexHelper;
import org.hibernate.metamodel.source.annotations.attribute.MappedAttribute;
import org.hibernate.type.CharacterArrayClobType;
import org.hibernate.type.PrimitiveCharacterArrayClobType;
import org.hibernate.type.SerializableToBlobType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.WrappedMaterializedBlobType;
import org.jboss.jandex.AnnotationInstance;

/**
 * @author Strong Liu
 */
public class LobTypeResolver extends AbstractAttributeTypeResolver {
	private final MappedAttribute mappedAttribute;

	public LobTypeResolver(MappedAttribute mappedAttribute) {
		if ( mappedAttribute == null ) {
			throw new AssertionFailure( "MappedAttribute is null" );
		}
		this.mappedAttribute = mappedAttribute;
	}

	@Override
	protected AnnotationInstance getTypeDeterminingAnnotationInstance() {
		return JandexHelper.getSingleAnnotation( mappedAttribute.annotations(), JPADotNames.LOB );
	}

	@Override
	public String resolveHibernateTypeName(AnnotationInstance annotationInstance) {
		if ( annotationInstance == null ) {
			return null;
		}
		String type = null;
		if ( Clob.class.isAssignableFrom( mappedAttribute.getAttributeType() ) ) {
			type = StandardBasicTypes.CLOB.getName();
		}
		else if ( Blob.class.isAssignableFrom( mappedAttribute.getAttributeType() ) ) {
			type = StandardBasicTypes.BLOB.getName();
		}
		else if ( String.class.isAssignableFrom( mappedAttribute.getAttributeType() ) ) {
			type = StandardBasicTypes.MATERIALIZED_CLOB.getName();
		}
		else if ( Character[].class.isAssignableFrom( mappedAttribute.getAttributeType() ) ) {
			type = CharacterArrayClobType.class.getName();
		}
		else if ( char[].class.isAssignableFrom( mappedAttribute.getAttributeType() ) ) {
			type = PrimitiveCharacterArrayClobType.class.getName();
		}
		else if ( Byte[].class.isAssignableFrom( mappedAttribute.getAttributeType() ) ) {
			type = WrappedMaterializedBlobType.class.getName();
		}
		else if ( byte[].class.isAssignableFrom( mappedAttribute.getAttributeType() ) ) {
			type = StandardBasicTypes.MATERIALIZED_BLOB.getName();
		}
		else if ( Serializable.class.isAssignableFrom( mappedAttribute.getAttributeType() ) ) {
			type = SerializableToBlobType.class.getName();
		}
		else {
			type = "blob";
		}
		return type;
	}

	@Override
	protected Map<String, String> resolveHibernateTypeParameters(AnnotationInstance annotationInstance) {
		if ( getExplicitHibernateTypeName().equals( SerializableToBlobType.class.getName() ) ) {
			HashMap<String, String> typeParameters = new HashMap<String, String>();
			typeParameters.put(
					SerializableToBlobType.CLASS_NAME,
					mappedAttribute.getAttributeType().getName()
			);
			return typeParameters;
		}
		return null;
	}
}
