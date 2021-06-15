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
package org.hibernate.test.lob;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.internal.util.collections.ArrayHelper;
import org.hibernate.testing.DialectChecks;
import org.hibernate.testing.RequiresDialectFeature;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Steve Ebersole
 */
@TestForIssue( jiraKey = "HHH-2680" )
@RequiresDialectFeature( {DialectChecks.SupportsExpectedLobUsagePattern.class, DialectChecks.SupportsLobValueChangePropogation.class} ) // Skip for Sybase. HHH-6807
public class LobMergeTest extends BaseCoreFunctionalTestCase {
	private static final int LOB_SIZE = 10000;

	public String[] getMappings() {
		return new String[] { "lob/LobMappings.hbm.xml" };
	}

	@Test
	public void testMergingBlobData() throws Exception {
		final byte[] original = BlobLocatorTest.buildByteArray( LOB_SIZE, true );
		final byte[] updated = BlobLocatorTest.buildByteArray( LOB_SIZE, false );

		Session s = openSession();
		s.beginTransaction();

		LobHolder entity = new LobHolder();
		entity.setBlobLocator( s.getLobHelper().createBlob( original ) );
		s.save( entity );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		// entity still detached...
		entity.setBlobLocator( s.getLobHelper().createBlob( updated ) );
		entity = (LobHolder) s.merge( entity );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		entity = (LobHolder) s.get( LobHolder.class, entity.getId() );
		assertEquals( "blob sizes did not match after merge", LOB_SIZE, entity.getBlobLocator().length() );
		assertTrue(
				"blob contents did not match after merge",
				ArrayHelper.isEquals( updated, BlobLocatorTest.extractData( entity.getBlobLocator() ) )
		);
		s.delete( entity );
		s.getTransaction().commit();
		s.close();
	}

	@Test
	public void testMergingClobData() throws Exception {
		final String original = ClobLocatorTest.buildString( LOB_SIZE, 'a' );
		final String updated = ClobLocatorTest.buildString( LOB_SIZE, 'z' );

		Session s = openSession();
		s.beginTransaction();

		LobHolder entity = new LobHolder();
		entity.setClobLocator( s.getLobHelper().createClob( original ) );
		s.save( entity );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		// entity still detached...
		entity.setClobLocator( s.getLobHelper().createClob( updated ) );
		entity = (LobHolder) s.merge( entity );
		s.flush();
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		entity = (LobHolder) s.get( LobHolder.class, entity.getId() );
		assertEquals( "clob sizes did not match after merge", LOB_SIZE, entity.getClobLocator().length() );
		assertEquals(
				"clob contents did not match after merge",
				updated,
				ClobLocatorTest.extractData( entity.getClobLocator() )
		);
		s.delete( entity );
		s.getTransaction().commit();
		s.close();
	}
}
