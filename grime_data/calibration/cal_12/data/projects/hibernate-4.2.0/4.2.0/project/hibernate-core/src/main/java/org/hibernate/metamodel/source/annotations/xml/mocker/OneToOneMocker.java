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

import org.jboss.jandex.AnnotationValue;
import org.jboss.jandex.ClassInfo;

import org.hibernate.internal.jaxb.mapping.orm.JaxbAccessType;
import org.hibernate.internal.jaxb.mapping.orm.JaxbOneToOne;

/**
 * @author Strong Liu
 */
class OneToOneMocker extends PropertyMocker {
	private JaxbOneToOne oneToOne;

	OneToOneMocker(IndexBuilder indexBuilder, ClassInfo classInfo, EntityMappingsMocker.Default defaults, JaxbOneToOne oneToOne) {
		super( indexBuilder, classInfo, defaults );
		this.oneToOne = oneToOne;
	}

	@Override
	protected String getFieldName() {
		return oneToOne.getName();
	}

	@Override
	protected void processExtra() {
		List<AnnotationValue> annotationValueList = new ArrayList<AnnotationValue>();
		MockHelper.classValue(
				"targetEntity", oneToOne.getTargetEntity(), annotationValueList, indexBuilder.getServiceRegistry()
		);
		MockHelper.enumValue( "fetch", FETCH_TYPE, oneToOne.getFetch(), annotationValueList );
		MockHelper.booleanValue( "optional", oneToOne.isOptional(), annotationValueList );
		MockHelper.booleanValue( "orphanRemoval", oneToOne.isOrphanRemoval(), annotationValueList );
		MockHelper.stringValue( "mappedBy", oneToOne.getMappedBy(), annotationValueList );
		MockHelper.cascadeValue( "cascade", oneToOne.getCascade(), isDefaultCascadePersist(), annotationValueList );
		create( ONE_TO_ONE, annotationValueList );

		parserPrimaryKeyJoinColumnList( oneToOne.getPrimaryKeyJoinColumn(), getTarget() );
		parserJoinColumnList( oneToOne.getJoinColumn(), getTarget() );
		parserJoinTable( oneToOne.getJoinTable(), getTarget() );
		if ( oneToOne.getMapsId() != null ) {
			create( MAPS_ID, MockHelper.stringValueArray( "value", oneToOne.getMapsId() ) );
		}
		if ( oneToOne.isId() != null && oneToOne.isId() ) {
			create( ID );
		}
	}

	@Override
	protected JaxbAccessType getAccessType() {
		return oneToOne.getAccess();
	}

	@Override
	protected void setAccessType(JaxbAccessType accessType) {
		oneToOne.setAccess( accessType );
	}
}
