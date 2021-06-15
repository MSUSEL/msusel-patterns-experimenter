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
@TestForIssue(jiraKey = "HHH-7003")
public class ColumnScalePrecisionTest extends BaseEnversJPAFunctionalTestCase {
    private Table auditTable = null;
    private Table originalTable = null;
    private Long id = null;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(ScalePrecisionEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        // Revision 1
        em.getTransaction().begin();
        ScalePrecisionEntity entity = new ScalePrecisionEntity(13.0);
        em.persist(entity);
        em.getTransaction().commit();

        id = entity.getId();
        auditTable = getCfg().getClassMapping("org.hibernate.envers.test.integration.basic.ScalePrecisionEntity_AUD").getTable();
        originalTable = getCfg().getClassMapping("org.hibernate.envers.test.integration.basic.ScalePrecisionEntity").getTable();
    }

    @Test
    public void testColumnScalePrecision() {
        Column testColumn = new Column("wholeNumber");
        Column scalePrecisionAuditColumn = auditTable.getColumn(testColumn);
        Column scalePrecisionColumn = originalTable.getColumn(testColumn);

        Assert.assertNotNull(scalePrecisionAuditColumn);
        Assert.assertEquals(scalePrecisionColumn.getPrecision(), scalePrecisionAuditColumn.getPrecision());
        Assert.assertEquals(scalePrecisionColumn.getScale(), scalePrecisionAuditColumn.getScale());
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1).equals(getAuditReader().getRevisions(ScalePrecisionEntity.class, id));
    }

    @Test
    public void testHistoryOfScalePrecisionEntity() {
        ScalePrecisionEntity ver1 = new ScalePrecisionEntity(13.0, id);

        Assert.assertEquals(ver1, getAuditReader().find(ScalePrecisionEntity.class, id, 1));
    }
}
