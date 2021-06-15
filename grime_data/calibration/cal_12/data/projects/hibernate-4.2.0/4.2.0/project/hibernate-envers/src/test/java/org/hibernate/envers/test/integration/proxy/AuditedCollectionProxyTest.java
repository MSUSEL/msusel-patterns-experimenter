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
import org.hibernate.envers.test.entities.onetomany.ListRefEdEntity;
import org.hibernate.envers.test.entities.onetomany.ListRefIngEntity;
import org.hibernate.proxy.HibernateProxy;

/**
 * Test case for HHH-5750: Proxied objects lose the temporary session used to
 * initialize them.
 * 
 * @author Erik-Berndt Scheper
 * 
 */
public class AuditedCollectionProxyTest extends BaseEnversJPAFunctionalTestCase {

    Integer id_ListRefEdEntity1;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(ListRefEdEntity.class);
        cfg.addAnnotatedClass(ListRefIngEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        ListRefEdEntity listReferencedEntity1 = new ListRefEdEntity(
                Integer.valueOf(1), "str1");
        ListRefIngEntity refingEntity1 = new ListRefIngEntity(
                Integer.valueOf(1), "refing1", listReferencedEntity1);

        // Revision 1
        em.getTransaction().begin();
        em.persist(listReferencedEntity1);
        em.persist(refingEntity1);
        em.getTransaction().commit();

        id_ListRefEdEntity1 = listReferencedEntity1.getId();

        // Revision 2
        ListRefIngEntity refingEntity2 = new ListRefIngEntity(
                Integer.valueOf(2), "refing2", listReferencedEntity1);

        em.getTransaction().begin();
        em.persist(refingEntity2);
        em.getTransaction().commit();
    }

    @Test
    public void testProxyIdentifier() {
        EntityManager em = getEntityManager();

        ListRefEdEntity listReferencedEntity1 = em.getReference(
                ListRefEdEntity.class, id_ListRefEdEntity1);

        assert listReferencedEntity1 instanceof HibernateProxy;

        // Revision 3
        ListRefIngEntity refingEntity3 = new ListRefIngEntity(
                Integer.valueOf(3), "refing3", listReferencedEntity1);

        em.getTransaction().begin();
        em.persist(refingEntity3);
        em.getTransaction().commit();

        listReferencedEntity1.getReffering().size();

    }

}
