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
package org.hibernate.envers.test.integration.manytomany.unidirectional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.UnversionedStrTestEntity;
import org.hibernate.envers.test.entities.manytomany.unidirectional.M2MTargetNotAuditedEntity;

import static org.hibernate.envers.test.tools.TestTools.checkList;
import static org.junit.Assert.assertTrue;

/**
 * A test for auditing a many-to-many relation where the target entity is not audited.
 * @author Adam Warski
 */
public class M2MRelationNotAuditedTarget extends BaseEnversJPAFunctionalTestCase {
	private Integer tnae1_id;
	private Integer tnae2_id;

	private Integer uste1_id;
	private Integer uste2_id;

	public void configure(Ejb3Configuration cfg) {
		cfg.addAnnotatedClass(M2MTargetNotAuditedEntity.class);
		cfg.addAnnotatedClass(UnversionedStrTestEntity.class);
	}

	@Test
    @Priority(10)
	public void initData() {
		EntityManager em = getEntityManager();

		UnversionedStrTestEntity uste1 = new UnversionedStrTestEntity("str1");
		UnversionedStrTestEntity uste2 = new UnversionedStrTestEntity("str2");

		// No revision
		em.getTransaction().begin();

		em.persist(uste1);
		em.persist(uste2);

		em.getTransaction().commit();

		// Revision 1
		em.getTransaction().begin();

		uste1 = em.find(UnversionedStrTestEntity.class, uste1.getId());
		uste2 = em.find(UnversionedStrTestEntity.class, uste2.getId());

		M2MTargetNotAuditedEntity tnae1 = new M2MTargetNotAuditedEntity(1, "tnae1", new ArrayList<UnversionedStrTestEntity>());
		M2MTargetNotAuditedEntity tnae2 = new M2MTargetNotAuditedEntity(2, "tnae2", new ArrayList<UnversionedStrTestEntity>());
        tnae2.getReferences().add(uste1);
        tnae2.getReferences().add(uste2);
		em.persist(tnae1);
		em.persist(tnae2);

		em.getTransaction().commit();

		// Revision 2
		em.getTransaction().begin();

		tnae1 = em.find(M2MTargetNotAuditedEntity.class, tnae1.getId());
		tnae2 = em.find(M2MTargetNotAuditedEntity.class, tnae2.getId());

		tnae1.getReferences().add(uste1);
		tnae2.getReferences().remove(uste1);

		em.getTransaction().commit();

		// Revision 3
		em.getTransaction().begin();

		tnae1 = em.find(M2MTargetNotAuditedEntity.class, tnae1.getId());
		tnae2 = em.find(M2MTargetNotAuditedEntity.class, tnae2.getId());

		//field not changed!!!
		tnae1.getReferences().add(uste1);
		tnae2.getReferences().remove(uste2);

		em.getTransaction().commit();

		// Revision 4
		em.getTransaction().begin();

		tnae1 = em.find(M2MTargetNotAuditedEntity.class, tnae1.getId());
		tnae2 = em.find(M2MTargetNotAuditedEntity.class, tnae2.getId());

		tnae1.getReferences().add(uste2);
		tnae2.getReferences().add(uste1);

		em.getTransaction().commit();

		//
		tnae1_id = tnae1.getId();
		tnae2_id = tnae2.getId();
		uste1_id = uste1.getId();
		uste2_id = uste2.getId();
	}

	@Test
	public void testRevisionsCounts() {
		List<Number> revisions = getAuditReader().getRevisions(M2MTargetNotAuditedEntity.class, tnae1_id);
		assert Arrays.asList(1, 2, 4).equals(revisions);
		revisions = getAuditReader().getRevisions(M2MTargetNotAuditedEntity.class, tnae2_id);
		assert Arrays.asList(1, 2, 3, 4).equals(revisions);
	}

	@Test
	public void testHistoryOfTnae1_id() {
		UnversionedStrTestEntity uste1 = getEntityManager().find(UnversionedStrTestEntity.class, uste1_id);
		UnversionedStrTestEntity uste2 = getEntityManager().find(UnversionedStrTestEntity.class, uste2_id);

		M2MTargetNotAuditedEntity rev1 = getAuditReader().find(M2MTargetNotAuditedEntity.class, tnae1_id, 1);
		M2MTargetNotAuditedEntity rev2 = getAuditReader().find(M2MTargetNotAuditedEntity.class, tnae1_id, 2);
		M2MTargetNotAuditedEntity rev3 = getAuditReader().find(M2MTargetNotAuditedEntity.class, tnae1_id, 3);
		M2MTargetNotAuditedEntity rev4 = getAuditReader().find(M2MTargetNotAuditedEntity.class, tnae1_id, 4);

		assertTrue(checkList(rev1.getReferences()));
		assertTrue(checkList(rev2.getReferences(), uste1));
		assertTrue(checkList(rev3.getReferences(), uste1));
		assertTrue(checkList(rev4.getReferences(), uste1, uste2));
	}

	@Test
	public void testHistoryOfTnae2_id() {
		UnversionedStrTestEntity uste1 = getEntityManager().find(UnversionedStrTestEntity.class, uste1_id);
		UnversionedStrTestEntity uste2 = getEntityManager().find(UnversionedStrTestEntity.class, uste2_id);

		M2MTargetNotAuditedEntity rev1 = getAuditReader().find(M2MTargetNotAuditedEntity.class, tnae2_id, 1);
		M2MTargetNotAuditedEntity rev2 = getAuditReader().find(M2MTargetNotAuditedEntity.class, tnae2_id, 2);
		M2MTargetNotAuditedEntity rev3 = getAuditReader().find(M2MTargetNotAuditedEntity.class, tnae2_id, 3);
		M2MTargetNotAuditedEntity rev4 = getAuditReader().find(M2MTargetNotAuditedEntity.class, tnae2_id, 4);

		assertTrue(checkList(rev1.getReferences(), uste1, uste2));
		assertTrue(checkList(rev2.getReferences(), uste2));
		assertTrue(checkList(rev3.getReferences()));
		assertTrue(checkList(rev4.getReferences(), uste1));
	}
}
