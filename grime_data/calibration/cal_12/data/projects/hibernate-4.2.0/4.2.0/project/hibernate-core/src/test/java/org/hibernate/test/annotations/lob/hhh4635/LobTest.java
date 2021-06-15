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
package org.hibernate.test.annotations.lob.hhh4635;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.dialect.Oracle8iDialect;
import org.hibernate.testing.RequiresDialect;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.jboss.logging.Logger;
import org.junit.Test;

/**
 * To reproduce this issue, Oracle MUST use a multi-byte character set (UTF-8)!
 * 
 * @author Brett Meyer
 */
@RequiresDialect( Oracle8iDialect.class )
@TestForIssue( jiraKey = "HHH-4635" )
public class LobTest extends BaseCoreFunctionalTestCase {
	
	private static final Logger LOG = Logger.getLogger( LobTest.class );

	@Test
	public void hibernateTest() {
		printConfig();
		
		Session session = openSession();
		session.beginTransaction();
		LobTestEntity entity = new LobTestEntity();
		entity.setId(1L);
		entity.setLobValue(session.getLobHelper().createBlob(new byte[9999]));
		entity.setQwerty(randomString(4000));
		session.save(entity);
		session.getTransaction().commit();
	}

	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[] { LobTestEntity.class };
	}
	
	private String randomString( int count ) {
		StringBuilder buffer = new StringBuilder(count);
		for( int i = 0; i < count; i++ ) {
			buffer.append( 'a' );
		}
		return buffer.toString();
	}

	private void printConfig() {
		String sql = "select value from V$NLS_PARAMETERS where parameter = 'NLS_CHARACTERSET'";
		
		Session session = openSession();
		session.beginTransaction();
		Query query = session.createSQLQuery( sql );
		
		String s = (String) query.uniqueResult();
		LOG.debug( "Using Oracle charset " + s );
	}
}
