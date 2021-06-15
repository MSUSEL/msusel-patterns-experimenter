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
package org.hibernate.test.ops;

import org.junit.Test;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Gail Badner
 */
public class SimpleOpsTest extends AbstractOperationTestCase {
	public void configure(Configuration cfg) {
		super.configure( cfg );
		cfg.setProperty( USE_NEW_METADATA_MAPPINGS, "true");
	}

	public String[] getMappings() {
		return new String[] { "ops/SimpleEntity.hbm.xml" };
	}

	@Test
	public void testBasicOperations() {
		clearCounts();

		Session s = openSession();
		Transaction tx = s.beginTransaction();
		SimpleEntity entity = new SimpleEntity(  );
		entity.setId( 1L );
		entity.setName( "name" );
		s.save( entity );
		tx.commit();
		s.close();

		assertInsertCount( 1 );
		assertUpdateCount( 0 );
		assertDeleteCount( 0 );

		clearCounts();

		s = openSession();
		tx = s.beginTransaction();
		entity = ( SimpleEntity ) s.get( SimpleEntity.class, entity.getId() );
		assertEquals( Long.valueOf( 1L ), entity.getId() );
		assertEquals( "name", entity.getName() );
		entity.setName( "new name" );
		tx.commit();
		s.close();

		assertInsertCount( 0 );
		assertUpdateCount( 1 );
		assertDeleteCount( 0 );

		clearCounts();

		s = openSession();
		tx = s.beginTransaction();
		entity = ( SimpleEntity ) s.load( SimpleEntity.class, entity.getId() );
		assertFalse( Hibernate.isInitialized( entity ) );
		assertEquals( Long.valueOf( 1L ), entity.getId() );
		assertEquals( "new name", entity.getName() );
		assertTrue( Hibernate.isInitialized( entity ) );
		tx.commit();
		s.close();

		assertInsertCount( 0 );
		assertUpdateCount( 0 );
		assertDeleteCount( 0 );

		entity.setName( "another new name" );

		s = openSession();
		tx = s.beginTransaction();
		s.merge( entity );
		tx.commit();
		s.close();

		assertInsertCount( 0 );
		assertUpdateCount( 1 );
		assertDeleteCount( 0 );

		clearCounts();

		s = openSession();
		tx = s.beginTransaction();
		entity = ( SimpleEntity ) s.get( SimpleEntity.class, entity.getId() );
		assertEquals( Long.valueOf( 1L ), entity.getId() );
		assertEquals( "another new name", entity.getName() );
		s.delete( entity );
		tx.commit();
		s.close();

		assertInsertCount( 0 );
		assertUpdateCount( 0 );
		assertDeleteCount( 1 );
	}
}

