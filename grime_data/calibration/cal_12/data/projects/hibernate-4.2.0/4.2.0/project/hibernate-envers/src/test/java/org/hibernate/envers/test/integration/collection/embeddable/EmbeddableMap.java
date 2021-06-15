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
import java.util.Collections;

import javax.persistence.EntityManager;

import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.collection.EmbeddableMapEntity;
import org.hibernate.envers.test.entities.components.Component3;
import org.hibernate.envers.test.entities.components.Component4;
import org.hibernate.envers.test.tools.TestTools;
import org.hibernate.testing.TestForIssue;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Kristoffer Lundberg (kristoffer at cambio dot se)
 */
@TestForIssue( jiraKey = "HHH-6613" )
public class EmbeddableMap extends BaseEnversJPAFunctionalTestCase {
	private Integer eme1_id = null;
	private Integer eme2_id = null;

	private final Component4 c4_1 = new Component4( "c41", "c41_value", "c41_description" );
	private final Component4 c4_2 = new Component4( "c42", "c42_value2", "c42_description" );
	private final Component3 c3_1 = new Component3( "c31", c4_1, c4_2 );
	private final Component3 c3_2 = new Component3( "c32", c4_1, c4_2 );

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] { EmbeddableMapEntity.class };
	}

	@Test
	@Priority(10)
	public void initData() {
		EntityManager em = getEntityManager();

		EmbeddableMapEntity eme1 = new EmbeddableMapEntity();
		EmbeddableMapEntity eme2 = new EmbeddableMapEntity();

		// Revision 1 (eme1: initialy empty, eme2: initialy 1 mapping)
		em.getTransaction().begin();
		eme2.getComponentMap().put( "1", c3_1 );
		em.persist( eme1 );
		em.persist( eme2 );
		em.getTransaction().commit();

		// Revision 2 (eme1: adding 2 mappings, eme2: no changes)
		em.getTransaction().begin();
		eme1 = em.find( EmbeddableMapEntity.class, eme1.getId() );
		eme2 = em.find( EmbeddableMapEntity.class, eme2.getId() );
		eme1.getComponentMap().put( "1", c3_1 );
		eme1.getComponentMap().put( "2", c3_2 );
		em.getTransaction().commit();

		// Revision 3 (eme1: removing an existing mapping, eme2: replacing a value)
		em.getTransaction().begin();
		eme1 = em.find( EmbeddableMapEntity.class, eme1.getId() );
		eme2 = em.find( EmbeddableMapEntity.class, eme2.getId() );
		eme1.getComponentMap().remove( "1" );
		eme2.getComponentMap().put( "1", c3_2 );
		em.getTransaction().commit();

		// No revision (eme1: removing a non-existing mapping, eme2: replacing with the same value)
		em.getTransaction().begin();
		eme1 = em.find( EmbeddableMapEntity.class, eme1.getId() );
		eme2 = em.find( EmbeddableMapEntity.class, eme2.getId() );
		eme1.getComponentMap().remove( "3" );
		eme2.getComponentMap().put( "1", c3_2 );
		em.getTransaction().commit();

		eme1_id = eme1.getId();
		eme2_id = eme2.getId();

		em.close();
	}

	@Test
	public void testRevisionsCounts() {
		Assert.assertEquals( Arrays.asList( 1, 2, 3 ), getAuditReader().getRevisions( EmbeddableMapEntity.class, eme1_id ) );
		Assert.assertEquals( Arrays.asList( 1, 3 ), getAuditReader().getRevisions( EmbeddableMapEntity.class, eme2_id ) );
	}

	@Test
	public void testHistoryOfEme1() {
		EmbeddableMapEntity rev1 = getAuditReader().find( EmbeddableMapEntity.class, eme1_id, 1 );
		EmbeddableMapEntity rev2 = getAuditReader().find( EmbeddableMapEntity.class, eme1_id, 2 );
		EmbeddableMapEntity rev3 = getAuditReader().find( EmbeddableMapEntity.class, eme1_id, 3 );
		EmbeddableMapEntity rev4 = getAuditReader().find( EmbeddableMapEntity.class, eme1_id, 4 );

		Assert.assertEquals( Collections.EMPTY_MAP, rev1.getComponentMap() );
		Assert.assertEquals( TestTools.makeMap( "1", c3_1, "2", c3_2 ), rev2.getComponentMap() );
		Assert.assertEquals( TestTools.makeMap( "2", c3_2 ), rev3.getComponentMap() );
		Assert.assertEquals( TestTools.makeMap( "2", c3_2 ), rev4.getComponentMap() );
	}

	@Test
	public void testHistoryOfEme2() {
		EmbeddableMapEntity rev1 = getAuditReader().find( EmbeddableMapEntity.class, eme2_id, 1 );
		EmbeddableMapEntity rev2 = getAuditReader().find( EmbeddableMapEntity.class, eme2_id, 2 );
		EmbeddableMapEntity rev3 = getAuditReader().find( EmbeddableMapEntity.class, eme2_id, 3 );
		EmbeddableMapEntity rev4 = getAuditReader().find( EmbeddableMapEntity.class, eme2_id, 4 );

		Assert.assertEquals( TestTools.makeMap( "1", c3_1 ), rev1.getComponentMap() );
		Assert.assertEquals( TestTools.makeMap( "1", c3_1 ), rev2.getComponentMap() );
		Assert.assertEquals( TestTools.makeMap( "1", c3_2 ), rev3.getComponentMap() );
		Assert.assertEquals( TestTools.makeMap( "1", c3_2 ), rev4.getComponentMap() );
	}
}