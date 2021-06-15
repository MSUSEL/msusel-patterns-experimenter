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
package org.hibernate.test.legacy;
import java.util.List;

import org.junit.Test;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.hibernate.dialect.SybaseASE15Dialect;
import org.hibernate.testing.SkipForDialect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Query by example test to allow nested components
 *
 * @author Emmanuel Bernard
 */
public class QueryByExampleTest extends LegacyTestCase {
	@Override
    public String[] getMappings() {
        return new String[] { "legacy/Componentizable.hbm.xml" };
    }

	@Test
    public void testSimpleQBE() throws Exception {
    	deleteData();
        initData();

        Session s = openSession();

        Transaction t = s.beginTransaction();
        Componentizable master = getMaster("hibernate", "open sourc%", "open source1");
        Criteria crit = s.createCriteria(Componentizable.class);
        Example ex = Example.create(master).enableLike();
        crit.add(ex);
        List result = crit.list();
        assertNotNull(result);
        assertEquals(1, result.size());

        t.commit();
        s.close();
    }

	@Test
    @SkipForDialect( value = SybaseASE15Dialect.class, jiraKey = "HHH-4580")
    public void testJunctionNotExpressionQBE() throws Exception {
        deleteData();
        initData();
        Session s = openSession();
        Transaction t = s.beginTransaction();
        Componentizable master = getMaster("hibernate", null, "ope%");
        Criteria crit = s.createCriteria(Componentizable.class);
        Example ex = Example.create(master).enableLike();

        crit.add(Restrictions.or(Restrictions.not(ex), ex));

        List result = crit.list();
        assertNotNull(result);
        assertEquals(2, result.size());
        t.commit();
        s.close();

    }

	@Test
    public void testExcludingQBE() throws Exception {
        deleteData();
        initData();
        Session s = openSession();
        Transaction t = s.beginTransaction();
        Componentizable master = getMaster("hibernate", null, "ope%");
        Criteria crit = s.createCriteria(Componentizable.class);
        Example ex = Example.create(master).enableLike()
            .excludeProperty("component.subComponent");
        crit.add(ex);
        List result = crit.list();
        assertNotNull(result);
        assertEquals(3, result.size());

        master = getMaster("hibernate", "ORM tool", "fake stuff");
        crit = s.createCriteria(Componentizable.class);
        ex = Example.create(master).enableLike()
            .excludeProperty("component.subComponent.subName1");
        crit.add(ex);
        result = crit.list();
        assertNotNull(result);
        assertEquals(1, result.size());
        t.commit();
        s.close();


    }

    private void initData() throws Exception {
        Session s = openSession();
        Transaction t = s.beginTransaction();
        Componentizable master = getMaster("hibernate", "ORM tool", "ORM tool1");
        s.saveOrUpdate(master);
        master = getMaster("hibernate", "open source", "open source1");
        s.saveOrUpdate(master);
        master = getMaster("hibernate", null, null);
        s.saveOrUpdate(master);
        t.commit();
        s.close();
    }

    private void deleteData() throws Exception {
    	Session s = openSession();
        Transaction t = s.beginTransaction();
		for ( Object entity : s.createQuery( "from Componentizable" ).list() ) {
			s.delete( entity );
		}
        t.commit();
        s.close();
    }

    private Componentizable getMaster(String name, String subname, String subname1) {
        Componentizable master = new Componentizable();
        if (name != null) {
            Component masterComp = new Component();
            masterComp.setName(name);
            if (subname != null || subname1 != null) {
                SubComponent subComponent = new SubComponent();
                subComponent.setSubName(subname);
                subComponent.setSubName1(subname1);
                masterComp.setSubComponent(subComponent);
            }
            master.setComponent(masterComp);
        }
        return master;
    }
}
