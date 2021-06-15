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
package org.hibernate.envers.test.integration.inheritance.joined.relation;

import java.util.Arrays;
import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.tools.TestTools;
import org.hibernate.testing.TestForIssue;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@TestForIssue(jiraKey = "HHH-3843")
public class ParentReferencingChildTest extends BaseEnversJPAFunctionalTestCase {
    Person expLukaszRev1 = null;
    Role expAdminRev1 = null;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(Person.class);
        cfg.addAnnotatedClass(Role.class);
        cfg.addAnnotatedClass(RightsSubject.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        // Revision 1
        em.getTransaction().begin();
        Person lukasz = new Person();
        lukasz.setName("Lukasz");
        lukasz.setGroup("IT");
        em.persist(lukasz);
        Role admin = new Role();
        admin.setName("Admin");
        admin.setGroup("Confidential");
        lukasz.getRoles().add(admin);
        admin.getMembers().add(lukasz);
        em.persist(admin);
        em.getTransaction().commit();

        expLukaszRev1 = new Person(lukasz.getId(), "IT", "Lukasz");
        expAdminRev1 = new Role(admin.getId(), "Confidential", "Admin");
    }

    @Test
    public void testRevisionsCounts() {
        Assert.assertEquals(Arrays.asList(1), getAuditReader().getRevisions(Person.class, expLukaszRev1.getId()));
        Assert.assertEquals(Arrays.asList(1), getAuditReader().getRevisions(RightsSubject.class, expLukaszRev1.getId()));

        Assert.assertEquals(Arrays.asList(1), getAuditReader().getRevisions(Role.class, expAdminRev1.getId()));
        Assert.assertEquals(Arrays.asList(1), getAuditReader().getRevisions(RightsSubject.class, expAdminRev1.getId()));
    }

    @Test
    public void testHistoryOfLukasz() {
        Person lukaszRev1 = getAuditReader().find(Person.class, expLukaszRev1.getId(), 1);
        RightsSubject rightsSubjectLukaszRev1 = getAuditReader().find(RightsSubject.class, expLukaszRev1.getId(), 1);

        Assert.assertEquals(expLukaszRev1, lukaszRev1);
        Assert.assertEquals(TestTools.makeSet(expAdminRev1), lukaszRev1.getRoles());
        Assert.assertEquals(TestTools.makeSet(expAdminRev1), rightsSubjectLukaszRev1.getRoles());
    }

    @Test
    public void testHistoryOfAdmin() {
        Role adminRev1 = getAuditReader().find(Role.class, expAdminRev1.getId(), 1);

        Assert.assertEquals(expAdminRev1, adminRev1);
        Assert.assertEquals(TestTools.makeSet(expLukaszRev1), adminRev1.getMembers());
    }
}
