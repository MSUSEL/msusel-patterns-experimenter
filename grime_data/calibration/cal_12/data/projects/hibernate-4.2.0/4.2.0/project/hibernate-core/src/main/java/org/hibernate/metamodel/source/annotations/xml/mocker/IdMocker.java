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
import org.jboss.jandex.ClassInfo;

import org.hibernate.internal.jaxb.mapping.orm.JaxbAccessType;
import org.hibernate.internal.jaxb.mapping.orm.JaxbGeneratedValue;
import org.hibernate.internal.jaxb.mapping.orm.JaxbId;

/**
 * @author Strong Liu
 */
class IdMocker extends PropertyMocker {
	private JaxbId id;

	IdMocker(IndexBuilder indexBuilder, ClassInfo classInfo, EntityMappingsMocker.Default defaults, JaxbId id) {
		super( indexBuilder, classInfo, defaults );
		this.id = id;
	}

	@Override
	protected void processExtra() {
		create( ID );
		parserColumn( id.getColumn(), getTarget() );
		parserGeneratedValue( id.getGeneratedValue(), getTarget() );
		parserTemporalType( id.getTemporal(), getTarget() );
	}

	private AnnotationInstance parserGeneratedValue(JaxbGeneratedValue generatedValue, AnnotationTarget target) {
		if ( generatedValue == null ) {
			return null;
		}
		List<AnnotationValue> annotationValueList = new ArrayList<AnnotationValue>();
		MockHelper.stringValue( "generator", generatedValue.getGenerator(), annotationValueList );
		MockHelper.enumValue(
				"strategy", GENERATION_TYPE, generatedValue.getStrategy(), annotationValueList
		);

		return create( GENERATED_VALUE, target, annotationValueList );
	}

	@Override
	protected String getFieldName() {
		return id.getName();
	}

	@Override
	protected JaxbAccessType getAccessType() {
		return id.getAccess();
	}

	@Override
	protected void setAccessType(JaxbAccessType accessType) {
		id.setAccess( accessType );
	}
}
