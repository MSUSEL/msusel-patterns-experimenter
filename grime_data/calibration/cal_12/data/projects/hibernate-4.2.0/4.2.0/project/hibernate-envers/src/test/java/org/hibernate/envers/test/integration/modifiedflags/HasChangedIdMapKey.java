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
import org.hibernate.envers.test.integration.collection.mapkey.IdMapKeyEntity;

import static junit.framework.Assert.assertEquals;
import static org.hibernate.envers.test.tools.TestTools.extractRevisionNumbers;
import static org.hibernate.envers.test.tools.TestTools.makeList;

/**
 * @author Adam Warski (adam at warski dot org)
 * @author Michal Skowronek (mskowr at o2 dot pl)
 */
public class HasChangedIdMapKey extends AbstractModifiedFlagsEntityTest {
    private Integer imke_id;

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

	}

	@Test
	public void testHasChanged() throws Exception {
		List list = queryForPropertyHasChanged(IdMapKeyEntity.class, imke_id,
				"idmap");
		assertEquals(2, list.size());
		assertEquals(makeList(1, 2), extractRevisionNumbers(list));

		list = queryForPropertyHasNotChanged(IdMapKeyEntity.class, imke_id,
				"idmap");
		assertEquals(0, list.size());
	}
}