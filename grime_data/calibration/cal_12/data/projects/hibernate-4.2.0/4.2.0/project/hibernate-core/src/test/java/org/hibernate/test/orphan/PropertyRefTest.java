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
package org.hibernate.test.orphan;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;


/**
 * @author Steve Ebersole
 */
@TestForIssue( jiraKey = "HHH-565" )
public class PropertyRefTest extends BaseCoreFunctionalTestCase {
	@Override
	public String[] getMappings() {
		return new String[] { "orphan/User.hbm.xml", "orphan/Mail.hbm.xml" };
	}

	@Test
	public void testDeleteParentWithBidirOrphanDeleteCollectionBasedOnPropertyRef() {
		Session session = openSession();
		Transaction txn = session.beginTransaction();
		User user = new User( "test" );
		user.addMail( "test" );
		user.addMail( "test" );
		session.save( user );
		txn.commit();
		session.close();

		session = openSession();
		txn = session.beginTransaction();
		user = ( User ) session.load( User.class, user.getId() );
		session.delete( user );
		txn.commit();
		session.close();

		session = openSession();
		txn = session.beginTransaction();
		session.createQuery( "delete from Mail where alias = :alias" ).setString( "alias", "test" ).executeUpdate();
		session.createQuery( "delete from User where userid = :userid" ).setString( "userid", "test" ).executeUpdate();
		txn.commit();
		session.close();
	}
	
}
