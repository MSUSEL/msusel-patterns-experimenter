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
package org.hibernate.metamodel.relational;

import java.sql.Types;

import org.junit.Test;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.testing.junit4.BaseUnitTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Steve Ebersole
 */
public class TableManipulationTests extends BaseUnitTestCase {
	public static final Datatype VARCHAR = new Datatype( Types.VARCHAR, "VARCHAR", String.class );
	public static final Datatype INTEGER = new Datatype( Types.INTEGER, "INTEGER", Long.class );

	@Test
	public void testTableCreation() {
		Schema schema = new Schema( null, null );
		Table table = schema.createTable( Identifier.toIdentifier( "my_table" ) );
		assertNull( table.getSchema().getName().getSchema() );
		assertNull( table.getSchema().getName().getCatalog() );
		assertEquals( "my_table", table.getTableName().toString() );
		assertEquals( "my_table", table.getExportIdentifier() );
		assertNull( table.getPrimaryKey().getName() );
		assertFalse( table.values().iterator().hasNext() );

		Column idColumn = table.locateOrCreateColumn( "id" );
		idColumn.setDatatype( INTEGER );
		idColumn.setSize( Size.precision( 18, 0 ) );
		table.getPrimaryKey().addColumn( idColumn );
		table.getPrimaryKey().setName( "my_table_pk" );
		assertEquals( "my_table_pk", table.getPrimaryKey().getName() );
		assertEquals( "my_table.PK", table.getPrimaryKey().getExportIdentifier() );

		Column col_1 = table.locateOrCreateColumn( "col_1" );
		col_1.setDatatype( VARCHAR );
		col_1.setSize( Size.length( 512 ) );

		for ( Value value : table.values() ) {
			assertTrue( Column.class.isInstance( value ) );
			Column column = ( Column ) value;
			if ( column.getColumnName().getName().equals( "id" ) ) {
				assertEquals( INTEGER, column.getDatatype() );
				assertEquals( 18, column.getSize().getPrecision() );
				assertEquals( 0, column.getSize().getScale() );
				assertEquals( -1, column.getSize().getLength() );
				assertNull( column.getSize().getLobMultiplier() );
			}
			else {
				assertEquals( "col_1", column.getColumnName().getName() );
				assertEquals( VARCHAR, column.getDatatype() );
				assertEquals( -1, column.getSize().getPrecision() );
				assertEquals( -1, column.getSize().getScale() );
				assertEquals( 512, column.getSize().getLength() );
				assertNull( column.getSize().getLobMultiplier() );
			}
		}
	}

	@Test
	public void testTableSpecificationCounter() {
		Schema schema = new Schema( null, null );
		Table table = schema.createTable( Identifier.toIdentifier( "my_table" ) );
		InLineView inLineView = schema.createInLineView( "my_inlineview", "subselect" );
		InLineView otherInLineView = schema.createInLineView( "my_other_inlineview", "other subselect" );
		Table otherTable = schema.createTable( Identifier.toIdentifier( "my_other_table" ) );

		int firstTableNumber = table.getTableNumber();
		assertEquals( firstTableNumber, table.getTableNumber() );
		assertEquals( firstTableNumber + 1, inLineView.getTableNumber() );
		assertEquals( firstTableNumber + 2, otherInLineView.getTableNumber() );
		assertEquals( firstTableNumber + 3, otherTable.getTableNumber() );
	}

	@Test
	public void testBasicForeignKeyDefinition() {
		Schema schema = new Schema( null, null );
		Table book = schema.createTable( Identifier.toIdentifier( "BOOK" ) );

		Column bookId = book.locateOrCreateColumn( "id" );
		bookId.setDatatype( INTEGER );
		bookId.setSize( Size.precision( 18, 0 ) );
		book.getPrimaryKey().addColumn( bookId );
		book.getPrimaryKey().setName( "BOOK_PK" );

		Table page = schema.createTable( Identifier.toIdentifier( "PAGE" ) );

		Column pageId = page.locateOrCreateColumn( "id" );
		pageId.setDatatype( INTEGER );
		pageId.setSize( Size.precision( 18, 0 ) );
		page.getPrimaryKey().addColumn( pageId );
		page.getPrimaryKey().setName( "PAGE_PK" );

		Column pageBookId = page.locateOrCreateColumn( "BOOK_ID" );
		pageId.setDatatype( INTEGER );
		pageId.setSize( Size.precision( 18, 0 ) );
		ForeignKey pageBookFk = page.createForeignKey( book, "PAGE_BOOK_FK" );
		pageBookFk.addColumn( pageBookId );

		assertEquals( page, pageBookFk.getSourceTable() );
		assertEquals( book, pageBookFk.getTargetTable() );
	}

	@Test
	public void testQualifiedName() {
		Dialect dialect = new H2Dialect();
		Schema schema = new Schema( Identifier.toIdentifier( "schema" ), Identifier.toIdentifier( "`catalog`" ) );
		Table table = schema.createTable( Identifier.toIdentifier( "my_table" ) );
		assertEquals( "my_table", table.getTableName().getName() );
		assertEquals( "my_table", table.getTableName().toString() );
		assertEquals( "schema.\"catalog\".my_table", table.getQualifiedName( dialect ) );

		table = schema.createTable( Identifier.toIdentifier( "`my_table`" ) );
		assertEquals( "my_table", table.getTableName().getName() );
		assertEquals( "`my_table`", table.getTableName().toString() );
		assertEquals( "schema.\"catalog\".\"my_table\"", table.getQualifiedName( dialect ) );

		InLineView inLineView = schema.createInLineView( "my_inlineview", "select ..." );
		assertEquals( "( select ... )", inLineView.getQualifiedName( dialect ) );
	}
}
