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
package org.hibernate.envers.test.integration.basic;

import java.util.Arrays;
import javax.persistence.EntityManager;

import junit.framework.Assert;
import org.junit.Test;

import org.hibernate.dialect.Oracle8iDialect;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.StrTestEntity;
import org.hibernate.testing.RequiresDialect;
import org.hibernate.testing.TestForIssue;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@TestForIssue(jiraKey = "HHH-7246")
@RequiresDialect(Oracle8iDialect.class)
public class EmptyStringTest extends BaseEnversJPAFunctionalTestCase {
    private Integer emptyId = null;
    private Integer nullId = null;

    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class<?>[]{StrTestEntity.class};
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        // Revision 1
        em.getTransaction().begin();
        StrTestEntity emptyEntity = new StrTestEntity("");
        em.persist(emptyEntity);
        StrTestEntity nullEntity = new StrTestEntity(null);
        em.persist(nullEntity);
        em.getTransaction().commit();

        emptyId = emptyEntity.getId();
        nullId = nullEntity.getId();

        em.close();
        em = getEntityManager();

        // Should not generate revision after NULL to "" modification and vice versa on Oracle.
        em.getTransaction().begin();
        emptyEntity = em.find(StrTestEntity.class, emptyId);
        emptyEntity.setStr(null);
        em.merge(emptyEntity);
        nullEntity = em.find(StrTestEntity.class, nullId);
        nullEntity.setStr("");
        em.merge(nullEntity);
        em.getTransaction().commit();

        em.close();
    }

    @Test
    public void testRevisionsCounts() {
        Assert.assertEquals(Arrays.asList(1), getAuditReader().getRevisions(StrTestEntity.class, emptyId));
        Assert.assertEquals(Arrays.asList(1), getAuditReader().getRevisions(StrTestEntity.class, nullId));
    }
}
