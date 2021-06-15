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
package org.hibernate.envers.test.integration.auditReader;

import java.util.Arrays;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.exception.NotAuditedException;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;

/**
 * A test which checks the correct behavior of AuditReader.isEntityClassAudited(Class entityClass).
 * 
 * @author Hernan Chanfreau
 */
public class AuditReaderAPITest extends BaseEnversJPAFunctionalTestCase {
    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(AuditedTestEntity.class);
        cfg.addAnnotatedClass(NotAuditedTestEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        AuditedTestEntity ent1 = new AuditedTestEntity(1, "str1");
        NotAuditedTestEntity ent2 = new NotAuditedTestEntity(1, "str1");

        em.persist(ent1);
        em.persist(ent2);
        em.getTransaction().commit();

        em.getTransaction().begin();
        
        ent1 = em.find(AuditedTestEntity.class, 1);
        ent2 = em.find(NotAuditedTestEntity.class, 1);
        ent1.setStr1("str2");
        ent2.setStr1("str2");
        em.getTransaction().commit();
    }

    @Test
    public void testIsEntityClassAuditedForAuditedEntity() {
        assert getAuditReader().isEntityClassAudited(AuditedTestEntity.class);
        
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(AuditedTestEntity.class, 1));
    }

    @Test
    public void testIsEntityClassAuditedForNotAuditedEntity() {
    	
        assert !getAuditReader().isEntityClassAudited(NotAuditedTestEntity.class);
        
        try {
        	getAuditReader().getRevisions(NotAuditedTestEntity.class, 1);
        } catch (NotAuditedException nae) {
			// it's ok because the entity is not audited
        	assert true;
		}
    }


}
