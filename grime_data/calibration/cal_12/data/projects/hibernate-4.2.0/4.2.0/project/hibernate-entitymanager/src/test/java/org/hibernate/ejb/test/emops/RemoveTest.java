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
package org.hibernate.ejb.test.emops;

import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;

import org.jboss.logging.Logger;
import org.junit.Assert;
import org.junit.Test;

import org.hibernate.ejb.test.BaseEntityManagerFunctionalTestCase;

/**
 * @author Emmanuel Bernard
 */
public class RemoveTest extends BaseEntityManagerFunctionalTestCase {
	private static final Logger log = Logger.getLogger( RemoveTest.class );

	@Test
	public void testRemove() {
		Race race = new Race();
		race.competitors.add( new Competitor() );
		race.competitors.add( new Competitor() );
		race.competitors.add( new Competitor() );
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		em.persist( race );
		em.flush();
		em.remove( race );
		em.flush();
		em.getTransaction().rollback();
		em.close();
	}

	@Test
	public void testRemoveAndFind() {
		Race race = new Race();
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		em.persist( race );
		em.remove( race );
		Assert.assertNull( em.find( Race.class, race.id ) );
		em.getTransaction().rollback();
		em.close();
	}

	@Test
	public void testUpdatedAndRemove() throws Exception {
		Music music = new Music();
		music.setName( "Classical" );
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		em.persist( music );
		em.getTransaction().commit();
		em.clear();


		EntityManager em2 = entityManagerFactory().createEntityManager();
		try {
			em2.getTransaction().begin();
			//read music from 2nd EM
			music = em2.find( Music.class, music.getId() );
		}
		catch (Exception e) {
			em2.getTransaction().rollback();
			em2.close();
			throw e;
		}

		//change music
        em = getOrCreateEntityManager();
		em.getTransaction().begin();
		em.find( Music.class, music.getId() ).setName( "Rap" );
		em.getTransaction().commit();

		try {
			em2.remove( music ); //remove changed music
			em2.flush();
			Assert.fail( "should have an optimistic lock exception" );
		}
        catch( OptimisticLockException e ) {
            log.debug("success");
		}
		finally {
			em2.getTransaction().rollback();
			em2.close();
		}

		//clean
        em.getTransaction().begin();
		em.remove( em.find( Music.class, music.getId() ) );
	    em.getTransaction().commit();
		em.close();
	}

	@Override
    public Class[] getAnnotatedClasses() {
		return new Class[] {
				Race.class,
				Competitor.class,
				Music.class
		};
	}


	@Override
	@SuppressWarnings( {"unchecked"})
	protected void addConfigOptions(Map options) {
		options.put( "hibernate.jdbc.batch_size", "0" );
	}
}
