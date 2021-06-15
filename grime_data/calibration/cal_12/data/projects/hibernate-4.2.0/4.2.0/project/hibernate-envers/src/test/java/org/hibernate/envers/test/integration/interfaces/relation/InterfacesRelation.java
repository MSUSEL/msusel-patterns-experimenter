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
package org.hibernate.envers.test.integration.interfaces.relation;

import java.util.Arrays;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class InterfacesRelation extends BaseEnversJPAFunctionalTestCase {
    private Integer ed1_id;
    private Integer ed2_id;

    private Integer ing1_id;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(SetRefEdEntity.class);
        cfg.addAnnotatedClass(SetRefIngEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        SetRefEdEntity ed1 = new SetRefEdEntity(1, "data_ed_1");
        SetRefEdEntity ed2 = new SetRefEdEntity(2, "data_ed_2");

        SetRefIngEntity ing1 = new SetRefIngEntity(3, "data_ing_1");

        // Revision 1
        em.getTransaction().begin();

        em.persist(ed1);
        em.persist(ed2);

        em.getTransaction().commit();

        // Revision 2

        em.getTransaction().begin();

        ed1 = em.find(SetRefEdEntity.class, ed1.getId());

        ing1.setReference(ed1);
        em.persist(ing1);

        em.getTransaction().commit();

        // Revision 3
        em.getTransaction().begin();

        ing1 = em.find(SetRefIngEntity.class, ing1.getId());
        ed2 = em.find(SetRefEdEntity.class, ed2.getId());

        ing1.setReference(ed2);

        em.getTransaction().commit();

        //

        ed1_id = ed1.getId();
        ed2_id = ed2.getId();

        ing1_id = ing1.getId();
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1).equals(getAuditReader().getRevisions(SetRefEdEntity.class, ed1_id));
        assert Arrays.asList(1).equals(getAuditReader().getRevisions(SetRefEdEntity.class, ed2_id));

        assert Arrays.asList(2, 3).equals(getAuditReader().getRevisions(SetRefIngEntity.class, ing1_id));
    }

    @Test
    public void testHistoryOfEdIng1() {
        SetRefEdEntity ed1 = getEntityManager().find(SetRefEdEntity.class, ed1_id);
        SetRefEdEntity ed2 = getEntityManager().find(SetRefEdEntity.class, ed2_id);

        SetRefIngEntity rev1 = getAuditReader().find(SetRefIngEntity.class, ing1_id, 1);
        SetRefIngEntity rev2 = getAuditReader().find(SetRefIngEntity.class, ing1_id, 2);
        SetRefIngEntity rev3 = getAuditReader().find(SetRefIngEntity.class, ing1_id, 3);

        assert rev1 == null;
        assert rev2.getReference().equals(ed1);
        assert rev3.getReference().equals(ed2);
    }
}