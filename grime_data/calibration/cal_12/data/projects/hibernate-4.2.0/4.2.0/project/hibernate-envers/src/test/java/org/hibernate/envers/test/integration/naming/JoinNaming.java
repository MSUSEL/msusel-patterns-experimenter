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
package org.hibernate.envers.test.integration.naming;

import java.util.Arrays;
import java.util.Iterator;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.mapping.Column;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class JoinNaming extends BaseEnversJPAFunctionalTestCase {
    private Integer ed_id1;
    private Integer ed_id2;
    private Integer ing_id1;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(JoinNamingRefEdEntity.class);
        cfg.addAnnotatedClass(JoinNamingRefIngEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        JoinNamingRefEdEntity ed1 = new JoinNamingRefEdEntity("data1");
        JoinNamingRefEdEntity ed2 = new JoinNamingRefEdEntity("data2");

        JoinNamingRefIngEntity ing1 = new JoinNamingRefIngEntity("x", ed1);

        // Revision 1
        EntityManager em = getEntityManager();
        em.getTransaction().begin();

        em.persist(ed1);
        em.persist(ed2);
        em.persist(ing1);

        em.getTransaction().commit();

        // Revision 2
        em.getTransaction().begin();

        ed2 = em.find(JoinNamingRefEdEntity.class, ed2.getId());

        ing1 = em.find(JoinNamingRefIngEntity.class, ing1.getId());
        ing1.setData("y");
        ing1.setReference(ed2);

        em.getTransaction().commit();

        //

        ed_id1 = ed1.getId();
        ed_id2 = ed2.getId();
        ing_id1 = ing1.getId();
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(JoinNamingRefEdEntity.class, ed_id1));
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(JoinNamingRefEdEntity.class, ed_id2));
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(JoinNamingRefIngEntity.class, ing_id1));
    }

    @Test
    public void testHistoryOfEdId1() {
        JoinNamingRefEdEntity ver1 = new JoinNamingRefEdEntity(ed_id1, "data1");

        assert getAuditReader().find(JoinNamingRefEdEntity.class, ed_id1, 1).equals(ver1);
        assert getAuditReader().find(JoinNamingRefEdEntity.class, ed_id1, 2).equals(ver1);
    }

    @Test
    public void testHistoryOfEdId2() {
        JoinNamingRefEdEntity ver1 = new JoinNamingRefEdEntity(ed_id2, "data2");

        assert getAuditReader().find(JoinNamingRefEdEntity.class, ed_id2, 1).equals(ver1);
        assert getAuditReader().find(JoinNamingRefEdEntity.class, ed_id2, 2).equals(ver1);
    }

    @Test
    public void testHistoryOfIngId1() {
        JoinNamingRefIngEntity ver1 = new JoinNamingRefIngEntity(ing_id1, "x", null);
        JoinNamingRefIngEntity ver2 = new JoinNamingRefIngEntity(ing_id1, "y", null);

        assert getAuditReader().find(JoinNamingRefIngEntity.class, ing_id1, 1).equals(ver1);
        assert getAuditReader().find(JoinNamingRefIngEntity.class, ing_id1, 2).equals(ver2);

        assert getAuditReader().find(JoinNamingRefIngEntity.class, ing_id1, 1).getReference().equals(
                new JoinNamingRefEdEntity(ed_id1, "data1"));
        assert getAuditReader().find(JoinNamingRefIngEntity.class, ing_id1, 2).getReference().equals(
                new JoinNamingRefEdEntity(ed_id2, "data2"));
    }

    @SuppressWarnings({"unchecked"})
    @Test
    public void testJoinColumnName() {
		Iterator<Column> columns =
				getCfg().getClassMapping("org.hibernate.envers.test.integration.naming.JoinNamingRefIngEntity_AUD")
						.getProperty("reference_id").getColumnIterator();
		assertTrue(columns.hasNext());
		assertEquals("jnree_column_reference", columns.next().getName());
		assertFalse(columns.hasNext());
	}
}