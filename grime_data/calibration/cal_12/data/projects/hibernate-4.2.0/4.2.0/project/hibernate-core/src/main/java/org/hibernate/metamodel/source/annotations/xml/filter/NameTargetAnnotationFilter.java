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

import java.util.Iterator;
import java.util.List;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.DotName;

import org.hibernate.metamodel.source.annotations.xml.mocker.MockHelper;

/**
 * @author Strong Liu
 */
class NameTargetAnnotationFilter extends AbstractAnnotationFilter {
	@Override
	protected void process(DotName annName, AnnotationInstance annotationInstance, List<AnnotationInstance> indexedAnnotationInstanceList) {
		AnnotationTarget target = annotationInstance.target();

		for ( Iterator<AnnotationInstance> iter = indexedAnnotationInstanceList.iterator(); iter.hasNext(); ) {
			AnnotationInstance ann = iter.next();
			if ( MockHelper.targetEquals( target, ann.target() ) ) {
				iter.remove();
			}
		}
	}

	public static NameTargetAnnotationFilter INSTANCE = new NameTargetAnnotationFilter();

	@Override
	protected DotName[] targetAnnotation() {
		return new DotName[] {
				LOB,
				ID,
				BASIC,
				GENERATED_VALUE,
				VERSION,
				TRANSIENT,
				ACCESS,
				POST_LOAD,
				POST_PERSIST,
				POST_REMOVE,
				POST_UPDATE,
				PRE_PERSIST,
				PRE_REMOVE,
				PRE_UPDATE,
				EMBEDDED_ID,
				EMBEDDED,
				MANY_TO_ONE,
				MANY_TO_MANY,
				ONE_TO_ONE,
				ONE_TO_MANY,
				ELEMENT_COLLECTION,
				COLLECTION_TABLE,
				COLUMN,
				ENUMERATED,
				JOIN_TABLE,
				TEMPORAL,
				ORDER_BY,
				ORDER_COLUMN,
				JOIN_COLUMN,
				JOIN_COLUMNS,
				MAPS_ID,
				MAP_KEY_TEMPORAL,
				MAP_KEY,
				MAP_KEY_CLASS,
				MAP_KEY_COLUMN,
				MAP_KEY_ENUMERATED
		};
	}
}
