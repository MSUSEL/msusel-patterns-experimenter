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
 * Test case to illustrate that when a child table attempts to cascade to a parent and the parent's Id
 * is set to assigned, an exception thrown (not-null property references a null or transient value). 
 * This error only occurs if the parent link in marked as not nullable.
 *
 * @author Wallace Wadge (based on code by Gail Badner)
 */
public class CascadeTestWithAssignedParentIdTest extends BaseCoreFunctionalTestCase {
	public String[] getMappings() {
		return new String[] {
				"cascade/ChildForParentWithAssignedId.hbm.xml",
				"cascade/ParentWithAssignedId.hbm.xml"
		};
	}

	@Test
	@SuppressWarnings( {"UnnecessaryBoxing"})
	public void testSaveChildWithParent() {
		Session session = openSession();
		Transaction txn = session.beginTransaction();
		Parent parent = new Parent();
		Child child = new Child();
		child.setParent( parent );
		parent.setChildren( Collections.singleton( child ) );
		parent.setId( Long.valueOf(123L) );
		// this should figure out that the parent needs saving first since id is assigned.
		session.save( child );
		txn.commit();
		session.close();

		session = openSession();
		txn = session.beginTransaction();
		parent = ( Parent ) session.get( Parent.class, parent.getId() );
		assertEquals( 1, parent.getChildren().size() );
		txn.commit();
		session.close();
	}
}
