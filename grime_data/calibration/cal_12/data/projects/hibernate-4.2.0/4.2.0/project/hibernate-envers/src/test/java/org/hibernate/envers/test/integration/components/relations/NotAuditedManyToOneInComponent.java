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
package org.hibernate.envers.test.integration.components.relations;

import java.util.Arrays;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.UnversionedStrTestEntity;
import org.hibernate.envers.test.entities.components.relations.NotAuditedManyToOneComponent;
import org.hibernate.envers.test.entities.components.relations.NotAuditedManyToOneComponentTestEntity;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class NotAuditedManyToOneInComponent extends BaseEnversJPAFunctionalTestCase {
    private Integer mtocte_id1;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(NotAuditedManyToOneComponentTestEntity.class);
		cfg.addAnnotatedClass(UnversionedStrTestEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
		// No revision
        EntityManager em = getEntityManager();
        em.getTransaction().begin();

		UnversionedStrTestEntity ste1 = new UnversionedStrTestEntity();
        ste1.setStr("str1");

		UnversionedStrTestEntity ste2 = new UnversionedStrTestEntity();
        ste2.setStr("str2");

        em.persist(ste1);
		em.persist(ste2);

        em.getTransaction().commit();

        // Revision 1
        em = getEntityManager();
        em.getTransaction().begin();

		NotAuditedManyToOneComponentTestEntity mtocte1 = new NotAuditedManyToOneComponentTestEntity(
				new NotAuditedManyToOneComponent(ste1, "data1"));

		em.persist(mtocte1);

        em.getTransaction().commit();

        // No revision
        em = getEntityManager();
        em.getTransaction().begin();

        mtocte1 = em.find(NotAuditedManyToOneComponentTestEntity.class, mtocte1.getId());
        mtocte1.getComp1().setEntity(ste2);

        em.getTransaction().commit();

        // Revision 2
        em = getEntityManager();
        em.getTransaction().begin();

        mtocte1 = em.find(NotAuditedManyToOneComponentTestEntity.class, mtocte1.getId());
        mtocte1.getComp1().setData("data2");

        em.getTransaction().commit();

        mtocte_id1 = mtocte1.getId();
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(NotAuditedManyToOneComponentTestEntity.class, mtocte_id1));
    }

    @Test
    public void testHistoryOfId1() {
		NotAuditedManyToOneComponentTestEntity ver1 = new NotAuditedManyToOneComponentTestEntity(mtocte_id1,
				new NotAuditedManyToOneComponent(null, "data1"));
		NotAuditedManyToOneComponentTestEntity ver2 = new NotAuditedManyToOneComponentTestEntity(mtocte_id1,
				new NotAuditedManyToOneComponent(null, "data2"));

        assert getAuditReader().find(NotAuditedManyToOneComponentTestEntity.class, mtocte_id1, 1).equals(ver1);
        assert getAuditReader().find(NotAuditedManyToOneComponentTestEntity.class, mtocte_id1, 2).equals(ver2);
    }
}