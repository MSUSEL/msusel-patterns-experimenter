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
package org.hibernate.test.collection.set;

import java.util.HashSet;

import org.junit.Test;

import org.hibernate.CacheMode;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.collection.internal.PersistentSet;
import org.hibernate.criterion.Restrictions;
import org.hibernate.stat.CollectionStatistics;
import org.hibernate.testing.FailureExpected;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Steve Ebersole
 */
public class PersistentSetTest extends BaseCoreFunctionalTestCase {
	@Override
	public String[] getMappings() {
		return new String[] { "collection/set/Mappings.hbm.xml" };
	}

	@Override
	public void configure(Configuration cfg) {
		super.configure( cfg );
		cfg.setProperty( Environment.GENERATE_STATISTICS, "true" );
	}

	@Test
	public void testWriteMethodDirtying() {
		Parent parent = new Parent( "p1" );
		Child child = new Child( "c1" );
		parent.getChildren().add( child );
		child.setParent( parent );
		Child otherChild = new Child( "c2" );

		Session session = openSession();
		session.beginTransaction();
		session.save( parent );
		session.flush();
		// at this point, the set on parent has now been replaced with a PersistentSet...
		PersistentSet children = ( PersistentSet ) parent.getChildren();

		assertFalse( children.add( child ) );
		assertFalse( children.isDirty() );

		assertFalse( children.remove( otherChild ) );
		assertFalse( children.isDirty() );

		HashSet otherSet = new HashSet();
		otherSet.add( child );
		assertFalse( children.addAll( otherSet ) );
		assertFalse( children.isDirty() );

		assertFalse( children.retainAll( otherSet ) );
		assertFalse( children.isDirty() );

		otherSet = new HashSet();
		otherSet.add( otherChild );
		assertFalse( children.removeAll( otherSet ) );
		assertFalse( children.isDirty() );

		assertTrue( children.retainAll( otherSet ));
		assertTrue( children.isDirty() );
		assertTrue( children.isEmpty() );

		children.clear();
		session.delete( child );
		assertTrue( children.isDirty() );

		session.flush();

		children.clear();
		assertFalse( children.isDirty() );

		session.delete( parent );
		session.getTransaction().commit();
		session.close();
	}

	@Test
	public void testCollectionMerging() {
		Session session = openSession();
		session.beginTransaction();
		Parent parent = new Parent( "p1" );
		Child child = new Child( "c1" );
		parent.getChildren().add( child );
		child.setParent( parent );
		session.save( parent );
		session.getTransaction().commit();
		session.close();

		CollectionStatistics stats =  sessionFactory().getStatistics().getCollectionStatistics( Parent.class.getName() + ".children" );
		long recreateCount = stats.getRecreateCount();
		long updateCount = stats.getUpdateCount();

		session = openSession();
		session.beginTransaction();
		parent = ( Parent ) session.merge( parent );
		session.getTransaction().commit();
		session.close();

		assertEquals( 1, parent.getChildren().size() );
		assertEquals( recreateCount, stats.getRecreateCount() );
		assertEquals( updateCount, stats.getUpdateCount() );

		session = openSession();
		session.beginTransaction();
		parent = ( Parent ) session.get( Parent.class, "p1" );
		assertEquals( 1, parent.getChildren().size() );
		session.delete( parent );
		session.getTransaction().commit();
		session.close();
	}

	@Test
	public void testCollectiondirtyChecking() {
		Session session = openSession();
		session.beginTransaction();
		Parent parent = new Parent( "p1" );
		Child child = new Child( "c1" );
		parent.getChildren().add( child );
		child.setParent( parent );
		session.save( parent );
		session.getTransaction().commit();
		session.close();

		CollectionStatistics stats =  sessionFactory().getStatistics().getCollectionStatistics( Parent.class.getName() + ".children" );
		long recreateCount = stats.getRecreateCount();
		long updateCount = stats.getUpdateCount();

		session = openSession();
		session.beginTransaction();
		parent = ( Parent ) session.get( Parent.class, "p1" );
		assertEquals( 1, parent.getChildren().size() );
		session.getTransaction().commit();
		session.close();

		assertEquals( 1, parent.getChildren().size() );
		assertEquals( recreateCount, stats.getRecreateCount() );
		assertEquals( updateCount, stats.getUpdateCount() );

		session = openSession();
		session.beginTransaction();
		assertEquals( 1, parent.getChildren().size() );
		session.delete( parent );
		session.getTransaction().commit();
		session.close();
	}

