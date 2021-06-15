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
package org.hibernate.ejb.test.ops;

import java.util.Map;
import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Test;

import org.hibernate.cfg.Environment;
import org.hibernate.ejb.EntityManagerFactoryImpl;
import org.hibernate.ejb.test.BaseEntityManagerFunctionalTestCase;

/**
 * @author Gavin King
 * @author Hardy Ferentschik
 */
public class MergeTest extends BaseEntityManagerFunctionalTestCase {
	@Test
	public void testMergeTree() {
		clearCounts();

		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		Node root = new Node( "root" );
		Node child = new Node( "child" );
		root.addChild( child );
		em.persist( root );
		em.getTransaction().commit();
		em.close();

		assertInsertCount( 2 );
		clearCounts();

		root.setDescription( "The root node" );
		child.setDescription( "The child node" );

		Node secondChild = new Node( "second child" );

		root.addChild( secondChild );

		em = getOrCreateEntityManager();
		em.getTransaction().begin();
		em.merge( root );
		em.getTransaction().commit();
		em.close();

		assertInsertCount( 1 );
		assertUpdateCount( 2 );
	}

	public void testMergeTreeWithGeneratedId() {
		clearCounts();

		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		NumberedNode root = new NumberedNode( "root" );
		NumberedNode child = new NumberedNode( "child" );
		root.addChild( child );
		em.persist( root );
		em.getTransaction().commit();
		em.close();

		assertInsertCount( 2 );
		clearCounts();

		root.setDescription( "The root node" );
		child.setDescription( "The child node" );

		NumberedNode secondChild = new NumberedNode( "second child" );

		root.addChild( secondChild );

		em = getOrCreateEntityManager();
		em.getTransaction().begin();
		em.merge( root );
		em.getTransaction().commit();
		em.close();

		assertInsertCount( 1 );
		assertUpdateCount( 2 );
	}

	private void clearCounts() {
		( ( EntityManagerFactoryImpl ) entityManagerFactory() ).getSessionFactory().getStatistics().clear();
	}

	private void assertInsertCount(int count) {
		int inserts = ( int ) ( ( EntityManagerFactoryImpl ) entityManagerFactory() ).getSessionFactory()
				.getStatistics()
				.getEntityInsertCount();
		Assert.assertEquals( count, inserts );
	}

	private void assertUpdateCount(int count) {
		int updates = ( int ) ( ( EntityManagerFactoryImpl ) entityManagerFactory() ).getSessionFactory()
				.getStatistics()
				.getEntityUpdateCount();
		Assert.assertEquals( count, updates );
	}

	@Override
	@SuppressWarnings( {"unchecked"})
	protected void addConfigOptions(Map options) {
		options.put( Environment.GENERATE_STATISTICS, "true" );
		options.put( Environment.STATEMENT_BATCH_SIZE, "0" );
	}

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[0];
	}

	@Override
	protected String[] getMappings() {
		return new String[] { "org/hibernate/ejb/test/ops/Node.hbm.xml" };
	}
}

