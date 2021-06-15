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
package org.hibernate.test.interfaceproxy;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.testing.DialectChecks;
import org.hibernate.testing.RequiresDialectFeature;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;


/**
 * @author Gavin King
 */
public class InterfaceProxyTest extends BaseCoreFunctionalTestCase {
	@Override
	public String[] getMappings() {
		return new String[] { "interfaceproxy/Item.hbm.xml" };
	}

	@Override
	public String getCacheConcurrencyStrategy() {
		return null;
	}

	@Test
	@RequiresDialectFeature(
			value = DialectChecks.SupportsExpectedLobUsagePattern.class,
			comment = "database/driver does not support expected LOB usage pattern"
	)
	public void testInterfaceProxies() {
		Session s = openSession( new DocumentInterceptor() );
		Transaction t = s.beginTransaction();
		Document d = new DocumentImpl();
		d.setName("Hibernate in Action");
		d.setContent( s.getLobHelper().createBlob( "blah blah blah".getBytes() ) );
		Long did = (Long) s.save(d);
		SecureDocument d2 = new SecureDocumentImpl();
		d2.setName("Secret");
		d2.setContent( s.getLobHelper().createBlob( "wxyz wxyz".getBytes() ) );
		// SybaseASE15Dialect only allows 7-bits in a byte to be inserted into a tinyint 
		// column (0 <= val < 128)		
		d2.setPermissionBits( (byte) 127 );
		d2.setOwner("gavin");
		Long d2id = (Long) s.save(d2);
		t.commit();
		s.close();

		s = openSession( new DocumentInterceptor() );
		t = s.beginTransaction();
		d = (Document) s.load(ItemImpl.class, did);
		assertEquals( did, d.getId() );
		assertEquals( "Hibernate in Action", d.getName() );
		assertNotNull( d.getContent() );
		
		d2 = (SecureDocument) s.load(ItemImpl.class, d2id);
		assertEquals( d2id, d2.getId() );
		assertEquals( "Secret", d2.getName() );
		assertNotNull( d2.getContent() );
		
		s.clear();
		
		d = (Document) s.load(DocumentImpl.class, did);
		assertEquals( did, d.getId() );
		assertEquals( "Hibernate in Action", d.getName() );
		assertNotNull( d.getContent() );
		
		d2 = (SecureDocument) s.load(SecureDocumentImpl.class, d2id);
		assertEquals( d2id, d2.getId() );
		assertEquals( "Secret", d2.getName() );
		assertNotNull( d2.getContent() );
		assertEquals( "gavin", d2.getOwner() );
		
		//s.clear();
		
		d2 = (SecureDocument) s.load(SecureDocumentImpl.class, did);
		assertEquals( did, d2.getId() );
		assertEquals( "Hibernate in Action", d2.getName() );
		assertNotNull( d2.getContent() );
		
		try {
			d2.getOwner(); //CCE
			assertFalse(true);
		}
		catch (ClassCastException cce) {
			//correct
		}

		s.createQuery( "delete ItemImpl" ).executeUpdate();
		t.commit();
		s.close();
	}
}