	@Test
	public void testCompositeElementWriteMethodDirtying() {
		Container container = new Container( "p1" );
		Container.Content c1 = new Container.Content( "c1" );
		container.getContents().add( c1 );
		Container.Content c2 = new Container.Content( "c2" );

		Session session = openSession();
		session.beginTransaction();
		session.save( container );
		session.flush();
		// at this point, the set on container has now been replaced with a PersistentSet...
		PersistentSet children = (PersistentSet) container.getContents();

		assertFalse( children.add( c1 ) );
		assertFalse( children.isDirty() );

		assertFalse( children.remove( c2 ) );
		assertFalse( children.isDirty() );

		HashSet otherSet = new HashSet();
		otherSet.add( c1 );
		assertFalse( children.addAll( otherSet ) );
		assertFalse( children.isDirty() );

		assertFalse( children.retainAll( otherSet ) );
		assertFalse( children.isDirty() );

		otherSet = new HashSet();
		otherSet.add( c2 );
		assertFalse( children.removeAll( otherSet ) );
		assertFalse( children.isDirty() );

		assertTrue( children.retainAll( otherSet ));
		assertTrue( children.isDirty() );
		assertTrue( children.isEmpty() );

		children.clear();
		assertTrue( children.isDirty() );

		session.flush();

		children.clear();
		assertFalse( children.isDirty() );

		session.delete( container );
		session.getTransaction().commit();
		session.close();
	}

	@Test
	@FailureExpected( jiraKey = "HHH-2485" )
	public void testCompositeElementMerging() {
		Session session = openSession();
		session.beginTransaction();
		Container container = new Container( "p1" );
		Container.Content c1 = new Container.Content( "c1" );
		container.getContents().add( c1 );
		session.save( container );
		session.getTransaction().commit();
		session.close();

		CollectionStatistics stats =  sessionFactory().getStatistics().getCollectionStatistics( Container.class.getName() + ".contents" );
		long recreateCount = stats.getRecreateCount();
		long updateCount = stats.getUpdateCount();

		container.setName( "another name" );

		session = openSession();
		session.beginTransaction();
		container = ( Container ) session.merge( container );
		session.getTransaction().commit();
		session.close();

		assertEquals( 1, container.getContents().size() );
		assertEquals( recreateCount, stats.getRecreateCount() );
		assertEquals( updateCount, stats.getUpdateCount() );

		session = openSession();
		session.beginTransaction();
		container = ( Container ) session.get( Container.class, container.getId() );
		assertEquals( 1, container.getContents().size() );
		session.delete( container );
		session.getTransaction().commit();
		session.close();
	}

	@Test
	@FailureExpected( jiraKey = "HHH-2485" )
	public void testCompositeElementCollectionDirtyChecking() {
		Session session = openSession();
		session.beginTransaction();
		Container container = new Container( "p1" );
		Container.Content c1 = new Container.Content( "c1" );
		container.getContents().add( c1 );
		session.save( container );
		session.getTransaction().commit();
		session.close();

		CollectionStatistics stats =  sessionFactory().getStatistics().getCollectionStatistics( Container.class.getName() + ".contents" );
		long recreateCount = stats.getRecreateCount();
		long updateCount = stats.getUpdateCount();

		session = openSession();
		session.beginTransaction();
		container = ( Container ) session.get( Container.class, container.getId() );
		assertEquals( 1, container.getContents().size() );
		session.getTransaction().commit();
		session.close();

		assertEquals( 1, container.getContents().size() );
		assertEquals( recreateCount, stats.getRecreateCount() );
		assertEquals( updateCount, stats.getUpdateCount() );

		session = openSession();
		session.beginTransaction();
		container = ( Container ) session.get( Container.class, container.getId() );
		assertEquals( 1, container.getContents().size() );
		session.delete( container );
		session.getTransaction().commit();
		session.close();
	}

