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
package org.hibernate.envers.test.integration.customtype;

import java.util.Arrays;
import java.util.Map;
import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Test;

import org.hibernate.dialect.Oracle8iDialect;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.testing.RequiresDialect;
import org.hibernate.testing.TestForIssue;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@TestForIssue(jiraKey = "HHH-7870")
@RequiresDialect(Oracle8iDialect.class)
public class ObjectUserTypeTest extends BaseEnversJPAFunctionalTestCase {
	private int id;

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] { ObjectUserTypeEntity.class };
	}

	@Override
	protected void addConfigOptions(Map options) {
		super.addConfigOptions( options );
		options.put( "org.hibernate.envers.store_data_at_delete", "true" );
	}

	@Test
	@Priority(10)
	public void initData() {
		EntityManager em = getEntityManager();

		// Revision 1 - add
		em.getTransaction().begin();
		ObjectUserTypeEntity entity = new ObjectUserTypeEntity( "builtInType1", "stringUserType1" );
		em.persist( entity );
		em.getTransaction().commit();

		id = entity.getId();

		// Revision 2 - modify
		em.getTransaction().begin();
		entity = em.find( ObjectUserTypeEntity.class, entity.getId() );
		entity.setUserType( 2 );
		entity = em.merge( entity );
		em.getTransaction().commit();

		// Revision 3 - remove
		em.getTransaction().begin();
		entity = em.find( ObjectUserTypeEntity.class, entity.getId() );
		em.remove( entity );
		em.getTransaction().commit();

		em.close();
	}

	@Test
	public void testRevisionCount() {
		Assert.assertEquals(
				Arrays.asList( 1, 2, 3 ),
				getAuditReader().getRevisions( ObjectUserTypeEntity.class, id )
		);
	}

	@Test
	public void testHistory() {
		ObjectUserTypeEntity ver1 = new ObjectUserTypeEntity( id, "builtInType1", "stringUserType1" );
		ObjectUserTypeEntity ver2 = new ObjectUserTypeEntity( id, "builtInType1", 2 );

		Assert.assertEquals( ver1, getAuditReader().find( ObjectUserTypeEntity.class, id, 1 ) );
		Assert.assertEquals( ver2, getAuditReader().find( ObjectUserTypeEntity.class, id, 2 ) );
		Assert.assertEquals(
				ver2,
				getAuditReader().createQuery().forRevisionsOfEntity( ObjectUserTypeEntity.class, true, true ).getResultList().get( 2 )
		); // Checking delete state.
	}
}
