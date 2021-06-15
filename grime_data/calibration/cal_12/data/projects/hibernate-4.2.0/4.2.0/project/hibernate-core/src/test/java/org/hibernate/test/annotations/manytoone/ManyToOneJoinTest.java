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
package org.hibernate.test.annotations.manytoone;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Emmanuel Bernard
 */
public class ManyToOneJoinTest extends BaseCoreFunctionalTestCase {
	@Test
	public void testManyToOneJoinTable() throws Exception {
		Session s = openSession();
		Transaction tx = s.beginTransaction();
		ForestType forest = new ForestType();
		forest.setName( "Original forest" );
		s.persist( forest );
		TreeType tree = new TreeType();
		tree.setForestType( forest );
		tree.setAlternativeForestType( forest );
		tree.setName( "just a tree");
		s.persist( tree );
		s.flush();
		s.clear();
		tree = (TreeType) s.get(TreeType.class, tree.getId() );
		assertNotNull( tree.getForestType() );
		assertNotNull( tree.getAlternativeForestType() );
		s.clear();
		forest = (ForestType) s.get( ForestType.class, forest.getId() );
		assertEquals( 1, forest.getTrees().size() );
		assertEquals( tree.getId(), forest.getTrees().iterator().next().getId() );
		tx.rollback();
		s.close();
	}

	@Test
	public void testOneToOneJoinTable() throws Exception {
		Session s = openSession();
		Transaction tx = s.beginTransaction();
		ForestType forest = new ForestType();
		forest.setName( "Original forest" );
		s.persist( forest );
		BiggestForest forestRepr = new BiggestForest();
		forestRepr.setType( forest );
		forest.setBiggestRepresentative( forestRepr );
		s.persist( forestRepr );
		s.flush();
		s.clear();
		forest = (ForestType) s.get( ForestType.class, forest.getId() );
		assertNotNull( forest.getBiggestRepresentative() );
		assertEquals( forest, forest.getBiggestRepresentative().getType() );
		tx.rollback();
		s.close();
	}

	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[] {
				BiggestForest.class,
				ForestType.class,
				TreeType.class
		};
	}
}
