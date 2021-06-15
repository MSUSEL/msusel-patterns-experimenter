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
package org.hibernate.test.annotations.various;

import java.util.Date;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

/**
 * @author Emmanuel Bernard
 */
public class IndexTest extends BaseCoreFunctionalTestCase {
	@Test
	public void testIndexManyToOne() throws Exception {
		//TODO find a way to test indexes???
		Session s = openSession();
		s.getTransaction().begin();
		Conductor emmanuel = new Conductor();
		emmanuel.setName( "Emmanuel" );
		s.persist( emmanuel );
		Vehicule tank = new Vehicule();
		tank.setCurrentConductor( emmanuel );
		tank.setRegistrationNumber( "324VX43" );
		s.persist( tank );
		s.flush();
		s.delete( tank );
		s.delete( emmanuel );
		s.getTransaction().rollback();
		s.close();
	}

	@Test
	public void testIndexAndJoined() throws Exception {
		Session s = openSession();
		s.getTransaction().begin();
		Conductor cond = new Conductor();
		cond.setName( "Bob" );
		s.persist( cond );
		ProfessionalAgreement agreement = new ProfessionalAgreement();
		agreement.setExpirationDate( new Date() );
		s.persist( agreement );
		Truck truck = new Truck();
		truck.setAgreement( agreement );
		truck.setWeight( 20 );
		truck.setRegistrationNumber( "2003424" );
		truck.setYear( 2005 );
		truck.setCurrentConductor( cond );
		s.persist( truck );
		s.flush();
		s.delete( truck );
		s.delete( agreement );
		s.delete( cond );
		s.getTransaction().rollback();
		s.close();
	}

	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[]{
				Conductor.class,
				Vehicule.class,
				ProfessionalAgreement.class,
				Truck.class
		};
	}
}
