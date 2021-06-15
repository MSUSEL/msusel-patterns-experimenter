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
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.QueryException;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.integration.basic.BasicTestEntity2;

import static junit.framework.Assert.assertEquals;
import static org.hibernate.envers.test.tools.TestTools.extractRevisionNumbers;
import static org.hibernate.envers.test.tools.TestTools.makeList;

/**
 * @author Adam Warski (adam at warski dot org)
 * @author Michal Skowronek (mskowr at o2 dot pl)
 */
public class HasChangedUnversionedProperties extends AbstractModifiedFlagsEntityTest {
    private Integer id1;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(BasicTestEntity2.class);
    }

    private Integer addNewEntity(String str1, String str2) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        BasicTestEntity2 bte2 = new BasicTestEntity2(str1, str2);
        em.persist(bte2);
        em.getTransaction().commit();

        return bte2.getId();
    }

    private void modifyEntity(Integer id, String str1, String str2) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        BasicTestEntity2 bte2 = em.find(BasicTestEntity2.class, id);
        bte2.setStr1(str1);
        bte2.setStr2(str2);
        em.getTransaction().commit();
    }

    @Test
    @Priority(10)
    public void initData() {
        id1 = addNewEntity("x", "a"); // rev 1
        modifyEntity(id1, "x", "a"); // no rev
        modifyEntity(id1, "y", "b"); // rev 2
        modifyEntity(id1, "y", "c"); // no rev
    }

	@Test
	public void testHasChangedQuery() throws Exception {
		List list = queryForPropertyHasChanged(BasicTestEntity2.class,
				id1, "str1");
		assertEquals(2, list.size());
		assertEquals(makeList(1, 2), extractRevisionNumbers(list));
	}

	@Test(expected = QueryException.class)
	public void testExceptionOnHasChangedQuery() throws Exception {
		queryForPropertyHasChangedWithDeleted(BasicTestEntity2.class,
				id1, "str2");
	}
}
