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
package org.hibernate.envers.test.integration.naming;

import java.util.Arrays;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class BasicNaming extends BaseEnversJPAFunctionalTestCase {
    private Integer id1;
    private Integer id2;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(NamingTestEntity1.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        NamingTestEntity1 nte1 = new NamingTestEntity1("data1");
        NamingTestEntity1 nte2 = new NamingTestEntity1("data2");

        // Revision 1
        EntityManager em = getEntityManager();
        em.getTransaction().begin();

        em.persist(nte1);
        em.persist(nte2);

        em.getTransaction().commit();

        // Revision 2
        em.getTransaction().begin();

        nte1 = em.find(NamingTestEntity1.class, nte1.getId());
        nte1.setData("data1'");

        em.getTransaction().commit();

        // Revision 3
        em.getTransaction().begin();

        nte2 = em.find(NamingTestEntity1.class, nte2.getId());
        nte2.setData("data2'");

        em.getTransaction().commit();

        //

        id1 = nte1.getId();
        id2 = nte2.getId();
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(NamingTestEntity1.class, id1));

        assert Arrays.asList(1, 3).equals(getAuditReader().getRevisions(NamingTestEntity1.class, id2));
    }

    @Test
    public void testHistoryOfId1() {
        NamingTestEntity1 ver1 = new NamingTestEntity1(id1, "data1");
        NamingTestEntity1 ver2 = new NamingTestEntity1(id1, "data1'");

        assert getAuditReader().find(NamingTestEntity1.class, id1, 1).equals(ver1);
        assert getAuditReader().find(NamingTestEntity1.class, id1, 2).equals(ver2);
        assert getAuditReader().find(NamingTestEntity1.class, id1, 3).equals(ver2);
    }

    @Test
    public void testHistoryOfId2() {
        NamingTestEntity1 ver1 = new NamingTestEntity1(id2, "data2");
        NamingTestEntity1 ver2 = new NamingTestEntity1(id2, "data2'");

        assert getAuditReader().find(NamingTestEntity1.class, id2, 1).equals(ver1);
        assert getAuditReader().find(NamingTestEntity1.class, id2, 2).equals(ver1);
        assert getAuditReader().find(NamingTestEntity1.class, id2, 3).equals(ver2);
    }

    @Test
    public void testTableName() {
        assert "naming_test_entity_1_versions".equals(
                getCfg().getClassMapping("org.hibernate.envers.test.integration.naming.NamingTestEntity1_AUD")
                        .getTable().getName());
    }
}
