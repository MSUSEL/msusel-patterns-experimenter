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
package org.hibernate.test.annotations.beanvalidation;

import java.math.BigDecimal;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Emmanuel Bernard
 */
public class HibernateTraversableResolverTest extends BaseCoreFunctionalTestCase {
	@Test
	public void testNonLazyAssocFieldWithConstraintsFailureExpected() {
		Session s = openSession();
		Transaction tx = s.beginTransaction();

		Screen screen = new Screen();
		screen.setPowerSupply( null );
		try {
			s.persist( screen );
			s.flush();
			fail( "@NotNull on a non lazy association is not evaluated" );
		}
		catch ( ConstraintViolationException e ) {
			assertEquals( 1, e.getConstraintViolations().size() );
		}

		tx.rollback();
		s.close();
	}

	@Test
	public void testEmbedded() {
		Session s = openSession();
		Transaction tx = s.beginTransaction();

		Screen screen = new Screen();
		PowerSupply ps = new PowerSupply();
		screen.setPowerSupply( ps );
		Button button = new Button();
		button.setName( null );
		button.setSize( 3 );
		screen.setStopButton( button );
		try {
			s.persist( screen );
			s.flush();
			fail( "@NotNull on embedded property is not evaluated" );
		}
		catch ( ConstraintViolationException e ) {
			assertEquals( 1, e.getConstraintViolations().size() );
			ConstraintViolation<?> cv = e.getConstraintViolations().iterator().next();
			assertEquals( Screen.class, cv.getRootBeanClass() );
			// toString works since hibernate validator's Path implementation works accordingly. Should do a Path comparison though
			assertEquals( "stopButton.name", cv.getPropertyPath().toString() );
		}

		tx.rollback();
		s.close();
	}

	@Test
	public void testToOneAssocNotValidated() {
		Session s = openSession();
		Transaction tx = s.beginTransaction();

		Screen screen = new Screen();
		PowerSupply ps = new PowerSupply();
		ps.setPosition( "1" );
		ps.setPower( new BigDecimal( 350 ) );
		screen.setPowerSupply( ps );
		try {
			s.persist( screen );
			s.flush();
			fail( "Associated objects should not be validated" );
		}
		catch ( ConstraintViolationException e ) {
			assertEquals( 1, e.getConstraintViolations().size() );
			final ConstraintViolation constraintViolation = e.getConstraintViolations().iterator().next();
			assertEquals( PowerSupply.class, constraintViolation.getRootBeanClass() );
		}

		tx.rollback();
		s.close();
	}

	@Test
	public void testCollectionAssocNotValidated() {
		Session s = openSession();
		Transaction tx = s.beginTransaction();

		Screen screen = new Screen();
		screen.setStopButton( new Button() );
		screen.getStopButton().setName( "STOOOOOP" );
		PowerSupply ps = new PowerSupply();
		screen.setPowerSupply( ps );
		Color c = new Color();
		c.setName( "Blue" );
		s.persist( c );
		c.setName( null );
		screen.getDisplayColors().add( c );
		try {
			s.persist( screen );
			s.flush();
			fail( "Associated objects should not be validated" );
		}
		catch ( ConstraintViolationException e ) {
			assertEquals( 1, e.getConstraintViolations().size() );
			final ConstraintViolation constraintViolation = e.getConstraintViolations().iterator().next();
			assertEquals( Color.class, constraintViolation.getRootBeanClass() );
		}

		tx.rollback();
		s.close();
	}

	@Test
	public void testEmbeddedCollection() {
		Session s = openSession();
		Transaction tx = s.beginTransaction();

		Screen screen = new Screen();
		PowerSupply ps = new PowerSupply();
		screen.setPowerSupply( ps );
		DisplayConnector conn = new DisplayConnector();
		conn.setNumber( 0 );
		screen.getConnectors().add( conn );
		try {
			s.persist( screen );
			s.flush();
			fail( "Collection of embedded objects should be validated" );
		}
		catch ( ConstraintViolationException e ) {
			assertEquals( 1, e.getConstraintViolations().size() );
			final ConstraintViolation constraintViolation = e.getConstraintViolations().iterator().next();
			assertEquals( Screen.class, constraintViolation.getRootBeanClass() );
			// toString works since hibernate validator's Path implementation works accordingly. Should do a Path comparison though
			assertEquals( "connectors[].number", constraintViolation.getPropertyPath().toString() );
		}

		tx.rollback();
		s.close();
	}

	@Test
	public void testAssocInEmbeddedNotValidated() {
		Session s = openSession();
		Transaction tx = s.beginTransaction();

		Screen screen = new Screen();
		screen.setStopButton( new Button() );
		screen.getStopButton().setName( "STOOOOOP" );
		PowerSupply ps = new PowerSupply();
		screen.setPowerSupply( ps );
		DisplayConnector conn = new DisplayConnector();
		conn.setNumber( 1 );
		screen.getConnectors().add( conn );
		final Display display = new Display();
		display.setBrand( "dell" );
		conn.setDisplay( display );
		s.persist( display );
		s.flush();
		try {
			display.setBrand( null );
			s.persist( screen );
			s.flush();
			fail( "Collection of embedded objects should be validated" );
		}
		catch ( ConstraintViolationException e ) {
			assertEquals( 1, e.getConstraintViolations().size() );
			final ConstraintViolation constraintViolation = e.getConstraintViolations().iterator().next();
			assertEquals( Display.class, constraintViolation.getRootBeanClass() );
		}

		tx.rollback();
		s.close();
	}

	@Override
	protected void configure(Configuration cfg) {
		super.configure( cfg );
		cfg.setProperty( "hibernate.validator.autoregister_listeners", "false" );
	}

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] {
				Button.class,
				Color.class,
				Display.class,
				DisplayConnector.class,
				PowerSupply.class,
				Screen.class
		};
	}
}