	@Test
	public void testLoadChildCheckParentContainsChildCache() {
		Parent parent = new Parent( "p1" );
		Child child = new Child( "c1" );
		child.setDescription( "desc1" );
		parent.getChildren().add( child );
		child.setParent( parent );
		Child otherChild = new Child( "c2" );
		otherChild.setDescription( "desc2" );
		parent.getChildren().add( otherChild );
		otherChild.setParent( parent );

		Session session = openSession();
		session.beginTransaction();
		session.save( parent );
		session.getTransaction().commit();

		session = openSession();
		session.beginTransaction();
		parent = ( Parent ) session.get( Parent.class, parent.getName() );
		assertTrue( parent.getChildren().contains( child ) );
		assertTrue( parent.getChildren().contains( otherChild ) );
		session.getTransaction().commit();

		session = openSession();
		session.beginTransaction();

		child = ( Child ) session.get( Child.class, child.getName() );
		assertTrue( child.getParent().getChildren().contains( child ) );
		session.clear();

		child = ( Child ) session.createCriteria( Child.class, child.getName() )
				.setCacheable( true )
				.add( Restrictions.idEq( "c1" ) )
				.uniqueResult();
		assertTrue( child.getParent().getChildren().contains( child ) );
		assertTrue( child.getParent().getChildren().contains( otherChild ) );
		session.clear();

		child = ( Child ) session.createCriteria( Child.class, child.getName() )
				.setCacheable( true )
				.add( Restrictions.idEq( "c1" ) )
				.uniqueResult();
		assertTrue( child.getParent().getChildren().contains( child ) );
		assertTrue( child.getParent().getChildren().contains( otherChild ) );
		session.clear();

		child = ( Child ) session.createQuery( "from Child where name = 'c1'" )
				.setCacheable( true )
				.uniqueResult();
		assertTrue( child.getParent().getChildren().contains( child ) );

		child = ( Child ) session.createQuery( "from Child where name = 'c1'" )
				.setCacheable( true )
				.uniqueResult();
		assertTrue( child.getParent().getChildren().contains( child ) );

		session.delete( child.getParent() );
		session.getTransaction().commit();
		session.close();
	}

	@Test
	public void testLoadChildCheckParentContainsChildNoCache() {
		Parent parent = new Parent( "p1" );
		Child child = new Child( "c1" );
		parent.getChildren().add( child );
		child.setParent( parent );
		Child otherChild = new Child( "c2" );
		parent.getChildren().add( otherChild );
		otherChild.setParent( parent );

		Session session = openSession();
		session.beginTransaction();
		session.save( parent );
		session.getTransaction().commit();

		session = openSession();
		session.beginTransaction();
		session.setCacheMode( CacheMode.IGNORE );
		parent = ( Parent ) session.get( Parent.class, parent.getName() );
		assertTrue( parent.getChildren().contains( child ) );
		assertTrue( parent.getChildren().contains( otherChild ) );
		session.getTransaction().commit();

		session = openSession();
		session.beginTransaction();
		session.setCacheMode( CacheMode.IGNORE );

		child = ( Child ) session.get( Child.class, child.getName() );
		assertTrue( child.getParent().getChildren().contains( child ) );
		session.clear();

		child = ( Child ) session.createCriteria( Child.class, child.getName() )
				.add( Restrictions.idEq( "c1" ) )
				.uniqueResult();
		assertTrue( child.getParent().getChildren().contains( child ) );
		assertTrue( child.getParent().getChildren().contains( otherChild ) );
		session.clear();

		child = ( Child ) session.createQuery( "from Child where name = 'c1'" ).uniqueResult();
		assertTrue( child.getParent().getChildren().contains( child ) );

		session.delete( child.getParent() );
		session.getTransaction().commit();
		session.close();
	}
}
