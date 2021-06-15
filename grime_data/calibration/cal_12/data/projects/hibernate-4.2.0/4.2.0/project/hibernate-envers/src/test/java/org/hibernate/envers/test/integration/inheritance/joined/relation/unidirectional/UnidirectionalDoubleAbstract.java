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
package org.hibernate.envers.test.integration.inheritance.joined.relation.unidirectional;

import java.util.Arrays;
import java.util.Set;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class UnidirectionalDoubleAbstract extends BaseEnversJPAFunctionalTestCase {
	private Long cce1_id;
	private Integer cse1_id;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(AbstractContainedEntity.class);
        cfg.addAnnotatedClass(AbstractSetEntity.class);
        cfg.addAnnotatedClass(ContainedEntity.class);
		cfg.addAnnotatedClass(SetEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        // Rev 1
        em.getTransaction().begin();

        ContainedEntity cce1 = new ContainedEntity();
		em.persist(cce1);

		SetEntity cse1 = new SetEntity();
		cse1.getEntities().add(cce1);
		em.persist(cse1);

        em.getTransaction().commit();

		cce1_id = cce1.getId();
		cse1_id = cse1.getId();
    }

	@Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1).equals(getAuditReader().getRevisions(ContainedEntity.class, cce1_id));
        assert Arrays.asList(1).equals(getAuditReader().getRevisions(SetEntity.class, cse1_id));
    }

    @Test
    public void testHistoryOfReferencedCollection() {
		ContainedEntity cce1 = getEntityManager().find(ContainedEntity.class, cce1_id);

		Set<AbstractContainedEntity> entities = getAuditReader().find(SetEntity.class, cse1_id, 1).getEntities();
        assert entities.size() == 1;
		assert entities.iterator().next() instanceof ContainedEntity;
		assert entities.contains(cce1);
    }
}