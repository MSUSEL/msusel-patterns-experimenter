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
package org.hibernate.test.onetomany;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;

/**
 * @author Gavin King
 */
public class OneToManyTest extends BaseCoreFunctionalTestCase {
	public String[] getMappings() {
		return new String[] { "onetomany/Parent.hbm.xml" };
	}

	@SuppressWarnings( {"unchecked", "UnusedAssignment"})
	@Test
	public void testOneToManyLinkTable() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Child c = new Child();
		c.setName("Child One");
		Parent p = new Parent();
		p.setName("Parent");
		p.getChildren().add(c);
		c.setParent(p);
		s.save(p);
		s.flush();
		
		p.getChildren().remove(c);
		c.setParent(null);
		s.flush();
		
		p.getChildren().add(c);
		c.setParent(p);
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		c.setParent(null);
		s.update(c);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		c.setParent(p);
		s.update(c);
		t.commit();
		s.close();

		
		s = openSession();
		t = s.beginTransaction();
		c = (Child) s.createQuery("from Child").uniqueResult();
		s.createQuery("from Child c left join fetch c.parent").list();
		s.createQuery("from Child c inner join fetch c.parent").list();
		s.clear();
		p = (Parent) s.createQuery("from Parent p left join fetch p.children").uniqueResult();
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		s.createQuery("delete from Child").executeUpdate();
		s.createQuery("delete from Parent").executeUpdate();		
		t.commit();
		s.close();

	}

	@Test
	public void testManyToManySize() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		assertEquals( 0, s.createQuery("from Parent p where size(p.children) = 0").list().size() );
		assertEquals( 0, s.createQuery("from Parent p where p.children.size = 0").list().size() );
		t.commit();
		s.close();
	}

}

