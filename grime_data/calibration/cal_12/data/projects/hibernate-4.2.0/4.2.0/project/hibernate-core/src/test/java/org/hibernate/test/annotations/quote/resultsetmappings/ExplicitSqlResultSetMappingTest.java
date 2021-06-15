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
package org.hibernate.test.annotations.quote.resultsetmappings;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

/**
 * @author Steve Ebersole
 */
public class ExplicitSqlResultSetMappingTest extends BaseCoreFunctionalTestCase {
	private String queryString = null;

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] { MyEntity.class };
	}

	@Override
	protected void configure(Configuration cfg) {
		cfg.setProperty( Environment.GLOBALLY_QUOTED_IDENTIFIERS, "true" );
	}

	private void prepareTestData() {
		char open = getDialect().openQuote();
		char close = getDialect().closeQuote();
		queryString="select t."+open+"NAME"+close+" as "+open+"QuotEd_nAMe"+close+" from "+open+"MY_ENTITY_TABLE"+close+" t";
		Session s = sessionFactory().openSession();
		s.beginTransaction();
		s.save( new MyEntity( "mine" ) );
		s.getTransaction().commit();
		s.close();
	}

	@Override
	protected boolean isCleanupTestDataRequired() {
		return true;
	}

	@Test
	public void testCompleteScalarAutoDiscovery() {
		prepareTestData();

		Session s = openSession();
		s.beginTransaction();
		s.createSQLQuery( queryString )
				.list();
		s.getTransaction().commit();
		s.close();
	}

	@Test
	public void testPartialScalarAutoDiscovery() {
		prepareTestData();

		Session s = openSession();
		s.beginTransaction();
		s.createSQLQuery( queryString )
				.setResultSetMapping( "explicitScalarResultSetMapping" )
				.list();
		s.getTransaction().commit();
		s.close();
	}
}
