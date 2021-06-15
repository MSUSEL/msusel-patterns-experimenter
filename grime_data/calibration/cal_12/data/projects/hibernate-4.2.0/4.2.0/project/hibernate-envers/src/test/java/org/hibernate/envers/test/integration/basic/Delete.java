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
package org.hibernate.envers.test.integration.basic;

import java.util.Arrays;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase ;
import org.hibernate.envers.test.Priority;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class Delete extends BaseEnversJPAFunctionalTestCase  {
    private Integer id1;
    private Integer id2;
    private Integer id3;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(BasicTestEntity2.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        // Revision 1
        EntityManager em = getEntityManager();
        em.getTransaction().begin();

        BasicTestEntity2 bte1 = new BasicTestEntity2("x", "a");
        BasicTestEntity2 bte2 = new BasicTestEntity2("y", "b");
        BasicTestEntity2 bte3 = new BasicTestEntity2("z", "c");
        em.persist(bte1);
        em.persist(bte2);
        em.persist(bte3);

        em.getTransaction().commit();

        // Revision 2
        em = getEntityManager();
        em.getTransaction().begin();

        bte1 = em.find(BasicTestEntity2.class, bte1.getId());
        bte2 = em.find(BasicTestEntity2.class, bte2.getId());
        bte3 = em.find(BasicTestEntity2.class, bte3.getId());
        bte1.setStr1("x2");
        bte2.setStr2("b2");
        em.remove(bte3);

        em.getTransaction().commit();

        // Revision 3
        em = getEntityManager();
        em.getTransaction().begin();

        bte2 = em.find(BasicTestEntity2.class, bte2.getId());
        em.remove(bte2);

        em.getTransaction().commit();

        // Revision 4
        em = getEntityManager();
        em.getTransaction().begin();

        bte1 = em.find(BasicTestEntity2.class, bte1.getId());
        em.remove(bte1);

        em.getTransaction().commit();

        id1 = bte1.getId();
        id2 = bte2.getId();
        id3 = bte3.getId();
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1, 2, 4).equals(getAuditReader().getRevisions(BasicTestEntity2.class, id1));

        assert Arrays.asList(1, 3).equals(getAuditReader().getRevisions(BasicTestEntity2.class, id2));

        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(BasicTestEntity2.class, id3));
    }

    @Test
    public void testHistoryOfId1() {
        BasicTestEntity2 ver1 = new BasicTestEntity2(id1, "x", null);
        BasicTestEntity2 ver2 = new BasicTestEntity2(id1, "x2", null);

        assert getAuditReader().find(BasicTestEntity2.class, id1, 1).equals(ver1);
        assert getAuditReader().find(BasicTestEntity2.class, id1, 2).equals(ver2);
        assert getAuditReader().find(BasicTestEntity2.class, id1, 3).equals(ver2);
        assert getAuditReader().find(BasicTestEntity2.class, id1, 4) == null;
    }

    @Test
    public void testHistoryOfId2() {
        BasicTestEntity2 ver1 = new BasicTestEntity2(id2, "y", null);

        assert getAuditReader().find(BasicTestEntity2.class, id2, 1).equals(ver1);
        assert getAuditReader().find(BasicTestEntity2.class, id2, 2).equals(ver1);
        assert getAuditReader().find(BasicTestEntity2.class, id2, 3) == null;
        assert getAuditReader().find(BasicTestEntity2.class, id2, 4) == null;
    }

    @Test
    public void testHistoryOfId3() {
        BasicTestEntity2 ver1 = new BasicTestEntity2(id3, "z", null);

        assert getAuditReader().find(BasicTestEntity2.class, id3, 1).equals(ver1);
        assert getAuditReader().find(BasicTestEntity2.class, id3, 2) == null;
        assert getAuditReader().find(BasicTestEntity2.class, id3, 3) == null;
        assert getAuditReader().find(BasicTestEntity2.class, id3, 4) == null;
    }
}
