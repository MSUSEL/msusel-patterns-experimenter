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
package org.hibernate.envers.test.integration.onetoone.bidirectional;

import java.util.Arrays;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class Bidirectional extends BaseEnversJPAFunctionalTestCase {
    private Integer ed1_id;
    private Integer ed2_id;

    private Integer ing1_id;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(BiRefEdEntity.class);
        cfg.addAnnotatedClass(BiRefIngEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        BiRefEdEntity ed1 = new BiRefEdEntity(1, "data_ed_1");
        BiRefEdEntity ed2 = new BiRefEdEntity(2, "data_ed_2");

        BiRefIngEntity ing1 = new BiRefIngEntity(3, "data_ing_1");

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

        ing1 = em.find(BiRefIngEntity.class, ing1.getId());
        ed2 = em.find(BiRefEdEntity.class, ed2.getId());

        ing1.setReference(ed2);

        em.getTransaction().commit();

        //

        ed1_id = ed1.getId();
        ed2_id = ed2.getId();

        ing1_id = ing1.getId();
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(BiRefEdEntity.class, ed1_id));
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(BiRefEdEntity.class, ed2_id));

        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(BiRefIngEntity.class, ing1_id));
    }

    @Test
    public void testHistoryOfEdId1() {
        BiRefIngEntity ing1 = getEntityManager().find(BiRefIngEntity.class, ing1_id);

        BiRefEdEntity rev1 = getAuditReader().find(BiRefEdEntity.class, ed1_id, 1);
        BiRefEdEntity rev2 = getAuditReader().find(BiRefEdEntity.class, ed1_id, 2);

        assert rev1.getReferencing().equals(ing1);
        assert rev2.getReferencing() == null;
    }

    @Test
    public void testHistoryOfEdId2() {
        BiRefIngEntity ing1 = getEntityManager().find(BiRefIngEntity.class, ing1_id);

        BiRefEdEntity rev1 = getAuditReader().find(BiRefEdEntity.class, ed2_id, 1);
        BiRefEdEntity rev2 = getAuditReader().find(BiRefEdEntity.class, ed2_id, 2);
        
        assert rev1.getReferencing() == null;
        assert rev2.getReferencing().equals(ing1);
    }
}