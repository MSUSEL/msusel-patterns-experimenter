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
package org.hibernate.envers.test.integration.manytomany.biowned;

import java.util.Arrays;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.manytomany.biowned.ListBiowning1Entity;
import org.hibernate.envers.test.entities.manytomany.biowned.ListBiowning2Entity;
import org.hibernate.envers.test.tools.TestTools;

import static org.junit.Assert.assertEquals;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class BasicBiowned extends BaseEnversJPAFunctionalTestCase {
    private Integer o1_1_id;
    private Integer o1_2_id;
    private Integer o2_1_id;
    private Integer o2_2_id;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(ListBiowning1Entity.class);
        cfg.addAnnotatedClass(ListBiowning2Entity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        ListBiowning1Entity o1_1 = new ListBiowning1Entity("o1_1");
        ListBiowning1Entity o1_2 = new ListBiowning1Entity("o1_2");
        ListBiowning2Entity o2_1 = new ListBiowning2Entity("o2_1");
        ListBiowning2Entity o2_2 = new ListBiowning2Entity("o2_2");

        // Revision 1
        em.getTransaction().begin();

        em.persist(o1_1);
        em.persist(o1_2);
        em.persist(o2_1);
        em.persist(o2_2);

        em.getTransaction().commit();
        em.clear();

        // Revision 2 (1_1 <-> 2_1; 1_2 <-> 2_2)

        em.getTransaction().begin();

        o1_1 = em.find(ListBiowning1Entity.class, o1_1.getId());
        o1_2 = em.find(ListBiowning1Entity.class, o1_2.getId());
        o2_1 = em.find(ListBiowning2Entity.class, o2_1.getId());
        o2_2 = em.find(ListBiowning2Entity.class, o2_2.getId());

        o1_1.getReferences().add(o2_1);
        o1_2.getReferences().add(o2_2);

        em.getTransaction().commit();
        em.clear();

        // Revision 3 (1_1 <-> 2_1, 2_2; 1_2 <-> 2_2)
        em.getTransaction().begin();

        o1_1 = em.find(ListBiowning1Entity.class, o1_1.getId());
        o2_2 = em.find(ListBiowning2Entity.class, o2_2.getId());

        o1_1.getReferences().add(o2_2);

        em.getTransaction().commit();
        em.clear();

        // Revision 4 (1_2 <-> 2_1, 2_2)
        em.getTransaction().begin();

        o1_1 = em.find(ListBiowning1Entity.class, o1_1.getId());
        o1_2 = em.find(ListBiowning1Entity.class, o1_2.getId());
        o2_1 = em.find(ListBiowning2Entity.class, o2_1.getId());
        o2_2 = em.find(ListBiowning2Entity.class, o2_2.getId());

        o2_2.getReferences().remove(o1_1);
        o2_1.getReferences().remove(o1_1);
        o2_1.getReferences().add(o1_2);

        em.getTransaction().commit();
        em.clear();

        // Revision 5 (1_1 <-> 2_2, 1_2 <-> 2_2)
        em.getTransaction().begin();

        o1_1 = em.find(ListBiowning1Entity.class, o1_1.getId());
        o1_2 = em.find(ListBiowning1Entity.class, o1_2.getId());
        o2_1 = em.find(ListBiowning2Entity.class, o2_1.getId());
        o2_2 = em.find(ListBiowning2Entity.class, o2_2.getId());

        o1_2.getReferences().remove(o2_1);
        o1_1.getReferences().add(o2_2);

        em.getTransaction().commit();
        em.clear();

        //

        o1_1_id = o1_1.getId();
        o1_2_id = o1_2.getId();
        o2_1_id = o2_1.getId();
        o2_2_id = o2_2.getId();
    }

    @Test
    public void testRevisionsCounts() {
        // Although it would seem that when modifying references both entities should be marked as modified, because
        // ownly the owning side is notified (because of the bi-owning mapping), a revision is created only for
        // the entity where the collection was directly modified.

        assertEquals(Arrays.asList(1, 2, 3, 5), getAuditReader().getRevisions(ListBiowning1Entity.class, o1_1_id));
        assertEquals(Arrays.asList(1, 2, 5), getAuditReader().getRevisions(ListBiowning1Entity.class, o1_2_id));

        assertEquals(Arrays.asList(1, 4), getAuditReader().getRevisions(ListBiowning2Entity.class, o2_1_id));
        assertEquals(Arrays.asList(1, 4), getAuditReader().getRevisions(ListBiowning2Entity.class, o2_2_id));
    }

    @Test
    public void testHistoryOfO1_1() {
        ListBiowning2Entity o2_1 = getEntityManager().find(ListBiowning2Entity.class, o2_1_id);
        ListBiowning2Entity o2_2 = getEntityManager().find(ListBiowning2Entity.class, o2_2_id);

        ListBiowning1Entity rev1 = getAuditReader().find(ListBiowning1Entity.class, o1_1_id, 1);
        ListBiowning1Entity rev2 = getAuditReader().find(ListBiowning1Entity.class, o1_1_id, 2);
        ListBiowning1Entity rev3 = getAuditReader().find(ListBiowning1Entity.class, o1_1_id, 3);
        ListBiowning1Entity rev4 = getAuditReader().find(ListBiowning1Entity.class, o1_1_id, 4);
        ListBiowning1Entity rev5 = getAuditReader().find(ListBiowning1Entity.class, o1_1_id, 5);

        assert TestTools.checkList(rev1.getReferences());
        assert TestTools.checkList(rev2.getReferences(), o2_1);
        assert TestTools.checkList(rev3.getReferences(), o2_1, o2_2);
        assert TestTools.checkList(rev4.getReferences());
        assert TestTools.checkList(rev5.getReferences(), o2_2);
    }

    @Test
    public void testHistoryOfO1_2() {
        ListBiowning2Entity o2_1 = getEntityManager().find(ListBiowning2Entity.class, o2_1_id);
        ListBiowning2Entity o2_2 = getEntityManager().find(ListBiowning2Entity.class, o2_2_id);

        ListBiowning1Entity rev1 = getAuditReader().find(ListBiowning1Entity.class, o1_2_id, 1);
        ListBiowning1Entity rev2 = getAuditReader().find(ListBiowning1Entity.class, o1_2_id, 2);
        ListBiowning1Entity rev3 = getAuditReader().find(ListBiowning1Entity.class, o1_2_id, 3);
        ListBiowning1Entity rev4 = getAuditReader().find(ListBiowning1Entity.class, o1_2_id, 4);
        ListBiowning1Entity rev5 = getAuditReader().find(ListBiowning1Entity.class, o1_2_id, 5);

        assert TestTools.checkList(rev1.getReferences());
        assert TestTools.checkList(rev2.getReferences(), o2_2);
        assert TestTools.checkList(rev3.getReferences(), o2_2);
        assert TestTools.checkList(rev4.getReferences(), o2_1, o2_2);
        System.out.println("rev5.getReferences() = " + rev5.getReferences());
        assert TestTools.checkList(rev5.getReferences(), o2_2);
    }

    @Test
    public void testHistoryOfO2_1() {
        ListBiowning1Entity o1_1 = getEntityManager().find(ListBiowning1Entity.class, o1_1_id);
        ListBiowning1Entity o1_2 = getEntityManager().find(ListBiowning1Entity.class, o1_2_id);

        ListBiowning2Entity rev1 = getAuditReader().find(ListBiowning2Entity.class, o2_1_id, 1);
        ListBiowning2Entity rev2 = getAuditReader().find(ListBiowning2Entity.class, o2_1_id, 2);
        ListBiowning2Entity rev3 = getAuditReader().find(ListBiowning2Entity.class, o2_1_id, 3);
        ListBiowning2Entity rev4 = getAuditReader().find(ListBiowning2Entity.class, o2_1_id, 4);
        ListBiowning2Entity rev5 = getAuditReader().find(ListBiowning2Entity.class, o2_1_id, 5);

        assert TestTools.checkList(rev1.getReferences());
        assert TestTools.checkList(rev2.getReferences(), o1_1);
        assert TestTools.checkList(rev3.getReferences(), o1_1);
        assert TestTools.checkList(rev4.getReferences(), o1_2);
        assert TestTools.checkList(rev5.getReferences());
    }

    @Test
    public void testHistoryOfO2_2() {
        ListBiowning1Entity o1_1 = getEntityManager().find(ListBiowning1Entity.class, o1_1_id);
        ListBiowning1Entity o1_2 = getEntityManager().find(ListBiowning1Entity.class, o1_2_id);

        ListBiowning2Entity rev1 = getAuditReader().find(ListBiowning2Entity.class, o2_2_id, 1);
        ListBiowning2Entity rev2 = getAuditReader().find(ListBiowning2Entity.class, o2_2_id, 2);
        ListBiowning2Entity rev3 = getAuditReader().find(ListBiowning2Entity.class, o2_2_id, 3);
        ListBiowning2Entity rev4 = getAuditReader().find(ListBiowning2Entity.class, o2_2_id, 4);
        ListBiowning2Entity rev5 = getAuditReader().find(ListBiowning2Entity.class, o2_2_id, 5);

        assert TestTools.checkList(rev1.getReferences());
        assert TestTools.checkList(rev2.getReferences(), o1_2);
        assert TestTools.checkList(rev3.getReferences(), o1_1, o1_2);
        assert TestTools.checkList(rev4.getReferences(), o1_2);
        assert TestTools.checkList(rev5.getReferences(), o1_1, o1_2);
    }
}