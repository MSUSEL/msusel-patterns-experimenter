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
package org.hibernate.metamodel.source.annotations.xml.filter;

import java.util.List;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.DotName;

/**
 * if class from index has matched annotations, then remove.
 *
 * @author Strong Liu
 */
class NameAnnotationFilter extends AbstractAnnotationFilter {
	@Override
	protected void process(DotName annName, AnnotationInstance annotationInstance, List<AnnotationInstance> indexedAnnotationInstanceList) {
		indexedAnnotationInstanceList.clear();
	}

	public static NameTargetAnnotationFilter INSTANCE = new NameTargetAnnotationFilter();

	@Override
	protected DotName[] targetAnnotation() {
		return new DotName[] {
				CACHEABLE,
				TABLE,
				EXCLUDE_DEFAULT_LISTENERS,
				EXCLUDE_SUPERCLASS_LISTENERS,
				ID_CLASS,
				INHERITANCE,
				DISCRIMINATOR_VALUE,
				DISCRIMINATOR_COLUMN,
				ENTITY_LISTENERS
		};
	}
}
