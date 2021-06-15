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
package org.hibernate.test.sql.check;

import org.junit.Test;

import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.fail;

/**
 * @author Steve Ebersole
 */
@SuppressWarnings( {"UnusedDeclaration"})
public abstract class ResultCheckStyleTest extends BaseCoreFunctionalTestCase {
	public String getCacheConcurrencyStrategy() {
		return null;
	}

	@Test
	public void testInsertionFailureWithExceptionChecking() {
		Session s = openSession();
		s.beginTransaction();
		ExceptionCheckingEntity e = new ExceptionCheckingEntity();
		e.setName( "dummy" );
		s.save( e );
		try {
			s.flush();
			fail( "expection flush failure!" );
		}
		catch( JDBCException ex ) {
			// these should specifically be JDBCExceptions...
		}
		s.clear();
		s.getTransaction().commit();
		s.close();
	}

	@Test
	public void testInsertionFailureWithParamChecking() {
		Session s = openSession();
		s.beginTransaction();
		ParamCheckingEntity e = new ParamCheckingEntity();
		e.setName( "dummy" );
		s.save( e );
		try {
			s.flush();
			fail( "expection flush failure!" );
		}
		catch( HibernateException ex ) {
			// these should specifically be HibernateExceptions...
		}
		s.clear();
		s.getTransaction().commit();
		s.close();
	}

	@Test
	@SuppressWarnings( {"UnnecessaryBoxing"})
	public void testUpdateFailureWithExceptionChecking() {
		Session s = openSession();
		s.beginTransaction();
		ExceptionCheckingEntity e = new ExceptionCheckingEntity();
		e.setId( Long.valueOf( 1 ) );
		e.setName( "dummy" );
		s.update( e );
		try {
			s.flush();
			fail( "expection flush failure!" );
		}
		catch( JDBCException ex ) {
			// these should specifically be JDBCExceptions...
		}
		s.clear();
		s.getTransaction().commit();
		s.close();
	}

	@Test
	@SuppressWarnings( {"UnnecessaryBoxing"})
	public void testUpdateFailureWithParamChecking() {
		Session s = openSession();
		s.beginTransaction();
		ParamCheckingEntity e = new ParamCheckingEntity();
		e.setId( Long.valueOf( 1 ) );
		e.setName( "dummy" );
		s.update( e );
		try {
			s.flush();
			fail( "expection flush failure!" );
		}
		catch( HibernateException ex ) {
			// these should specifically be HibernateExceptions...
		}
		s.clear();
		s.getTransaction().commit();
		s.close();
	}

	@Test
	@SuppressWarnings( {"UnnecessaryBoxing"})
	public void testDeleteWithExceptionChecking() {
		Session s = openSession();
		s.beginTransaction();
		ExceptionCheckingEntity e = new ExceptionCheckingEntity();
		e.setId( Long.valueOf( 1 ) );
		e.setName( "dummy" );
		s.delete( e );
		try {
			s.flush();
			fail( "expection flush failure!" );
		}
		catch( JDBCException ex ) {
			// these should specifically be JDBCExceptions...
		}
		s.clear();
		s.getTransaction().commit();
		s.close();
	}

	@Test
	@SuppressWarnings( {"UnnecessaryBoxing"})
	public void testDeleteWithParamChecking() {
		Session s = openSession();
		s.beginTransaction();
		ParamCheckingEntity e = new ParamCheckingEntity();
		e.setId( Long.valueOf( 1 ) );
		e.setName( "dummy" );
		s.delete( e );
		try {
			s.flush();
			fail( "expection flush failure!" );
		}
		catch( HibernateException ex ) {
			// these should specifically be HibernateExceptions...
		}
		s.clear();
		s.getTransaction().commit();
		s.close();
	}
}
