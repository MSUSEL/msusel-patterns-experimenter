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
package org.hibernate.test.cascade;
import java.util.Collections;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;

/**
 * Test case to illustrate that when a delete-orphan cascade is used on a
 * one-to-many collection and the many-to-one side is also cascaded a
 * TransientObjectException is thrown.
 *
 * (based on annotations test case submitted by Edward Costello)
 *
 * @author Gail Badner
 */
public class BidirectionalOneToManyCascadeTest extends BaseCoreFunctionalTestCase {
	@Override
	public String[] getMappings() {
		return new String[] {
				"cascade/Child.hbm.xml",
				"cascade/DeleteOrphanChild.hbm.xml",
				"cascade/Parent.hbm.xml"
		};
	}

	/**
	 * Saves the parent object with a child when both the one-to-many and
	 * many-to-one associations use cascade="all"
	 */
	@Test
	public void testSaveParentWithChild() {
		Session session = openSession();
		Transaction txn = session.beginTransaction();
		Parent parent = new Parent();
		Child child = new Child();
		child.setParent( parent );
		parent.setChildren( Collections.singleton( child ) );
		session.save( parent );
		txn.commit();
		session.close();

		session = openSession();
		txn = session.beginTransaction();
		parent = ( Parent ) session.get( Parent.class, parent.getId() );
		assertEquals( 1, parent.getChildren().size() );
		assertEquals( 0, parent.getDeleteOrphanChildren().size() );
		session.delete( parent );
		txn.commit();
		session.close();
	}

	/**
	 * Saves the child object with the parent when both the one-to-many and
	 * many-to-one associations use cascade="all"
	 */
	@Test
	public void testSaveChildWithParent() {
		Session session = openSession();
		Transaction txn = session.beginTransaction();
		Parent parent = new Parent();
		Child child = new Child();
		child.setParent( parent );
		parent.setChildren( Collections.singleton( child ) );
		session.save( child );
		txn.commit();
		session.close();

		session = openSession();
		txn = session.beginTransaction();
		parent = ( Parent ) session.get( Parent.class, parent.getId() );
		assertEquals( 1, parent.getChildren().size() );
		assertEquals( 0, parent.getDeleteOrphanChildren().size() );
		session.delete( parent );
		txn.commit();
		session.close();
	}

	/**
	 * Saves the parent object with a child when the one-to-many association
	 * uses cascade="all-delete-orphan" and the many-to-one association uses
	 * cascade="all"
	 */
	@Test
	public void testSaveParentWithOrphanDeleteChild() {
		Session session = openSession();
		Transaction txn = session.beginTransaction();
		Parent parent = new Parent();
		DeleteOrphanChild child = new DeleteOrphanChild();
		child.setParent( parent );
		parent.setDeleteOrphanChildren( Collections.singleton( child ) );
		session.save( parent );
		txn.commit();
		session.close();

		session = openSession();
		txn = session.beginTransaction();
		parent = ( Parent ) session.get( Parent.class, parent.getId() );
		assertEquals( 0, parent.getChildren().size() );
		assertEquals( 1, parent.getDeleteOrphanChildren().size() );
		session.delete( parent );
		txn.commit();
		session.close();
	}

	/**
	 * Saves the child object with the parent when the one-to-many association
	 * uses cascade="all-delete-orphan" and the many-to-one association uses
	 * cascade="all"
	 */
	@Test
	public void testSaveOrphanDeleteChildWithParent() {
		Session session = openSession();
		Transaction txn = session.beginTransaction();
		Parent parent = new Parent();
		DeleteOrphanChild child = new DeleteOrphanChild();
		child.setParent( parent );
		parent.setDeleteOrphanChildren( Collections.singleton( child ) );
		session.save( child );
		txn.commit();
		session.close();

		session = openSession();
		txn = session.beginTransaction();
		parent = ( Parent ) session.get( Parent.class, parent.getId() );
		assertEquals( 0, parent.getChildren().size() );
		assertEquals( 1, parent.getDeleteOrphanChildren().size() );
		session.delete( parent );
		txn.commit();
		session.close();
	}

}
