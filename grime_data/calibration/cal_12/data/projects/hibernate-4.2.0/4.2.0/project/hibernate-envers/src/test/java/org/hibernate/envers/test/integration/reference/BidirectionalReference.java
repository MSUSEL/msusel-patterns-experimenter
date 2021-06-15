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
package org.hibernate.envers.test.integration.reference;

import java.util.Arrays;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.tools.TestTools;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class BidirectionalReference extends BaseEnversJPAFunctionalTestCase {
    private Long set1_id;
    private Long set2_id;

    private Long g1_id;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(GreetingPO.class);
        cfg.addAnnotatedClass(GreetingSetPO.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        GreetingSetPO set1 = new GreetingSetPO();
		set1.setName("a1");

		GreetingSetPO set2 = new GreetingSetPO();
		set2.setName("a2");

        // Revision 1
        em.getTransaction().begin();

        em.persist(set1);
        em.persist(set2);

		set1_id = set1.getId();
		set2_id = set2.getId();

        em.getTransaction().commit();
		em.clear();

        // Revision 2
        em.getTransaction().begin();

        GreetingPO g1 = new GreetingPO();
		g1.setGreeting("g1");
		g1.setGreetingSet(em.getReference(GreetingSetPO.class, set1_id));

        em.persist(g1);
		g1_id = g1.getId();

        em.getTransaction().commit();
		em.clear();

        // Revision 3
        em.getTransaction().begin();

        g1 = em.find(GreetingPO.class, g1_id);

		g1.setGreetingSet(em.getReference(GreetingSetPO.class, set2_id));

        em.getTransaction().commit();
    }

    @Test
    public void testRevisionsCounts() { 
        assert Arrays.asList(2, 3).equals(getAuditReader().getRevisions(GreetingPO.class, g1_id));

        assert Arrays.asList(1, 2, 3).equals(getAuditReader().getRevisions(GreetingSetPO.class, set1_id));
		assert Arrays.asList(1, 3).equals(getAuditReader().getRevisions(GreetingSetPO.class, set2_id));
    }
	
    @Test
    public void testHistoryOfG1() {
		GreetingPO rev1 = getAuditReader().find(GreetingPO.class, g1_id, 1);
		GreetingPO rev2 = getAuditReader().find(GreetingPO.class, g1_id, 2);
		GreetingPO rev3 = getAuditReader().find(GreetingPO.class, g1_id, 3);

        assert rev1 == null;
        assert rev2.getGreetingSet().getName().equals("a1");
		assert rev3.getGreetingSet().getName().equals("a2");
    }

	@Test
    public void testHistoryOfSet1() {
		GreetingSetPO rev1 = getAuditReader().find(GreetingSetPO.class, set1_id, 1);
		GreetingSetPO rev2 = getAuditReader().find(GreetingSetPO.class, set1_id, 2);
		GreetingSetPO rev3 = getAuditReader().find(GreetingSetPO.class, set1_id, 3);

        assert rev1.getName().equals("a1");
        assert rev2.getName().equals("a1");
		assert rev3.getName().equals("a1");

		GreetingPO g1 = new GreetingPO();
		g1.setId(g1_id);
		g1.setGreeting("g1");

		assert rev1.getGreetings().size() == 0;
		assert rev2.getGreetings().equals(TestTools.makeSet(g1));
		assert rev3.getGreetings().size() == 0;
    }

	@Test
    public void testHistoryOfSet2() {
		GreetingSetPO rev1 = getAuditReader().find(GreetingSetPO.class, set2_id, 1);
		GreetingSetPO rev2 = getAuditReader().find(GreetingSetPO.class, set2_id, 2);
		GreetingSetPO rev3 = getAuditReader().find(GreetingSetPO.class, set2_id, 3);

        assert rev1.getName().equals("a2");
        assert rev2.getName().equals("a2");
		assert rev3.getName().equals("a2");

		GreetingPO g1 = new GreetingPO();
		g1.setId(g1_id);
		g1.setGreeting("g1");

		assert rev1.getGreetings().size() == 0;
		assert rev2.getGreetings().size() == 0;
		assert rev3.getGreetings().equals(TestTools.makeSet(g1));
    }
}