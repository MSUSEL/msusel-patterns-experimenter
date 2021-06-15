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
package org.hibernate.test.stateless;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;

/**
 * @author stliu
 */
public class StatelessSessionQueryTest extends BaseCoreFunctionalTestCase {
	@Override
	public void configure( Configuration cfg ) {
		super.configure( cfg );
		cfg.setProperty( Environment.MAX_FETCH_DEPTH, "1" );
	}

	@Override
	public String[] getMappings() {
		return new String[] { "stateless/Contact.hbm.xml" };
	}

	@Test
	public void testCriteria() {
		TestData testData=new TestData();
		testData.createData();
		StatelessSession s = sessionFactory().openStatelessSession();
		assertEquals( 1, s.createCriteria( Contact.class ).list().size() );
		s.close();
		testData.cleanData();
	}

	@Test
	public void testCriteriaWithSelectFetchMode() {
		TestData testData=new TestData();
		testData.createData();
		StatelessSession s = sessionFactory().openStatelessSession();
		assertEquals( 1, s.createCriteria( Contact.class ).setFetchMode( "org", FetchMode.SELECT )
				.list().size() );
		s.close();
		testData.cleanData();
	}

	@Test
	public void testHQL() {
		TestData testData=new TestData();
		testData.createData();
		StatelessSession s = sessionFactory().openStatelessSession();
		assertEquals( 1, s.createQuery( "from Contact c join fetch c.org join fetch c.org.country" )
				.list().size() );
		s.close();
		testData.cleanData();
	}

	private class TestData{
		List list = new ArrayList();
		public void createData(){
			Session session = openSession();
			Transaction tx = session.beginTransaction();
			Country usa = new Country();
			session.save( usa );
			list.add( usa );
			Org disney = new Org();
			disney.setCountry( usa );
			session.save( disney );
			list.add( disney );
			Contact waltDisney = new Contact();
			waltDisney.setOrg( disney );
			session.save( waltDisney );
			list.add( waltDisney );
			tx.commit();
			session.close();
		}
		public void cleanData(){
			Session session = openSession();
			Transaction tx = session.beginTransaction();
			for(Object obj: list){
				session.delete( obj );
			}
			tx.commit();
			session.close();
		}
	}
}
