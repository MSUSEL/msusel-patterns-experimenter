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
import org.hibernate.envers.test.entities.customtype.Component;
import org.hibernate.envers.test.entities.customtype.CompositeCustomTypeEntity;

import static junit.framework.Assert.assertEquals;
import static org.hibernate.envers.test.tools.TestTools.extractRevisionNumbers;
import static org.hibernate.envers.test.tools.TestTools.makeList;

/**
 * @author Adam Warski (adam at warski dot org)
 * @author Michal Skowronek (mskowr at o2 dot pl)
 */
public class HasChangedCompositeCustom extends AbstractModifiedFlagsEntityTest {
    private Integer ccte_id;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(CompositeCustomTypeEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        CompositeCustomTypeEntity ccte = new CompositeCustomTypeEntity();

        // Revision 1 (persisting 1 entity)
        em.getTransaction().begin();

        ccte.setComponent(new Component("a", 1));

        em.persist(ccte);

        em.getTransaction().commit();

        // Revision 2 (changing the component)
        em.getTransaction().begin();

        ccte = em.find(CompositeCustomTypeEntity.class, ccte.getId());

        ccte.getComponent().setProp1("b");

        em.getTransaction().commit();

        // Revision 3 (replacing the component)
        em.getTransaction().begin();

        ccte = em.find(CompositeCustomTypeEntity.class, ccte.getId());

        ccte.setComponent(new Component("c", 3));

        em.getTransaction().commit();

        //

        ccte_id = ccte.getId();
    }

	@Test
	public void testHasChanged() throws Exception {
		List list = queryForPropertyHasChanged(CompositeCustomTypeEntity.class,ccte_id, "component");
		assertEquals(3, list.size());
		assertEquals(makeList(1, 2, 3), extractRevisionNumbers(list));

		list = queryForPropertyHasNotChanged(CompositeCustomTypeEntity.class,ccte_id, "component");
		assertEquals(0, list.size());
	}
}