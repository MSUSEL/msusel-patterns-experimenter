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
package org.hibernate.metamodel.source.annotations.attribute;

import org.jboss.jandex.DotName;

import org.hibernate.metamodel.source.annotations.JPADotNames;

/**
 * An enum defining the type of a mapped attribute.
 *
 * @author Hardy Ferentschik
 */
public enum AttributeNature {
	BASIC( JPADotNames.BASIC ),
	ONE_TO_ONE( JPADotNames.ONE_TO_ONE ),
	ONE_TO_MANY( JPADotNames.ONE_TO_MANY ),
	MANY_TO_ONE( JPADotNames.MANY_TO_ONE ),
	MANY_TO_MANY( JPADotNames.MANY_TO_MANY ),
	ELEMENT_COLLECTION( JPADotNames.ELEMENT_COLLECTION ),
	EMBEDDED_ID( JPADotNames.EMBEDDED_ID ),
	EMBEDDED( JPADotNames.EMBEDDED );

	private final DotName annotationDotName;

	AttributeNature(DotName annotationDotName) {
		this.annotationDotName = annotationDotName;
	}

	public DotName getAnnotationDotName() {
		return annotationDotName;
	}
}
