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
import org.hibernate.envers.test.entities.StrTestEntity;
import org.hibernate.envers.test.entities.components.relations.OneToManyComponent;
import org.hibernate.envers.test.entities.components.relations.OneToManyComponentTestEntity;

import static junit.framework.Assert.assertEquals;
import static org.hibernate.envers.test.tools.TestTools.extractRevisionNumbers;
import static org.hibernate.envers.test.tools.TestTools.makeList;

/**
 * @author Adam Warski (adam at warski dot org)
 * @author Michal Skowronek (mskowr at o2 dot pl)
 */
public class HasChangedOneToManyInComponent extends AbstractModifiedFlagsEntityTest {
    private Integer otmcte_id1;

	public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(OneToManyComponentTestEntity.class);
		cfg.addAnnotatedClass(StrTestEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        // Revision 1
        EntityManager em = getEntityManager();
        em.getTransaction().begin();

		StrTestEntity ste1 = new StrTestEntity();
        ste1.setStr("str1");

		StrTestEntity ste2 = new StrTestEntity();
        ste2.setStr("str2");

        em.persist(ste1);
		em.persist(ste2);

        em.getTransaction().commit();

        // Revision 2
        em = getEntityManager();
        em.getTransaction().begin();

		OneToManyComponentTestEntity otmcte1 = new OneToManyComponentTestEntity(new OneToManyComponent("data1"));
		otmcte1.getComp1().getEntities().add(ste1);

		em.persist(otmcte1);

        em.getTransaction().commit();

        // Revision 3
        em = getEntityManager();
        em.getTransaction().begin();

        otmcte1 = em.find(OneToManyComponentTestEntity.class, otmcte1.getId());
        otmcte1.getComp1().getEntities().add(ste2);

        em.getTransaction().commit();

        otmcte_id1 = otmcte1.getId();
	}

	@Test
	public void testHasChangedId1() throws Exception {
		List list =
				queryForPropertyHasChanged(OneToManyComponentTestEntity.class,
				otmcte_id1, "comp1");
		assertEquals(2, list.size());
		assertEquals(makeList(2, 3), extractRevisionNumbers(list));

		list = queryForPropertyHasNotChanged(OneToManyComponentTestEntity.class,
				otmcte_id1, "comp1");
		assertEquals(0, list.size());
	}
}