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
import org.hibernate.envers.test.entities.collection.StringSetEntity;
import org.hibernate.envers.test.tools.TestTools;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class StringSet extends BaseEnversJPAFunctionalTestCase {
    private Integer sse1_id;
    private Integer sse2_id;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(StringSetEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        StringSetEntity sse1 = new StringSetEntity();
        StringSetEntity sse2 = new StringSetEntity();

        // Revision 1 (sse1: initialy empty, sse2: initialy 2 elements)
        em.getTransaction().begin();

        sse2.getStrings().add("sse2_string1");
        sse2.getStrings().add("sse2_string2");

        em.persist(sse1);
        em.persist(sse2);

        em.getTransaction().commit();

        // Revision 2 (sse1: adding 2 elements, sse2: adding an existing element)
        em.getTransaction().begin();

        sse1 = em.find(StringSetEntity.class, sse1.getId());
        sse2 = em.find(StringSetEntity.class, sse2.getId());

        sse1.getStrings().add("sse1_string1");
        sse1.getStrings().add("sse1_string2");

        sse2.getStrings().add("sse2_string1");

        em.getTransaction().commit();

        // Revision 3 (sse1: removing a non-existing element, sse2: removing one element)
        em.getTransaction().begin();

        sse1 = em.find(StringSetEntity.class, sse1.getId());
        sse2 = em.find(StringSetEntity.class, sse2.getId());

        sse1.getStrings().remove("sse1_string3");
        sse2.getStrings().remove("sse2_string1");

        em.getTransaction().commit();

        //

        sse1_id = sse1.getId();
        sse2_id = sse2.getId();
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(StringSetEntity.class, sse1_id));
        assert Arrays.asList(1, 3).equals(getAuditReader().getRevisions(StringSetEntity.class, sse2_id));
    }

    @Test
    public void testHistoryOfSse1() {
        StringSetEntity rev1 = getAuditReader().find(StringSetEntity.class, sse1_id, 1);
        StringSetEntity rev2 = getAuditReader().find(StringSetEntity.class, sse1_id, 2);
        StringSetEntity rev3 = getAuditReader().find(StringSetEntity.class, sse1_id, 3);

        assert rev1.getStrings().equals(Collections.EMPTY_SET);
        assert rev2.getStrings().equals(TestTools.makeSet("sse1_string1", "sse1_string2"));
        assert rev3.getStrings().equals(TestTools.makeSet("sse1_string1", "sse1_string2"));
    }

    @Test
    public void testHistoryOfSse2() {
        StringSetEntity rev1 = getAuditReader().find(StringSetEntity.class, sse2_id, 1);
        StringSetEntity rev2 = getAuditReader().find(StringSetEntity.class, sse2_id, 2);
        StringSetEntity rev3 = getAuditReader().find(StringSetEntity.class, sse2_id, 3);

        assert rev1.getStrings().equals(TestTools.makeSet("sse2_string1", "sse2_string2"));
        assert rev2.getStrings().equals(TestTools.makeSet("sse2_string1", "sse2_string2"));
        assert rev3.getStrings().equals(TestTools.makeSet("sse2_string2"));
    }
}