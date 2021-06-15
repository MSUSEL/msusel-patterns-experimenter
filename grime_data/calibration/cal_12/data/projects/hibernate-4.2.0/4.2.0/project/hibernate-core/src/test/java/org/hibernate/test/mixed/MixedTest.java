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
package org.hibernate.test.mixed;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.dialect.SybaseASE15Dialect;
import org.hibernate.testing.SkipForDialect;
import org.hibernate.testing.SkipLog;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Gavin King
 */
@SkipForDialect( SybaseASE15Dialect.class )
public class MixedTest extends BaseCoreFunctionalTestCase {
	@Override
	public String[] getMappings() {
		return new String[]{"mixed/Item.hbm.xml"};
	}

	@Override
	public String getCacheConcurrencyStrategy() {
		return null;
	}

	@Test
	public void testMixedInheritance() {
		Session s = openSession( new DocumentInterceptor() );
		Transaction t = s.beginTransaction();
		Folder f = new Folder();
		f.setName( "/" );
		s.save( f );

		Document d = new Document();
		d.setName( "Hibernate in Action" );
		d.setContent( s.getLobHelper().createBlob( "blah blah blah".getBytes() ) );
		d.setParent( f );
		Long did = (Long) s.save( d );

		SecureDocument d2 = new SecureDocument();
		d2.setName( "Secret" );
		d2.setContent( s.getLobHelper().createBlob( "wxyz wxyz".getBytes() ) );
		// SybaseASE15Dialect only allows 7-bits in a byte to be inserted into a tinyint 
		// column (0 <= val < 128)
		d2.setPermissionBits( (byte) 127 );
		d2.setOwner( "gavin" );
		d2.setParent( f );
		Long d2id = (Long) s.save( d2 );

		t.commit();
		s.close();

		if ( ! getDialect().supportsExpectedLobUsagePattern() ) {
			SkipLog.reportSkip( "database/driver does not support expected LOB usage pattern", "LOB support" );
			return;
		}

		s = openSession( new DocumentInterceptor() );
		t = s.beginTransaction();
		Item id = (Item) s.load( Item.class, did );
		assertEquals( did, id.getId() );
		assertEquals( "Hibernate in Action", id.getName() );
		assertEquals( "/", id.getParent().getName() );

		Item id2 = (Item) s.load( Item.class, d2id );
		assertEquals( d2id, id2.getId() );
		assertEquals( "Secret", id2.getName() );
		assertEquals( "/", id2.getParent().getName() );

		id.setName( "HiA" );

		d2 = (SecureDocument) s.load( SecureDocument.class, d2id );
		d2.setOwner( "max" );

		s.flush();

		s.clear();

		d = (Document) s.load( Document.class, did );
		assertEquals( did, d.getId() );
		assertEquals( "HiA", d.getName() );
		assertNotNull( d.getContent() );
		assertEquals( "/", d.getParent().getName() );
		assertNotNull( d.getCreated() );
		assertNotNull( d.getModified() );

		d2 = (SecureDocument) s.load( SecureDocument.class, d2id );
		assertEquals( d2id, d2.getId() );
		assertEquals( "Secret", d2.getName() );
		assertNotNull( d2.getContent() );
		assertEquals( "max", d2.getOwner() );
		assertEquals( "/", d2.getParent().getName() );
		// SybaseASE15Dialect only allows 7-bits in a byte to be inserted into a tinyint 
		// column (0 <= val < 128)
		assertEquals( (byte) 127, d2.getPermissionBits() );
		assertNotNull( d2.getCreated() );
		assertNotNull( d2.getModified() );

		s.delete( d.getParent() );
		s.delete( d );
		s.delete( d2 );

		t.commit();
		s.close();
	}
}

