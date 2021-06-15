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
package org.hibernate.test.component.cascading.toone;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author Steve Ebersole
 */
public class CascadeToComponentAssociationTest extends BaseCoreFunctionalTestCase {
	@Override
	public String[] getMappings() {
		return new String[] { "component/cascading/toone/Mappings.hbm.xml" };
	}

	@Test
	public void testMerging() {
		// step1, we create a document with owner
		Session session = openSession();
		session.beginTransaction();
		User user = new User();
		Document document = new Document();
		document.setOwner( user );
		session.persist( document );
		session.getTransaction().commit();
		session.close();

		// step2, we verify that the document has owner and that owner has no personal-info; then we detach
		session = openSession();
		session.beginTransaction();
		document = ( Document ) session.get( Document.class, document.getId() );
		assertNotNull( document.getOwner() );
		assertNull( document.getOwner().getPersonalInfo() );
		session.getTransaction().commit();
		session.close();

		// step3, try to specify the personal-info during detachment
		Address addr = new Address();
		addr.setStreet1( "123 6th St" );
		addr.setCity( "Austin" );
		addr.setState( "TX" );
		document.getOwner().setPersonalInfo( new PersonalInfo( addr ) );

		// step4 we merge the document
		session = openSession();
		session.beginTransaction();
		session.merge( document );
		session.getTransaction().commit();
		session.close();

		// step5, final test
		session = openSession();
		session.beginTransaction();
		document = ( Document ) session.get( Document.class, document.getId() );
		assertNotNull( document.getOwner() );
		assertNotNull( document.getOwner().getPersonalInfo() );
		assertNotNull( document.getOwner().getPersonalInfo().getHomeAddress() );
		session.getTransaction().commit();
		session.close();
	}
}
