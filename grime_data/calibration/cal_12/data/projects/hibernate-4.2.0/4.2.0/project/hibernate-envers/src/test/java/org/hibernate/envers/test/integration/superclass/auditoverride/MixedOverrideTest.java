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
package org.hibernate.envers.test.integration.superclass.auditoverride;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Table;
import org.hibernate.testing.TestForIssue;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@TestForIssue(jiraKey = "HHH-4439")
public class MixedOverrideTest  extends BaseEnversJPAFunctionalTestCase {
    private Integer mixedEntityId = null;
    private Table mixedTable = null;

    @Override
    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(MixedOverrideEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        // Revision 1
        em.getTransaction().begin();
        MixedOverrideEntity mixedEntity = new MixedOverrideEntity("data 1", 1, "data 2");
        em.persist(mixedEntity);
        em.getTransaction().commit();
        mixedEntityId = mixedEntity.getId();

        mixedTable = getCfg().getClassMapping("org.hibernate.envers.test.integration.superclass.auditoverride.MixedOverrideEntity_AUD").getTable();
    }

    @Test
    public void testAuditedProperty() {
        Assert.assertNotNull(mixedTable.getColumn(new Column("number1")));
        Assert.assertNotNull(mixedTable.getColumn(new Column("str2")));
    }

    @Test
    public void testNotAuditedProperty() {
        Assert.assertNull(mixedTable.getColumn(new Column("str1")));
    }

    @Test
    public void testHistoryOfMixedEntity() {
        MixedOverrideEntity ver1 = new MixedOverrideEntity(null, 1, mixedEntityId, "data 2");
        Assert.assertEquals(ver1, getAuditReader().find(MixedOverrideEntity.class, mixedEntityId, 1));
    }
}