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
package org.hibernate.envers.test.integration.modifiedflags;

import java.util.List;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.integration.inheritance.joined.childrelation.ChildIngEntity;
import org.hibernate.envers.test.integration.inheritance.joined.childrelation.ParentNotIngEntity;
import org.hibernate.envers.test.integration.inheritance.joined.childrelation.ReferencedEntity;

import static junit.framework.Assert.assertEquals;
import static org.hibernate.envers.test.tools.TestTools.extractRevisionNumbers;
import static org.hibernate.envers.test.tools.TestTools.makeList;

/**
 * @author Adam Warski (adam at warski dot org)
 * @author Michal Skowronek (mskowr at o2 dot pl)
 */
public class HasChangedChildReferencing extends AbstractModifiedFlagsEntityTest {
    private Integer re_id1;
    private Integer re_id2;

	public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(ChildIngEntity.class);
        cfg.addAnnotatedClass(ParentNotIngEntity.class);
        cfg.addAnnotatedClass(ReferencedEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        re_id1 = 1;
        re_id2 = 10;
		Integer c_id = 100;

        // Rev 1
        em.getTransaction().begin();

        ReferencedEntity re1 = new ReferencedEntity(re_id1);
        em.persist(re1);

        ReferencedEntity re2 = new ReferencedEntity(re_id2);
        em.persist(re2);

        em.getTransaction().commit();

        // Rev 2
        em.getTransaction().begin();

        re1 = em.find(ReferencedEntity.class, re_id1);

        ChildIngEntity cie = new ChildIngEntity(c_id, "y", 1l);
        cie.setReferenced(re1);
        em.persist(cie);
        c_id = cie.getId();

        em.getTransaction().commit();

        // Rev 3
        em.getTransaction().begin();

        re2 = em.find(ReferencedEntity.class, re_id2);
        cie = em.find(ChildIngEntity.class, c_id);

        cie.setReferenced(re2);

        em.getTransaction().commit();
    }

	@Test
	public void testReferencedEntityHasChanged() throws Exception {
		List list = queryForPropertyHasChanged(ReferencedEntity.class, re_id1, "referencing");
		assertEquals(2, list.size());
		assertEquals(makeList(2, 3), extractRevisionNumbers(list));

		list = queryForPropertyHasNotChanged(ReferencedEntity.class, re_id1, "referencing");
		assertEquals(1, list.size()); // initially referencing collection is null
		assertEquals(makeList(1), extractRevisionNumbers(list));

		list = queryForPropertyHasChanged(ReferencedEntity.class, re_id2, "referencing");
		assertEquals(1, list.size());
		assertEquals(makeList(3), extractRevisionNumbers(list));
	}

}