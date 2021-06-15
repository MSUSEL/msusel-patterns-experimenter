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
package org.hibernate.test.annotations.cascade.circle.identity;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.testing.DialectChecks;
import org.hibernate.testing.RequiresDialectFeature;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

@RequiresDialectFeature(DialectChecks.SupportsIdentityColumns.class)
public class CascadeCircleIdentityIdTest extends BaseCoreFunctionalTestCase {
	@Test
	@TestForIssue( jiraKey = "HHH-5472" )
	public void testCascade() {
		A a = new A();
		B b = new B();
		C c = new C();
		D d = new D();
		E e = new E();
		F f = new F();
		G g = new G();
		H h = new H();

		a.getBCollection().add(b);
		b.setA(a);
		
		a.getCCollection().add(c);
		c.setA(a);
		
		b.getCCollection().add(c);
		c.setB(b);
		
		a.getDCollection().add(d);
		d.getACollection().add(a);
		
		d.getECollection().add(e);
		e.setF(f);
		
		f.getBCollection().add(b);
		b.setF(f);
		
		c.setG(g);
		g.getCCollection().add(c);
		
		f.setH(h);
		h.setG(g);
		
		Session s;
		s = openSession();
		s.getTransaction().begin();
		try {
			// Fails: says that C.b is null (even though it isn't). Doesn't fail if you persist c, g or h instead of a
			s.persist(a);
			s.flush();
		} finally {
			s.getTransaction().rollback();
			s.close();
		}
	}

	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[]{
				A.class,
				B.class,
				C.class,
				D.class,
				E.class,
				F.class,
				G.class,
				H.class
		};
	}

}
