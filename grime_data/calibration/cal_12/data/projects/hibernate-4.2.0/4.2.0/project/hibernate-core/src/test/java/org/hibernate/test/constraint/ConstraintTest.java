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
package org.hibernate.test.constraint;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.mapping.Column;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Test;

/**
 * HHH-7797 re-wrote the way dialects handle unique constraints.  Test
 * variations of unique & not null to ensure the constraints are created
 * correctly for each dialect.
 * 
 * @author Brett Meyer
 */
@TestForIssue( jiraKey = "HHH-7797" )
public class ConstraintTest extends BaseCoreFunctionalTestCase {
	
	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] {
				Entity1.class
		};
	}
	
	@Test
	public void testConstraints() {
		Column column = (Column) configuration().getClassMapping( Entity1.class.getName() )
				.getProperty( "foo1" ).getColumnIterator().next();
		assertFalse( column.isNullable() );
		assertTrue( column.isUnique() );

		column = (Column) configuration().getClassMapping( Entity1.class.getName() )
				.getProperty( "foo2" ).getColumnIterator().next();
		assertTrue( column.isNullable() );
		assertTrue( column.isUnique() );

		column = (Column) configuration().getClassMapping( Entity1.class.getName() )
				.getProperty( "id" ).getColumnIterator().next();
		assertFalse( column.isNullable() );
		assertTrue( column.isUnique() );
	}
	
	@Entity
	@Table( name = "Entity1" )
	public static class Entity1 {
		@Id
		@GeneratedValue
		@javax.persistence.Column( nullable = false, unique = true)
		public long id;
		
		@javax.persistence.Column( nullable = false, unique = true)
		public String foo1;
		
		@javax.persistence.Column( nullable = true, unique = true)
		public String foo2;
	}
}