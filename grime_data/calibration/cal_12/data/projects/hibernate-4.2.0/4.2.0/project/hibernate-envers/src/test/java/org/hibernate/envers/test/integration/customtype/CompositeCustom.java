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
package org.hibernate.envers.test.integration.customtype;

import java.util.Arrays;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.customtype.Component;
import org.hibernate.envers.test.entities.customtype.CompositeCustomTypeEntity;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class CompositeCustom extends BaseEnversJPAFunctionalTestCase {
    private Integer ccte_id;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(CompositeCustomTypeEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        CompositeCustomTypeEntity ccte = new CompositeCustomTypeEntity();

        // Revision 1 (persisting 1 entity)
        em.getTransaction().begin();

        ccte.setComponent(new Component("a", 1));

        em.persist(ccte);

        em.getTransaction().commit();

        // Revision 2 (changing the component)
        em.getTransaction().begin();

        ccte = em.find(CompositeCustomTypeEntity.class, ccte.getId());

        ccte.getComponent().setProp1("b");

        em.getTransaction().commit();

        // Revision 3 (replacing the component)
        em.getTransaction().begin();

        ccte = em.find(CompositeCustomTypeEntity.class, ccte.getId());

        ccte.setComponent(new Component("c", 3));

        em.getTransaction().commit();

        //

        ccte_id = ccte.getId();
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1, 2, 3).equals(getAuditReader().getRevisions(CompositeCustomTypeEntity.class, ccte_id));
    }

    @Test
    public void testHistoryOfCcte() {
        CompositeCustomTypeEntity rev1 = getAuditReader().find(CompositeCustomTypeEntity.class, ccte_id, 1);
        CompositeCustomTypeEntity rev2 = getAuditReader().find(CompositeCustomTypeEntity.class, ccte_id, 2);
        CompositeCustomTypeEntity rev3 = getAuditReader().find(CompositeCustomTypeEntity.class, ccte_id, 3);

        assert rev1.getComponent().equals(new Component("a", 1));
        assert rev2.getComponent().equals(new Component("b", 1));
        assert rev3.getComponent().equals(new Component("c", 3));
    }
}