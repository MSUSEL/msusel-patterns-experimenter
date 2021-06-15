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
public class UnversionedPropertiesChange extends BaseEnversJPAFunctionalTestCase {
    private Integer id1;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(BasicTestEntity2.class);
    }

    private Integer addNewEntity(String str1, String str2) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        BasicTestEntity2 bte2 = new BasicTestEntity2(str1, str2);
        em.persist(bte2);
        em.getTransaction().commit();

        return bte2.getId();
    }

    private void modifyEntity(Integer id, String str1, String str2) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        BasicTestEntity2 bte2 = em.find(BasicTestEntity2.class, id);
        bte2.setStr1(str1);
        bte2.setStr2(str2);
        em.getTransaction().commit();
    }

    @Test
    @Priority(10)
    public void initData() {
        id1 = addNewEntity("x", "a"); // rev 1
        modifyEntity(id1, "x", "a"); // no rev
        modifyEntity(id1, "y", "b"); // rev 2
        modifyEntity(id1, "y", "c"); // no rev
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(BasicTestEntity2.class, id1));
    }

    @Test
    public void testHistoryOfId1() {
        BasicTestEntity2 ver1 = new BasicTestEntity2(id1, "x", null);
        BasicTestEntity2 ver2 = new BasicTestEntity2(id1, "y", null);

        assert getAuditReader().find(BasicTestEntity2.class, id1, 1).equals(ver1);
        assert getAuditReader().find(BasicTestEntity2.class, id1, 2).equals(ver2);
    }
}
