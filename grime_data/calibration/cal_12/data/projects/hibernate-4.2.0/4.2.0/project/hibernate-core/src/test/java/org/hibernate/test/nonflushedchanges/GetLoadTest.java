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

import java.util.List;

import org.junit.Test;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.testing.jta.TestingJtaPlatformImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * adapted this from "ops" tests version
 *
 * @author Gail Badner
 * @author Gavin King
 */
public class GetLoadTest extends AbstractOperationTestCase {
	public void configure(Configuration cfg) {
		super.configure( cfg );
		cfg.setProperty( Environment.GENERATE_STATISTICS, "true" );
		cfg.setProperty( Environment.STATEMENT_BATCH_SIZE, "0" );
	}

	@Test
	@SuppressWarnings( {"UnusedAssignment"})
	public void testGetLoad() throws Exception {
		clearCounts();

		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		Session s = openSession();
		Employer emp = new Employer();
		s.persist( emp );
		Node node = new Node( "foo" );
		Node parent = new Node( "bar" );
		parent.addChild( node );
		s.persist( parent );
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().commit();

		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		s = openSession();
		emp = ( Employer ) s.get( Employer.class, emp.getId() );
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		emp = ( Employer ) getOldToNewEntityRefMap().get( emp );
		assertTrue( Hibernate.isInitialized( emp ) );
		assertFalse( Hibernate.isInitialized( emp.getEmployees() ) );
		node = ( Node ) s.get( Node.class, node.getName() );
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		node = ( Node ) getOldToNewEntityRefMap().get( node );
		emp = ( Employer ) getOldToNewEntityRefMap().get( emp );
		assertTrue( Hibernate.isInitialized( node ) );
		assertFalse( Hibernate.isInitialized( node.getChildren() ) );
		assertFalse( Hibernate.isInitialized( node.getParent() ) );
		assertNull( s.get( Node.class, "xyz" ) );
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().commit();

		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		s = openSession();
		emp = ( Employer ) s.load( Employer.class, emp.getId() );
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		emp = ( Employer ) getOldToNewEntityRefMap().get( emp );
		emp.getId();
		assertFalse( Hibernate.isInitialized( emp ) );
		node = ( Node ) s.load( Node.class, node.getName() );
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		node = ( Node ) getOldToNewEntityRefMap().get( node );
		assertEquals( node.getName(), "foo" );
		assertFalse( Hibernate.isInitialized( node ) );
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().commit();

		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		s = openSession();
		emp = ( Employer ) s.get( "org.hibernate.test.nonflushedchanges.Employer", emp.getId() );
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		emp = ( Employer ) getOldToNewEntityRefMap().get( emp );
		assertTrue( Hibernate.isInitialized( emp ) );
		node = ( Node ) s.get( "org.hibernate.test.nonflushedchanges.Node", node.getName() );
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		node = ( Node ) getOldToNewEntityRefMap().get( node );
		assertTrue( Hibernate.isInitialized( node ) );
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().commit();

		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		s = openSession();
		emp = ( Employer ) s.load( "org.hibernate.test.nonflushedchanges.Employer", emp.getId() );
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		emp = ( Employer ) getOldToNewEntityRefMap().get( emp );
		emp.getId();
		assertFalse( Hibernate.isInitialized( emp ) );
		node = ( Node ) s.load( "org.hibernate.test.nonflushedchanges.Node", node.getName() );
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		emp = ( Employer ) getOldToNewEntityRefMap().get( emp );
		node = ( Node ) getOldToNewEntityRefMap().get( node );
		assertEquals( node.getName(), "foo" );
		assertFalse( Hibernate.isInitialized( node ) );
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().commit();

		assertFetchCount( 0 );

		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		s = openSession();
		s.createQuery( "delete from Employer" ).executeUpdate();
		List list = s.createQuery( "from Node" ).list();
		for ( Object aList : list ) {
			s.delete( aList );
		}
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().commit();
	}

