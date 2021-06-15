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
package org.hibernate.test.annotations.derivedidentities.e1.b.specjmapid;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
/**
 * A test.
 *
 * @author <a href="mailto:stale.pedersen@jboss.org">Stale W. Pedersen</a>
 */
public class IdMapManyToOneSpecjTest extends BaseCoreFunctionalTestCase {

	public IdMapManyToOneSpecjTest() {
		System.setProperty( "hibernate.enable_specj_proprietary_syntax", "true" );
	}

	@Test
	public void testComplexIdClass() {
		Session s = openSession();
		Transaction tx = s.beginTransaction();

		Customer c1 = new Customer(
				"foo", "bar", "contact1", "100", new BigDecimal( 1000 ), new BigDecimal( 1000 ), new BigDecimal( 1000 )
		);

		s.persist( c1 );
		s.flush();
		s.clear();

		Item boat = new Item();
		boat.setId( "1" );
		boat.setName( "cruiser" );
		boat.setPrice( new BigDecimal( 500 ) );
		boat.setDescription( "a boat" );
		boat.setCategory( 42 );

		s.persist( boat );


		Item house = new Item();
		house.setId( "2" );
		house.setName( "blada" );
		house.setPrice( new BigDecimal( 5000 ) );
		house.setDescription( "a house" );
		house.setCategory( 74 );

		s.persist( house );
		s.flush();
		s.clear();

		c1.addInventory( boat, 10, new BigDecimal( 5000 ) );

		c1.addInventory( house, 100, new BigDecimal( 50000 ) );
		s.merge( c1 );
		tx.commit();
		

		tx = s.beginTransaction();
		Customer c12 = ( Customer ) s.createQuery( "select c from Customer c" ).uniqueResult();

		List<CustomerInventory> inventory = c12.getInventories();

		assertEquals( 2, inventory.size() );
		assertEquals( 10, inventory.get( 0 ).getQuantity() );
		assertEquals( "2", inventory.get(1).getVehicle().getId());


		Item house2 = new Item();
		house2.setId( "3" );
		house2.setName( "blada" );
		house2.setPrice( new BigDecimal( 5000 ) );
		house2.setDescription( "a house" );
		house2.setCategory( 74 );

		s.persist( house2 );
		s.flush();
		s.clear();

		c12.addInventory( house2, 200, new BigDecimal( 500000 ) );
		s.merge( c12 );

		s.flush();
		s.clear();

		Customer c13 = ( Customer ) s.createQuery( "select c from Customer c where c.id = " + c12.getId() )
				.uniqueResult();
		assertEquals( 3, c13.getInventories().size() );

		
		
		Customer customer2 = new Customer(
                "foo2", "bar2", "contact12", "1002", new BigDecimal( 10002 ), new BigDecimal( 10002 ), new BigDecimal( 1000 ));
		customer2.setId(2);
		s.persist(customer2);
		
		customer2.addInventory(boat, 10, new BigDecimal(400));
		customer2.addInventory(house2, 3, new BigDecimal(4000));
		s.merge(customer2);
		
		Customer c23 = ( Customer ) s.createQuery( "select c from Customer c where c.id = 2" ).uniqueResult();
		assertEquals( 2, c23.getInventories().size() );
	
		tx.rollback();
		s.close();
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
