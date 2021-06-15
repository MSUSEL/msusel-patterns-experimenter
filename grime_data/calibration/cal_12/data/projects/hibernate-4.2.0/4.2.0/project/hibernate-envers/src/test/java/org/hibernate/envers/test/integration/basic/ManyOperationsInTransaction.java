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
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class ManyOperationsInTransaction extends BaseEnversJPAFunctionalTestCase {
    private Integer id1;
    private Integer id2;
    private Integer id3;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(BasicTestEntity1.class);
    }

    @Test

    @Priority(10)
    public void initData() {
        // Revision 1
        EntityManager em = getEntityManager();
        em.getTransaction().begin();

        BasicTestEntity1 bte1 = new BasicTestEntity1("x", 1);
        BasicTestEntity1 bte2 = new BasicTestEntity1("y", 20);
        em.persist(bte1);
        em.persist(bte2);

        em.getTransaction().commit();

        // Revision 2
        em.getTransaction().begin();

        bte1 = em.find(BasicTestEntity1.class, bte1.getId());
        bte2 = em.find(BasicTestEntity1.class, bte2.getId());
        BasicTestEntity1 bte3 = new BasicTestEntity1("z", 300);
        bte1.setStr1("x2");
        bte2.setLong1(21);
        em.persist(bte3);

        em.getTransaction().commit();

        // Revision 3
        em.getTransaction().begin();

        bte2 = em.find(BasicTestEntity1.class, bte2.getId());
        bte3 = em.find(BasicTestEntity1.class, bte3.getId());
        bte2.setStr1("y3");
        bte2.setLong1(22);
        bte3.setStr1("z3");

        em.getTransaction().commit();

        id1 = bte1.getId();
        id2 = bte2.getId();
        id3 = bte3.getId();
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(BasicTestEntity1.class, id1));

        assert Arrays.asList(1, 2, 3).equals(getAuditReader().getRevisions(BasicTestEntity1.class, id2));

        assert Arrays.asList(2, 3).equals(getAuditReader().getRevisions(BasicTestEntity1.class, id3));
    }

    @Test
    public void testHistoryOfId1() {
        BasicTestEntity1 ver1 = new BasicTestEntity1(id1, "x", 1);
        BasicTestEntity1 ver2 = new BasicTestEntity1(id1, "x2", 1);

        assert getAuditReader().find(BasicTestEntity1.class, id1, 1).equals(ver1);
        assert getAuditReader().find(BasicTestEntity1.class, id1, 2).equals(ver2);
        assert getAuditReader().find(BasicTestEntity1.class, id1, 3).equals(ver2);
    }

    @Test
    public void testHistoryOfId2() {
        BasicTestEntity1 ver1 = new BasicTestEntity1(id2, "y", 20);
        BasicTestEntity1 ver2 = new BasicTestEntity1(id2, "y", 21);
        BasicTestEntity1 ver3 = new BasicTestEntity1(id2, "y3", 22);

        assert getAuditReader().find(BasicTestEntity1.class, id2, 1).equals(ver1);
        assert getAuditReader().find(BasicTestEntity1.class, id2, 2).equals(ver2);
        assert getAuditReader().find(BasicTestEntity1.class, id2, 3).equals(ver3);
    }

    @Test
    public void testHistoryOfId3() {
        BasicTestEntity1 ver1 = new BasicTestEntity1(id3, "z", 300);
        BasicTestEntity1 ver2 = new BasicTestEntity1(id3, "z3", 300);

        assert getAuditReader().find(BasicTestEntity1.class, id3, 1) == null;
        assert getAuditReader().find(BasicTestEntity1.class, id3, 2).equals(ver1);
        assert getAuditReader().find(BasicTestEntity1.class, id3, 3).equals(ver2);
    }
}
