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
package org.hibernate.envers.test.integration.notupdatable;

import junit.framework.Assert;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.testing.TestForIssue;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@TestForIssue(jiraKey = "HHH-5411")
public class PropertyNotUpdatableTest extends BaseEnversJPAFunctionalTestCase {
    private Long id = null;

    @Override
    protected void addConfigOptions(Map options) {
        options.put("org.hibernate.envers.store_data_at_delete", "true");
    }

    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class[] { PropertyNotUpdatableEntity.class };
    }

    @Test
    @Priority(10)
    public void initData() {
        // Revision 1
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        PropertyNotUpdatableEntity entity = new PropertyNotUpdatableEntity("data", "constant data 1", "constant data 2");
        em.persist(entity);
        em.getTransaction().commit();

        id = entity.getId();

        // Revision 2
        em.getTransaction().begin();
        entity = em.find(PropertyNotUpdatableEntity.class, entity.getId());
        entity.setData("modified data");
        entity.setConstantData1(null);
        em.merge(entity);
        em.getTransaction().commit();

        em.close();
        em = getEntityManager(); // Re-opening entity manager to re-initialize non-updatable fields
                                 // with database values. Otherwise PostUpdateEvent#getOldState() returns previous
                                 // memory state. This can be achieved using EntityManager#refresh(Object) method as well.

        // Revision 3
        em.getTransaction().begin();
        entity = em.find(PropertyNotUpdatableEntity.class, entity.getId());
        entity.setData("another modified data");
        entity.setConstantData2("invalid data");
        em.merge(entity);
        em.getTransaction().commit();

        // Revision 4
        em.getTransaction().begin();
        em.refresh(entity);
        em.remove(entity);
        em.getTransaction().commit();
    }

    @Test
    public void testRevisionsCounts() {
        Assert.assertEquals(Arrays.asList(1, 2, 3, 4), getAuditReader().getRevisions(PropertyNotUpdatableEntity.class, id));
    }

    @Test
    public void testHistoryOfId() {
        PropertyNotUpdatableEntity ver1 = new PropertyNotUpdatableEntity("data", "constant data 1", "constant data 2", id);
        Assert.assertEquals(ver1, getAuditReader().find(PropertyNotUpdatableEntity.class, id, 1));

        PropertyNotUpdatableEntity ver2 = new PropertyNotUpdatableEntity("modified data", "constant data 1", "constant data 2", id);
        Assert.assertEquals(ver2, getAuditReader().find(PropertyNotUpdatableEntity.class, id, 2));

        PropertyNotUpdatableEntity ver3 = new PropertyNotUpdatableEntity("another modified data", "constant data 1", "constant data 2", id);
        Assert.assertEquals(ver3, getAuditReader().find(PropertyNotUpdatableEntity.class, id, 3));
    }

    @Test
    public void testDeleteState() {
        PropertyNotUpdatableEntity delete = new PropertyNotUpdatableEntity("another modified data", "constant data 1", "constant data 2", id);
        List<Object> results = getAuditReader().createQuery().forRevisionsOfEntity(PropertyNotUpdatableEntity.class, true, true).getResultList();
        Assert.assertEquals(delete, results.get(3));
    }
}
