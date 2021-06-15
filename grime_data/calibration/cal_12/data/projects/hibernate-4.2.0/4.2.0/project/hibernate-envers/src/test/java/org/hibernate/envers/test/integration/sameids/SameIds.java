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
package org.hibernate.envers.test.integration.sameids;

import java.util.Arrays;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;

/**
 * A test which checks that if we add two different entities with the same ids in one revision, they
 * will both be stored.
 * @author Adam Warski (adam at warski dot org)
 */
public class SameIds extends BaseEnversJPAFunctionalTestCase {
    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(SameIdTestEntity1.class);
        cfg.addAnnotatedClass(SameIdTestEntity2.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        SameIdTestEntity1 site1 = new SameIdTestEntity1(1, "str1");
        SameIdTestEntity2 site2 = new SameIdTestEntity2(1, "str1");

        em.persist(site1);
        em.persist(site2);
        em.getTransaction().commit();

        em.getTransaction().begin();
        site1 = em.find(SameIdTestEntity1.class, 1);
        site2 = em.find(SameIdTestEntity2.class, 1);
        site1.setStr1("str2");
        site2.setStr1("str2");
        em.getTransaction().commit();
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(SameIdTestEntity1.class, 1));
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(SameIdTestEntity2.class, 1));
    }

    @Test
    public void testHistoryOfSite1() {
        SameIdTestEntity1 ver1 = new SameIdTestEntity1(1, "str1");
        SameIdTestEntity1 ver2 = new SameIdTestEntity1(1, "str2");

        assert getAuditReader().find(SameIdTestEntity1.class, 1, 1).equals(ver1);
        assert getAuditReader().find(SameIdTestEntity1.class, 1, 2).equals(ver2);
    }

    @Test
    public void testHistoryOfSite2() {
        SameIdTestEntity2 ver1 = new SameIdTestEntity2(1, "str1");
        SameIdTestEntity2 ver2 = new SameIdTestEntity2(1, "str2");

        assert getAuditReader().find(SameIdTestEntity2.class, 1, 1).equals(ver1);
        assert getAuditReader().find(SameIdTestEntity2.class, 1, 2).equals(ver2);
    }
}
