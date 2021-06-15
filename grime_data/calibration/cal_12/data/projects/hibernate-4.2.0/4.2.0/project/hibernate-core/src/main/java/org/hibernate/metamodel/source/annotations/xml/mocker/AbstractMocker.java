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
package org.hibernate.metamodel.source.annotations.xml.mocker;

import java.util.ArrayList;
import java.util.List;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.AnnotationValue;
import org.jboss.jandex.DotName;

import org.hibernate.internal.jaxb.mapping.orm.JaxbAccessType;
import org.hibernate.internal.jaxb.mapping.orm.JaxbUniqueConstraint;
import org.hibernate.metamodel.source.annotations.JPADotNames;

/**
 * Base class for the mock jandex annotations created from orm.xml.
 *
 * @author Strong Liu
 */
abstract class AbstractMocker implements JPADotNames {
	final protected IndexBuilder indexBuilder;

	AbstractMocker(IndexBuilder indexBuilder) {
		this.indexBuilder = indexBuilder;
	}


	abstract protected AnnotationInstance push(AnnotationInstance annotationInstance);


	protected AnnotationInstance create(DotName name, AnnotationTarget target) {
		return create( name, target, MockHelper.EMPTY_ANNOTATION_VALUE_ARRAY );
	}


	protected AnnotationInstance create(DotName name, AnnotationTarget target, List<AnnotationValue> annotationValueList) {
		return create( name, target, MockHelper.toArray( annotationValueList ) );
	}

	protected AnnotationInstance create(DotName name, AnnotationTarget target, AnnotationValue[] annotationValues) {
		AnnotationInstance annotationInstance = MockHelper.create( name, target, annotationValues );
		push( annotationInstance );
		return annotationInstance;

	}


	protected AnnotationInstance parserAccessType(JaxbAccessType accessType, AnnotationTarget target) {
		if ( accessType == null ) {
			return null;
		}
		return create( ACCESS, target, MockHelper.enumValueArray( "value", ACCESS_TYPE, accessType ) );
	}

	protected void nestedUniqueConstraintList(String name, List<JaxbUniqueConstraint> constraints, List<AnnotationValue> annotationValueList) {
		if ( MockHelper.isNotEmpty( constraints ) ) {
			AnnotationValue[] values = new AnnotationValue[constraints.size()];
			for ( int i = 0; i < constraints.size(); i++ ) {
				AnnotationInstance annotationInstance = parserUniqueConstraint( constraints.get( i ), null );
				values[i] = MockHelper.nestedAnnotationValue(
						"", annotationInstance
				);
			}
			MockHelper.addToCollectionIfNotNull(
					annotationValueList, AnnotationValue.createArrayValue( name, values )
			);
		}

	}

	//@UniqueConstraint
	protected AnnotationInstance parserUniqueConstraint(JaxbUniqueConstraint uniqueConstraint, AnnotationTarget target) {
		if ( uniqueConstraint == null ) {
			return null;
		}
		List<AnnotationValue> annotationValueList = new ArrayList<AnnotationValue>();
		MockHelper.stringValue( "name", uniqueConstraint.getName(), annotationValueList );
		MockHelper.stringArrayValue( "columnNames", uniqueConstraint.getColumnName(), annotationValueList );
		return
				create( UNIQUE_CONSTRAINT, target,
						annotationValueList );
	}

}
