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
package org.hibernate.envers.test.integration.data;

import java.util.Arrays;
import java.util.Map;
import javax.persistence.EntityManager;

import org.hibernate.dialect.PostgreSQL82Dialect;
import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.testing.DialectChecks;
import org.hibernate.testing.RequiresDialectFeature;

/**
 * @author Adam Warski (adam at warski dot org)
 */
@RequiresDialectFeature(DialectChecks.SupportsExpectedLobUsagePattern.class)
public class LobSerializables extends BaseEnversJPAFunctionalTestCase {
    private Integer id1;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(LobSerializableTestEntity.class);
    }

    @Override
    protected void addConfigOptions(Map options) {
        super.addConfigOptions(options);
        if (getDialect() instanceof PostgreSQL82Dialect) {
            // In PostgreSQL LOBs cannot be used in auto-commit mode.
            options.put("hibernate.connection.autocommit", "false");
        }
    }

    @Test
    public void initData() {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        LobSerializableTestEntity ste = new LobSerializableTestEntity(new SerObject("d1"));
        em.persist(ste);
        id1 = ste.getId();
        em.getTransaction().commit();

        em.getTransaction().begin();
        ste = em.find(LobSerializableTestEntity.class, id1);
        ste.setObj(new SerObject("d2"));
        em.getTransaction().commit();
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(LobSerializableTestEntity.class, id1));
    }

    @Test
    public void testHistoryOfId1() {
        LobSerializableTestEntity ver1 = new LobSerializableTestEntity(id1, new SerObject("d1"));
        LobSerializableTestEntity ver2 = new LobSerializableTestEntity(id1, new SerObject("d2"));

        assert getAuditReader().find(LobSerializableTestEntity.class, id1, 1).equals(ver1);
        assert getAuditReader().find(LobSerializableTestEntity.class, id1, 2).equals(ver2);
    }
}