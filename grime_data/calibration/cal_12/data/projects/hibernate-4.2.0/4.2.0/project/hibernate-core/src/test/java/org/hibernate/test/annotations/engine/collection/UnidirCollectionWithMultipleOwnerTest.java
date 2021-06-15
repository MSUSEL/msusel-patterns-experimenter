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
package org.hibernate.test.annotations.engine.collection;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

/**
 * @author Emmanuel Bernard
 */
public class UnidirCollectionWithMultipleOwnerTest extends BaseCoreFunctionalTestCase {
	@Test
	public void testUnidirCollectionWithMultipleOwner() throws Exception {
		Session s = openSession();
		Transaction tx;
		tx = s.beginTransaction();
		Father father = new Father();
		Mother mother = new Mother();
		s.save( father );
		//s.save( mother );
		Son son = new Son();
		father.getOrderedSons().add( son );
		son.setFather( father );
		mother.getSons().add( son );
		son.setMother( mother );
		s.save( mother );
		s.save( father );
		tx.commit();

		s.clear();

		tx = s.beginTransaction();
		son = (Son) s.get( Son.class, son.getId() );
		s.delete( son );
		s.flush();
		father = (Father) s.get( Father.class, father.getId() );
		mother = (Mother) s.get( Mother.class, mother.getId() );
		s.delete( father );
		s.delete( mother );
		tx.commit();
		s.close();
	}

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] {
				Father.class,
				Mother.class,
				Son.class
		};
	}
}
