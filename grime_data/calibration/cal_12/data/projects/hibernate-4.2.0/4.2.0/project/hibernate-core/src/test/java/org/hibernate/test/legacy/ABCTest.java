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
package org.hibernate.test.legacy;

import java.util.List;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.Transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@SuppressWarnings( {"UnnecessaryBoxing"})
public class ABCTest extends LegacyTestCase {
	public String[] getMappings() {
		return new String[] { "legacy/ABC.hbm.xml", "legacy/ABCExtends.hbm.xml" };
	}

	@Test
	public void testFormulaAssociation() throws Throwable {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Long did = Long.valueOf(12);
		D d = new D( did );
		s.save(d);
		A a = new A();
		a.setName("a");
		s.save( a );
		d.setReverse( a );
		d.inverse = a;
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		d = (D) s.get(D.class, did);
		assertNotNull( d.getReverse() );
		s.clear();
		sessionFactory().getCache().evictEntityRegion( D.class );
		sessionFactory().getCache().evictEntityRegion(A.class);
		d = (D) s.get(D.class, did);
		assertNotNull( d.inverse );
		assertTrue(d.inverse.getName().equals("a"));
		s.clear();
		sessionFactory().getCache().evictEntityRegion( D.class );
		sessionFactory().getCache().evictEntityRegion( A.class );
		assertTrue( s.createQuery( "from D d join d.reverse r join d.inverse i where i = r" ).list().size()==1 );
		t.commit();
		s.close();
	}

	@Test
	public void testHigherLevelIndexDefinition() throws Throwable {
		String[] commands = configuration().generateSchemaCreationScript( getDialect() );
		int max = commands.length;
		boolean found = false;
		for (int indx = 0; indx < max; indx++) {
			System.out.println("Checking command : " + commands[indx]);
			found = commands[indx].indexOf("create index indx_a_name") >= 0;
			if (found)
				break;
		}
		assertTrue("Unable to locate indx_a_name index creation", found);
	}

	@Test
	public void testSubclassing() throws Exception {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		C1 c1 = new C1();
		D d = new D();
		d.setAmount(213.34f);
		c1.setAddress("foo bar");
		c1.setCount(23432);
		c1.setName("c1");
		c1.setBName("a funny name");
		c1.setD(d);
		s.save(c1);
		d.setId( c1.getId() );
		s.save(d);

		assertTrue( s.createQuery( "from C2 c where 1=1 or 1=1" ).list().size()==0 );

		t.commit();
		s.close();

		sessionFactory().getCache().evictEntityRegion( A.class );
		
		s = openSession();
		t = s.beginTransaction();
		c1 = (C1) s.get( A.class, c1.getId() );
		assertTrue(
			c1.getAddress().equals("foo bar") &&
			(c1.getCount()==23432) &&
			c1.getName().equals("c1") &&
			c1.getD().getAmount()>213.3f
		);
		assertEquals( "a funny name", c1.getBName() );
		t.commit();
		s.close();
		
		sessionFactory().getCache().evictEntityRegion( A.class );

		s = openSession();
		t = s.beginTransaction();
		c1 = (C1) s.get( B.class, c1.getId() );
		assertTrue(
			c1.getAddress().equals("foo bar") &&
			(c1.getCount()==23432) &&
			c1.getName().equals("c1") &&
			c1.getD().getAmount()>213.3f
		);
		assertEquals( "a funny name", c1.getBName() );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		c1 = (C1) s.load( C1.class, c1.getId() );
		assertTrue(
			c1.getAddress().equals("foo bar") &&
			(c1.getCount()==23432) &&
			c1.getName().equals("c1") &&
			c1.getD().getAmount()>213.3f
		);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		List bs = s.createQuery("from B").list();
		for (int i=0; i<bs.size(); i++) {
			C1 b = (C1) bs.get(i);
			s.delete(b);
			s.delete( b.getD() );
		}
		t.commit();
		s.close();
	}
	
	@Test
	public void testGetSave() throws Exception {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		assertNull( s.get( D.class, Long.valueOf(1) ) );
		D d = new D();
		d.setId( Long.valueOf(1) );
		s.save(d);
		s.flush();
		assertNotNull( s.get( D.class, Long.valueOf(1) ) );
		s.delete(d);
		s.flush();
		t.commit();
		s.close();
	}

}

