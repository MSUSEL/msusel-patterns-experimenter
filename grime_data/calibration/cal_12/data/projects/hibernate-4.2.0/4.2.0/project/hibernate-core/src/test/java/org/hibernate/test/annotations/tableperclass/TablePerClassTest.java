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
package org.hibernate.test.annotations.tableperclass;

import java.util.List;

import org.junit.Test;

import org.hibernate.JDBCException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Emmanuel Bernard
 */
public class TablePerClassTest extends BaseCoreFunctionalTestCase {
	@Test
	public void testUnionSubClass() throws Exception {
		Session s;
		Transaction tx;
		s = openSession();
		tx = s.beginTransaction();
		Machine computer = new Machine();
		computer.setWeight( new Double( 4 ) );
		Robot asimov = new Robot();
		asimov.setWeight( new Double( 120 ) );
		asimov.setName( "Asimov" );
		T800 terminator = new T800();
		terminator.setName( "Terminator" );
		terminator.setWeight( new Double( 300 ) );
		terminator.setTargetName( "Sarah Connor" );
		s.persist( computer );
		s.persist( asimov );
		s.persist( terminator );
		tx.commit();
		s.close();
		s = openSession();
		tx = s.beginTransaction();
		Query q = s.createQuery( "from Machine m where m.weight >= :weight" );
		q.setDouble( "weight", new Double( 10 ) );
		List result = q.list();
		assertEquals( 2, result.size() );
		tx.commit();
		s.close();
		s = openSession();
		tx = s.beginTransaction();
		tx.commit();
		s.close();
	}

	@Test
	public void testConstraintsOnSuperclassProperties() throws Exception {
		Session s = openSession();
		Transaction tx = s.beginTransaction();
		Product product1 = new Product();
		product1.setId( 1l );
		product1.setManufacturerId( 1l );
		product1.setManufacturerPartNumber( "AAFR");
		s.persist( product1 );
		s.flush();
		Product product2 = new Product();
		product2.setId( 2l );
		product2.setManufacturerId( 1l );
		product2.setManufacturerPartNumber( "AAFR");
		s.persist( product2 );
		try {
			s.flush();
			fail("Database Exception not handled");
		}
		catch( JDBCException e ) {
			//success
		}
		tx.rollback();
		s.close();
	}

	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[] {
				Robot.class,
				T800.class,
				Machine.class,
				Component.class,
				Product.class
		};
	}
}
