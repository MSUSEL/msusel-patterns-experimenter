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
package org.hibernate.test.generatedkeys.identity;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.testing.DialectChecks;
import org.hibernate.testing.RequiresDialectFeature;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author Steve Ebersole
 */
@RequiresDialectFeature( DialectChecks.SupportsIdentityColumns.class )
public class IdentityGeneratedKeysTest extends BaseCoreFunctionalTestCase {
	public void configure(Configuration cfg) {
		super.configure( cfg );
		cfg.setProperty( Environment.GENERATE_STATISTICS, "true" );
	}

	public String[] getMappings() {
		return new String[] { "generatedkeys/identity/MyEntity.hbm.xml" };
	}

	@Test
	public void testIdentityColumnGeneratedIds() {
		Session s = openSession();
		s.beginTransaction();
		MyEntity myEntity = new MyEntity( "test" );
		Long id = ( Long ) s.save( myEntity );
		assertNotNull( "identity column did not force immediate insert", id );
		assertEquals( id, myEntity.getId() );
		s.delete( myEntity );
		s.getTransaction().commit();
		s.close();
	}

	@Test
	public void testPersistOutsideTransaction() {
		Session s = openSession();

		// first test save() which should force an immediate insert...
		MyEntity myEntity1 = new MyEntity( "test-save" );
		Long id = ( Long ) s.save( myEntity1 );
		assertNotNull( "identity column did not force immediate insert", id );
		assertEquals( id, myEntity1.getId() );

		// next test persist() which should cause a delayed insert...
		long initialInsertCount = sessionFactory().getStatistics().getEntityInsertCount();
		MyEntity myEntity2 = new MyEntity( "test-persist");
		s.persist( myEntity2 );
		assertEquals( "persist on identity column not delayed", initialInsertCount, sessionFactory().getStatistics().getEntityInsertCount() );
		assertNull( myEntity2.getId() );

		// an explicit flush should cause execution of the delayed insertion
		s.flush();
		assertEquals( "delayed persist insert not executed on flush", initialInsertCount + 1, sessionFactory().getStatistics().getEntityInsertCount() );
		s.close();

		s = openSession();
		s.beginTransaction();
		s.delete( myEntity1 );
		s.delete( myEntity2 );
		s.getTransaction().commit();
		s.close();
	}

	@Test
	@SuppressWarnings( {"unchecked"})
	public void testPersistOutsideTransactionCascadedToNonInverseCollection() {
		long initialInsertCount = sessionFactory().getStatistics().getEntityInsertCount();
		Session s = openSession();
		MyEntity myEntity = new MyEntity( "test-persist");
		myEntity.getNonInverseChildren().add( new MyChild( "test-child-persist-non-inverse" ) );
		s.persist( myEntity );
		assertEquals( "persist on identity column not delayed", initialInsertCount, sessionFactory().getStatistics().getEntityInsertCount() );
		assertNull( myEntity.getId() );
		s.flush();
		assertEquals( "delayed persist insert not executed on flush", initialInsertCount + 2, sessionFactory().getStatistics().getEntityInsertCount() );
		s.close();

		s = openSession();
		s.beginTransaction();
		s.createQuery( "delete MyChild" ).executeUpdate();
		s.createQuery( "delete MyEntity" ).executeUpdate();
		s.getTransaction().commit();
		s.close();
	}

	@Test
	@SuppressWarnings( {"unchecked"})
	public void testPersistOutsideTransactionCascadedToInverseCollection() {
		long initialInsertCount = sessionFactory().getStatistics().getEntityInsertCount();
		Session s = openSession();
		MyEntity myEntity2 = new MyEntity( "test-persist-2");
		MyChild child = new MyChild( "test-child-persist-inverse" );
		myEntity2.getInverseChildren().add( child );
		child.setInverseParent( myEntity2 );
		s.persist( myEntity2 );
		assertEquals( "persist on identity column not delayed", initialInsertCount, sessionFactory().getStatistics().getEntityInsertCount() );
		assertNull( myEntity2.getId() );
		s.flush();
		assertEquals( "delayed persist insert not executed on flush", initialInsertCount + 2, sessionFactory().getStatistics().getEntityInsertCount() );
		s.close();

		s = openSession();
		s.beginTransaction();
		s.createQuery( "delete MyChild" ).executeUpdate();
		s.createQuery( "delete MyEntity" ).executeUpdate();
		s.getTransaction().commit();
		s.close();
	}

	@Test
	public void testPersistOutsideTransactionCascadedToManyToOne() {
		long initialInsertCount = sessionFactory().getStatistics().getEntityInsertCount();
		Session s = openSession();
		MyEntity myEntity = new MyEntity( "test-persist");
		myEntity.setSibling( new MySibling( "test-persist-sibling-out" ) );
		s.persist( myEntity );
		assertEquals( "persist on identity column not delayed", initialInsertCount, sessionFactory().getStatistics().getEntityInsertCount() );
		assertNull( myEntity.getId() );
		s.flush();
		assertEquals( "delayed persist insert not executed on flush", initialInsertCount + 2, sessionFactory().getStatistics().getEntityInsertCount() );
		s.close();

		s = openSession();
		s.beginTransaction();
		s.createQuery( "delete MyEntity" ).executeUpdate();
		s.createQuery( "delete MySibling" ).executeUpdate();
		s.getTransaction().commit();
		s.close();
	}

	@Test
	public void testPersistOutsideTransactionCascadedFromManyToOne() {
		long initialInsertCount = sessionFactory().getStatistics().getEntityInsertCount();
		Session s = openSession();
		MyEntity myEntity2 = new MyEntity( "test-persist-2");
		MySibling sibling = new MySibling( "test-persist-sibling-in" );
		sibling.setEntity( myEntity2 );
		s.persist( sibling );
		assertEquals( "persist on identity column not delayed", initialInsertCount, sessionFactory().getStatistics().getEntityInsertCount() );
		assertNull( myEntity2.getId() );
		s.flush();
		assertEquals( "delayed persist insert not executed on flush", initialInsertCount + 2, sessionFactory().getStatistics().getEntityInsertCount() );
		s.close();

		s = openSession();
		s.beginTransaction();
		s.createQuery( "delete MySibling" ).executeUpdate();
		s.createQuery( "delete MyEntity" ).executeUpdate();
		s.getTransaction().commit();
		s.close();
	}
}
