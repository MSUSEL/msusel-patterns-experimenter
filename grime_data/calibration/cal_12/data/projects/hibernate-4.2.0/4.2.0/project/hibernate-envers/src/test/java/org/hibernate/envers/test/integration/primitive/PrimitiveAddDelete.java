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
package org.hibernate.envers.test.integration.primitive;

import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.PrimitiveTestEntity;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class PrimitiveAddDelete extends BaseEnversJPAFunctionalTestCase {
    private Integer id1;

    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class[]{PrimitiveTestEntity.class};
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

		// Revision 1
        em.getTransaction().begin();
        PrimitiveTestEntity pte = new PrimitiveTestEntity(10, 11);
        em.persist(pte);
        id1 = pte.getId();
        em.getTransaction().commit();

		// Revision 2
        em.getTransaction().begin();
        pte = em.find(PrimitiveTestEntity.class, id1);
        pte.setNumVal1(20);
		pte.setNumVal2(21);
        em.getTransaction().commit();

		// Revision 3
        em.getTransaction().begin();
        pte = em.find(PrimitiveTestEntity.class, id1);
        em.remove(pte);
        em.getTransaction().commit();		
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1, 2, 3).equals(getAuditReader().getRevisions(PrimitiveTestEntity.class, id1));
    }

    @Test
    public void testHistoryOfId1() {
        PrimitiveTestEntity ver1 = new PrimitiveTestEntity(id1, 10, 0);
        PrimitiveTestEntity ver2 = new PrimitiveTestEntity(id1, 20, 0);

        assert getAuditReader().find(PrimitiveTestEntity.class, id1, 1).equals(ver1);
        assert getAuditReader().find(PrimitiveTestEntity.class, id1, 2).equals(ver2);
        assert getAuditReader().find(PrimitiveTestEntity.class, id1, 3) == null;
    }

	@Test
	public void testQueryWithDeleted() {
		// Selecting all entities, also the deleted ones
		List entities = getAuditReader().createQuery().forRevisionsOfEntity(PrimitiveTestEntity.class, true, true)
				.getResultList();

		assert entities.size() == 3;
		assert entities.get(0).equals(new PrimitiveTestEntity(id1, 10, 0));
		assert entities.get(1).equals(new PrimitiveTestEntity(id1, 20, 0));
		assert entities.get(2).equals(new PrimitiveTestEntity(id1, 0, 0));
	}
}