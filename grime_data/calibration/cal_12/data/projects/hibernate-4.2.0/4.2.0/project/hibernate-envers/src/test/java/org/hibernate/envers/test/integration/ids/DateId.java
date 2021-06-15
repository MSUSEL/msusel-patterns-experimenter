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
package org.hibernate.envers.test.integration.ids;

import java.util.Arrays;
import java.util.Date;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.ids.DateIdTestEntity;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class DateId extends BaseEnversJPAFunctionalTestCase {
    private Date id1;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(DateIdTestEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {

        // Revision 1
        EntityManager em = getEntityManager();
        em.getTransaction().begin();

        DateIdTestEntity dite = new DateIdTestEntity(new Date(), "x");
        em.persist(dite);

        id1 = dite.getId();

        em.getTransaction().commit();

        // Revision 2
        em = getEntityManager();
        em.getTransaction().begin();

        dite = em.find(DateIdTestEntity.class, id1);
        dite.setStr1("y");

        em.getTransaction().commit();
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(DateIdTestEntity.class, id1));
    }

    @Test
    public void testHistoryOfId1() {
        DateIdTestEntity ver1 = new DateIdTestEntity(id1, "x");
        DateIdTestEntity ver2 = new DateIdTestEntity(id1, "y");

        assert getAuditReader().find(DateIdTestEntity.class, id1, 1).getStr1().equals("x");
        assert getAuditReader().find(DateIdTestEntity.class, id1, 2).getStr1().equals("y");
    }
}