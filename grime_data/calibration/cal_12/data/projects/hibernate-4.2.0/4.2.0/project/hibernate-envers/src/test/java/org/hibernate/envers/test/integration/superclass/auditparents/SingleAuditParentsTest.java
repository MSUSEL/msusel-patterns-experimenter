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
package org.hibernate.envers.test.integration.superclass.auditparents;

import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.MappedSuperclass;

import org.junit.Assert;
import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.Audited;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.StrIntTestEntity;
import org.hibernate.envers.test.tools.TestTools;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Table;

/**
 * Tests mapping of child entity that declares one of its ancestors as audited with {@link Audited#auditParents()} property.
 * All supperclasses are marked with {@link MappedSuperclass} annotation but not {@link Audited}.
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public class SingleAuditParentsTest extends BaseEnversJPAFunctionalTestCase {
    private long childSingleId = 1L;
    private Integer siteSingleId = null;

    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class[]{MappedGrandparentEntity.class, MappedParentEntity.class, ChildSingleParentEntity.class, StrIntTestEntity.class};
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();
        // Revision 1
        em.getTransaction().begin();
        StrIntTestEntity siteSingle = new StrIntTestEntity("data 1", 1);
        em.persist(siteSingle);
        em.persist(new ChildSingleParentEntity(childSingleId, "grandparent 1", "notAudited 1", "parent 1", "child 1", siteSingle));
        em.getTransaction().commit();
        siteSingleId = siteSingle.getId();
        em.close();
    }

    @Test
    public void testCreatedAuditTable() {
        Set<String> expectedColumns = TestTools.makeSet("child", "grandparent", "id");
        Set<String> unexpectedColumns = TestTools.makeSet("parent", "relation_id", "notAudited");

        Table table = getCfg().getClassMapping("org.hibernate.envers.test.integration.superclass.auditparents.ChildSingleParentEntity_AUD").getTable();

        for (String columnName : expectedColumns) {
            // Check whether expected column exists.
            Assert.assertNotNull(table.getColumn(new Column(columnName)));
        }
        for (String columnName : unexpectedColumns) {
            // Check whether unexpected column does not exist.
            Assert.assertNull(table.getColumn(new Column(columnName)));
        }
    }

    @Test
    public void testSingleAuditParent() {
        // expectedSingleChild.parent, expectedSingleChild.relation and expectedSingleChild.notAudited shall be null, because they are not audited.
        ChildSingleParentEntity expectedSingleChild = new ChildSingleParentEntity(childSingleId, "grandparent 1", null, null, "child 1", null);
        ChildSingleParentEntity child = getAuditReader().find(ChildSingleParentEntity.class, childSingleId, 1);
        Assert.assertEquals(expectedSingleChild, child);
        Assert.assertNull(child.getRelation());
    }
}
