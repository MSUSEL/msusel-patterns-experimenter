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
package org.hibernate.envers.test.integration.collection.embeddable;

import java.util.Arrays;
import javax.persistence.EntityManager;

import junit.framework.Assert;
import org.junit.Test;

import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.testing.TestForIssue;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@TestForIssue( jiraKey = "HHH-6613" )
public class BasicEmbeddableCollection extends BaseEnversJPAFunctionalTestCase {
	private int id = -1;

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] { DarkCharacter.class };
	}

	@Test
	@Priority(10)
	public void initData() {
		EntityManager em = getEntityManager();

		// Revision 1 - empty element collection
		em.getTransaction().begin();
		DarkCharacter darkCharacter = new DarkCharacter( 1, 1 );
		em.persist( darkCharacter );
		em.getTransaction().commit();

		id = darkCharacter.getId();

		// Revision 2 - adding collection element
		em.getTransaction().begin();
		darkCharacter = em.find( DarkCharacter.class, darkCharacter.getId() );
		darkCharacter.getNames().add( new Name( "Action", "Hank" ) );
		darkCharacter = em.merge( darkCharacter );
		em.getTransaction().commit();

		// Revision 3 - adding another collection element
		em.getTransaction().begin();
		darkCharacter = em.find( DarkCharacter.class, darkCharacter.getId() );
		darkCharacter.getNames().add( new Name( "Green", "Lantern" ) );
		darkCharacter = em.merge( darkCharacter );
		em.getTransaction().commit();

		// Revision 4 - removing single collection element
		em.getTransaction().begin();
		darkCharacter = em.find( DarkCharacter.class, darkCharacter.getId() );
		darkCharacter.getNames().remove( new Name( "Action", "Hank" ) );
		darkCharacter = em.merge( darkCharacter );
		em.getTransaction().commit();

		// Revision 5 - removing all collection elements
		em.getTransaction().begin();
		darkCharacter = em.find( DarkCharacter.class, darkCharacter.getId() );
		darkCharacter.getNames().clear();
		darkCharacter = em.merge( darkCharacter );
		em.getTransaction().commit();

		em.close();
	}

	@Test
	public void testRevisionsCount() {
		Assert.assertEquals( Arrays.asList( 1, 2, 3, 4, 5 ), getAuditReader().getRevisions( DarkCharacter.class, id ) );
	}

	@Test
	public void testHistoryOfCharacter() {
		DarkCharacter darkCharacter = new DarkCharacter( id, 1 );

		DarkCharacter ver1 = getAuditReader().find( DarkCharacter.class, id, 1 );

		Assert.assertEquals( darkCharacter, ver1 );
		Assert.assertEquals( 0, ver1.getNames().size() );

		darkCharacter.getNames().add( new Name( "Action", "Hank" ) );
		DarkCharacter ver2 = getAuditReader().find( DarkCharacter.class, id, 2 );

		Assert.assertEquals( darkCharacter, ver2 );
		Assert.assertEquals( darkCharacter.getNames(), ver2.getNames() );

		darkCharacter.getNames().add( new Name( "Green", "Lantern" ) );
		DarkCharacter ver3 = getAuditReader().find( DarkCharacter.class, id, 3 );

		Assert.assertEquals( darkCharacter, ver3 );
		Assert.assertEquals( darkCharacter.getNames(), ver3.getNames() );

		darkCharacter.getNames().remove( new Name( "Action", "Hank" ) );
		DarkCharacter ver4 = getAuditReader().find( DarkCharacter.class, id, 4 );

		Assert.assertEquals( darkCharacter, ver4 );
		Assert.assertEquals( darkCharacter.getNames(), ver4.getNames() );

		darkCharacter.getNames().clear();
		DarkCharacter ver5 = getAuditReader().find( DarkCharacter.class, id, 5 );

		Assert.assertEquals( darkCharacter, ver5 );
		Assert.assertEquals( darkCharacter.getNames(), ver5.getNames() );
	}
}
