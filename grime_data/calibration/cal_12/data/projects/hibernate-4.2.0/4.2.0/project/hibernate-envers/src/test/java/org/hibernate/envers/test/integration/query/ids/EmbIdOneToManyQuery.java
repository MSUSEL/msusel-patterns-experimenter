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
package org.hibernate.envers.test.integration.query.ids;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.ids.EmbId;
import org.hibernate.envers.test.entities.onetomany.ids.SetRefEdEmbIdEntity;
import org.hibernate.envers.test.entities.onetomany.ids.SetRefIngEmbIdEntity;
import org.hibernate.envers.test.tools.TestTools;

/**
 * @author Adam Warski (adam at warski dot org)
 */
@SuppressWarnings({"unchecked"})
public class EmbIdOneToManyQuery extends BaseEnversJPAFunctionalTestCase {
    private EmbId id1;
    private EmbId id2;

    private EmbId id3;
    private EmbId id4;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(SetRefEdEmbIdEntity.class);
        cfg.addAnnotatedClass(SetRefIngEmbIdEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        id1 = new EmbId(0, 1);
        id2 = new EmbId(10, 11);
        id3 = new EmbId(20, 21);
        id4 = new EmbId(30, 31);

        // Revision 1
        EntityManager em = getEntityManager();
        em.getTransaction().begin();

        SetRefIngEmbIdEntity refIng1 = new SetRefIngEmbIdEntity(id1, "x", null);
        SetRefIngEmbIdEntity refIng2 = new SetRefIngEmbIdEntity(id2, "y", null);

        em.persist(refIng1);
        em.persist(refIng2);

        em.getTransaction().commit();

        // Revision 2
        em.getTransaction().begin();

        SetRefEdEmbIdEntity refEd3 = new SetRefEdEmbIdEntity(id3, "a");
        SetRefEdEmbIdEntity refEd4 = new SetRefEdEmbIdEntity(id4, "a");

        em.persist(refEd3);
        em.persist(refEd4);

        refIng1 = em.find(SetRefIngEmbIdEntity.class, id1);
        refIng2 = em.find(SetRefIngEmbIdEntity.class, id2);

        refIng1.setReference(refEd3);
        refIng2.setReference(refEd4);

        em.getTransaction().commit();

        // Revision 3
        em.getTransaction().begin();

        refEd3 = em.find(SetRefEdEmbIdEntity.class, id3);
        refIng2 = em.find(SetRefIngEmbIdEntity.class, id2);
        refIng2.setReference(refEd3);

        em.getTransaction().commit();
    }

    @Test
    public void testEntitiesReferencedToId3() {
        Set rev1_related = new HashSet(getAuditReader().createQuery()
                .forEntitiesAtRevision(SetRefIngEmbIdEntity.class, 1)
                .add(AuditEntity.relatedId("reference").eq(id3))
                .getResultList());

        Set rev1 = new HashSet(getAuditReader().createQuery()
                .forEntitiesAtRevision(SetRefIngEmbIdEntity.class, 1)
                .add(AuditEntity.property("reference").eq(new SetRefEdEmbIdEntity(id3, null)))
                .getResultList());

        Set rev2_related = new HashSet(getAuditReader().createQuery()
                .forEntitiesAtRevision(SetRefIngEmbIdEntity.class, 2)
                .add(AuditEntity.relatedId("reference").eq(id3))
                .getResultList());

        Set rev2 = new HashSet(getAuditReader().createQuery()
                .forEntitiesAtRevision(SetRefIngEmbIdEntity.class, 2)
                .add(AuditEntity.property("reference").eq(new SetRefEdEmbIdEntity(id3, null)))
                .getResultList());

        Set rev3_related = new HashSet(getAuditReader().createQuery()
                .forEntitiesAtRevision(SetRefIngEmbIdEntity.class, 3)
                .add(AuditEntity.relatedId("reference").eq(id3))
                .getResultList());

        Set rev3 = new HashSet(getAuditReader().createQuery()
                .forEntitiesAtRevision(SetRefIngEmbIdEntity.class, 3)
                .add(AuditEntity.property("reference").eq(new SetRefEdEmbIdEntity(id3, null)))
                .getResultList());

        assert rev1.equals(rev1_related);
        assert rev2.equals(rev2_related);
        assert rev3.equals(rev3_related);

        assert rev1.equals(TestTools.makeSet());
        assert rev2.equals(TestTools.makeSet(new SetRefIngEmbIdEntity(id1, "x", null)));
        assert rev3.equals(TestTools.makeSet(new SetRefIngEmbIdEntity(id1, "x", null),
                new SetRefIngEmbIdEntity(id2, "y", null)));
    }

