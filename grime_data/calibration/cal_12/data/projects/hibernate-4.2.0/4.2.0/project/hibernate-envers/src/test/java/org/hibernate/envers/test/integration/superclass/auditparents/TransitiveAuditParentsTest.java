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

import org.junit.Assert;
import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.Audited;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.tools.TestTools;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Table;

/**
 * Tests mapping of child entity which parent declares one of its ancestors as audited with {@link Audited#auditParents()}
 * property. Child entity may mark explicitly its parent as audited or not.
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public class TransitiveAuditParentsTest extends BaseEnversJPAFunctionalTestCase {
    private long childImpTransId = 1L;
    private long childExpTransId = 2L;
    
    @Override
    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(MappedGrandparentEntity.class);
        cfg.addAnnotatedClass(TransitiveParentEntity.class);
        cfg.addAnnotatedClass(ImplicitTransitiveChildEntity.class);
        cfg.addAnnotatedClass(ExplicitTransitiveChildEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        // Revision 1
        em.getTransaction().begin();
        em.persist(new ImplicitTransitiveChildEntity(childImpTransId, "grandparent 1", "notAudited 1", "parent 1", "child 1"));
        em.getTransaction().commit();

        // Revision 2
        em.getTransaction().begin();
        em.persist(new ExplicitTransitiveChildEntity(childExpTransId, "grandparent 2", "notAudited 2", "parent 2", "child 2"));
        em.getTransaction().commit();
    }

    @Test
    public void testCreatedAuditTables() {
        Table explicitTransChildTable = getCfg().getClassMapping("org.hibernate.envers.test.integration.superclass.auditparents.ExplicitTransitiveChildEntity_AUD").getTable();
        checkTableColumns(TestTools.makeSet("child", "parent", "grandparent", "id"), TestTools.makeSet("notAudited"), explicitTransChildTable);

        Table implicitTransChildTable = getCfg().getClassMapping("org.hibernate.envers.test.integration.superclass.auditparents.ImplicitTransitiveChildEntity_AUD").getTable();
        checkTableColumns(TestTools.makeSet("child", "parent", "grandparent", "id"), TestTools.makeSet("notAudited"), implicitTransChildTable);
    }

    private void checkTableColumns(Set<String> expectedColumns, Set<String> unexpectedColumns, Table table) {
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
    public void testImplicitTransitiveAuditParents() {
        // expectedChild.notAudited shall be null, because it is not audited.
        ImplicitTransitiveChildEntity expectedChild = new ImplicitTransitiveChildEntity(childImpTransId, "grandparent 1", null, "parent 1", "child 1");
        ImplicitTransitiveChildEntity child = getAuditReader().find(ImplicitTransitiveChildEntity.class, childImpTransId, 1);
        Assert.assertEquals(expectedChild, child);
    }

    @Test
    public void testExplicitTransitiveAuditParents() {
        // expectedChild.notAudited shall be null, because it is not audited.
        ExplicitTransitiveChildEntity expectedChild = new ExplicitTransitiveChildEntity(childExpTransId, "grandparent 2", null, "parent 2", "child 2");
        ExplicitTransitiveChildEntity child = getAuditReader().find(ExplicitTransitiveChildEntity.class, childExpTransId, 2);
        Assert.assertEquals(expectedChild, child);
    }
}
