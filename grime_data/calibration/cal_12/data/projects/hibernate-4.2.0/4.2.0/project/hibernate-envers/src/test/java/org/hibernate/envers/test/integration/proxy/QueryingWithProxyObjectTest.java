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
package org.hibernate.envers.test.integration.proxy;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import org.hibernate.MappingException;
import org.hibernate.envers.test.BaseEnversFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.StrTestEntity;
import org.hibernate.testing.TestForIssue;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public class QueryingWithProxyObjectTest extends BaseEnversFunctionalTestCase {
    private Integer id = null;

    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class[]{ StrTestEntity.class};
    }

    @Test
    @Priority(10)
    public void initData() {
        // Revision 1
        getSession().getTransaction().begin();
        StrTestEntity ste = new StrTestEntity("data");
        getSession().persist(ste);
        getSession().getTransaction().commit();
        id = ste.getId();
        getSession().close();
    }

    @Test
    @TestForIssue(jiraKey="HHH-4760")
    @SuppressWarnings("unchecked")
    public void testQueryingWithProxyObject() {
        StrTestEntity originalSte = new StrTestEntity("data", id);
        // Load the proxy instance
        StrTestEntity proxySte = (StrTestEntity) getSession().load(StrTestEntity.class, id);

        Assert.assertTrue(getAuditReader().isEntityClassAudited(proxySte.getClass()));

        StrTestEntity ste = getAuditReader().find(proxySte.getClass(), proxySte.getId(), 1);
        Assert.assertEquals(originalSte, ste);

        List<Number> revisions = getAuditReader().getRevisions(proxySte.getClass(), proxySte.getId());
        Assert.assertEquals(Arrays.asList(1), revisions);

        List<StrTestEntity> entities = getAuditReader().createQuery().forEntitiesAtRevision(proxySte.getClass(), 1).getResultList();
        Assert.assertEquals(Arrays.asList(originalSte), entities);

        ste = (StrTestEntity) getAuditReader().createQuery().forRevisionsOfEntity(proxySte.getClass(), true, false).getSingleResult();
        Assert.assertEquals(originalSte, ste);

        ste = (StrTestEntity) getAuditReader().createQuery().forEntitiesModifiedAtRevision(proxySte.getClass(), 1).getSingleResult();
        Assert.assertEquals(originalSte, ste);

        getSession().close();
    }
}
