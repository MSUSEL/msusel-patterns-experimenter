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
package org.hibernate.envers.test.integration.modifiedflags;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.configuration.GlobalConfiguration;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.integration.basic.BasicTestEntity1;
import org.hibernate.envers.test.tools.TestTools;

import static junit.framework.Assert.assertEquals;
import static org.hibernate.envers.test.tools.TestTools.extractRevisionNumbers;
import static org.hibernate.envers.test.tools.TestTools.makeList;

/**
 * @author Adam Warski (adam at warski dot org)
 * @author Michal Skowronek (mskowr at o2 dot pl)
 */
public class ModifiedFlagSuffix extends AbstractModifiedFlagsEntityTest {
    private Integer id1;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(BasicTestEntity1.class);
    }

	@Override
	protected void addConfigOptions(Map options) {
		super.addConfigOptions(options);
		options.put(GlobalConfiguration.MODIFIED_FLAG_SUFFIX_PROPERTY, "_CHANGED");
	}

	private Integer addNewEntity(String str, long lng) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        BasicTestEntity1 bte1 = new BasicTestEntity1(str, lng);
        em.persist(bte1);
        em.getTransaction().commit();

        return bte1.getId();
    }

    @Test
    @Priority(10)
    public void initData() {
        id1 = addNewEntity("x", 1); // rev 1
    }

	@Test
	public void testModFlagProperties() {
		assertEquals(TestTools.makeSet("str1_CHANGED", "long1_CHANGED"),
				TestTools.extractModProperties(getCfg().getClassMapping(
						"org.hibernate.envers.test.integration.basic.BasicTestEntity1_AUD"),
						"_CHANGED"));
	}

	@Test
	public void testHasChanged() throws Exception {
		List list = queryForPropertyHasChangedWithDeleted(BasicTestEntity1.class,
				id1, "str1");
		assertEquals(1, list.size());
		assertEquals(makeList(1), extractRevisionNumbers(list));

		list = queryForPropertyHasChangedWithDeleted(BasicTestEntity1.class,
				id1, "long1");
		assertEquals(1, list.size());
		assertEquals(makeList(1), extractRevisionNumbers(list));

		list = getAuditReader().createQuery().forRevisionsOfEntity(BasicTestEntity1.class, false, true)
				.add(AuditEntity.property("str1").hasChanged())
				.add(AuditEntity.property("long1").hasChanged())
				.getResultList();
		assertEquals(1, list.size());
		assertEquals(makeList(1), extractRevisionNumbers(list));
	}
}