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
package org.hibernate.ejb.test.cascade;

import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.test.BaseEntityManagerFunctionalTestCase;

import static org.junit.Assert.fail;

public class FetchTest2 extends BaseEntityManagerFunctionalTestCase {
	@Test
	public void testProxyTransientStuff() throws Exception {
		EntityManager em = entityManagerFactory().createEntityManager();
		em.getTransaction().begin();

		Troop2 disney = new Troop2();
		disney.setName( "Disney" );

		Soldier2 mickey = new Soldier2();
		mickey.setName( "Mickey" );
		mickey.setTroop( disney );

		em.persist( disney );
		em.persist( mickey );

		em.getTransaction().commit();
		em.close();

		em = entityManagerFactory().createEntityManager();
		em.getTransaction().begin();

		Soldier2 soldier = em.find( Soldier2.class, mickey.getId() );
		soldier.getTroop().getId();
		try {
			em.flush();
		}
		catch (IllegalStateException e) {
			fail( "Should not raise an exception" );
		}

		em.getTransaction().commit();
		em.close();

		em = entityManagerFactory().createEntityManager();
		em.getTransaction().begin();

		//load troop wo a proxy
		disney = em.find( Troop2.class, disney.getId() );
		soldier = em.find( Soldier2.class, mickey.getId() );

		try {
			em.flush();
		}
		catch (IllegalStateException e) {
			fail( "Should not raise an exception" );
		}
		em.remove( soldier );
		em.remove( disney );
		em.getTransaction().commit();
		em.close();
	}

	@Override
	public Class[] getAnnotatedClasses() {
		return new Class[]{
				Troop2.class,
				Soldier2.class
		};
	}
}
