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
package org.hibernate.envers.test.integration.naming;

import javax.persistence.EntityManager;

import ee.estonia.entities.Child;
import ee.estonia.entities.Parent;
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
@TestForIssue(jiraKey = "HHH-6738")
public class EstonianTableAlias extends BaseEnversJPAFunctionalTestCase {
    private Long parentId = null;
    private Long childId = null;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(Parent.class);
        cfg.addAnnotatedClass(Child.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        // Revision 1
        em.getTransaction().begin();
        Parent parent = new Parent("parent");
        Child child = new Child("child");
        parent.getCollection().add(child);
        em.persist(child);
        em.persist(parent);
        em.getTransaction().commit();

        parentId = parent.getId();
        childId = child.getId();
    }

    @Test
    public void testAuditChildTableAlias() {
        Parent parent = new Parent("parent", parentId);
        Child child = new Child("child", childId);

        Parent ver1 = getAuditReader().find(Parent.class, parentId, 1);

        Assert.assertEquals(parent, ver1);
        Assert.assertEquals(TestTools.makeSet(child), ver1.getCollection());
    }
}