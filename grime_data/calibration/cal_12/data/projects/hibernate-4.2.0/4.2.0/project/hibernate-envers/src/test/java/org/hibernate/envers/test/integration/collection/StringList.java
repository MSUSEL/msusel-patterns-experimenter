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
import org.hibernate.envers.test.entities.collection.StringListEntity;
import org.hibernate.envers.test.tools.TestTools;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class StringList extends BaseEnversJPAFunctionalTestCase {
    private Integer sle1_id;
    private Integer sle2_id;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(StringListEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        StringListEntity sle1 = new StringListEntity();
        StringListEntity sle2 = new StringListEntity();

        // Revision 1 (sle1: initialy empty, sle2: initialy 2 elements)
        em.getTransaction().begin();

        sle2.getStrings().add("sle2_string1");
        sle2.getStrings().add("sle2_string2");

        em.persist(sle1);
        em.persist(sle2);

        em.getTransaction().commit();

        // Revision 2 (sle1: adding 2 elements, sle2: adding an existing element)
        em.getTransaction().begin();

        sle1 = em.find(StringListEntity.class, sle1.getId());
        sle2 = em.find(StringListEntity.class, sle2.getId());

        sle1.getStrings().add("sle1_string1");
        sle1.getStrings().add("sle1_string2");

        sle2.getStrings().add("sle2_string1");

        em.getTransaction().commit();

        // Revision 3 (sle1: replacing an element at index 0, sle2: removing an element at index 0)
        em.getTransaction().begin();

        sle1 = em.find(StringListEntity.class, sle1.getId());
        sle2 = em.find(StringListEntity.class, sle2.getId());

        sle1.getStrings().set(0, "sle1_string3");

        sle2.getStrings().remove(0);

        em.getTransaction().commit();

        //

        sle1_id = sle1.getId();
        sle2_id = sle2.getId();
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1, 2, 3).equals(getAuditReader().getRevisions(StringListEntity.class, sle1_id));
        assert Arrays.asList(1, 2, 3).equals(getAuditReader().getRevisions(StringListEntity.class, sle2_id));
    }

    @Test
    public void testHistoryOfSle1() {
        StringListEntity rev1 = getAuditReader().find(StringListEntity.class, sle1_id, 1);
        StringListEntity rev2 = getAuditReader().find(StringListEntity.class, sle1_id, 2);
        StringListEntity rev3 = getAuditReader().find(StringListEntity.class, sle1_id, 3);

        assert rev1.getStrings().equals(Collections.EMPTY_LIST);
        assert rev2.getStrings().equals(TestTools.makeList("sle1_string1", "sle1_string2"));
        assert rev3.getStrings().equals(TestTools.makeList("sle1_string3", "sle1_string2"));
    }

    @Test
    public void testHistoryOfSse2() {
        StringListEntity rev1 = getAuditReader().find(StringListEntity.class, sle2_id, 1);
        StringListEntity rev2 = getAuditReader().find(StringListEntity.class, sle2_id, 2);
        StringListEntity rev3 = getAuditReader().find(StringListEntity.class, sle2_id, 3);

        assert rev1.getStrings().equals(TestTools.makeList("sle2_string1", "sle2_string2"));
        assert rev2.getStrings().equals(TestTools.makeList("sle2_string1", "sle2_string2", "sle2_string1"));
        assert rev3.getStrings().equals(TestTools.makeList("sle2_string2", "sle2_string1"));
    }
}