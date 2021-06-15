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
package org.hibernate.envers.test.integration.notinsertable.manytoone;

import java.util.Arrays;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;

public class ManyToOneNotInsertable extends BaseEnversJPAFunctionalTestCase {
    private Integer mto_id1;
    private Integer type_id1;
    private Integer type_id2;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(ManyToOneNotInsertableEntity.class);
        cfg.addAnnotatedClass(NotInsertableEntityType.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        mto_id1 = 1;
        type_id1 = 2;
        type_id2 = 3;

		// Rev 1
		// Preparing the types
		em.getTransaction().begin();

		NotInsertableEntityType type1 = new NotInsertableEntityType(type_id1, "type1");
		NotInsertableEntityType type2 = new NotInsertableEntityType(type_id2, "type2");

		em.persist(type1);
		em.persist(type2);

		em.getTransaction().commit();

        // Rev 2
        em.getTransaction().begin();

        ManyToOneNotInsertableEntity master = new ManyToOneNotInsertableEntity(mto_id1, type_id1, type1);
        em.persist(master);

        em.getTransaction().commit();

		// Rev 2
        em.getTransaction().begin();

        master = em.find(ManyToOneNotInsertableEntity.class, mto_id1);
        master.setNumber(type_id2);

        em.getTransaction().commit();
    }

    @Test
    public void testRevisionCounts() {
		assert Arrays.asList(1).equals(getAuditReader().getRevisions(NotInsertableEntityType.class, type_id1));
		assert Arrays.asList(1).equals(getAuditReader().getRevisions(NotInsertableEntityType.class, type_id2));

        assert Arrays.asList(2, 3).equals(getAuditReader().getRevisions(ManyToOneNotInsertableEntity.class, mto_id1));        
    }

    @Test
    public void testNotInsertableEntity() {
        ManyToOneNotInsertableEntity ver1 = getAuditReader().find(ManyToOneNotInsertableEntity.class, mto_id1, 1);
		ManyToOneNotInsertableEntity ver2 = getAuditReader().find(ManyToOneNotInsertableEntity.class, mto_id1, 2);
		ManyToOneNotInsertableEntity ver3 = getAuditReader().find(ManyToOneNotInsertableEntity.class, mto_id1, 3);

		NotInsertableEntityType type1 = getEntityManager().find(NotInsertableEntityType.class, type_id1);
		NotInsertableEntityType type2 = getEntityManager().find(NotInsertableEntityType.class, type_id2);

		assert ver1 == null;
		assert ver2.getType().equals(type1);
		assert ver3.getType().equals(type2);
    }
}
