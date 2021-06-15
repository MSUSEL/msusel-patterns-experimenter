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
import org.jboss.jandex.DotName;

import org.hibernate.HibernateException;
import org.hibernate.internal.jaxb.mapping.orm.JaxbAccessType;
import org.hibernate.internal.jaxb.mapping.orm.JaxbEnumType;
import org.hibernate.internal.jaxb.mapping.orm.JaxbMapKey;
import org.hibernate.internal.jaxb.mapping.orm.JaxbMapKeyClass;
import org.hibernate.internal.jaxb.mapping.orm.JaxbMapKeyColumn;
import org.hibernate.internal.jaxb.mapping.orm.JaxbMapKeyJoinColumn;
import org.hibernate.internal.jaxb.mapping.orm.JaxbTemporalType;

/**
 * @author Strong Liu
 */
abstract class PropertyMocker extends AnnotationMocker {
	protected ClassInfo classInfo;
	private AnnotationTarget target;

	PropertyMocker(IndexBuilder indexBuilder, ClassInfo classInfo, EntityMappingsMocker.Default defaults) {
		super( indexBuilder, defaults );
		this.classInfo = classInfo;
	}

	protected abstract void processExtra();

	protected abstract String getFieldName();

	protected abstract JaxbAccessType getAccessType();

	protected abstract void setAccessType(JaxbAccessType accessType);

	@Override
	protected DotName getTargetName() {
		return classInfo.name();
	}

	protected void resolveTarget() {
		//attribute in orm.xml has access sub-element
		JaxbAccessType accessType = getAccessType();
		if ( accessType == null ) {
			//attribute in the entity class has @Access
			accessType = AccessHelper.getAccessFromAttributeAnnotation( getTargetName(), getFieldName(), indexBuilder );
			if ( accessType == null ) {
				accessType = AccessHelper.getEntityAccess( getTargetName(), indexBuilder );
			}
			if ( accessType == null ) {
				accessType = AccessHelper.getAccessFromIdPosition( getTargetName(), indexBuilder );
			}
			if (accessType == null ) {
				//this should only for determin @Id position
				accessType = AccessHelper.getAccessFromDefault( indexBuilder );
			}
			if ( accessType == null ) {
				accessType = JaxbAccessType.PROPERTY;

			}
			setAccessType( accessType );
		}

	}

	@Override
	protected AnnotationTarget getTarget() {
		if ( target == null ) {
			target = getTargetFromAttributeAccessType( getAccessType() );
		}
		return target;
	}

	protected AnnotationTarget getTargetFromAttributeAccessType(JaxbAccessType accessType) {
		if ( accessType == null ) {
			throw new IllegalArgumentException( "access type can't be null." );
		}
		switch ( accessType ) {
			case FIELD:
				return MockHelper.getTarget(
						indexBuilder.getServiceRegistry(),
						classInfo,
						getFieldName(),
						MockHelper.TargetType.FIELD
				);
			case PROPERTY:
				return MockHelper.getTarget(
						indexBuilder.getServiceRegistry(),
						classInfo,
						getFieldName(),
						MockHelper.TargetType.PROPERTY
				);
			default:
				throw new HibernateException( "can't determin access type [" + accessType + "]" );
		}
	}


	@Override
	final void process() {
		resolveTarget();
		processExtra();
	}

	protected AnnotationInstance parserMapKeyColumn(JaxbMapKeyColumn mapKeyColumn, AnnotationTarget target) {
		if ( mapKeyColumn == null ) {
			return null;
		}
		List<AnnotationValue> annotationValueList = new ArrayList<AnnotationValue>();
		MockHelper.stringValue( "name", mapKeyColumn.getName(), annotationValueList );
		MockHelper.stringValue( "columnDefinition", mapKeyColumn.getColumnDefinition(), annotationValueList );
		MockHelper.stringValue( "table", mapKeyColumn.getTable(), annotationValueList );
		MockHelper.booleanValue( "nullable", mapKeyColumn.isNullable(), annotationValueList );
		MockHelper.booleanValue( "insertable", mapKeyColumn.isInsertable(), annotationValueList );
		MockHelper.booleanValue( "updatable", mapKeyColumn.isUpdatable(), annotationValueList );
		MockHelper.booleanValue( "unique", mapKeyColumn.isUnique(), annotationValueList );
		MockHelper.integerValue( "length", mapKeyColumn.getLength(), annotationValueList );
		MockHelper.integerValue( "precision", mapKeyColumn.getPrecision(), annotationValueList );
		MockHelper.integerValue( "scale", mapKeyColumn.getScale(), annotationValueList );
		return create( MAP_KEY_COLUMN, target, annotationValueList );
	}

