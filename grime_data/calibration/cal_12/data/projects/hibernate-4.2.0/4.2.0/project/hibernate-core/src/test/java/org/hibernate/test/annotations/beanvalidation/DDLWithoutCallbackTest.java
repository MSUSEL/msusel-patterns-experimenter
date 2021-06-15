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
package org.hibernate.test.annotations.beanvalidation;

import java.math.BigDecimal;
import javax.validation.ConstraintViolationException;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.testing.DialectChecks;
import org.hibernate.testing.RequiresDialectFeature;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

/**
 * @author Vladimir Klyushnikov
 * @author Hardy Ferentschik
 */
public class DDLWithoutCallbackTest extends BaseCoreFunctionalTestCase {
	@Test
	@RequiresDialectFeature(DialectChecks.SupportsColumnCheck.class)
	public void testListeners() {
		CupHolder ch = new CupHolder();
		ch.setRadius( new BigDecimal( "12" ) );
		assertDatabaseConstraintViolationThrown( ch );
	}

	@Test
	@RequiresDialectFeature(DialectChecks.SupportsColumnCheck.class)
	public void testMinAndMaxChecksGetApplied() {
		MinMax minMax = new MinMax( 1 );
		assertDatabaseConstraintViolationThrown( minMax );

		minMax = new MinMax( 11 );
		assertDatabaseConstraintViolationThrown( minMax );

		minMax = new MinMax( 5 );
		Session s = openSession();
		Transaction tx = s.beginTransaction();
		s.persist( minMax );
		s.flush();
		tx.rollback();
		s.close();
	}

	@Test
	@RequiresDialectFeature(DialectChecks.SupportsColumnCheck.class)
	public void testRangeChecksGetApplied() {
		Range range = new Range( 1 );
		assertDatabaseConstraintViolationThrown( range );

		range = new Range( 11 );
		assertDatabaseConstraintViolationThrown( range );

		range = new Range( 5 );
		Session s = openSession();
		Transaction tx = s.beginTransaction();
		s.persist( range );
		s.flush();
		tx.rollback();
		s.close();
	}

	@Test
	public void testDDLEnabled() {
		PersistentClass classMapping = configuration().getClassMapping( Address.class.getName() );
		Column countryColumn = (Column) classMapping.getProperty( "country" ).getColumnIterator().next();
		assertFalse( "DDL constraints are not applied", countryColumn.isNullable() );
	}

	@Override
	protected void configure(Configuration cfg) {
		super.configure( cfg );
		cfg.setProperty( "javax.persistence.validation.mode", "ddl" );
	}

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] {
				Address.class,
				CupHolder.class,
				MinMax.class,
				Range.class
		};
	}

	private void assertDatabaseConstraintViolationThrown(Object o) {
		Session s = openSession();
		Transaction tx = s.beginTransaction();
		try {
			s.persist( o );
			s.flush();
			fail( "expecting SQL constraint violation" );
		}
		catch ( ConstraintViolationException e ) {
			fail( "invalid object should not be validated" );
		}
		catch ( org.hibernate.exception.ConstraintViolationException e ) {
			if ( getDialect().supportsColumnCheck() ) {
				// expected
			}
			else {
				fail( "Unexpected SQL constraint violation [" + e.getConstraintName() + "] : " + e.getSQLException() );
			}
		}
		tx.rollback();
		s.close();
	}
}
