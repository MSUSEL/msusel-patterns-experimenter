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
package org.hibernate.test.annotations.immutable;

import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;
import org.junit.Test;

import org.hibernate.AnnotationException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.testing.ServiceRegistryBuilder;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for <code>Immutable</code> annotation.
 *
 * @author Hardy Ferentschik
 */
@SuppressWarnings("unchecked")
public class ImmutableTest extends BaseCoreFunctionalTestCase {
	private static final Logger log = Logger.getLogger( ImmutableTest.class );

	@Test
	public void testImmutableEntity() throws Exception {
		Session s = openSession();
		Transaction tx = s.beginTransaction();
		Country country = new Country();
		country.setName("Germany");
		s.persist(country);
		tx.commit();
		s.close();

		// try changing the entity
		s = openSession();
		tx = s.beginTransaction();
		Country germany = (Country) s.get(Country.class, country.getId());
		assertNotNull(germany);
		germany.setName("France");
		assertEquals("Local name can be changed", "France", germany.getName());
		s.save(germany);
		tx.commit();
		s.close();

		// retrieving the country again - it should be unmodified
		s = openSession();
		tx = s.beginTransaction();
		germany = (Country) s.get(Country.class, country.getId());
		assertNotNull(germany);
		assertEquals("Name should not have changed", "Germany", germany.getName());
		tx.commit();
		s.close();
	}

	@Test
	public void testImmutableCollection() {
		Country country = new Country();
		country.setName("Germany");
		List states = new ArrayList<State>();
		State bayern = new State();
		bayern.setName("Bayern");
		State hessen = new State();
		hessen.setName("Hessen");
		State sachsen = new State();
		sachsen.setName("Sachsen");
		states.add(bayern);
		states.add(hessen);
		states.add(sachsen);
		country.setStates(states);

		Session s = openSession();
		Transaction tx = s.beginTransaction();
		s.persist(country);
		tx.commit();
		s.close();

		s = openSession();
		tx = s.beginTransaction();
		Country germany = (Country) s.get(Country.class, country.getId());
		assertNotNull(germany);
		assertEquals("Wrong number of states", 3, germany.getStates().size());

		// try adding a state
		State foobar = new State();
		foobar.setName("foobar");
		s.save(foobar);
		germany.getStates().add(foobar);
		try {
			tx.commit();
			fail();
		}
		catch (HibernateException e) {
			assertTrue(e.getMessage().contains("changed an immutable collection instance"));
            log.debug("success");
		}
		s.close();

		s = openSession();
		tx = s.beginTransaction();
		germany = (Country) s.get(Country.class, country.getId());
		assertNotNull(germany);
		assertEquals("Wrong number of states", 3, germany.getStates().size());

		// try deleting a state
		germany.getStates().remove(0);
		try {
			tx.commit();
			fail();
		} catch (HibernateException e) {
			assertTrue(e.getMessage().contains("changed an immutable collection instance"));
            log.debug("success");
		}
		s.close();

		s = openSession();
		tx = s.beginTransaction();
		germany = (Country) s.get(Country.class, country.getId());
		assertNotNull(germany);
		assertEquals("Wrong number of states", 3, germany.getStates().size());
		tx.commit();
		s.close();
	}

	@Test
	public void testMiscplacedImmutableAnnotation() {
		try {
			Configuration config = new Configuration();
			config.addAnnotatedClass(Foobar.class);
			config.buildSessionFactory( ServiceRegistryBuilder.buildServiceRegistry( config.getProperties() ) );
			fail();
		}
		catch (AnnotationException ae) {
            log.debug("succes");
		}
	}

	@Override
    protected Class[] getAnnotatedClasses() {
		return new Class[] { Country.class, State.class};
	}
}
