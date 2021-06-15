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
import java.util.Map;
import javax.persistence.OrderColumn;

import org.hibernate.mapping.Join;

/**
 * index column
 *
 * @author inger
 */
public class IndexColumn extends Ejb3Column {
	private int base;

	// TODO move to a getter setter strategy for readability
	public IndexColumn(
			boolean isImplicit,
			String sqlType,
			int length,
			int precision,
			int scale,
			String name,
			boolean nullable,
			boolean unique,
			boolean insertable,
			boolean updatable,
			String secondaryTableName,
			Map<String, Join> joins,
			PropertyHolder propertyHolder,
			Mappings mappings) {
		super();
		setImplicit( isImplicit );
		setSqlType( sqlType );
		setLength( length );
		setPrecision( precision );
		setScale( scale );
		setLogicalColumnName( name );
		setNullable( nullable );
		setUnique( unique );
		setInsertable( insertable );
		setUpdatable( updatable );
		setSecondaryTableName( secondaryTableName );
		setPropertyHolder( propertyHolder );
		setJoins( joins );
		setMappings( mappings );
		bind();
	}

	public int getBase() {
		return base;
	}

	public void setBase(int base) {
		this.base = base;
	}

	//JPA 2 @OrderColumn processing
	public static IndexColumn buildColumnFromAnnotation(
			OrderColumn ann,
			PropertyHolder propertyHolder,
			PropertyData inferredData,
			Map<String, Join> secondaryTables,
			Mappings mappings) {
		IndexColumn column;
		if ( ann != null ) {
			String sqlType = BinderHelper.isEmptyAnnotationValue( ann.columnDefinition() ) ? null : ann.columnDefinition();
			String name = BinderHelper.isEmptyAnnotationValue( ann.name() ) ? inferredData.getPropertyName() + "_ORDER" : ann.name();
			//TODO move it to a getter based system and remove the constructor
// The JPA OrderColumn annotation defines no table element...
//			column = new IndexColumn(
//					false, sqlType, 0, 0, 0, name, ann.nullable(),
//					false, ann.insertable(), ann.updatable(), ann.table(),
//					secondaryTables, propertyHolder, mappings
//			);
			column = new IndexColumn(
					false, sqlType, 0, 0, 0, name, ann.nullable(),
					false, ann.insertable(), ann.updatable(), /*ann.table()*/null,
					secondaryTables, propertyHolder, mappings
			);
		}
		else {
			column = new IndexColumn(
					true, null, 0, 0, 0, null, true,
					false, true, true, null, null, propertyHolder, mappings
			);
		}
		return column;
	}

	//legacy @IndexColumn processing
	public static IndexColumn buildColumnFromAnnotation(
			org.hibernate.annotations.IndexColumn ann,
			PropertyHolder propertyHolder,
			PropertyData inferredData,
			Mappings mappings) {
		IndexColumn column;
		if ( ann != null ) {
			String sqlType = BinderHelper.isEmptyAnnotationValue( ann.columnDefinition() ) ? null : ann.columnDefinition();
			String name = BinderHelper.isEmptyAnnotationValue( ann.name() ) ? inferredData.getPropertyName() : ann.name();
			//TODO move it to a getter based system and remove the constructor
			column = new IndexColumn(
					false, sqlType, 0, 0, 0, name, ann.nullable(),
					false, true, true, null, null, propertyHolder, mappings
			);
			column.setBase( ann.base() );
		}
		else {
			column = new IndexColumn(
					true, null, 0, 0, 0, null, true,
					false, true, true, null, null, propertyHolder, mappings
			);
		}
		return column;
	}
}
