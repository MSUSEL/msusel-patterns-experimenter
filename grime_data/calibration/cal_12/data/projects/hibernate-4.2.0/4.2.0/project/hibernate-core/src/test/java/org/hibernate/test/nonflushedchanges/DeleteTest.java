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

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.testing.jta.TestingJtaPlatformImpl;

/**
 * adapted this from "ops" tests version
 *
 * @author Gail Badner
 * @author Steve Ebersole
 */
public class DeleteTest extends AbstractOperationTestCase {
	@Test
	@SuppressWarnings( {"unchecked"})
	public void testDeleteVersionedWithCollectionNoUpdate() throws Exception {
		// test adapted from HHH-1564...
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		Session s = openSession();
		VersionedEntity c = new VersionedEntity( "c1", "child-1" );
		VersionedEntity p = new VersionedEntity( "root", "root" );
		p.getChildren().add( c );
		c.setParent( p );
		s.save( p );
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().commit();

		clearCounts();

		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		s = openSession();
		VersionedEntity loadedParent = ( VersionedEntity ) s.get( VersionedEntity.class, "root" );
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		loadedParent = ( VersionedEntity ) getOldToNewEntityRefMap().get( loadedParent );
		s.delete( loadedParent );
		applyNonFlushedChangesToNewSessionCloseOldSession( s );
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().commit();

		assertInsertCount( 0 );
		assertUpdateCount( 0 );
		assertDeleteCount( 2 );
	}

	@Test
	public void testNoUpdateOnDelete() throws Exception {
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		Session s = openSession();
		Node node = new Node( "test" );
		s.persist( node );
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().commit();

		clearCounts();

		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		s = openSession();
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		s.delete( node );
		applyNonFlushedChangesToNewSessionCloseOldSession( s );
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().commit();

		assertUpdateCount( 0 );
		assertInsertCount( 0 );
	}

	@Test
	@SuppressWarnings( {"unchecked"})
	public void testNoUpdateOnDeleteWithCollection() throws Exception {
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		Session s = openSession();
		Node parent = new Node( "parent" );
		Node child = new Node( "child" );
		parent.getCascadingChildren().add( child );
		s.persist( parent );
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().commit();

		clearCounts();

		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();
		s = openSession();
		parent = ( Node ) s.get( Node.class, "parent" );
		s = applyNonFlushedChangesToNewSessionCloseOldSession( s );
		parent = ( Node ) getOldToNewEntityRefMap().get( parent );
		s.delete( parent );
		applyNonFlushedChangesToNewSessionCloseOldSession( s );
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().commit();

		assertUpdateCount( 0 );
		assertInsertCount( 0 );
		assertDeleteCount( 2 );
	}
}
