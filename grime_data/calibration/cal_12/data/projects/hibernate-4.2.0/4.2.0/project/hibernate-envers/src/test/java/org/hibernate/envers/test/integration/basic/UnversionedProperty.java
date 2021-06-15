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
import org.hibernate.envers.test.entities.UnversionedEntity;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class UnversionedProperty extends BaseEnversJPAFunctionalTestCase  {
    private Integer id1;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(UnversionedEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        // Rev 1
        em.getTransaction().begin();
        UnversionedEntity ue1 = new UnversionedEntity("a1", "b1");
        em.persist(ue1);
        id1 = ue1.getId();
        em.getTransaction().commit();

        // Rev 2
        em.getTransaction().begin();
        ue1 = em.find(UnversionedEntity.class, id1);
        ue1.setData1("a2");
        ue1.setData2("b2");
        em.getTransaction().commit();
    }

     @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(UnversionedEntity.class, id1));
    }

    @Test
    public void testHistoryOfId1() {
        UnversionedEntity rev1 = new UnversionedEntity(id1, "a1", null);
        UnversionedEntity rev2 = new UnversionedEntity(id1, "a2", null);

        assert getAuditReader().find(UnversionedEntity.class, id1, 1).equals(rev1);
        assert getAuditReader().find(UnversionedEntity.class, id1, 2).equals(rev2);
    }
}
