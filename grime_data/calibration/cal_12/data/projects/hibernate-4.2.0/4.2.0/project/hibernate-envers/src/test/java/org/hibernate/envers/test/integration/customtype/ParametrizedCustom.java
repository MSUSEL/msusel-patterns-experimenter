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
import org.hibernate.envers.test.entities.customtype.ParametrizedCustomTypeEntity;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class ParametrizedCustom extends BaseEnversJPAFunctionalTestCase {
    private Integer pcte_id;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(ParametrizedCustomTypeEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        ParametrizedCustomTypeEntity pcte = new ParametrizedCustomTypeEntity();

        // Revision 1 (persisting 1 entity)
        em.getTransaction().begin();

        pcte.setStr("U");

        em.persist(pcte);

        em.getTransaction().commit();

        // Revision 2 (changing the value)
        em.getTransaction().begin();

        pcte = em.find(ParametrizedCustomTypeEntity.class, pcte.getId());

        pcte.setStr("V");

        em.getTransaction().commit();

        //

        pcte_id = pcte.getId();
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(ParametrizedCustomTypeEntity.class, pcte_id));
    }

    @Test
    public void testHistoryOfCcte() {
        ParametrizedCustomTypeEntity rev1 = getAuditReader().find(ParametrizedCustomTypeEntity.class, pcte_id, 1);
        ParametrizedCustomTypeEntity rev2 = getAuditReader().find(ParametrizedCustomTypeEntity.class, pcte_id, 2);

        assert "xUy".equals(rev1.getStr());
        assert "xVy".equals(rev2.getStr());
    }
}