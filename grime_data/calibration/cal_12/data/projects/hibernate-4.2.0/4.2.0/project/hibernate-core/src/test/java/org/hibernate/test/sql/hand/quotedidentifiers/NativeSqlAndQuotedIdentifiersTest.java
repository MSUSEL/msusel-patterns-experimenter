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
package org.hibernate.test.sql.hand.quotedidentifiers;

import org.junit.Test;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.dialect.Dialect;
import org.hibernate.testing.DialectCheck;
import org.hibernate.testing.RequiresDialectFeature;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

/**
 * Test of various situations with native-sql queries and quoted identifiers
 *
 * @author Steve Ebersole
 */
@RequiresDialectFeature( value = NativeSqlAndQuotedIdentifiersTest.LocalDialectCheck.class )
public class NativeSqlAndQuotedIdentifiersTest extends BaseCoreFunctionalTestCase {
	public String[] getMappings() {
		return new String[] { "sql/hand/quotedidentifiers/Mappings.hbm.xml" };
	}

	public static class LocalDialectCheck implements DialectCheck {
		@Override
		public boolean isMatch(Dialect dialect) {
			return '\"' == dialect.openQuote();
		}
	}

	@Override
	protected void prepareTest() throws Exception {
		if( sessionFactory()==null)return;
		Session session = sessionFactory().openSession();
		session.beginTransaction();
		session.save( new Person( "me" ) );
		session.getTransaction().commit();
		session.close();
	}

	@Override
	protected void cleanupTest() throws Exception {
		if( sessionFactory()==null)return;
		Session session = sessionFactory().openSession();
		session.beginTransaction();
		session.createQuery( "delete Person" ).executeUpdate();
		session.getTransaction().commit();
		session.close();
	}

	@Test
	public void testCompleteScalarDiscovery() {
		Session session = openSession();
		session.beginTransaction();
		session.getNamedQuery( "query-person" ).list();
		session.getTransaction().commit();
		session.close();
	}

	@Test
	public void testPartialScalarDiscovery() {
		Session session = openSession();
		session.beginTransaction();
		SQLQuery query = (SQLQuery) session.getNamedQuery( "query-person" );
		query.setResultSetMapping( "person-scalar" );
		query.list();
		session.getTransaction().commit();
		session.close();
	}

	@Test
	public void testBasicEntityMapping() {
		Session session = openSession();
		session.beginTransaction();
		SQLQuery query = (SQLQuery) session.getNamedQuery( "query-person" );
		query.setResultSetMapping( "person-entity-basic" );
		query.list();
		session.getTransaction().commit();
		session.close();
	}

	@Test
	public void testExpandedEntityMapping() {
		Session session = openSession();
		session.beginTransaction();
		SQLQuery query = (SQLQuery) session.getNamedQuery( "query-person" );
		query.setResultSetMapping( "person-entity-expanded" );
		query.list();
		session.getTransaction().commit();
		session.close();
	}
}
