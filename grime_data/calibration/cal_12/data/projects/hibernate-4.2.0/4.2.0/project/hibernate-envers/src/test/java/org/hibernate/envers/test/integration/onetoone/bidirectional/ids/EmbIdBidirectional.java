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
package org.hibernate.envers.test.integration.onetoone.bidirectional.ids;

import java.util.Arrays;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.ids.EmbId;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class EmbIdBidirectional extends BaseEnversJPAFunctionalTestCase {
    private EmbId ed1_id;
    private EmbId ed2_id;

    private EmbId ing1_id;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(BiEmbIdRefEdEntity.class);
        cfg.addAnnotatedClass(BiEmbIdRefIngEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        ed1_id = new EmbId(1, 2);
        ed2_id = new EmbId(3, 4);

        ing1_id = new EmbId(5, 6);

        BiEmbIdRefEdEntity ed1 = new BiEmbIdRefEdEntity(ed1_id, "data_ed_1");
        BiEmbIdRefEdEntity ed2 = new BiEmbIdRefEdEntity(ed2_id, "data_ed_2");

        BiEmbIdRefIngEntity ing1 = new BiEmbIdRefIngEntity(ing1_id, "data_ing_1");

        // Revision 1
        EntityManager em = getEntityManager();
        em.getTransaction().begin();

        ing1.setReference(ed1);

        em.persist(ed1);
        em.persist(ed2);

        em.persist(ing1);

        em.getTransaction().commit();

        // Revision 2
        em.getTransaction().begin();

        ing1 = em.find(BiEmbIdRefIngEntity.class, ing1.getId());
        ed2 = em.find(BiEmbIdRefEdEntity.class, ed2.getId());

        ing1.setReference(ed2);

        em.getTransaction().commit();
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(BiEmbIdRefEdEntity.class, ed1_id));
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(BiEmbIdRefEdEntity.class, ed2_id));

        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(BiEmbIdRefIngEntity.class, ing1_id));
    }

    @Test
    public void testHistoryOfEdId1() {
        BiEmbIdRefIngEntity ing1 = getEntityManager().find(BiEmbIdRefIngEntity.class, ing1_id);

        BiEmbIdRefEdEntity rev1 = getAuditReader().find(BiEmbIdRefEdEntity.class, ed1_id, 1);
        BiEmbIdRefEdEntity rev2 = getAuditReader().find(BiEmbIdRefEdEntity.class, ed1_id, 2);

        assert rev1.getReferencing().equals(ing1);
        assert rev2.getReferencing() == null;
    }

    @Test
    public void testHistoryOfEdId2() {
        BiEmbIdRefIngEntity ing1 = getEntityManager().find(BiEmbIdRefIngEntity.class, ing1_id);

        BiEmbIdRefEdEntity rev1 = getAuditReader().find(BiEmbIdRefEdEntity.class, ed2_id, 1);
        BiEmbIdRefEdEntity rev2 = getAuditReader().find(BiEmbIdRefEdEntity.class, ed2_id, 2);

        assert rev1.getReferencing() == null;
        assert rev2.getReferencing().equals(ing1);
    }

    @Test
    public void testHistoryOfIngId1() {
        BiEmbIdRefEdEntity ed1 = getEntityManager().find(BiEmbIdRefEdEntity.class, ed1_id);
        BiEmbIdRefEdEntity ed2 = getEntityManager().find(BiEmbIdRefEdEntity.class, ed2_id);

        BiEmbIdRefIngEntity rev1 = getAuditReader().find(BiEmbIdRefIngEntity.class, ing1_id, 1);
        BiEmbIdRefIngEntity rev2 = getAuditReader().find(BiEmbIdRefIngEntity.class, ing1_id, 2);

        assert rev1.getReference().equals(ed1);
        assert rev2.getReference().equals(ed2);
    }
}