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
package org.hibernate.envers.test.integration.onetomany.inverseToSuperclass;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;

/**
 * @author Hern�n Chanfreau
 * 
 */

public class OneToManyInverseToSuperclassTest extends BaseEnversJPAFunctionalTestCase {

	private long m1_id;

	public void configure(Ejb3Configuration cfg) {
		try {
			URL url = Thread.currentThread().getContextClassLoader()
					.getResource(
							"mappings/oneToMany/inverseToSuperclass/mappings.hbm.xml");
			cfg.addFile(new File(url.toURI()));
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
    @Priority(10)
	public void initData() {
		EntityManager em = getEntityManager();

		Master m1 = new Master();
		DetailSubclass det1 = new DetailSubclass2();
		DetailSubclass det2 = new DetailSubclass2();

		// Revision 1
		em.getTransaction().begin();

		det1.setStr2("detail 1");

		m1.setStr("master");
		m1.setItems(new ArrayList<DetailSubclass>());
		m1.getItems().add(det1);
		det1.setParent(m1);

		em.persist(m1);
		em.getTransaction().commit();
		m1_id = m1.getId();

		// Revision 2
		em.getTransaction().begin();

		m1 = em.find(Master.class, m1_id);

		det2.setStr2("detail 2");
		det2.setParent(m1);
		m1.getItems().add(det2);
		em.getTransaction().commit();

		// Revision 3
		em.getTransaction().begin();

		m1 = em.find(Master.class, m1_id);
		m1.setStr("new master");

		det1 = m1.getItems().get(0);
		det1.setStr2("new detail");
		DetailSubclass det3 = new DetailSubclass2();
		det3.setStr2("detail 3");
		det3.setParent(m1);

		m1.getItems().get(1).setParent(null);
		// m1.getItems().remove(1);
		m1.getItems().add(det3);

		em.persist(m1);
		em.getTransaction().commit();

		// Revision 4
		em.getTransaction().begin();

		m1 = em.find(Master.class, m1_id);

		det1 = m1.getItems().get(0);
		det1.setParent(null);
		// m1.getItems().remove(det1);

		em.persist(m1);
		em.getTransaction().commit();

	}

	@Test
	public void testHistoryExists() {
		Master rev1_1 = getAuditReader().find(Master.class, m1_id, 1);
		Master rev1_2 = getAuditReader().find(Master.class, m1_id, 2);
		Master rev1_3 = getAuditReader().find(Master.class, m1_id, 3);
		Master rev1_4 = getAuditReader().find(Master.class, m1_id, 4);

		assert (rev1_1 != null);
		assert (rev1_2 != null);
		assert (rev1_3 != null);
		assert (rev1_4 != null);
	}

}
