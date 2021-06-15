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
package org.hibernate.test.annotations.derivedidentities.e1.b2;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * A test.
 *
 * @author <a href="mailto:stale.pedersen@jboss.org">Stale W. Pedersen</a>
 */
public class IdClassGeneratedValueManyToOneTest extends BaseCoreFunctionalTestCase {
	@Test
	public void testComplexIdClass() {
		Session s = openSession();
		Transaction tx = s.beginTransaction();

		Customer c1 = new Customer(
				"foo", "bar", "contact1", "100", new BigDecimal( 1000 ), new BigDecimal( 1000 ), new BigDecimal( 1000 ));
		s.persist( c1 );
		s.flush();
        s.clear();
		
//      why does this cause a failure?        
//		Customer c2 = new Customer(
//              "foo1", "bar1", "contact2", "200", new BigDecimal( 2000 ), new BigDecimal( 2000 ), new BigDecimal( 2000 ));
//		s.persist( c2 );
//		s.flush();
//        s.clear();
		
		Item boat = new Item();
		boat.setId( "1" );
		boat.setName( "cruiser" );
		boat.setPrice( new BigDecimal( 500 ) );
		boat.setDescription( "a boat" );
		boat.setCategory( 42 );

		s.persist( boat );
		s.flush();
		s.clear();

		c1.addInventory( boat, 10, new BigDecimal( 5000 ) );
		s.merge( c1 );
		s.flush();
		s.clear();

		Customer c12 = ( Customer ) s.createQuery( "select c from Customer c" ).uniqueResult();

		List<CustomerInventory> inventory = c12.getInventories();

		assertEquals( 1, inventory.size() );
		assertEquals( 10, inventory.get( 0 ).getQuantity() );

		tx.rollback();
		s.close();

		assertTrue( true );
	}

	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[] {
				Customer.class,
				CustomerInventory.class,
				CustomerInventoryPK.class,
				Item.class

		};
	}
}
