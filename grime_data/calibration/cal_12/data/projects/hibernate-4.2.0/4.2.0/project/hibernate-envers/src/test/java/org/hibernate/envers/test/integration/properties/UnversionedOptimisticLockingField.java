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
package org.hibernate.envers.test.integration.properties;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;

/**
 * @author Nicolas Doroskevich
 */
public class UnversionedOptimisticLockingField extends BaseEnversJPAFunctionalTestCase {
	private Integer id1;

	public void configure(Ejb3Configuration cfg) {
		cfg.addAnnotatedClass(UnversionedOptimisticLockingFieldEntity.class);

	}

	@Override
	public void addConfigOptions(Map configuration) {
		super.addConfigOptions( configuration );
		configuration.put("org.hibernate.envers.doNotAuditOptimisticLockingField", "true");

	}

	@Test
    @Priority(10)
	public void initData() {
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		UnversionedOptimisticLockingFieldEntity olfe = new UnversionedOptimisticLockingFieldEntity("x");
		em.persist(olfe);
		id1 = olfe.getId();
		em.getTransaction().commit();

		em.getTransaction().begin();
		olfe = em.find(UnversionedOptimisticLockingFieldEntity.class, id1);
		olfe.setStr("y");
		em.getTransaction().commit();
	}

	@Test
	public void testRevisionCounts() {
		assert Arrays.asList(1, 2).equals(
				getAuditReader().getRevisions(UnversionedOptimisticLockingFieldEntity.class,
						id1));
	}

	@Test
	public void testHistoryOfId1() {
		UnversionedOptimisticLockingFieldEntity ver1 = new UnversionedOptimisticLockingFieldEntity(id1, "x");
		UnversionedOptimisticLockingFieldEntity ver2 = new UnversionedOptimisticLockingFieldEntity(id1, "y");
		
		assert getAuditReader().find(UnversionedOptimisticLockingFieldEntity.class, id1, 1)
				.equals(ver1);
		assert getAuditReader().find(UnversionedOptimisticLockingFieldEntity.class, id1, 2)
				.equals(ver2);
	}
	
	@Test
	public void testMapping() {
		PersistentClass pc = getCfg().getClassMapping(UnversionedOptimisticLockingFieldEntity.class.getName() + "_AUD");
		Iterator pi = pc.getPropertyIterator();
		while(pi.hasNext()) {
			Property p = (Property) pi.next();
			assert !"optLocking".equals(p.getName());
		}
	}
}
