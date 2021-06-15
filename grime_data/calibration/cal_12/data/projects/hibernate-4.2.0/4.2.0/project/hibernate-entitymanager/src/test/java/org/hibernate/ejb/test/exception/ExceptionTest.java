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
package org.hibernate.ejb.test.exception;

import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

import org.jboss.logging.Logger;
import org.junit.Test;

import org.hibernate.cfg.Environment;
import org.hibernate.ejb.test.BaseEntityManagerFunctionalTestCase;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.testing.TestForIssue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Emmanuel Bernard
 */
@SuppressWarnings("unchecked")
public class ExceptionTest extends BaseEntityManagerFunctionalTestCase {
	private static final Logger log = Logger.getLogger( ExceptionTest.class );

	@Test
	public void testOptimisticLockingException() throws Exception {
		EntityManager em = getOrCreateEntityManager();
		EntityManager em2 = entityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		Music music = new Music();
		music.setName( "Old Country" );
		em.persist( music );
		em.getTransaction().commit();

		try {
			em2.getTransaction().begin();
			Music music2 = em2.find( Music.class, music.getId() );
			music2.setName( "HouseMusic" );
			em2.getTransaction().commit();
		}
		catch ( Exception e ) {
			em2.getTransaction().rollback();
			throw e;
		}
		finally {
			em2.close();
		}

		em.getTransaction().begin();
		music.setName( "Rock" );
		try {

			em.flush();
			fail( "Should raise an optimistic lock exception" );
		}
		catch ( OptimisticLockException e ) {
			//success
			assertEquals( music, e.getEntity() );
		}
		catch ( Exception e ) {
			fail( "Should raise an optimistic lock exception" );
		}
		finally {
			em.getTransaction().rollback();
			em.close();
		}
	}

	@Test
	public void testEntityNotFoundException() throws Exception {
		EntityManager em = getOrCreateEntityManager();
		Music music = em.getReference( Music.class, -1 );
		try {
			music.getName();
			fail( "Non existent entity should raise an exception when state is accessed" );
		}
		catch ( EntityNotFoundException e ) {
            log.debug("success");
		}
		finally {
			em.close();
		}
	}

	@Test
	public void testConstraintViolationException() throws Exception {
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		Music music = new Music();
		music.setName( "Jazz" );
		em.persist( music );
		Musician lui = new Musician();
		lui.setName( "Lui Armstrong" );
		lui.setFavouriteMusic( music );
		em.persist( lui );
		em.getTransaction().commit();
		try {
			em.getTransaction().begin();
			String hqlDelete = "delete Music where name = :name";
			em.createQuery( hqlDelete ).setParameter( "name", "Jazz" ).executeUpdate();
			em.getTransaction().commit();
			fail();
		}
		catch ( PersistenceException e ) {
			Throwable t = e.getCause();
			assertTrue( "Should be a constraint violation", t instanceof ConstraintViolationException );
			em.getTransaction().rollback();
		}
		finally {
			em.close();
		}
	}

	@Test
	@TestForIssue( jiraKey = "HHH-4676" )
	public void testInterceptor() throws Exception {
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		Instrument instrument = new Instrument();
		instrument.setName( "Guitar" );
		try {
			em.persist( instrument );
			fail( "Commit should have failed." );
		}
		catch ( RuntimeException e ) {
			assertTrue( em.getTransaction().getRollbackOnly() );
		}
		em.close();
	}

	@Override
	protected void addConfigOptions(Map options) {
		options.put( Environment.BATCH_VERSIONED_DATA, "false" );
	}

	@Override
    public Class[] getAnnotatedClasses() {
		return new Class[] {
				Music.class, Musician.class, Instrument.class
		};
	}
}
