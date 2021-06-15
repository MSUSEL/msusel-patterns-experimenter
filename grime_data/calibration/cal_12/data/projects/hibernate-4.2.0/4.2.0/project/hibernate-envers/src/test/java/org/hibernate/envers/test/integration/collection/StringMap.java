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
package org.hibernate.envers.test.integration.collection;

import java.util.Arrays;
import java.util.Collections;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.collection.StringMapEntity;
import org.hibernate.envers.test.tools.TestTools;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class StringMap extends BaseEnversJPAFunctionalTestCase {
    private Integer sme1_id;
    private Integer sme2_id;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(StringMapEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        StringMapEntity sme1 = new StringMapEntity();
        StringMapEntity sme2 = new StringMapEntity();

        // Revision 1 (sme1: initialy empty, sme2: initialy 1 mapping)
        em.getTransaction().begin();

        sme2.getStrings().put("1", "a");

        em.persist(sme1);
        em.persist(sme2);

        em.getTransaction().commit();

        // Revision 2 (sme1: adding 2 mappings, sme2: no changes)
        em.getTransaction().begin();

        sme1 = em.find(StringMapEntity.class, sme1.getId());
        sme2 = em.find(StringMapEntity.class, sme2.getId());

        sme1.getStrings().put("1", "a");
        sme1.getStrings().put("2", "b");

        em.getTransaction().commit();

        // Revision 3 (sme1: removing an existing mapping, sme2: replacing a value)
        em.getTransaction().begin();

        sme1 = em.find(StringMapEntity.class, sme1.getId());
        sme2 = em.find(StringMapEntity.class, sme2.getId());

        sme1.getStrings().remove("1");
        sme2.getStrings().put("1", "b");
        
        em.getTransaction().commit();

        // No revision (sme1: removing a non-existing mapping, sme2: replacing with the same value)
        em.getTransaction().begin();

        sme1 = em.find(StringMapEntity.class, sme1.getId());
        sme2 = em.find(StringMapEntity.class, sme2.getId());

        sme1.getStrings().remove("3");
        sme2.getStrings().put("1", "b");

        em.getTransaction().commit();

        //

        sme1_id = sme1.getId();
        sme2_id = sme2.getId();
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1, 2, 3).equals(getAuditReader().getRevisions(StringMapEntity.class, sme1_id));
        assert Arrays.asList(1, 3).equals(getAuditReader().getRevisions(StringMapEntity.class, sme2_id));
    }

    @Test
    public void testHistoryOfSse1() {
        StringMapEntity rev1 = getAuditReader().find(StringMapEntity.class, sme1_id, 1);
        StringMapEntity rev2 = getAuditReader().find(StringMapEntity.class, sme1_id, 2);
        StringMapEntity rev3 = getAuditReader().find(StringMapEntity.class, sme1_id, 3);
        StringMapEntity rev4 = getAuditReader().find(StringMapEntity.class, sme1_id, 4);

        assert rev1.getStrings().equals(Collections.EMPTY_MAP);
        assert rev2.getStrings().equals(TestTools.makeMap("1", "a", "2", "b"));
        assert rev3.getStrings().equals(TestTools.makeMap("2", "b"));
        assert rev4.getStrings().equals(TestTools.makeMap("2", "b"));
    }

    @Test
    public void testHistoryOfSse2() {
        StringMapEntity rev1 = getAuditReader().find(StringMapEntity.class, sme2_id, 1);
        StringMapEntity rev2 = getAuditReader().find(StringMapEntity.class, sme2_id, 2);
        StringMapEntity rev3 = getAuditReader().find(StringMapEntity.class, sme2_id, 3);
        StringMapEntity rev4 = getAuditReader().find(StringMapEntity.class, sme2_id, 4);

        assert rev1.getStrings().equals(TestTools.makeMap("1", "a"));
        assert rev2.getStrings().equals(TestTools.makeMap("1", "a"));
        assert rev3.getStrings().equals(TestTools.makeMap("1", "b"));
        assert rev4.getStrings().equals(TestTools.makeMap("1", "b"));
    }
}