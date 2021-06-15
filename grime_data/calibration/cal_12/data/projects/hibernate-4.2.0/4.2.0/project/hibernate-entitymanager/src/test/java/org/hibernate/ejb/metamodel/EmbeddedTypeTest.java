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
package org.hibernate.ejb.metamodel;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.SingularAttribute;

import org.junit.Test;

import org.hibernate.ejb.test.BaseEntityManagerFunctionalTestCase;
import org.hibernate.testing.TestForIssue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Steve Ebersole
 */
public class EmbeddedTypeTest extends BaseEntityManagerFunctionalTestCase {
	@Override
	public Class[] getAnnotatedClasses() {
		return new Class[] {
				Product.class, ShelfLife.class, VersionedEntity.class
		};
	}

	@Test
	@TestForIssue( jiraKey = "HHH-6896" )
	public void ensureComponentsReturnedAsManagedType() {
		ManagedType<ShelfLife> managedType = entityManagerFactory().getMetamodel().managedType( ShelfLife.class );
		// the issue was in regards to throwing an exception, but also check for nullness
		assertNotNull( managedType );
	}

	@Test
	@TestForIssue( jiraKey = "HHH-4702" )
	public void testSingularAttributeAccessByName() {
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();

		SingularAttribute soldDate_ = em.getMetamodel().embeddable( ShelfLife.class )
				.getSingularAttribute( "soldDate" );
		assertEquals( java.sql.Date.class, soldDate_.getBindableJavaType());
		assertEquals( java.sql.Date.class, soldDate_.getType().getJavaType() );
		assertEquals( java.sql.Date.class, soldDate_.getJavaType() );

		em.getTransaction().commit();
		em.close();
	}

	@Test
	@TestForIssue( jiraKey = "HHH-5821" )
	public void testVersionAttributeMetadata() {
		EntityManager em = getOrCreateEntityManager();
		EntityType<VersionedEntity> metadata = em.getMetamodel().entity( VersionedEntity.class );
		assertNotNull( metadata.getDeclaredVersion( int.class ) );
		assertTrue( metadata.getDeclaredVersion( int.class ).isVersion() );
		assertEquals( 3, metadata.getDeclaredSingularAttributes().size() );
		assertTrue( metadata.getDeclaredSingularAttributes().contains( metadata.getDeclaredVersion( int.class ) ) );
		em.close();
	}

}
