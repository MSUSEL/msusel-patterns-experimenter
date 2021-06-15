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
package org.hibernate.test.propertyref;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;

import org.junit.Test;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.testing.RequiresDialect;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Steve Ebersole
 */
@RequiresDialect(H2Dialect.class)
public class DoesNotWorkWithHbmTest extends BaseCoreFunctionalTestCase {

	@Override
	protected String[] getMappings() {
		return new String[] { "propertyref/Mapping.hbm.xml" };
	}

	@Override
	protected void configure(Configuration configuration) {
		super.configure( configuration );
		configuration.setProperty( AvailableSettings.USE_SECOND_LEVEL_CACHE, "false" );
		configuration.setProperty( AvailableSettings.HBM2DDL_IMPORT_FILES, "/org/hibernate/test/propertyref/import.sql" );
	}

	@Test
	public void testIt() {
		DoesNotWorkPk pk = new DoesNotWorkPk();
		pk.setId1( "ZZZ" );
		pk.setId2( "00" );

//		{
//			Session session = openSession();
//			session.beginTransaction();
//			DoesNotWork entity = new DoesNotWork( pk );
//			entity.setGlobalNotes( Arrays.asList( "My first note!" ) );
//			session.save( entity );
//			session.getTransaction().commit();
//			session.close();
//		}

		{
			Session session = openSession();
			session.beginTransaction();
			DoesNotWork entity = (DoesNotWork) session.get( DoesNotWork.class, pk );
			assertNotNull( entity );
			List<String> notes = entity.getGlobalNotes();
			assertNotNull( notes );
			assertEquals( 2, notes.size() );
			for ( String s : notes ) {
				System.out.println( s );
			}
			session.delete( entity );
			session.getTransaction().commit();
			session.close();
		}
	}
}
