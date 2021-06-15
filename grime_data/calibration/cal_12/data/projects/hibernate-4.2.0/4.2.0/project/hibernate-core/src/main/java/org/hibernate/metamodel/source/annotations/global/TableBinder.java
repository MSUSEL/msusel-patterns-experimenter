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
package org.hibernate.metamodel.source.annotations.global;

import java.util.List;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.logging.Logger;

import org.hibernate.AnnotationException;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.metamodel.relational.Column;
import org.hibernate.metamodel.relational.ObjectName;
import org.hibernate.metamodel.relational.Schema;
import org.hibernate.metamodel.relational.SimpleValue;
import org.hibernate.metamodel.relational.Table;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.metamodel.source.annotations.AnnotationBindingContext;
import org.hibernate.metamodel.source.annotations.HibernateDotNames;
import org.hibernate.metamodel.source.annotations.JandexHelper;

/**
 * Binds table related information. This binder is called after the entities are bound.
 *
 * @author Hardy Ferentschik
 */
public class TableBinder {

	private static final CoreMessageLogger LOG = Logger.getMessageLogger(
			CoreMessageLogger.class,
			TableBinder.class.getName()
	);

	private TableBinder() {
	}

	/**
	 * Binds {@link org.hibernate.annotations.Tables} and {@link org.hibernate.annotations.Table} annotations to the supplied
	 * metadata.
	 *
	 * @param bindingContext the context for annotation binding
	 */
	public static void bind(AnnotationBindingContext bindingContext) {
		List<AnnotationInstance> annotations = bindingContext.getIndex().getAnnotations( HibernateDotNames.TABLE );
		for ( AnnotationInstance tableAnnotation : annotations ) {
			bind( bindingContext.getMetadataImplementor(), tableAnnotation );
		}

		annotations = bindingContext.getIndex().getAnnotations( HibernateDotNames.TABLES );
		for ( AnnotationInstance tables : annotations ) {
			for ( AnnotationInstance table : JandexHelper.getValue( tables, "value", AnnotationInstance[].class ) ) {
				bind( bindingContext.getMetadataImplementor(), table );
			}
		}
	}

	private static void bind(MetadataImplementor metadata, AnnotationInstance tableAnnotation) {
		String tableName = JandexHelper.getValue( tableAnnotation, "appliesTo", String.class );
		ObjectName objectName = new ObjectName( tableName );
		Schema schema = metadata.getDatabase().getSchema( objectName.getSchema(), objectName.getCatalog() );
		Table table = schema.locateTable( objectName.getName() );
		if ( table != null ) {
			bindHibernateTableAnnotation( table, tableAnnotation );
		}
	}

	private static void bindHibernateTableAnnotation(Table table, AnnotationInstance tableAnnotation) {
		for ( AnnotationInstance indexAnnotation : JandexHelper.getValue(
				tableAnnotation,
				"indexes",
				AnnotationInstance[].class
		) ) {
			bindIndexAnnotation( table, indexAnnotation );
		}
		String comment = JandexHelper.getValue( tableAnnotation, "comment", String.class );
		if ( StringHelper.isNotEmpty( comment ) ) {
			table.addComment( comment.trim() );
		}
	}

	private static void bindIndexAnnotation(Table table, AnnotationInstance indexAnnotation) {
		String indexName = JandexHelper.getValue( indexAnnotation, "appliesTo", String.class );
		String[] columnNames = JandexHelper.getValue( indexAnnotation, "columnNames", String[].class );
		if ( columnNames == null ) {
			LOG.noColumnsSpecifiedForIndex( indexName, table.toLoggableString() );
			return;
		}
		org.hibernate.metamodel.relational.Index index = table.getOrCreateIndex( indexName );
		for ( String columnName : columnNames ) {
			Column column = findColumn( table, columnName );
			if ( column == null ) {
				throw new AnnotationException( "@Index references a unknown column: " + columnName );
			}
			index.addColumn( column );
		}
	}

	private static Column findColumn(Table table, String columnName) {
		Column column = null;
		for ( SimpleValue value : table.values() ) {
			if ( value instanceof Column && ( (Column) value ).getColumnName().getName().equals( columnName ) ) {
				column = (Column) value;
				break;
			}
		}
		return column;
	}
}
