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
package org.hibernate.envers.test.integration.naming.ids;

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
public class JoinMulIdNaming extends BaseEnversJPAFunctionalTestCase {
    private MulIdNaming ed_id1;
    private MulIdNaming ed_id2;
    private MulIdNaming ing_id1;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(JoinMulIdNamingRefEdEntity.class);
        cfg.addAnnotatedClass(JoinMulIdNamingRefIngEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        ed_id1 = new MulIdNaming(10, 20);
        ed_id2 = new MulIdNaming(11, 21);
        ing_id1 = new MulIdNaming(12, 22);

        JoinMulIdNamingRefEdEntity ed1 = new JoinMulIdNamingRefEdEntity(ed_id1, "data1");
        JoinMulIdNamingRefEdEntity ed2 = new JoinMulIdNamingRefEdEntity(ed_id2, "data2");

        JoinMulIdNamingRefIngEntity ing1 = new JoinMulIdNamingRefIngEntity(ing_id1, "x", ed1);

        // Revision 1
        EntityManager em = getEntityManager();
        em.getTransaction().begin();

        em.persist(ed1);
        em.persist(ed2);
        em.persist(ing1);

        em.getTransaction().commit();

        // Revision 2
        em.getTransaction().begin();

        ed2 = em.find(JoinMulIdNamingRefEdEntity.class, ed_id2);

        ing1 = em.find(JoinMulIdNamingRefIngEntity.class, ing_id1);
        ing1.setData("y");
        ing1.setReference(ed2);

        em.getTransaction().commit();
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(JoinMulIdNamingRefEdEntity.class, ed_id1));
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(JoinMulIdNamingRefEdEntity.class, ed_id2));
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(JoinMulIdNamingRefIngEntity.class, ing_id1));
    }

    @Test
    public void testHistoryOfEdId1() {
        JoinMulIdNamingRefEdEntity ver1 = new JoinMulIdNamingRefEdEntity(ed_id1, "data1");

        assert getAuditReader().find(JoinMulIdNamingRefEdEntity.class, ed_id1, 1).equals(ver1);
        assert getAuditReader().find(JoinMulIdNamingRefEdEntity.class, ed_id1, 2).equals(ver1);
    }

    @Test
    public void testHistoryOfEdId2() {
        JoinMulIdNamingRefEdEntity ver1 = new JoinMulIdNamingRefEdEntity(ed_id2, "data2");

        assert getAuditReader().find(JoinMulIdNamingRefEdEntity.class, ed_id2, 1).equals(ver1);
        assert getAuditReader().find(JoinMulIdNamingRefEdEntity.class, ed_id2, 2).equals(ver1);
    }

    @Test
    public void testHistoryOfIngId1() {
        JoinMulIdNamingRefIngEntity ver1 = new JoinMulIdNamingRefIngEntity(ing_id1, "x", null);
        JoinMulIdNamingRefIngEntity ver2 = new JoinMulIdNamingRefIngEntity(ing_id1, "y", null);

        assert getAuditReader().find(JoinMulIdNamingRefIngEntity.class, ing_id1, 1).equals(ver1);
        assert getAuditReader().find(JoinMulIdNamingRefIngEntity.class, ing_id1, 2).equals(ver2);

        assert getAuditReader().find(JoinMulIdNamingRefIngEntity.class, ing_id1, 1).getReference().equals(
                new JoinMulIdNamingRefEdEntity(ed_id1, "data1"));
        assert getAuditReader().find(JoinMulIdNamingRefIngEntity.class, ing_id1, 2).getReference().equals(
                new JoinMulIdNamingRefEdEntity(ed_id2, "data2"));
    }

    @SuppressWarnings({"unchecked"})
    @Test
    public void testJoinColumnNames() {
		Iterator<Column> columns =
				getCfg().getClassMapping("org.hibernate.envers.test.integration.naming.ids.JoinMulIdNamingRefIngEntity_AUD")
						.getProperty("reference_id1").getColumnIterator();
		assertTrue(columns.hasNext());
		assertEquals("ID1_reference", columns.next().getName());
		assertFalse(columns.hasNext());

		columns = getCfg().getClassMapping("org.hibernate.envers.test.integration.naming.ids.JoinMulIdNamingRefIngEntity_AUD")
				.getProperty("reference_id2").getColumnIterator();
		assertTrue(columns.hasNext());
		assertEquals("ID2_reference", columns.next().getName());
		assertFalse(columns.hasNext());
	}
}