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
package org.hibernate.envers.test.integration.superclass.auditAtMethodSuperclassLevel.auditAllSubclass;

import java.util.Arrays;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.exception.NotAuditedException;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.integration.superclass.auditAtMethodSuperclassLevel.AuditedMethodMappedSuperclass;
import org.hibernate.envers.test.integration.superclass.auditAtMethodSuperclassLevel.NotAuditedSubclassEntity;

/**
 * @author Adam Warski (adam at warski dot org)
 * 
 * @author Hern&aacut;n Chanfreau
 * 
 */
public class MappedSubclassingAllAuditedTest extends BaseEnversJPAFunctionalTestCase {
	private Integer id2_1;
	private Integer id1_1;

	public void configure(Ejb3Configuration cfg) {
		cfg.addAnnotatedClass(AuditedMethodMappedSuperclass.class);
		cfg.addAnnotatedClass(AuditedAllSubclassEntity.class);
		cfg.addAnnotatedClass(NotAuditedSubclassEntity.class);
	}

	@Test
    @Priority(10)
	public void initData() {
		// Revision 1
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		NotAuditedSubclassEntity nas = new NotAuditedSubclassEntity("nae","super str","not audited str");
		em.persist(nas);
		AuditedAllSubclassEntity ae = new AuditedAllSubclassEntity("ae", "super str", "audited str");
		em.persist(ae);
		id1_1 = ae.getId();
		id2_1 = nas.getId();
		em.getTransaction().commit();

		// Revision 2
		em.getTransaction().begin();
		ae = em.find(AuditedAllSubclassEntity.class, id1_1);
		ae.setStr("ae new");
		ae.setSubAuditedStr("audited str new");
		nas = em.find(NotAuditedSubclassEntity.class, id2_1);
		nas.setStr("nae new");
		nas.setNotAuditedStr("not aud str new");
		em.getTransaction().commit();
	}

	@Test
	public void testRevisionsCountsForAudited() {
		assert Arrays.asList(1, 2).equals(
				getAuditReader().getRevisions(AuditedAllSubclassEntity.class, id1_1));
	}
	
	@Test(expected= NotAuditedException.class )
	public void testRevisionsCountsForNotAudited() {
		try {
			getAuditReader().getRevisions(NotAuditedSubclassEntity.class, id2_1);
			assert(false);
		} catch (NotAuditedException nae) {
			throw nae;
		}
	}
	

	@Test
	public void testHistoryOfAudited() {
		AuditedAllSubclassEntity ver1 = new AuditedAllSubclassEntity(id1_1, "ae", "super str", "audited str");
		AuditedAllSubclassEntity ver2 = new AuditedAllSubclassEntity(id1_1, "ae new", "super str", "audited str new");
		
		AuditedAllSubclassEntity rev1 = getAuditReader().find(AuditedAllSubclassEntity.class, id1_1, 1);  
		AuditedAllSubclassEntity rev2 = getAuditReader().find(AuditedAllSubclassEntity.class, id1_1, 2);
		
		//this property is not audited on superclass
		assert(rev1.getOtherStr() == null);
		assert(rev2.getOtherStr() == null);
			
		assert rev1.equals(ver1);
		assert rev2.equals(ver2);
	}
	
	@Test(expected=NotAuditedException.class)
	public void testHistoryOfNotAudited() {
		try {
			getAuditReader().find(NotAuditedSubclassEntity.class, id2_1, 1);
			assert(false);
		} catch (NotAuditedException nae) {
			throw nae;
		}
	}
	
}
