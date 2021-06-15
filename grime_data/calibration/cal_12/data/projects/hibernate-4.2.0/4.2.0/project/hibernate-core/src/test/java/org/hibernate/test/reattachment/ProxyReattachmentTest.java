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
package org.hibernate.test.reattachment;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;

/**
 * Test of proxy reattachment semantics
 *
 * @author Steve Ebersole
 */
public class ProxyReattachmentTest extends BaseCoreFunctionalTestCase {
	public String[] getMappings() {
		return new String[] { "reattachment/Mappings.hbm.xml" };
	}

	@Test
	public void testUpdateAfterEvict() {
		Session s = openSession();
		s.beginTransaction();
		Parent p = new Parent( "p" );
		s.save( p );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		p = ( Parent ) s.load( Parent.class, "p" );
		// evict...
		s.evict( p );
		// now try to reattach...
		s.update( p );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		s.delete( p );
		s.getTransaction().commit();
		s.close();
	}

	@Test
	public void testUpdateAfterClear() {
		Session s = openSession();
		s.beginTransaction();
		Parent p = new Parent( "p" );
		s.save( p );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		p = ( Parent ) s.load( Parent.class, "p" );
		// clear...
		s.clear();
		// now try to reattach...
		s.update( p );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		s.delete( p );
		s.getTransaction().commit();
		s.close();
	}

	@Test
	@SuppressWarnings( {"unchecked"})
	public void testIterateWithClearTopOfLoop() {
		Session s = openSession();
		s.beginTransaction();
		Set parents = new HashSet();
		for (int i=0; i<5; i++) {
			Parent p = new Parent( String.valueOf( i ) );
			Child child = new Child( "child" + i );
			child.setParent( p );
			p.getChildren().add( child );
			s.save( p );
			parents.add(p);
		}
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		int i = 0;
		for ( Iterator it = s.createQuery( "from Parent" ).iterate(); it.hasNext(); ) {
			i++;
			if (i % 2 == 0) {
				s.flush();
				s.clear();
			}
			Parent p = (Parent) it.next();
			assertEquals( 1, p.getChildren().size() );
		}
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		for ( Object parent : parents ) {
			s.delete( parent );
		}
		s.getTransaction().commit();
		s.close();
	}

	@Test
	@SuppressWarnings( {"unchecked"})
	public void testIterateWithClearBottomOfLoop() {
		Session s = openSession();
		s.beginTransaction();
		Set parents = new HashSet();
		for (int i=0; i<5; i++) {
			Parent p = new Parent( String.valueOf( i ) );
			Child child = new Child( "child" + i );
			child.setParent( p );
			p.getChildren().add( child );
			s.save( p );
			parents.add(p);
		}
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		int i = 0;
		for (Iterator it = s.createQuery( "from Parent" ).iterate(); it.hasNext(); ) {
			Parent p = (Parent) it.next();
			assertEquals( 1, p.getChildren().size() );
			i++;
			if (i % 2 == 0) {
				s.flush();
				s.clear();
			}
		}
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		for ( Object parent : parents ) {
			s.delete( parent );
		}
		s.getTransaction().commit();
		s.close();
	}

	@Test
	@SuppressWarnings( {"unchecked"})
	public void testIterateWithEvictTopOfLoop() {
		Session s = openSession();
		s.beginTransaction();
		Set parents = new HashSet();
		for (int i=0; i<5; i++) {
			Parent p = new Parent( String.valueOf( i + 100 ) );
			Child child = new Child( "child" + i );
			child.setParent( p );
			p.getChildren().add( child );
			s.save( p );
			parents.add(p);
		}
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		Parent p = null;
		for (Iterator it = s.createQuery( "from Parent" ).iterate(); it.hasNext(); ) {
			if ( p != null) { s.evict(p); }
			p = (Parent) it.next();
			assertEquals( 1, p.getChildren().size() );
		}
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		for ( Object parent : parents ) {
			s.delete( parent );
		}
		s.getTransaction().commit();
		s.close();
	}

	@Test
	@SuppressWarnings( {"unchecked"})
	public void testIterateWithEvictBottomOfLoop() {
		Session s = openSession();
		s.beginTransaction();
		Set parents = new HashSet();
		for (int i=0; i<5; i++) {
			Parent p = new Parent( String.valueOf( i + 100 ) );
			Child child = new Child( "child" + i );
			child.setParent( p );
			p.getChildren().add( child );
			s.save( p );
			parents.add(p);
		}
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		for (Iterator it = s.createQuery( "from Parent" ).iterate(); it.hasNext(); ) {
			Parent p = (Parent) it.next();
			assertEquals( 1, p.getChildren().size() );
			s.evict(p);
		}
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		for ( Object parent : parents ) {
			s.delete( parent );
		}
		s.getTransaction().commit();
		s.close();
	}
}
