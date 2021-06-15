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
package org.hibernate.test.annotations.derivedidentities.e1.b.specjmapid.lazy;

import java.math.BigDecimal;
import java.util.List;

import junit.framework.Assert;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.test.annotations.derivedidentities.e1.b.specjmapid.Item;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Test;

public class CompositeKeyDeleteTest extends BaseCoreFunctionalTestCase {

   public String[] getMappings() {
      return new String[] { "annotations/derivedidentities/e1/b/specjmapid/lazy/order_orm.xml" };
   }

   public CompositeKeyDeleteTest() {
      System.setProperty( "hibernate.enable_specj_proprietary_syntax", "true" );
   }
   /**
    * This test checks to make sure the non null column is not updated with a
    * null value when a CustomerInventory is removed.
    */
   @Test
   public void testRemove() {
      Session s = openSession();
      Transaction tx = s.beginTransaction();

      CustomerTwo c1 = new CustomerTwo(
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
      Integer id = c1.getId();
      tx.commit();
      s.close();
      
      s = openSession();
      tx = s.beginTransaction();

      CustomerTwo c12 = ( CustomerTwo) s.createQuery( "select c from CustomerTwo c" ).uniqueResult();
      Assert.assertNotNull(c12);
      List<CustomerInventoryTwo> list = c12.getInventories();
      Assert.assertNotNull(list);
      Assert.assertEquals(2, list.size());
      CustomerInventoryTwo ci = list.get(1);
      list.remove(ci);
      s.delete(ci);
      s.flush();
      
      tx.commit();//fail
      s.close();

   }

   @Override
   protected Class[] getAnnotatedClasses() {
      return new Class[] {
            CustomerTwo.class,
            CustomerInventoryTwo.class,
            CustomerInventoryTwoPK.class,
            Item.class

      };
   }
}
