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
/*
  * Hibernate, Relational Persistence for Idiomatic Java
  *
  * Copyright (c) 2009, Red Hat, Inc. and/or its affiliates or third-
  * party contributors as indicated by the @author tags or express 
  * copyright attribution statements applied by the authors.  
  * All third-party contributions are distributed under license by 
  * Red Hat, Inc.
  *
  * This copyrighted material is made available to anyone wishing to 
  * use, modify, copy, or redistribute it subject to the terms and 
  * conditions of the GNU Lesser General Public License, as published 
  * by the Free Software Foundation.
  *
  * This program is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of 
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU 
  * Lesser General Public License for more details.
  *
  * You should have received a copy of the GNU Lesser General Public 
  * License along with this distribution; if not, write to:
  * 
  * Free Software Foundation, Inc.
  * 51 Franklin Street, Fifth Floor
  * Boston, MA  02110-1301  USA
  */
package org.hibernate.test.annotations.subselect;

import org.junit.Assert;
import org.junit.Test;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.hibernate.type.StringType;

/**
 * @author Sharath Reddy
 */
public class SubselectTest extends BaseCoreFunctionalTestCase {
	@Test
	public void testSubselectWithSynchronize() {

		Session s = openSession();
		Transaction tx = s.beginTransaction();

		//We don't use auto-generated ids because these seem to cause the session to flush.
		//We want to test that the session flushes because of the 'synchronize' annotation
		long itemId = 1;
		Item item = new Item();
		item.setName("widget");
		item.setId(itemId);
		s.save(item);
		
		Bid bid1 = new Bid();
		bid1.setAmount(100.0);
		bid1.setItemId(itemId);
		bid1.setId(1);
		s.save(bid1);
		
		Bid bid2 = new Bid();
		bid2.setAmount(200.0);
		bid2.setItemId(itemId);
		bid2.setId(2);
		s.save(bid2);
		
		//Because we use 'synchronize' annotation, this query should trigger session flush
		Query query = s.createQuery("from HighestBid b where b.name = :name");
		query.setParameter( "name", "widget", StringType.INSTANCE );
		HighestBid highestBid = (HighestBid) query.list().iterator().next();
		
		Assert.assertEquals( 200.0, highestBid.getAmount(), 0.01 );
		tx.rollback();		
		s.close();	
		
		
	}
	
	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class[]{
				Item.class,
				Bid.class,
				HighestBid.class
		};
	}

}
