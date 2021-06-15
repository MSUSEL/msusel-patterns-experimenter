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
package org.hibernate.envers.test.integration.query;

import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.IntTestEntity;

/**
 * @author Adam Warski (adam at warski dot org)
 */
@SuppressWarnings({"unchecked"})
public class OrderByLimitQuery extends BaseEnversJPAFunctionalTestCase {
    private Integer id1;
    private Integer id2;
    private Integer id3;
    private Integer id4;
    private Integer id5;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(IntTestEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        // Revision 1
        EntityManager em = getEntityManager();
        em.getTransaction().begin();

        IntTestEntity ite1 = new IntTestEntity(12);
        IntTestEntity ite2 = new IntTestEntity(5);
        IntTestEntity ite3 = new IntTestEntity(8);
        IntTestEntity ite4 = new IntTestEntity(1);

        em.persist(ite1);
        em.persist(ite2);
        em.persist(ite3);
        em.persist(ite4);

        id1 = ite1.getId();
        id2 = ite2.getId();
        id3 = ite3.getId();
        id4 = ite4.getId();

        em.getTransaction().commit();

        // Revision 2
        em.getTransaction().begin();

        IntTestEntity ite5 = new IntTestEntity(3);
        em.persist(ite5);
        id5 = ite5.getId();

        ite1 = em.find(IntTestEntity.class, id1);
        ite1.setNumber(0);

        ite4 = em.find(IntTestEntity.class, id4);
        ite4.setNumber(15);

        em.getTransaction().commit();
    }

    @Test
    public void testEntitiesOrderLimitByQueryRev1() {
        List res_0_to_1 = getAuditReader().createQuery()
                .forEntitiesAtRevision(IntTestEntity.class, 1)
                .addOrder(AuditEntity.property("number").desc())
                .setFirstResult(0)
                .setMaxResults(2)
                .getResultList();

        List res_2_to_3 = getAuditReader().createQuery()
                .forEntitiesAtRevision(IntTestEntity.class, 1)
                .addOrder(AuditEntity.property("number").desc())
                .setFirstResult(2)
                .setMaxResults(2)
                .getResultList();

        List res_empty = getAuditReader().createQuery()
                .forEntitiesAtRevision(IntTestEntity.class, 1)
                .addOrder(AuditEntity.property("number").desc())
                .setFirstResult(4)
                .setMaxResults(2)
                .getResultList();

        assert Arrays.asList(new IntTestEntity(12, id1), new IntTestEntity(8, id3)).equals(res_0_to_1);
        assert Arrays.asList(new IntTestEntity(5, id2), new IntTestEntity(1, id4)).equals(res_2_to_3);
        assert Arrays.asList().equals(res_empty);
    }

    @Test
    public void testEntitiesOrderLimitByQueryRev2() {
        List res_0_to_1 = getAuditReader().createQuery()
                .forEntitiesAtRevision(IntTestEntity.class, 2)
                .addOrder(AuditEntity.property("number").desc())
                .setFirstResult(0)
                .setMaxResults(2)
                .getResultList();

        List res_2_to_3 = getAuditReader().createQuery()
                .forEntitiesAtRevision(IntTestEntity.class, 2)
                .addOrder(AuditEntity.property("number").desc())
                .setFirstResult(2)
                .setMaxResults(2)
                .getResultList();

        List res_4 = getAuditReader().createQuery()
                .forEntitiesAtRevision(IntTestEntity.class, 2)
                .addOrder(AuditEntity.property("number").desc())
                .setFirstResult(4)
                .setMaxResults(2)
                .getResultList();

        assert Arrays.asList(new IntTestEntity(15, id4), new IntTestEntity(8, id3)).equals(res_0_to_1);
        assert Arrays.asList(new IntTestEntity(5, id2), new IntTestEntity(3, id5)).equals(res_2_to_3);
        assert Arrays.asList(new IntTestEntity(0, id1)).equals(res_4);
    }
}