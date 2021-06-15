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
package org.hibernate.test.nonflushedchanges;

import java.util.ArrayList;
import java.util.Collection;
import javax.transaction.RollbackException;

import org.junit.Assert;
import org.junit.Test;

import org.hibernate.Hibernate;
import org.hibernate.PersistentObjectException;
import org.hibernate.Session;
import org.hibernate.engine.transaction.internal.jta.JtaStatusHelper;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.testing.jta.TestingJtaPlatformImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * @author Gavin King, Gail Badner (adapted this from "ops" tests version)
 */
public class CreateTest extends AbstractOperationTestCase {
	@Test
	@SuppressWarnings( {"unchecked"})
	public void testNoUpdatesOnCreateVersionedWithCollection() throws Exception {
		clearCounts();

		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		Session s = openSession();
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		VersionedEntity root = new VersionedEntity( "root", "root" );
		VersionedEntity child = new VersionedEntity( "c1", "child-1" );
		root.getChildren().add( child );
		child.setParent( root );
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		s.save( root );
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		root = ( VersionedEntity ) getOldToNewEntityRefMap().get( root );
		applyNonFlushedChangesToNewSessionCloseOldSession( s );
		root = ( VersionedEntity ) getOldToNewEntityRefMap().get( root );
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().commit();

		assertInsertCount( 2 );
		assertUpdateCount( 0 );
		assertDeleteCount( 0 );

		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		s = openSession();
		s.delete( root );
		applyNonFlushedChangesToNewSessionCloseOldSession( s );
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().commit();

		assertUpdateCount( 0 );
		assertDeleteCount( 2 );
	}

	@Test
	public void testCreateTree() throws Exception {
		clearCounts();

		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		Session s = openSession();
		Node root = new Node( "root" );
		Node child = new Node( "child" );
		root.addChild( child );
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		s.persist( root );
		applyNonFlushedChangesToNewSessionCloseOldSession( s );
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().commit();

		assertInsertCount( 2 );
		assertUpdateCount( 0 );

		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		s = openSession();
		System.out.println( "getting" );
		root = ( Node ) s.get( Node.class, "root" );
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		root = ( Node ) getOldToNewEntityRefMap().get( root );
		Node child2 = new Node( "child2" );
		root.addChild( child2 );
		System.out.println( "committing" );
		applyNonFlushedChangesToNewSessionCloseOldSession( s );
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().commit();

		assertInsertCount( 3 );
		assertUpdateCount( 0 );
	}

	@Test
	@SuppressWarnings( {"UnnecessaryBoxing"})
	public void testCreateTreeWithGeneratedId() throws Exception {
		clearCounts();

		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		Session s = openSession();
		NumberedNode root = new NumberedNode( "root" );
		NumberedNode child = new NumberedNode( "child" );
		root.addChild( child );
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		s.persist( root );
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		root = ( NumberedNode ) getOldToNewEntityRefMap().get( root );
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().commit();

		assertInsertCount( 2 );
		assertUpdateCount( 0 );

		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		s = openSession();
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		root = ( NumberedNode ) s.get( NumberedNode.class, Long.valueOf( root.getId() ) );
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		NumberedNode child2 = new NumberedNode( "child2" );
		root = ( NumberedNode ) getOldToNewEntityRefMap().get( root );
		root.addChild( child2 );
		applyNonFlushedChangesToNewSessionCloseOldSession( s );
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().commit();

		assertInsertCount( 3 );
		assertUpdateCount( 0 );
	}

