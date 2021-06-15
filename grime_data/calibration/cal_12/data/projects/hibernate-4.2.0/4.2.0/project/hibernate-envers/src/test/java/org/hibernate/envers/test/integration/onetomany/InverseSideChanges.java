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
package org.hibernate.envers.test.integration.onetomany;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.onetomany.SetRefEdEntity;
import org.hibernate.envers.test.entities.onetomany.SetRefIngEntity;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class InverseSideChanges extends BaseEnversJPAFunctionalTestCase {
    private Integer ed1_id;

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

        SetRefIngEntity ing1 = new SetRefIngEntity(3, "data_ing_1");

        // Revision 1
        em.getTransaction().begin();

        em.persist(ed1);

        em.getTransaction().commit();

        // Revision 2

        em.getTransaction().begin();

        ed1 = em.find(SetRefEdEntity.class, ed1.getId());
        
        em.persist(ing1);

        ed1.setReffering(new HashSet<SetRefIngEntity>());
        ed1.getReffering().add(ing1);

        em.getTransaction().commit();

        //

        ed1_id = ed1.getId();

        ing1_id = ing1.getId();
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1).equals(getAuditReader().getRevisions(SetRefEdEntity.class, ed1_id));

        assert Arrays.asList(2).equals(getAuditReader().getRevisions(SetRefIngEntity.class, ing1_id));
    }

    @Test
    public void testHistoryOfEdId1() {
        SetRefEdEntity rev1 = getAuditReader().find(SetRefEdEntity.class, ed1_id, 1);

        assert rev1.getReffering().equals(Collections.EMPTY_SET);
    }

    @Test
    public void testHistoryOfEdIng1() {
        SetRefIngEntity rev2 = getAuditReader().find(SetRefIngEntity.class, ing1_id, 2);

        assert rev2.getReference() == null;
    }
}