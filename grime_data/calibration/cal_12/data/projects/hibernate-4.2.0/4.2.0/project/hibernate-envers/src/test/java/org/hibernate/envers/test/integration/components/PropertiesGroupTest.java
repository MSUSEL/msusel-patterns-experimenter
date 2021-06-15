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
package org.hibernate.envers.test.integration.components;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Test;

import org.hibernate.MappingException;
import org.hibernate.envers.test.BaseEnversFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.components.UniquePropsEntity;
import org.hibernate.envers.test.entities.components.UniquePropsNotAuditedEntity;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.testing.TestForIssue;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@TestForIssue(jiraKey = "HHH-6636")
public class PropertiesGroupTest extends BaseEnversFunctionalTestCase {
    private PersistentClass uniquePropsAudit = null;
    private PersistentClass uniquePropsNotAuditedAudit = null;
    private UniquePropsEntity entityRev1 = null;
    private UniquePropsNotAuditedEntity entityNotAuditedRev2 = null;

    @Override
    protected String[] getMappings() {
        return new String[] {
                "mappings/components/UniquePropsEntity.hbm.xml",
                "mappings/components/UniquePropsNotAuditedEntity.hbm.xml"
        };
    }

    @Test
    @Priority(10)
    public void initData() {
        uniquePropsAudit = configuration().getClassMapping("org.hibernate.envers.test.entities.components.UniquePropsEntity_AUD");
        uniquePropsNotAuditedAudit = configuration().getClassMapping("org.hibernate.envers.test.entities.components.UniquePropsNotAuditedEntity_AUD");

        // Revision 1
        Session session = openSession();
        session.getTransaction().begin();
        UniquePropsEntity ent = new UniquePropsEntity();
        ent.setData1("data1");
        ent.setData2("data2");
        session.persist(ent);
        session.getTransaction().commit();

        entityRev1 = new UniquePropsEntity(ent.getId(), ent.getData1(), ent.getData2());

        // Revision 2
        session.getTransaction().begin();
        UniquePropsNotAuditedEntity entNotAud = new UniquePropsNotAuditedEntity();
        entNotAud.setData1("data3");
        entNotAud.setData2("data4");
        session.persist(entNotAud);
        session.getTransaction().commit();

        entityNotAuditedRev2 = new UniquePropsNotAuditedEntity(entNotAud.getId(), entNotAud.getData1(), null);
    }

    @Test
    public void testAuditTableColumns() {
        Assert.assertNotNull(uniquePropsAudit.getTable().getColumn(new Column("DATA1")));
        Assert.assertNotNull(uniquePropsAudit.getTable().getColumn(new Column("DATA2")));

        Assert.assertNotNull(uniquePropsNotAuditedAudit.getTable().getColumn(new Column("DATA1")));
        Assert.assertNull(uniquePropsNotAuditedAudit.getTable().getColumn(new Column("DATA2")));
    }

    @Test
    public void testHistoryOfUniquePropsEntity() {
        Assert.assertEquals(entityRev1, getAuditReader().find(UniquePropsEntity.class, entityRev1.getId(), 1));
    }

    @Test
    public void testHistoryOfUniquePropsNotAuditedEntity() {
        Assert.assertEquals(entityNotAuditedRev2, getAuditReader().find(UniquePropsNotAuditedEntity.class, entityNotAuditedRev2.getId(), 2));
    }
}
