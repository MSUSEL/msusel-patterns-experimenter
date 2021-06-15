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
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase ;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.StrTestEntity;
import org.hibernate.envers.test.entities.components.relations.ManyToOneComponent;
import org.hibernate.envers.test.entities.components.relations.ManyToOneComponentTestEntity;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class ManyToOneInComponent extends BaseEnversJPAFunctionalTestCase  {
    private Integer mtocte_id1;
	private Integer ste_id1;
	private Integer ste_id2;

    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class[]{ManyToOneComponentTestEntity.class, StrTestEntity.class};

    }

    @Test
    @Priority(10)
    public void initData() {
        // Revision 1
        EntityManager em = getEntityManager();
        em.getTransaction().begin();

		StrTestEntity ste1 = new StrTestEntity();
        ste1.setStr("str1");

		StrTestEntity ste2 = new StrTestEntity();
        ste2.setStr("str2");

        em.persist(ste1);
		em.persist(ste2);

        em.getTransaction().commit();

        // Revision 2
        em = getEntityManager();
        em.getTransaction().begin();

		ManyToOneComponentTestEntity mtocte1 = new ManyToOneComponentTestEntity(new ManyToOneComponent(ste1, "data1"));

		em.persist(mtocte1);

        em.getTransaction().commit();

        // Revision 3
        em = getEntityManager();
        em.getTransaction().begin();

        mtocte1 = em.find(ManyToOneComponentTestEntity.class, mtocte1.getId());
        mtocte1.getComp1().setEntity(ste2);

        em.getTransaction().commit();

        mtocte_id1 = mtocte1.getId();
		ste_id1 = ste1.getId();
		ste_id2 = ste2.getId();
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(2, 3).equals(getAuditReader().getRevisions(ManyToOneComponentTestEntity.class, mtocte_id1));
    }

    @Test
    public void testHistoryOfId1() {
		StrTestEntity ste1 = getEntityManager().find(StrTestEntity.class, ste_id1);
		StrTestEntity ste2 = getEntityManager().find(StrTestEntity.class, ste_id2);

        ManyToOneComponentTestEntity ver2 = new ManyToOneComponentTestEntity(mtocte_id1, new ManyToOneComponent(ste1, "data1"));
		ManyToOneComponentTestEntity ver3 = new ManyToOneComponentTestEntity(mtocte_id1, new ManyToOneComponent(ste2, "data1"));

        assert getAuditReader().find(ManyToOneComponentTestEntity.class, mtocte_id1, 1) == null;
        assert getAuditReader().find(ManyToOneComponentTestEntity.class, mtocte_id1, 2).equals(ver2);
        assert getAuditReader().find(ManyToOneComponentTestEntity.class, mtocte_id1, 3).equals(ver3);
    }
}
