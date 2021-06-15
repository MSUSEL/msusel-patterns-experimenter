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
import org.hibernate.envers.test.entities.collection.EnumSetEntity;
import org.hibernate.envers.test.entities.collection.EnumSetEntity.E1;
import org.hibernate.envers.test.entities.collection.EnumSetEntity.E2;

import static junit.framework.Assert.assertEquals;
import static org.hibernate.envers.test.tools.TestTools.extractRevisionNumbers;
import static org.hibernate.envers.test.tools.TestTools.makeList;

/**
 * @author Adam Warski (adam at warski dot org)
 * @author Michal Skowronek (mskowr at o2 dot pl)
 */
public class HasChangedEnumSet extends AbstractModifiedFlagsEntityTest {
    private Integer sse1_id;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(EnumSetEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        EnumSetEntity sse1 = new EnumSetEntity();

        // Revision 1 (sse1: initialy 1 element)
        em.getTransaction().begin();

        sse1.getEnums1().add(E1.X);
        sse1.getEnums2().add(E2.A);

        em.persist(sse1);

        em.getTransaction().commit();

        // Revision 2 (sse1: adding 1 element/removing a non-existing element)
        em.getTransaction().begin();

        sse1 = em.find(EnumSetEntity.class, sse1.getId());

        sse1.getEnums1().add(E1.Y);
        sse1.getEnums2().remove(E2.B);

        em.getTransaction().commit();

        // Revision 3 (sse1: removing 1 element/adding an exisiting element)
        em.getTransaction().begin();

        sse1 = em.find(EnumSetEntity.class, sse1.getId());

        sse1.getEnums1().remove(E1.X);
        sse1.getEnums2().add(E2.A);

        em.getTransaction().commit();

        //

        sse1_id = sse1.getId();
    }

	@Test
	public void testHasChanged() throws Exception {
		List list = queryForPropertyHasChanged(EnumSetEntity.class, sse1_id,
				"enums1");
		assertEquals(3, list.size());
		assertEquals(makeList(1, 2, 3), extractRevisionNumbers(list));

		list = queryForPropertyHasChanged(EnumSetEntity.class, sse1_id,
				"enums2");
		assertEquals(1, list.size());
		assertEquals(makeList(1), extractRevisionNumbers(list));

		list = queryForPropertyHasNotChanged(EnumSetEntity.class, sse1_id,
				"enums1");
		assertEquals(0, list.size());

		list = queryForPropertyHasNotChanged(EnumSetEntity.class, sse1_id,
				"enums2");
		assertEquals(2, list.size());
		assertEquals(makeList(2, 3), extractRevisionNumbers(list));
	}
}