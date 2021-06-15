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

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.enhanced.SequenceIdRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.StrIntTestEntity;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class DeletedEntities extends BaseEnversJPAFunctionalTestCase {
    private Integer id2;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(StrIntTestEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        // Revision 1
        EntityManager em = getEntityManager();
        em.getTransaction().begin();

        StrIntTestEntity site1 = new StrIntTestEntity("a", 10);
        StrIntTestEntity site2 = new StrIntTestEntity("b", 11);

        em.persist(site1);
        em.persist(site2);

        id2 = site2.getId();

        em.getTransaction().commit();

        // Revision 2
        em.getTransaction().begin();

        site2 = em.find(StrIntTestEntity.class, id2);
        em.remove(site2);

        em.getTransaction().commit();
    }

    @Test
    public void testProjectionsInEntitiesAtRevision() {
        assert getAuditReader().createQuery().forEntitiesAtRevision(StrIntTestEntity.class, 1)
            .getResultList().size() == 2;
        assert getAuditReader().createQuery().forEntitiesAtRevision(StrIntTestEntity.class, 2)
            .getResultList().size() == 1;

        assert (Long) getAuditReader().createQuery().forEntitiesAtRevision(StrIntTestEntity.class, 1)
            .addProjection(AuditEntity.id().count("id")).getResultList().get(0) == 2;
        assert (Long) getAuditReader().createQuery().forEntitiesAtRevision(StrIntTestEntity.class, 2)
            .addProjection(AuditEntity.id().count()).getResultList().get(0) == 1;
    }

    @Test
    public void testRevisionsOfEntityWithoutDelete() {
        List result = getAuditReader().createQuery()
                .forRevisionsOfEntity(StrIntTestEntity.class, false, false)
                .add(AuditEntity.id().eq(id2))
                .getResultList();

        assert result.size() == 1;

        assert ((Object []) result.get(0))[0].equals(new StrIntTestEntity("b", 11, id2));
        assert ((SequenceIdRevisionEntity) ((Object []) result.get(0))[1]).getId() == 1;
        assert ((Object []) result.get(0))[2].equals(RevisionType.ADD);
    }
}
