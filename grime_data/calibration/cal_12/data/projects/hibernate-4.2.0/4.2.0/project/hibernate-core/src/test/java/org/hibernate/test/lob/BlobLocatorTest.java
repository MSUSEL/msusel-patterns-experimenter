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
import java.sql.Blob;

import junit.framework.AssertionFailedError;
import org.junit.Assert;
import org.junit.Test;

import org.hibernate.Hibernate;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.internal.util.collections.ArrayHelper;
import org.hibernate.testing.DialectChecks;
import org.hibernate.testing.RequiresDialectFeature;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertNotNull;

/**
 * Tests lazy materialization of data mapped by
 * {@link org.hibernate.type.BlobType}, as well as bounded and unbounded
 * materialization and mutation.
 *
 * @author Steve Ebersole
 */
@RequiresDialectFeature( DialectChecks.SupportsExpectedLobUsagePattern.class )
public class BlobLocatorTest extends BaseCoreFunctionalTestCase {
	private static final long BLOB_SIZE = 10000L;

	public String[] getMappings() {
		return new String[] { "lob/LobMappings.hbm.xml" };
	}

	@Test
	public void testBoundedBlobLocatorAccess() throws Throwable {
		byte[] original = buildByteArray( BLOB_SIZE, true );
		byte[] changed = buildByteArray( BLOB_SIZE, false );
		byte[] empty = new byte[] {};

		Session s = openSession();
		s.beginTransaction();
		LobHolder entity = new LobHolder();
		entity.setBlobLocator( s.getLobHelper().createBlob( original ) );
		s.save( entity );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		entity = ( LobHolder ) s.get( LobHolder.class, entity.getId() );
		Assert.assertEquals( BLOB_SIZE, entity.getBlobLocator().length() );
		assertEquals( original, extractData( entity.getBlobLocator() ) );
		s.getTransaction().commit();
		s.close();

		// test mutation via setting the new clob data...
		if ( getDialect().supportsLobValueChangePropogation() ) {
			s = openSession();
			s.beginTransaction();
			entity = ( LobHolder ) s.get( LobHolder.class, entity.getId(), LockMode.UPGRADE );
			entity.getBlobLocator().truncate( 1 );
			entity.getBlobLocator().setBytes( 1, changed );
			s.getTransaction().commit();
			s.close();

			s = openSession();
			s.beginTransaction();
			entity = ( LobHolder ) s.get( LobHolder.class, entity.getId(), LockMode.UPGRADE );
			assertNotNull( entity.getBlobLocator() );
			Assert.assertEquals( BLOB_SIZE, entity.getBlobLocator().length() );
			assertEquals( changed, extractData( entity.getBlobLocator() ) );
			entity.getBlobLocator().truncate( 1 );
			entity.getBlobLocator().setBytes( 1, original );
			s.getTransaction().commit();
			s.close();
		}

		// test mutation via supplying a new clob locator instance...
		s = openSession();
		s.beginTransaction();
		entity = ( LobHolder ) s.get( LobHolder.class, entity.getId(), LockMode.UPGRADE );
		assertNotNull( entity.getBlobLocator() );
		Assert.assertEquals( BLOB_SIZE, entity.getBlobLocator().length() );
		assertEquals( original, extractData( entity.getBlobLocator() ) );
		entity.setBlobLocator( s.getLobHelper().createBlob( changed ) );
		s.getTransaction().commit();
		s.close();

		// test empty blob
		s = openSession();
		s.beginTransaction();
		entity = ( LobHolder ) s.get( LobHolder.class, entity.getId() );
		Assert.assertEquals( BLOB_SIZE, entity.getBlobLocator().length() );
		assertEquals( changed, extractData( entity.getBlobLocator() ) );
		entity.setBlobLocator( s.getLobHelper().createBlob( empty ) );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		entity = ( LobHolder ) s.get( LobHolder.class, entity.getId() );
		if ( entity.getBlobLocator() != null) {
			Assert.assertEquals( empty.length, entity.getBlobLocator().length() );
			assertEquals( empty, extractData( entity.getBlobLocator() ) );
		}
		s.delete( entity );
		s.getTransaction().commit();
		s.close();

	}

	@Test
	@RequiresDialectFeature(
			value = DialectChecks.SupportsUnboundedLobLocatorMaterializationCheck.class,
			comment = "database/driver does not support materializing a LOB locator outside the owning transaction"
	)
	public void testUnboundedBlobLocatorAccess() throws Throwable {
		// Note: unbounded mutation of the underlying lob data is completely
		// unsupported; most databases would not allow such a construct anyway.
		// Thus here we are only testing materialization...

		byte[] original = buildByteArray( BLOB_SIZE, true );

		Session s = openSession();
		s.beginTransaction();
		LobHolder entity = new LobHolder();
		entity.setBlobLocator( Hibernate.getLobCreator( s ).createBlob( original ) );
		s.save( entity );
		s.getTransaction().commit();
		s.close();

		// load the entity with the clob locator, and close the session/transaction;
		// at that point it is unbounded...
		s = openSession();
		s.beginTransaction();
		entity = ( LobHolder ) s.get( LobHolder.class, entity.getId() );
		s.getTransaction().commit();
		s.close();

		Assert.assertEquals( BLOB_SIZE, entity.getBlobLocator().length() );
		assertEquals( original, extractData( entity.getBlobLocator() ) );

		s = openSession();
		s.beginTransaction();
		s.delete( entity );
		s.getTransaction().commit();
		s.close();
	}

	public static byte[] extractData(Blob blob) throws Exception {
		return blob.getBytes( 1, ( int ) blob.length() );
	}


	public static byte[] buildByteArray(long size, boolean on) {
		byte[] data = new byte[(int)size];
		data[0] = mask( on );
		for ( int i = 0; i < size; i++ ) {
			data[i] = mask( on );
			on = !on;
		}
		return data;
	}

	private static byte mask(boolean on) {
		return on ? ( byte ) 1 : ( byte ) 0;
	}

	public static void assertEquals(byte[] val1, byte[] val2) {
		if ( !ArrayHelper.isEquals( val1, val2 ) ) {
			throw new AssertionFailedError( "byte arrays did not match" );
		}
	}
}
