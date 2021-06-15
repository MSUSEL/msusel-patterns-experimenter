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
package org.hibernate.metamodel.source.internal;

import java.util.Iterator;

import org.junit.Test;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.metamodel.Metadata;
import org.hibernate.metamodel.MetadataSources;
import org.hibernate.metamodel.SessionFactoryBuilder;
import org.hibernate.metamodel.binding.FetchProfile;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.testing.junit4.BaseUnitTestCase;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * @author Hardy Ferentschik
 */
public class MetadataImplTest extends BaseUnitTestCase {

	@Test(expected = IllegalArgumentException.class)
	public void testAddingNullClass() {
		MetadataSources sources = new MetadataSources( new ServiceRegistryBuilder().buildServiceRegistry() );
		sources.addClass( null );
		sources.buildMetadata();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddingNullPackageName() {
		MetadataSources sources = new MetadataSources( new ServiceRegistryBuilder().buildServiceRegistry() );
		sources.addPackage( null );
		sources.buildMetadata();
	}

	@Test(expected = HibernateException.class)
	public void testAddingNonExistingPackageName() {
		MetadataSources sources = new MetadataSources( new ServiceRegistryBuilder().buildServiceRegistry() );
		sources.addPackage( "not.a.package" );
		sources.buildMetadata();
	}

	@Test
	public void testAddingPackageName() {
		MetadataSources sources = new MetadataSources( new ServiceRegistryBuilder().buildServiceRegistry() );
		sources.addPackage( "org.hibernate.metamodel.source.internal" );
		MetadataImpl metadata = (MetadataImpl) sources.buildMetadata();

		assertFetchProfile( metadata );
	}

	@Test
	public void testAddingPackageNameWithTrailingDot() {
		MetadataSources sources = new MetadataSources( new ServiceRegistryBuilder().buildServiceRegistry() );
		sources.addPackage( "org.hibernate.metamodel.source.internal." );
		MetadataImpl metadata = (MetadataImpl) sources.buildMetadata();

		assertFetchProfile( metadata );
	}

	@Test
	public void testGettingSessionFactoryBuilder() {
		MetadataSources sources = new MetadataSources( new ServiceRegistryBuilder().buildServiceRegistry() );
		Metadata metadata = sources.buildMetadata();

		SessionFactoryBuilder sessionFactoryBuilder = metadata.getSessionFactoryBuilder();
		assertNotNull( sessionFactoryBuilder );
		assertTrue( SessionFactoryBuilderImpl.class.isInstance( sessionFactoryBuilder ) );

		SessionFactory sessionFactory = metadata.buildSessionFactory();
		assertNotNull( sessionFactory );
	}

	private void assertFetchProfile(MetadataImpl metadata) {
		Iterator<FetchProfile> profiles = metadata.getFetchProfiles().iterator();
		assertTrue( profiles.hasNext() );
		FetchProfile profile = profiles.next();
		assertEquals( "wrong profile name", "package-configured-profile", profile.getName() );
		assertFalse( profiles.hasNext() );
	}
}


