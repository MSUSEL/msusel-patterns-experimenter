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
package org.hibernate.test.annotations.inheritance.discriminatoroptions;

import org.junit.Test;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.RootClass;
import org.hibernate.testing.junit4.BaseUnitTestCase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for the @DiscriminatorOptions annotations.
 *
 * @author Hardy Ferentschik
 */
public class DiscriminatorOptionsTest extends BaseUnitTestCase {
	@Test
	public void testNonDefaultOptions() throws Exception {
		Configuration configuration = new Configuration();
		configuration.addAnnotatedClass( BaseClass.class );
		configuration.addAnnotatedClass( SubClass.class );
		configuration.buildMappings();
		PersistentClass persistentClass = configuration.getClassMapping( BaseClass.class.getName() );
		assertNotNull( persistentClass );
		assertTrue( persistentClass instanceof RootClass );

		RootClass root = ( RootClass ) persistentClass;
		assertTrue( "Discriminator should be forced", root.isForceDiscriminator() );
		assertFalse( "Discriminator should not be insertable", root.isDiscriminatorInsertable() );
	}

	@Test
	public void testBaseline() throws Exception {
		Configuration configuration = new Configuration()
				.addAnnotatedClass( BaseClass2.class )
				.addAnnotatedClass( SubClass2.class );
		configuration.buildMappings();
		PersistentClass persistentClass = configuration.getClassMapping( BaseClass2.class.getName() );
		assertNotNull( persistentClass );
		assertTrue( persistentClass instanceof RootClass );

		RootClass root = ( RootClass ) persistentClass;
		assertFalse( "Discriminator should not be forced by default", root.isForceDiscriminator() );
	}

	@Test
	public void testPropertyBasedDiscriminatorForcing() throws Exception {
		Configuration configuration = new Configuration()
				.setProperty( AvailableSettings.FORCE_DISCRIMINATOR_IN_SELECTS_BY_DEFAULT, "true" )
				.addAnnotatedClass( BaseClass2.class )
				.addAnnotatedClass( SubClass2.class );
		configuration.buildMappings();
		PersistentClass persistentClass = configuration.getClassMapping( BaseClass2.class.getName() );
		assertNotNull( persistentClass );
		assertTrue( persistentClass instanceof RootClass );

		RootClass root = ( RootClass ) persistentClass;
		assertTrue( "Discriminator should be forced by property", root.isForceDiscriminator() );
	}
}
