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
package org.hibernate.metamodel.source.annotations.entity;

import javax.persistence.AccessType;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.ClassInfo;

import org.hibernate.metamodel.source.annotations.AnnotationBindingContext;
import org.hibernate.metamodel.source.annotations.HibernateDotNames;
import org.hibernate.metamodel.source.annotations.JandexHelper;

/**
 * Represents the information about an entity annotated with {@code @Embeddable}.
 *
 * @author Hardy Ferentschik
 */
public class EmbeddableClass extends ConfiguredClass {
	private final String embeddedAttributeName;
	private final String parentReferencingAttributeName;

	public EmbeddableClass(
			ClassInfo classInfo,
			String embeddedAttributeName,
			ConfiguredClass parent,
			AccessType defaultAccessType,
			AnnotationBindingContext context) {
		super( classInfo, defaultAccessType, parent, context );
		this.embeddedAttributeName = embeddedAttributeName;
		this.parentReferencingAttributeName = checkParentAnnotation();
	}

	private String checkParentAnnotation() {
		AnnotationInstance parentAnnotation = JandexHelper.getSingleAnnotation(
				getClassInfo(),
				HibernateDotNames.PARENT
		);
		if ( parentAnnotation == null ) {
			return null;
		}
		else {
			return JandexHelper.getPropertyName( parentAnnotation.target() );
		}
	}

	public String getEmbeddedAttributeName() {
		return embeddedAttributeName;
	}

	public String getParentReferencingAttributeName() {
		return parentReferencingAttributeName;
	}
}