    @Test
    public void testEntitiesReferencedToId4() {
        Set rev1_related = new HashSet(getAuditReader().createQuery()
                .forEntitiesAtRevision(SetRefIngEmbIdEntity.class, 1)
                .add(AuditEntity.relatedId("reference").eq(id4))
                .getResultList());

        Set rev2_related = new HashSet(getAuditReader().createQuery()
                .forEntitiesAtRevision(SetRefIngEmbIdEntity.class, 2)
                .add(AuditEntity.relatedId("reference").eq(id4))
                .getResultList());

        Set rev3_related = new HashSet(getAuditReader().createQuery()
                .forEntitiesAtRevision(SetRefIngEmbIdEntity.class, 3)
                .add(AuditEntity.relatedId("reference").eq(id4))
                .getResultList());

        assert rev1_related.equals(TestTools.makeSet());
        assert rev2_related.equals(TestTools.makeSet(new SetRefIngEmbIdEntity(id2, "y", null)));
        assert rev3_related.equals(TestTools.makeSet());
    }

    @Test
    public void testEntitiesReferencedByIng1ToId3() {
        List rev1_related = getAuditReader().createQuery()
                .forEntitiesAtRevision(SetRefIngEmbIdEntity.class, 1)
                .add(AuditEntity.relatedId("reference").eq(id3))
                .add(AuditEntity.id().eq(id1))
                .getResultList();

        Object rev2_related = getAuditReader().createQuery()
                .forEntitiesAtRevision(SetRefIngEmbIdEntity.class, 2)
                .add(AuditEntity.relatedId("reference").eq(id3))
                .add(AuditEntity.id().eq(id1))
                .getSingleResult();

        Object rev3_related = getAuditReader().createQuery()
                .forEntitiesAtRevision(SetRefIngEmbIdEntity.class, 3)
                .add(AuditEntity.relatedId("reference").eq(id3))
                .add(AuditEntity.id().eq(id1))
                .getSingleResult();

        assert rev1_related.size() == 0;
        assert rev2_related.equals(new SetRefIngEmbIdEntity(id1, "x", null));
        assert rev3_related.equals(new SetRefIngEmbIdEntity(id1, "x", null));
    }

    @Test
    public void testEntitiesReferencedByIng2ToId3() {
        List rev1_related = getAuditReader().createQuery()
                .forEntitiesAtRevision(SetRefIngEmbIdEntity.class, 1)
                .add(AuditEntity.relatedId("reference").eq(id3))
                .add(AuditEntity.id().eq(id2))
                .getResultList();

        List rev2_related = getAuditReader().createQuery()
                .forEntitiesAtRevision(SetRefIngEmbIdEntity.class, 2)
                .add(AuditEntity.relatedId("reference").eq(id3))
                .add(AuditEntity.id().eq(id2))
                .getResultList();

        Object rev3_related = getAuditReader().createQuery()
                .forEntitiesAtRevision(SetRefIngEmbIdEntity.class, 3)
                .add(AuditEntity.relatedId("reference").eq(id3))
                .add(AuditEntity.id().eq(id2))
                .getSingleResult();

        assert rev1_related.size() == 0;
        assert rev2_related.size() == 0;
        assert rev3_related.equals(new SetRefIngEmbIdEntity(id2, "y", null));
    }
}
