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
package org.hibernate.test.instrument.cases;
import java.util.Iterator;

import junit.framework.Assert;

import org.hibernate.Session;
import org.hibernate.bytecode.instrumentation.internal.FieldInterceptionHelper;
import org.hibernate.test.instrument.domain.Problematic;

/**
 * {@inheritDoc}
 *
 * @author Steve Ebersole
 */
public class TestLazyPropertyCustomTypeExecutable extends AbstractExecutable {

	protected String[] getResources() {
		return new String[] { "org/hibernate/test/instrument/domain/Problematic.hbm.xml" };
	}

	public void execute() throws Exception {
		Session s = getFactory().openSession();
		Problematic p = new Problematic();
		try {
			s.beginTransaction();
			p.setName( "whatever" );
			p.setBytes( new byte[] { 1, 0, 1, 1, 0 } );
			s.save( p );
			s.getTransaction().commit();
		} catch (Exception e) {
			s.getTransaction().rollback();
			throw e;
		} finally {
			s.close();
		}

		// this access should be ok because p1 is not a lazy proxy 
		s = getFactory().openSession();
		try {
			s.beginTransaction();
			Problematic p1 = (Problematic) s.get( Problematic.class, p.getId() );
			Assert.assertTrue( FieldInterceptionHelper.isInstrumented( p1 ) );
			p1.getRepresentation();
			s.getTransaction().commit();
		} catch (Exception e) {
			s.getTransaction().rollback();
			throw e;
		} finally {
			s.close();
		}
		
		s = getFactory().openSession();
		try {
			s.beginTransaction();
			Problematic p1 = (Problematic) s.createQuery( "from Problematic" ).setReadOnly(true ).list().get( 0 );
			p1.getRepresentation();
			s.getTransaction().commit();
		} catch (Exception e) {
			s.getTransaction().rollback();
			throw e;
		} finally {
			s.close();
		}
		
		s = getFactory().openSession();
		try {
			s.beginTransaction();
			Problematic p1 = (Problematic) s.load( Problematic.class, p.getId() );
			Assert.assertFalse( FieldInterceptionHelper.isInstrumented( p1 ) );
			p1.setRepresentation( p.getRepresentation() );
			s.getTransaction().commit();
		} catch (Exception e) {
			s.getTransaction().rollback();
			throw e;
		} finally {
			s.close();
		}
	}

	protected void cleanup() {
		Session s = getFactory().openSession();
		s.beginTransaction();
		Iterator itr = s.createQuery( "from Problematic" ).list().iterator();
		while ( itr.hasNext() ) {
			Problematic p = (Problematic) itr.next();
			s.delete( p );
		}
		s.getTransaction().commit();
		s.close();
	}
}