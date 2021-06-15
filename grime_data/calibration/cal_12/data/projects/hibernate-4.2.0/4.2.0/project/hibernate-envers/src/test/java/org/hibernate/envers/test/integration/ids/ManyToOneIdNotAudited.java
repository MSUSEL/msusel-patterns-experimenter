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

import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.StrTestEntity;
import org.hibernate.envers.test.entities.UnversionedStrTestEntity;
import org.hibernate.envers.test.entities.ids.ManyToOneIdNotAuditedTestEntity;
import org.hibernate.envers.test.entities.ids.ManyToOneNotAuditedEmbId;

/**
 * A test checking that when using Envers it is possible to have non-audited entities that use unsupported
 * components in their ids, e.g. a many-to-one join to another entity.
 * @author Adam Warski (adam at warski dot org)
 */
public class ManyToOneIdNotAudited extends BaseEnversJPAFunctionalTestCase {
    private ManyToOneNotAuditedEmbId id1;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(ManyToOneIdNotAuditedTestEntity.class);
        cfg.addAnnotatedClass(UnversionedStrTestEntity.class);
        cfg.addAnnotatedClass(StrTestEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        // Revision 1
        EntityManager em = getEntityManager();
        em.getTransaction().begin();

        UnversionedStrTestEntity uste = new UnversionedStrTestEntity();
        uste.setStr("test1");
        em.persist(uste);

        id1 = new ManyToOneNotAuditedEmbId(uste);

        em.getTransaction().commit();

        // Revision 2
        em = getEntityManager();
        em.getTransaction().begin();

        ManyToOneIdNotAuditedTestEntity mtoinate = new ManyToOneIdNotAuditedTestEntity();
        mtoinate.setData("data1");
        mtoinate.setId(id1);
        em.persist(mtoinate);

        em.getTransaction().commit();
    }
}
