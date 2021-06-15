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
package org.hibernate.envers.test.integration.merge;

import java.util.Arrays;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Test;

import org.hibernate.envers.test.BaseEnversFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.StrTestEntity;
import org.hibernate.testing.TestForIssue;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@TestForIssue(jiraKey = "HHH-6753")
public class AddDelTest extends BaseEnversFunctionalTestCase {
    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class[]{StrTestEntity.class, GivenIdStrEntity.class};
    }

    @Test
    @Priority(10)
    public void initData() {
        // Revision 1
        Session session = openSession();
        session.getTransaction().begin();
        GivenIdStrEntity entity = new GivenIdStrEntity(1, "data");
        session.persist(entity);
        session.getTransaction().commit();

        // Revision 2
        session.getTransaction().begin();
        session.persist(new StrTestEntity("another data")); // Just to create second revision.
        entity = (GivenIdStrEntity) session.get(GivenIdStrEntity.class, 1);
        session.delete(entity); // First try to remove the entity.
        session.save(entity); // Then save it.
        session.getTransaction().commit();

        // Revision 3
        session.getTransaction().begin();
        entity = (GivenIdStrEntity) session.get(GivenIdStrEntity.class, 1);
        session.delete(entity); // First try to remove the entity.
        entity.setData("modified data"); // Then change it's state.
        session.save(entity); // Finally save it.
        session.getTransaction().commit();

        session.close();
    }

    @Test
    public void testRevisionsCountOfGivenIdStrEntity() {
        // Revision 2 has not changed entity's state.
        Assert.assertEquals(Arrays.asList(1, 3), getAuditReader().getRevisions(GivenIdStrEntity.class, 1));

        getSession().close();
    }

    @Test
    public void testHistoryOfGivenIdStrEntity() {
        Assert.assertEquals(new GivenIdStrEntity(1, "data"), getAuditReader().find(GivenIdStrEntity.class, 1, 1));
        Assert.assertEquals(new GivenIdStrEntity(1, "modified data"), getAuditReader().find(GivenIdStrEntity.class, 1, 3));

        getSession().close();
    }
}
