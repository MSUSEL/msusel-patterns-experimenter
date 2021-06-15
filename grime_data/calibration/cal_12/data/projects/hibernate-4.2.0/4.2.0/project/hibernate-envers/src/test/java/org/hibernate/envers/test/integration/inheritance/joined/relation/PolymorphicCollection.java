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
package org.hibernate.envers.test.integration.inheritance.joined.relation;

import java.util.Arrays;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.tools.TestTools;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class PolymorphicCollection extends BaseEnversJPAFunctionalTestCase {
    private Integer ed_id1;
    private Integer c_id;
    private Integer p_id;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(ChildIngEntity.class);
        cfg.addAnnotatedClass(ParentIngEntity.class);
        cfg.addAnnotatedClass(ReferencedEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        ed_id1 = 1;
        p_id = 10;
        c_id = 100;

        // Rev 1
        em.getTransaction().begin();

        ReferencedEntity re = new ReferencedEntity(ed_id1);
        em.persist(re);

        em.getTransaction().commit();

        // Rev 2
        em.getTransaction().begin();

        re = em.find(ReferencedEntity.class, ed_id1);

        ParentIngEntity pie = new ParentIngEntity(p_id,"x");
        pie.setReferenced(re);
        em.persist(pie);
        p_id = pie.getId();

        em.getTransaction().commit();

        // Rev 3
        em.getTransaction().begin();

        re = em.find(ReferencedEntity.class, ed_id1);

        ChildIngEntity cie = new ChildIngEntity(c_id, "y", 1l);
        cie.setReferenced(re);
        em.persist(cie);
        c_id = cie.getId();

        em.getTransaction().commit();
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1, 2, 3).equals(getAuditReader().getRevisions(ReferencedEntity.class, ed_id1));
        assert Arrays.asList(2).equals(getAuditReader().getRevisions(ParentIngEntity.class, p_id));
        assert Arrays.asList(3).equals(getAuditReader().getRevisions(ChildIngEntity.class, c_id));
    }

    @Test
    public void testHistoryOfReferencedCollection() {
        assert getAuditReader().find(ReferencedEntity.class, ed_id1, 1).getReferencing().size() == 0;
        assert getAuditReader().find(ReferencedEntity.class, ed_id1, 2).getReferencing().equals(
                TestTools.makeSet(new ParentIngEntity(p_id, "x")));
        assert getAuditReader().find(ReferencedEntity.class, ed_id1, 3).getReferencing().equals(
                TestTools.makeSet(new ParentIngEntity(p_id, "x"), new ChildIngEntity(c_id, "y", 1l)));
    }
}