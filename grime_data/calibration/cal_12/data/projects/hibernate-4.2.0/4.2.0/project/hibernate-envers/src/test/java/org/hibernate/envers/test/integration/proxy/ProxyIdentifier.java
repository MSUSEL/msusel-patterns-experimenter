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
package org.hibernate.envers.test.integration.proxy;

import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.UnversionedStrTestEntity;
import org.hibernate.envers.test.entities.manytoone.unidirectional.TargetNotAuditedEntity;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;


/**
 * @author Eugene Goroschenya
 */
public class ProxyIdentifier extends BaseEnversJPAFunctionalTestCase {
    private TargetNotAuditedEntity tnae1;
    private UnversionedStrTestEntity uste1;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(TargetNotAuditedEntity.class);
        cfg.addAnnotatedClass(UnversionedStrTestEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        uste1 = new UnversionedStrTestEntity("str1");

        // No revision
        em.getTransaction().begin();
        em.persist(uste1);
        em.getTransaction().commit();

        // Revision 1
        em.getTransaction().begin();
        uste1 = em.find(UnversionedStrTestEntity.class, uste1.getId());
        tnae1 = new TargetNotAuditedEntity(1, "tnae1", uste1);
        em.persist(tnae1);
        em.getTransaction().commit();
    }

    @Test
    public void testProxyIdentifier() {
        TargetNotAuditedEntity rev1 = getAuditReader().find(TargetNotAuditedEntity.class, tnae1.getId(), 1);

        assert rev1.getReference() instanceof HibernateProxy;

        HibernateProxy proxyCreateByEnvers = (HibernateProxy) rev1.getReference();
        LazyInitializer lazyInitializer = proxyCreateByEnvers.getHibernateLazyInitializer();

        assert lazyInitializer.isUninitialized();
        assert lazyInitializer.getIdentifier() != null;
        assert lazyInitializer.getIdentifier().equals(tnae1.getId());
        assert lazyInitializer.isUninitialized();

        assert rev1.getReference().getId().equals(uste1.getId());
        assert rev1.getReference().getStr().equals(uste1.getStr());
        assert !lazyInitializer.isUninitialized();
    }
}
