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
import org.hibernate.envers.test.entities.components.Component1;
import org.hibernate.envers.test.entities.components.Component2;
import org.hibernate.envers.test.entities.components.ComponentTestEntity;
import org.hibernate.envers.test.integration.collection.mapkey.ComponentMapKeyEntity;

import static junit.framework.Assert.assertEquals;
import static org.hibernate.envers.test.tools.TestTools.extractRevisionNumbers;
import static org.hibernate.envers.test.tools.TestTools.makeList;

/**
 * @author Adam Warski (adam at warski dot org)
 * @author Michal Skowronek (mskowr at o2 dot pl)
 */
public class HasChangedComponentMapKey extends AbstractModifiedFlagsEntityTest {
    private Integer cmke_id;

    private Integer cte1_id;
    private Integer cte2_id;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(ComponentMapKeyEntity.class);
        cfg.addAnnotatedClass(ComponentTestEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        ComponentMapKeyEntity imke = new ComponentMapKeyEntity();

        // Revision 1 (intialy 1 mapping)
        em.getTransaction().begin();

        ComponentTestEntity cte1 = new ComponentTestEntity(new Component1("x1", "y2"), new Component2("a1", "b2"));
        ComponentTestEntity cte2 = new ComponentTestEntity(new Component1("x1", "y2"), new Component2("a1", "b2"));

        em.persist(cte1);
        em.persist(cte2);

        imke.getIdmap().put(cte1.getComp1(), cte1);

        em.persist(imke);

        em.getTransaction().commit();

        // Revision 2 (sse1: adding 1 mapping)
        em.getTransaction().begin();

        cte2 = em.find(ComponentTestEntity.class, cte2.getId());
        imke = em.find(ComponentMapKeyEntity.class, imke.getId());

        imke.getIdmap().put(cte2.getComp1(), cte2);

        em.getTransaction().commit();

        //

        cmke_id = imke.getId();

        cte1_id = cte1.getId();
        cte2_id = cte2.getId();
    }

	@Test
	public void testHasChangedMapEntity() throws Exception {
		List list = queryForPropertyHasChanged(ComponentMapKeyEntity.class, cmke_id, "idmap");
		assertEquals(2, list.size());
		assertEquals(makeList(1, 2), extractRevisionNumbers(list));

		list = queryForPropertyHasNotChanged(ComponentMapKeyEntity.class,
				cmke_id, "idmap");
		assertEquals(0, list.size());
	}

	@Test
	public void testHasChangedComponentEntity() throws Exception {
		List list = queryForPropertyHasChanged(ComponentTestEntity.class,
				cte1_id, "comp1");
		assertEquals(1, list.size());
		assertEquals(makeList(1), extractRevisionNumbers(list));

		list = queryForPropertyHasNotChanged(ComponentTestEntity.class, cte1_id,
				"comp1");
		assertEquals(0, list.size());

		list = queryForPropertyHasChanged(ComponentTestEntity.class, cte2_id, "comp1");
		assertEquals(1, list.size());
		assertEquals(makeList(1), extractRevisionNumbers(list));

		list = queryForPropertyHasNotChanged(ComponentTestEntity.class, cte2_id, "comp1");
		assertEquals(0, list.size());
	}
}