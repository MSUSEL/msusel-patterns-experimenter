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
package org.hibernate.envers.test.integration.collection.mapkey;

import java.util.Arrays;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.StrTestEntity;
import org.hibernate.envers.test.tools.TestTools;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class IdMapKey extends BaseEnversJPAFunctionalTestCase {
    private Integer imke_id;

    private Integer ste1_id;
    private Integer ste2_id;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(IdMapKeyEntity.class);
        cfg.addAnnotatedClass(StrTestEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        IdMapKeyEntity imke = new IdMapKeyEntity();

        // Revision 1 (intialy 1 mapping)
        em.getTransaction().begin();

        StrTestEntity ste1 = new StrTestEntity("x");
        StrTestEntity ste2 = new StrTestEntity("y");

        em.persist(ste1);
        em.persist(ste2);

        imke.getIdmap().put(ste1.getId(), ste1);

        em.persist(imke);

        em.getTransaction().commit();

        // Revision 2 (sse1: adding 1 mapping)
        em.getTransaction().begin();

        ste2 = em.find(StrTestEntity.class, ste2.getId());
        imke = em.find(IdMapKeyEntity.class, imke.getId());

        imke.getIdmap().put(ste2.getId(), ste2);

        em.getTransaction().commit();

        //

        imke_id = imke.getId();

        ste1_id = ste1.getId();
        ste2_id = ste2.getId();
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(IdMapKeyEntity.class, imke_id));
    }

    @Test
    public void testHistoryOfImke() {
        StrTestEntity ste1 = getEntityManager().find(StrTestEntity.class, ste1_id);
        StrTestEntity ste2 = getEntityManager().find(StrTestEntity.class, ste2_id);

        IdMapKeyEntity rev1 = getAuditReader().find(IdMapKeyEntity.class, imke_id, 1);
        IdMapKeyEntity rev2 = getAuditReader().find(IdMapKeyEntity.class, imke_id, 2);

        assert rev1.getIdmap().equals(TestTools.makeMap(ste1.getId(), ste1));
        assert rev2.getIdmap().equals(TestTools.makeMap(ste1.getId(), ste1, ste2.getId(), ste2));
    }
}