	@Test
	public void testCreateException() throws Exception {
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		Session s = openSession();
		Node dupe = new Node( "dupe" );
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		s.persist( dupe );
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		dupe = ( Node ) getOldToNewEntityRefMap().get( dupe );
		s.persist( dupe );
		applyNonFlushedChangesToNewSessionCloseOldSession( s );
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().commit();

		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		s = openSession();
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		s.persist( dupe );
		applyNonFlushedChangesToNewSessionCloseOldSession( s );
		try {
			TestingJtaPlatformImpl.INSTANCE.getTransactionManager().commit();
			Assert.fail();
		}
		catch ( ConstraintViolationException cve ) {
			//verify that an exception is thrown!
		}
		catch ( RollbackException e ) {
			if ( ! ConstraintViolationException.class.isInstance( e.getCause() ) ) {
				throw (Exception) e.getCause();
			}
		}
		if ( JtaStatusHelper.isActive( TestingJtaPlatformImpl.INSTANCE.getTransactionManager() ) ) {
			// ugh! really!?!
			TestingJtaPlatformImpl.INSTANCE.getTransactionManager().rollback();
		}

		Node nondupe = new Node( "nondupe" );
		nondupe.addChild( dupe );

		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		s = openSession();
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		s.persist( nondupe );
		applyNonFlushedChangesToNewSessionCloseOldSession( s );
		try {
			TestingJtaPlatformImpl.INSTANCE.getTransactionManager().commit();
			Assert.fail();
		}
		catch ( ConstraintViolationException cve ) {
			//verify that an exception is thrown!
		}
		catch ( RollbackException e ) {
			if ( ! ConstraintViolationException.class.isInstance( e.getCause() ) ) {
				throw (Exception) e.getCause();
			}
		}
		if ( JtaStatusHelper.isActive( TestingJtaPlatformImpl.INSTANCE.getTransactionManager() ) ) {
			// ugh! really!?!
			TestingJtaPlatformImpl.INSTANCE.getTransactionManager().rollback();
		}
	}

	@Test
	public void testCreateExceptionWithGeneratedId() throws Exception {
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		Session s = openSession();
		NumberedNode dupe = new NumberedNode( "dupe" );
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		s.persist( dupe );
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		dupe = ( NumberedNode ) getOldToNewEntityRefMap().get( dupe );
		s.persist( dupe );
		applyNonFlushedChangesToNewSessionCloseOldSession( s );
		dupe = ( NumberedNode ) getOldToNewEntityRefMap().get( dupe );
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().commit();

		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		s = openSession();
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		try {
			s.persist( dupe );
			s.flush();
			assertFalse( true );
		}
		catch ( PersistentObjectException poe ) {
			//verify that an exception is thrown!
		}
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().rollback();

		NumberedNode nondupe = new NumberedNode( "nondupe" );
		nondupe.addChild( dupe );

		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		s = openSession();
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		try {
			s.persist( nondupe );
			assertFalse( true );
		}
		catch ( PersistentObjectException poe ) {
			//verify that an exception is thrown!
		}
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().rollback();
	}

	@Test
	@SuppressWarnings( {"unchecked"})
	public void testBasic() throws Exception {
		Session s;
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		s = openSession();
		Employer er = new Employer();
		Employee ee = new Employee();
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		s.persist( ee );
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		ee = ( Employee ) getOldToNewEntityRefMap().get( ee );
		Collection erColl = new ArrayList();
		Collection eeColl = new ArrayList();
		erColl.add( ee );
		eeColl.add( er );
		er.setEmployees( erColl );
		ee.setEmployers( eeColl );
		applyNonFlushedChangesToNewSessionCloseOldSession( s );
		ee = ( Employee ) getOldToNewEntityRefMap().get( ee );
		er = ( Employer ) ee.getEmployers().iterator().next();
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().commit();

		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		s = openSession();
		er = ( Employer ) s.load( Employer.class, er.getId() );
		assertNotNull( er );
		assertFalse( Hibernate.isInitialized( er ) );
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		er = ( Employer ) getOldToNewEntityRefMap().get( er );
		assertNotNull( er );
		assertFalse( Hibernate.isInitialized( er ) );
		assertNotNull( er.getEmployees() );
		assertEquals( 1, er.getEmployees().size() );
		Employee eeFromDb = ( Employee ) er.getEmployees().iterator().next();
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		eeFromDb = ( Employee ) getOldToNewEntityRefMap().get( eeFromDb );
		assertEquals( ee.getId(), eeFromDb.getId() );
		applyNonFlushedChangesToNewSessionCloseOldSession( s );
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().commit();
	}
}