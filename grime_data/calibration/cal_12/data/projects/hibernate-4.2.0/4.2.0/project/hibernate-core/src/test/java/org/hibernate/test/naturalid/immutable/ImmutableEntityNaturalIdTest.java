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
package org.hibernate.test.naturalid.immutable;

import java.lang.reflect.Field;

import org.junit.Test;

import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.criterion.Restrictions;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.fail;

/**
 * @author Alex Burgel
 */
public class ImmutableEntityNaturalIdTest extends BaseCoreFunctionalTestCase {
	public String[] getMappings() {
		return new String[] { "naturalid/immutable/ParentChildWithManyToOne.hbm.xml" };
	}

    public void configure(Configuration cfg) {
        cfg.setProperty(Environment.USE_SECOND_LEVEL_CACHE, "true");
        cfg.setProperty(Environment.USE_QUERY_CACHE, "true");
        cfg.setProperty(Environment.GENERATE_STATISTICS, "true");
    }

	@Test
	public void testNaturalIdCheck() throws Exception {
        Session s = openSession();
        Transaction t = s.beginTransaction();
        Parent p = new Parent("alex");
        Child c = new Child("billy", p);
        s.persist(p);
        s.persist(c);
		t.commit();
		s.close();

        Field name = c.getClass().getDeclaredField("name");
        name.setAccessible(true);
        name.set(c, "phil");

		s = openSession();
		t = s.beginTransaction();
        try {
            s.saveOrUpdate( c );
			s.flush();
            fail( "should have failed because immutable natural ID was altered");
        }
        catch (HibernateException he) {
			// expected
		}
		finally {
			t.rollback();
			s.close();
		}

        name.set(c, "billy");

		s = openSession();
		t = s.beginTransaction();
        s.delete(c);
        s.delete(p);
        t.commit();
        s.close();
    }

	@Test
	@SuppressWarnings( {"unchecked"})
    public void testSaveParentWithDetachedChildren() throws Exception {
        Session s = openSession();
        Transaction t = s.beginTransaction();

        Parent p = new Parent("alex");
        Child c = new Child("billy", p);

        s.persist(p);
        s.persist(c);
        t.commit();
        s.close();

        s = openSession();
        t = s.beginTransaction();

        p = (Parent) s.createCriteria(Parent.class)
				.add( Restrictions.eq("name", "alex") )
				.setFetchMode("children", FetchMode.JOIN)
        .setCacheable(true)
        .uniqueResult();

        t.commit();
        s.close();

        s = openSession();
        t = s.beginTransaction();

        Child c2 = new Child("joey", p);
        p.getChildren().add(c2);

        s.update(p);

        // this fails if AbstractEntityPersister returns identifiers instead of entities from
        // AbstractEntityPersister.getNaturalIdSnapshot()
        s.flush();

        s.delete(p);
        t.commit();
        s.close();
    }

}
