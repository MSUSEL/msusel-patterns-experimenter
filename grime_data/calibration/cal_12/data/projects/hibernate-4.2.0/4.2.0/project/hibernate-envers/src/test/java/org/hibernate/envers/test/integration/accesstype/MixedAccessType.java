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
package org.hibernate.envers.test.integration.accesstype;

import java.util.Arrays;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class MixedAccessType extends BaseEnversJPAFunctionalTestCase {
    private Integer id1;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(MixedAccessTypeEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        MixedAccessTypeEntity mate = new MixedAccessTypeEntity("data");
        em.persist(mate);
        id1 = mate.readId();
        em.getTransaction().commit();

        em.getTransaction().begin();
        mate = em.find(MixedAccessTypeEntity.class, id1);
        mate.writeData("data2");
        em.getTransaction().commit();
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(MixedAccessTypeEntity.class, id1));
    }

    @Test
    public void testHistoryOfId1() {
        MixedAccessTypeEntity ver1 = new MixedAccessTypeEntity(id1, "data");
        MixedAccessTypeEntity ver2 = new MixedAccessTypeEntity(id1, "data2");

        MixedAccessTypeEntity rev1 = getAuditReader().find(MixedAccessTypeEntity.class, id1, 1);
        MixedAccessTypeEntity rev2 = getAuditReader().find(MixedAccessTypeEntity.class, id1, 2);

        assert rev1.isDataSet();
        assert rev2.isDataSet();

        assert rev1.equals(ver1);
        assert rev2.equals(ver2);
    }
}