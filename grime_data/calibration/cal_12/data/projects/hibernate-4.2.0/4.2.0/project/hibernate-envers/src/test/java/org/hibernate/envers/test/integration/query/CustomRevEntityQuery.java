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

import java.util.List;
import javax.persistence.EntityManager;

import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.StrIntTestEntity;
import org.hibernate.envers.test.entities.reventity.CustomRevEntity;

/**
 * @author Adam Warski (adam at warski dot org)
 */
@SuppressWarnings({"unchecked"})
public class CustomRevEntityQuery extends BaseEnversJPAFunctionalTestCase {
    private Integer id1;
    private Integer id2;
    private Long timestamp;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(StrIntTestEntity.class);
        cfg.addAnnotatedClass(CustomRevEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() throws InterruptedException {
        // Revision 1
        EntityManager em = getEntityManager();
        em.getTransaction().begin();

        StrIntTestEntity site1 = new StrIntTestEntity("a", 10);
        StrIntTestEntity site2 = new StrIntTestEntity("b", 15);

        em.persist(site1);
        em.persist(site2);

        id1 = site1.getId();
        id2 = site2.getId();

        em.getTransaction().commit();

        Thread.sleep(100);

        timestamp = System.currentTimeMillis();

        Thread.sleep(100);

        // Revision 2
        em.getTransaction().begin();

        site1 = em.find(StrIntTestEntity.class, id1);

        site1.setStr1("c");

        em.getTransaction().commit();
    }

    @Test
    public void testRevisionsOfId1Query() {
        List<Object[]> result = getAuditReader().createQuery()
                .forRevisionsOfEntity(StrIntTestEntity.class, false, true)
                .add(AuditEntity.id().eq(id1))
                .getResultList();

        assert result.get(0)[0].equals(new StrIntTestEntity("a", 10, id1));
        assert result.get(0)[1] instanceof CustomRevEntity;
        assert ((CustomRevEntity) result.get(0)[1]).getCustomId() == 1;

        assert result.get(1)[0].equals(new StrIntTestEntity("c", 10, id1));
        assert result.get(1)[1] instanceof CustomRevEntity;
        assert ((CustomRevEntity) result.get(1)[1]).getCustomId() == 2;
    }

    @Test
    public void testRevisionsOfId2Query() {
        List<Object[]> result = getAuditReader().createQuery()
                .forRevisionsOfEntity(StrIntTestEntity.class, false, true)
                .add(AuditEntity.id().eq(id2))
                .getResultList();

        assert result.get(0)[0].equals(new StrIntTestEntity("b", 15, id2));
        assert result.get(0)[1] instanceof CustomRevEntity;
        assert ((CustomRevEntity) result.get(0)[1]).getCustomId() == 1;
    }

    @Test
    public void testRevisionPropertyRestriction() {
        List<Object[]> result = getAuditReader().createQuery()
                .forRevisionsOfEntity(StrIntTestEntity.class, false, true)
                .add(AuditEntity.id().eq(id1))
                .add(AuditEntity.revisionProperty("customTimestamp").ge(timestamp))
                .getResultList();

        assert result.get(0)[0].equals(new StrIntTestEntity("c", 10, id1));
        assert result.get(0)[1] instanceof CustomRevEntity;
        assert ((CustomRevEntity) result.get(0)[1]).getCustomId() == 2;  
        assert ((CustomRevEntity) result.get(0)[1]).getCustomTimestamp() >= timestamp;
    }
}