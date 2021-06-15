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
package org.hibernate.envers.test.integration.properties;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class VersionsProperties extends BaseEnversJPAFunctionalTestCase {
    private Integer id1;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(PropertiesTestEntity.class);


    }

	@Override
	protected void addConfigOptions(Map options) {
		super.addConfigOptions( options );
		options.put("org.hibernate.envers.auditTablePrefix", "VP_");
		options.put("org.hibernate.envers.auditTableSuffix", "_VS");
		options.put("org.hibernate.envers.revisionFieldName", "ver_rev");
		options.put("org.hibernate.envers.revisionTypeFieldName", "ver_rev_type");
	}

	@Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        PropertiesTestEntity pte = new PropertiesTestEntity("x");
        em.persist(pte);
        id1 = pte.getId();
        em.getTransaction().commit();

        em.getTransaction().begin();
        pte = em.find(PropertiesTestEntity.class, id1);
        pte.setStr("y");
        em.getTransaction().commit();
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(PropertiesTestEntity.class, id1));
    }

    @Test
    public void testHistoryOfId1() {
        PropertiesTestEntity ver1 = new PropertiesTestEntity(id1, "x");
        PropertiesTestEntity ver2 = new PropertiesTestEntity(id1, "y");

        assert getAuditReader().find(PropertiesTestEntity.class, id1, 1).equals(ver1);
        assert getAuditReader().find(PropertiesTestEntity.class, id1, 2).equals(ver2);
    }
}