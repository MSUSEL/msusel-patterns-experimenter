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
package org.hibernate.test.id.uuid.sqlrep.sqlbinary;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.test.id.uuid.sqlrep.Node;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertNotNull;

/**
 * @author Steve Ebersole
 */
public class UUIDBinaryTest extends BaseCoreFunctionalTestCase {
	@Override
	public String[] getMappings() {
		return new String[] { "id/uuid/sqlrep/Node.hbm.xml" };
	}

	@Test
	public void testUsage() {
		Session session = openSession();
		session.beginTransaction();
		Node root = new Node( "root" );
		session.save( root );
		assertNotNull( root.getId() );
		Node child = new Node( "child", root );
		session.save( child );
		assertNotNull( child.getId() );
		session.getTransaction().commit();
		session.close();

		session = openSession();
		session.beginTransaction();
		Node node = (Node) session.get( Node.class, root.getId() );
		assertNotNull( node );
		node = (Node) session.get( Node.class, child.getId() );
		assertNotNull( node );
		session.getTransaction().commit();
		session.close();

		session = openSession();
		session.beginTransaction();
		// test joining
		node = (Node) session.createQuery( "from Node n join fetch n.parent where n.parent is not null" ).uniqueResult();
		assertNotNull( node );
		assertNotNull( node.getParent() );
		session.getTransaction().commit();
		session.close();

		session = openSession();
		session.beginTransaction();
		session.delete( child );
		session.delete( root );
		session.getTransaction().commit();
		session.close();
	}
}
