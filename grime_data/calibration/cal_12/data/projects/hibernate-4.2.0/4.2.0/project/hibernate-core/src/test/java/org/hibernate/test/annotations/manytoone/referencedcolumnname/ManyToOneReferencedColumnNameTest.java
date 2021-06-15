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
package org.hibernate.test.annotations.manytoone.referencedcolumnname;

import java.math.BigDecimal;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.testing.DialectChecks;
import org.hibernate.testing.RequiresDialectFeature;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

/**
 * @author Emmanuel Bernard
 */
public class ManyToOneReferencedColumnNameTest extends BaseCoreFunctionalTestCase {
	@Test
	@RequiresDialectFeature(DialectChecks.SupportsIdentityColumns.class)
	public void testReoverableExceptionInFkOrdering() throws Exception {
		//SF should not blow up
		Vendor v = new Vendor();
		Item i = new Item();
		ZItemCost ic = new ZItemCost();
		ic.setCost( new BigDecimal( 2 ) );
		ic.setItem( i );
		ic.setVendor( v );
		WarehouseItem wi = new WarehouseItem();
		wi.setDefaultCost( ic );
		wi.setItem( i );
		wi.setVendor( v );
		wi.setQtyInStock( new BigDecimal( 2 ) );
		Session s = openSession();
		s.getTransaction().begin();
		s.save( i );
		s.save( v );
		s.save( ic );
		s.save( wi );
		s.flush();
		s.getTransaction().rollback();
		s.close();
	}

	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[] {
				Item.class,
				Vendor.class,
				WarehouseItem.class,
				ZItemCost.class
		};
	}
}
