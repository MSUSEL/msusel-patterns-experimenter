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
package org.hibernate.envers.test.integration.reventity;

import java.util.Arrays;
import java.util.Map;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.cfg.Environment;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.StrTestEntity;
import org.hibernate.mapping.Table;
import org.hibernate.testing.RequiresDialect;

/**
 * Tests simple auditing process (read and write operations) when <i>REVINFO</i> and audit tables
 * exist in a different database schema.
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@RequiresDialect({H2Dialect.class})
public class DifferentDBSchemaTest extends BaseEnversJPAFunctionalTestCase {
    private static final String SCHEMA_NAME = "ENVERS_AUDIT";
    private Integer steId = null;

    @Override
    protected void addConfigOptions(Map options) {
        super.addConfigOptions(options);
        // Creates new schema after establishing connection
        options.putAll(Environment.getProperties());
        options.put("org.hibernate.envers.default_schema", SCHEMA_NAME);
    }

    @Override
    protected String createSecondSchema() {
        return SCHEMA_NAME;
    }

    @Override
    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(StrTestEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() throws InterruptedException {
        // Revision 1
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        StrTestEntity ste = new StrTestEntity("x");
        em.persist(ste);
        steId = ste.getId();
        em.getTransaction().commit();

        // Revision 2
        em.getTransaction().begin();
        ste = em.find(StrTestEntity.class, steId);
        ste.setStr("y");
        em.getTransaction().commit();
    }

    @Test
    public void testRevinfoSchemaName() {
        Table revisionTable = getCfg().getClassMapping("org.hibernate.envers.enhanced.SequenceIdRevisionEntity").getTable();
        assert SCHEMA_NAME.equals(revisionTable.getSchema());
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(StrTestEntity.class, steId));
    }

    @Test
    public void testHistoryOfId1() {
        StrTestEntity ver1 = new StrTestEntity("x", steId);
        StrTestEntity ver2 = new StrTestEntity("y", steId);

        assert getAuditReader().find(StrTestEntity.class, steId, 1).equals(ver1);
        assert getAuditReader().find(StrTestEntity.class, steId, 2).equals(ver2);
    }
}
