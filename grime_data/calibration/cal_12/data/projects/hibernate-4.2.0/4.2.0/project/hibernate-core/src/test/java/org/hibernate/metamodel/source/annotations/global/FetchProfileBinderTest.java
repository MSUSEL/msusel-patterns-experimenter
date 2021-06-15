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
package org.hibernate.metamodel.source.annotations.global;

import java.util.Iterator;

import org.jboss.jandex.Index;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.hibernate.MappingException;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;
import org.hibernate.annotations.FetchProfiles;
import org.hibernate.metamodel.MetadataSources;
import org.hibernate.metamodel.source.annotations.AnnotationBindingContextImpl;
import org.hibernate.metamodel.source.annotations.JandexHelper;
import org.hibernate.metamodel.source.internal.MetadataImpl;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.service.classloading.spi.ClassLoaderService;
import org.hibernate.service.internal.StandardServiceRegistryImpl;
import org.hibernate.testing.junit4.BaseUnitTestCase;

import static junit.framework.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Hardy Ferentschik
 */
public class FetchProfileBinderTest extends BaseUnitTestCase {

	private StandardServiceRegistryImpl serviceRegistry;
	private ClassLoaderService service;
	private MetadataImpl meta;

	@Before
	public void setUp() {
		serviceRegistry = (StandardServiceRegistryImpl) new ServiceRegistryBuilder().buildServiceRegistry();
		service = serviceRegistry.getService( ClassLoaderService.class );
		meta = (MetadataImpl) new MetadataSources( serviceRegistry ).buildMetadata();
	}

	@After
	public void tearDown() {
		serviceRegistry.destroy();
	}

	@Test
	public void testSingleFetchProfile() {
		@FetchProfile(name = "foo", fetchOverrides = {
				@FetchProfile.FetchOverride(entity = Foo.class, association = "bar", mode = FetchMode.JOIN)
		})
		class Foo {
		}
		Index index = JandexHelper.indexForClass( service, Foo.class );

		FetchProfileBinder.bind( new AnnotationBindingContextImpl( meta, index ) );

		Iterator<org.hibernate.metamodel.binding.FetchProfile> mappedFetchProfiles = meta.getFetchProfiles().iterator();
		assertTrue( mappedFetchProfiles.hasNext() );
		org.hibernate.metamodel.binding.FetchProfile profile = mappedFetchProfiles.next();
		assertEquals( "Wrong fetch profile name", "foo", profile.getName() );
		org.hibernate.metamodel.binding.FetchProfile.Fetch fetch = profile.getFetches().iterator().next();
		assertEquals( "Wrong association name", "bar", fetch.getAssociation() );
		assertEquals( "Wrong association type", Foo.class.getName(), fetch.getEntity() );
	}

	@Test
	public void testFetchProfiles() {
		Index index = JandexHelper.indexForClass( service, FooBar.class );
		FetchProfileBinder.bind( new AnnotationBindingContextImpl( meta, index ) );

		Iterator<org.hibernate.metamodel.binding.FetchProfile> mappedFetchProfiles = meta.getFetchProfiles().iterator();
		assertTrue( mappedFetchProfiles.hasNext() );
		org.hibernate.metamodel.binding.FetchProfile profile = mappedFetchProfiles.next();
		assertProfiles( profile );

		assertTrue( mappedFetchProfiles.hasNext() );
		profile = mappedFetchProfiles.next();
		assertProfiles( profile );
	}

	private void assertProfiles(org.hibernate.metamodel.binding.FetchProfile profile) {
		if ( profile.getName().equals( "foobar" ) ) {
			org.hibernate.metamodel.binding.FetchProfile.Fetch fetch = profile.getFetches().iterator().next();
			assertEquals( "Wrong association name", "foobar", fetch.getAssociation() );
			assertEquals( "Wrong association type", FooBar.class.getName(), fetch.getEntity() );
		}
		else if ( profile.getName().equals( "fubar" ) ) {
			org.hibernate.metamodel.binding.FetchProfile.Fetch fetch = profile.getFetches().iterator().next();
			assertEquals( "Wrong association name", "fubar", fetch.getAssociation() );
			assertEquals( "Wrong association type", FooBar.class.getName(), fetch.getEntity() );
		}
		else {
			fail( "Wrong fetch name:" + profile.getName() );
		}
	}

	@Test(expected = MappingException.class)
	public void testNonJoinFetchThrowsException() {
		@FetchProfile(name = "foo", fetchOverrides = {
				@FetchProfile.FetchOverride(entity = Foo.class, association = "bar", mode = FetchMode.SELECT)
		})
		class Foo {
		}
		Index index = JandexHelper.indexForClass( service, Foo.class );

		FetchProfileBinder.bind( new AnnotationBindingContextImpl( meta, index ) );
	}

	@FetchProfiles( {
			@FetchProfile(name = "foobar", fetchOverrides = {
					@FetchProfile.FetchOverride(entity = FooBar.class, association = "foobar", mode = FetchMode.JOIN)
			}),
			@FetchProfile(name = "fubar", fetchOverrides = {
					@FetchProfile.FetchOverride(entity = FooBar.class, association = "fubar", mode = FetchMode.JOIN)
			})
	})
	class FooBar {
	}
}


