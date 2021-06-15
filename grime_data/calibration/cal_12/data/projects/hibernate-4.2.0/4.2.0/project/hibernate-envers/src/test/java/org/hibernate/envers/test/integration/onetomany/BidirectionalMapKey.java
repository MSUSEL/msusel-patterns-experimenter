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
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.tools.TestTools;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class BidirectionalMapKey extends BaseEnversJPAFunctionalTestCase {
    private Integer ed_id;

    private Integer ing1_id;
    private Integer ing2_id;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(RefIngMapKeyEntity.class);
        cfg.addAnnotatedClass(RefEdMapKeyEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        // Revision 1 (intialy 1 relation: ing1 -> ed)
        em.getTransaction().begin();

        RefEdMapKeyEntity ed = new RefEdMapKeyEntity();

        em.persist(ed);

        RefIngMapKeyEntity ing1 = new RefIngMapKeyEntity();
        ing1.setData("a");
        ing1.setReference(ed);

        RefIngMapKeyEntity ing2 = new RefIngMapKeyEntity();
        ing2.setData("b");

        em.persist(ing1);
        em.persist(ing2);

        em.getTransaction().commit();

        // Revision 2 (adding second relation: ing2 -> ed)
        em.getTransaction().begin();

        ed = em.find(RefEdMapKeyEntity.class, ed.getId());
        ing2 = em.find(RefIngMapKeyEntity.class, ing2.getId());

        ing2.setReference(ed);

        em.getTransaction().commit();

        //

        ed_id = ed.getId();

        ing1_id = ing1.getId();
        ing2_id = ing2.getId();
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(RefEdMapKeyEntity.class, ed_id));

        assert Arrays.asList(1).equals(getAuditReader().getRevisions(RefIngMapKeyEntity.class, ing1_id));
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(RefIngMapKeyEntity.class, ing2_id));
    }

    @Test
    public void testHistoryOfEd() {
        RefIngMapKeyEntity ing1 = getEntityManager().find(RefIngMapKeyEntity.class, ing1_id);
        RefIngMapKeyEntity ing2 = getEntityManager().find(RefIngMapKeyEntity.class, ing2_id);

        RefEdMapKeyEntity rev1 = getAuditReader().find(RefEdMapKeyEntity.class, ed_id, 1);
        RefEdMapKeyEntity rev2 = getAuditReader().find(RefEdMapKeyEntity.class, ed_id, 2);

        assert rev1.getIdmap().equals(TestTools.makeMap("a", ing1));
        assert rev2.getIdmap().equals(TestTools.makeMap("a", ing1, "b", ing2));
    }
}