	protected AnnotationInstance parserMapKeyClass(JaxbMapKeyClass mapKeyClass, AnnotationTarget target) {
		if ( mapKeyClass == null ) {
			return null;
		}
		return create(
				MAP_KEY_CLASS, target, MockHelper.classValueArray(
				"value", mapKeyClass.getClazz(), indexBuilder.getServiceRegistry()
		)
		);
	}

	protected AnnotationInstance parserMapKeyTemporal(JaxbTemporalType temporalType, AnnotationTarget target) {
		if ( temporalType == null ) {
			return null;
		}
		return create(
				MAP_KEY_TEMPORAL, target,
				MockHelper.enumValueArray( "value", TEMPORAL_TYPE, temporalType )
		);
	}

	protected AnnotationInstance parserMapKeyEnumerated(JaxbEnumType enumType, AnnotationTarget target) {
		if ( enumType == null ) {
			return null;
		}
		return create(
				MAP_KEY_ENUMERATED, target,
				MockHelper.enumValueArray( "value", ENUM_TYPE, enumType )
		);
	}

	protected AnnotationInstance parserMapKey(JaxbMapKey mapKey, AnnotationTarget target) {
		if ( mapKey == null ) {
			return null;
		}
		return create( MAP_KEY, target, MockHelper.stringValueArray( "name", mapKey.getName() ) );
	}

	private AnnotationValue[] nestedMapKeyJoinColumnList(String name, List<JaxbMapKeyJoinColumn> columns, List<AnnotationValue> annotationValueList) {
		if ( MockHelper.isNotEmpty( columns ) ) {
			AnnotationValue[] values = new AnnotationValue[columns.size()];
			for ( int i = 0; i < columns.size(); i++ ) {
				AnnotationInstance annotationInstance = parserMapKeyJoinColumn( columns.get( i ), null );
				values[i] = MockHelper.nestedAnnotationValue(
						"", annotationInstance
				);
			}
			MockHelper.addToCollectionIfNotNull(
					annotationValueList, AnnotationValue.createArrayValue( name, values )
			);
			return values;
		}
		return MockHelper.EMPTY_ANNOTATION_VALUE_ARRAY;
	}

	protected AnnotationInstance parserMapKeyJoinColumnList(List<JaxbMapKeyJoinColumn> joinColumnList, AnnotationTarget target) {
		if ( MockHelper.isNotEmpty( joinColumnList ) ) {
			if ( joinColumnList.size() == 1 ) {
				return parserMapKeyJoinColumn( joinColumnList.get( 0 ), target );
			}
			else {
				AnnotationValue[] values = nestedMapKeyJoinColumnList( "value", joinColumnList, null );
				return create(
						MAP_KEY_JOIN_COLUMNS,
						target,
						values
				);
			}
		}
		return null;

	}

	//@MapKeyJoinColumn
	private AnnotationInstance parserMapKeyJoinColumn(JaxbMapKeyJoinColumn column, AnnotationTarget target) {
		if ( column == null ) {
			return null;
		}
		List<AnnotationValue> annotationValueList = new ArrayList<AnnotationValue>();
		MockHelper.stringValue( "name", column.getName(), annotationValueList );
		MockHelper.stringValue( "columnDefinition", column.getColumnDefinition(), annotationValueList );
		MockHelper.stringValue( "table", column.getTable(), annotationValueList );
		MockHelper.stringValue(
				"referencedColumnName", column.getReferencedColumnName(), annotationValueList
		);
		MockHelper.booleanValue( "unique", column.isUnique(), annotationValueList );
		MockHelper.booleanValue( "nullable", column.isNullable(), annotationValueList );
		MockHelper.booleanValue( "insertable", column.isInsertable(), annotationValueList );
		MockHelper.booleanValue( "updatable", column.isUpdatable(), annotationValueList );
		return create( MAP_KEY_JOIN_COLUMN, target, annotationValueList );
	}


}
