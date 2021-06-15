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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.AssertionFailure;
import org.hibernate.annotations.common.reflection.XClass;
import org.hibernate.annotations.common.reflection.XProperty;

/**
 * Work in progress
 * The goal of this class is to aggregate all operations
 * related to ToOne binding operations
 *
 * @author Emmanuel Bernard
 */
public class ToOneBinder {
	public static String getReferenceEntityName(PropertyData propertyData, XClass targetEntity, Mappings mappings) {
		if ( AnnotationBinder.isDefault( targetEntity, mappings ) ) {
			return propertyData.getClassOrElementName();
		}
		else {
			return targetEntity.getName();
		}
	}

	public static String getReferenceEntityName(PropertyData propertyData, Mappings mappings) {
		XClass targetEntity = getTargetEntity( propertyData, mappings );
		if ( AnnotationBinder.isDefault( targetEntity, mappings ) ) {
			return propertyData.getClassOrElementName();
		}
		else {
			return targetEntity.getName();
		}
	}

	public static XClass getTargetEntity(PropertyData propertyData, Mappings mappings) {
		XProperty property = propertyData.getProperty();
		return mappings.getReflectionManager().toXClass( getTargetEntityClass( property ) );
	}

	private static Class<?> getTargetEntityClass(XProperty property) {
		final ManyToOne mTo = property.getAnnotation( ManyToOne.class );
		if (mTo != null) {
			return mTo.targetEntity();
		}
		final OneToOne oTo = property.getAnnotation( OneToOne.class );
		if (oTo != null) {
			return oTo.targetEntity();
		}
		throw new AssertionFailure("Unexpected discovery of a targetEntity: " + property.getName() );
	}
}
