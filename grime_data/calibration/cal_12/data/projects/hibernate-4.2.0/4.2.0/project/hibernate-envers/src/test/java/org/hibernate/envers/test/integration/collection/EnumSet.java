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
package org.hibernate.envers.test.integration.collection;

import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase ;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.collection.EnumSetEntity;
import org.hibernate.envers.test.entities.collection.EnumSetEntity.E1;
import org.hibernate.envers.test.entities.collection.EnumSetEntity.E2;
import org.hibernate.envers.test.tools.TestTools;
import org.hibernate.testing.TestForIssue;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class EnumSet extends BaseEnversJPAFunctionalTestCase  {
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
    public void testRevisionsCounts() {
        assert Arrays.asList(1, 2, 3).equals(getAuditReader().getRevisions(EnumSetEntity.class, sse1_id));
    }

    @Test
    public void testHistoryOfSse1() {
        EnumSetEntity rev1 = getAuditReader().find(EnumSetEntity.class, sse1_id, 1);
        EnumSetEntity rev2 = getAuditReader().find(EnumSetEntity.class, sse1_id, 2);
        EnumSetEntity rev3 = getAuditReader().find(EnumSetEntity.class, sse1_id, 3);

        assert rev1.getEnums1().equals(TestTools.makeSet(E1.X));
        assert rev2.getEnums1().equals(TestTools.makeSet(E1.X, E1.Y));
        assert rev3.getEnums1().equals(TestTools.makeSet(E1.Y));

        assert rev1.getEnums2().equals(TestTools.makeSet(E2.A));
        assert rev2.getEnums2().equals(TestTools.makeSet(E2.A));
        assert rev3.getEnums2().equals(TestTools.makeSet(E2.A));
    }

	@Test
	@TestForIssue( jiraKey = "HHH-7780" )
	public void testEnumRepresentation() {
		EntityManager entityManager = getEntityManager();
		List<Object> enums1 = entityManager.createNativeQuery( "SELECT enums1 FROM EnumSetEntity_enums1_AUD ORDER BY rev ASC" ).getResultList();
		List<Object> enums2 = entityManager.createNativeQuery( "SELECT enums2 FROM EnumSetEntity_enums2_AUD ORDER BY rev ASC" ).getResultList();
		entityManager.close();

		Assert.assertEquals( Arrays.asList( "X", "Y", "X" ), enums1 );

		Assert.assertEquals( 1, enums2.size() );
		Object enum2 = enums2.get( 0 );
		// Compare the Strings to account for, as an example, Oracle returning a BigDecimal instead of an int.
		Assert.assertEquals( "0", enum2.toString() );
	}
}