	@Test
	public void testGetReadOnly() throws Exception {
		clearCounts();

		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		Session s = openSession();
		Employer emp = new Employer();
		s.persist( emp );
		Node node = new Node( "foo" );
		Node parent = new Node( "bar" );
		parent.addChild( node );
		s.persist( parent );
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().commit();

		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		s = openSession();
		assertFalse( s.isDefaultReadOnly() );
		s.setDefaultReadOnly( true );
		emp = ( Employer ) s.get( Employer.class, emp.getId() );
		assertTrue( s.isDefaultReadOnly() );
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		assertTrue( s.isDefaultReadOnly() );
		emp = ( Employer ) getOldToNewEntityRefMap().get( emp );
		assertTrue( Hibernate.isInitialized( emp ) );
		assertFalse( Hibernate.isInitialized( emp.getEmployees() ) );
		node = ( Node ) s.get( Node.class, node.getName() );
		assertTrue( s.isReadOnly( emp ) );
		assertTrue( s.isReadOnly( node ) );
		s.setDefaultReadOnly( false );
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		assertFalse( s.isDefaultReadOnly() );
		node = ( Node ) getOldToNewEntityRefMap().get( node );
		emp = ( Employer ) getOldToNewEntityRefMap().get( emp );
		assertTrue( Hibernate.isInitialized( node ) );
		assertTrue( s.isReadOnly( node ) );
		assertFalse( Hibernate.isInitialized( node.getParent() ) );
		assertTrue( s.isReadOnly( emp ) );
		assertFalse( Hibernate.isInitialized( node.getChildren() ) );
		Hibernate.initialize( node.getChildren() );
		for ( Object o : node.getChildren() ) {
			assertFalse( s.isReadOnly( o ) );
		}
		assertFalse( Hibernate.isInitialized( node.getParent() ) );
		assertNull( s.get( Node.class, "xyz" ) );
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().commit();

		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		s = openSession();
		assertFalse( s.isDefaultReadOnly() );
		emp = ( Employer ) s.get( "org.hibernate.test.nonflushedchanges.Employer", emp.getId() );
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		assertFalse( s.isDefaultReadOnly() );
		emp = ( Employer ) getOldToNewEntityRefMap().get( emp );
		assertTrue( Hibernate.isInitialized( emp ) );
		assertFalse( s.isReadOnly( emp ) );
		s.setReadOnly( emp, true );
		node = ( Node ) s.get( "org.hibernate.test.nonflushedchanges.Node", node.getName() );
		assertFalse( s.isReadOnly( node ) );
		s.setReadOnly( node, true );
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		emp = ( Employer ) getOldToNewEntityRefMap().get( emp );
		assertTrue( s.isReadOnly( emp ) );
		node = ( Node ) getOldToNewEntityRefMap().get( node );
		assertTrue( Hibernate.isInitialized( node ) );
		assertTrue( s.isReadOnly( node ) );
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().commit();

		assertFetchCount( 0 );

		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		s = openSession();
		s.createQuery( "delete from Employer" ).executeUpdate();
		List list = s.createQuery( "from Node" ).list();
		for ( Object aList : list ) {
			s.delete( aList );
		}
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().commit();
	}

	@Test
	public void testLoadReadOnly() throws Exception {
		clearCounts();

		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		Session s = openSession();
		Employer emp = new Employer();
		s.persist( emp );
		Node node = new Node( "foo" );
		Node parent = new Node( "bar" );
		parent.addChild( node );
		s.persist( parent );
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().commit();

		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		s = openSession();
		assertFalse( s.isDefaultReadOnly() );
		s.setDefaultReadOnly( true );
		emp = ( Employer ) s.load( Employer.class, emp.getId() );
		assertFalse( Hibernate.isInitialized( emp ) );
		assertTrue( s.isReadOnly( emp ) );
		assertTrue( s.isDefaultReadOnly() );
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		assertTrue( s.isDefaultReadOnly() );
		emp = ( Employer ) getOldToNewEntityRefMap().get( emp );
		assertFalse( Hibernate.isInitialized( emp ) );
		assertTrue( s.isReadOnly( emp ) );
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().commit();

		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		s = openSession();
		s.createQuery( "delete from Employer" ).executeUpdate();
		List list = s.createQuery( "from Node" ).list();
		for ( Object aList : list ) {
			s.delete( aList );
		}
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().commit();
	}

	@Test
	public void testGetAfterDelete() throws Exception {
		clearCounts();

		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		Session s = openSession();
		Employer emp = new Employer();
		s.persist( emp );
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().commit();

		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		s = openSession();
		s.delete( emp );
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		emp = ( Employer ) s.get( Employee.class, emp.getId() );
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().commit();

		assertNull( "get did not return null after delete", emp );
	}

}

