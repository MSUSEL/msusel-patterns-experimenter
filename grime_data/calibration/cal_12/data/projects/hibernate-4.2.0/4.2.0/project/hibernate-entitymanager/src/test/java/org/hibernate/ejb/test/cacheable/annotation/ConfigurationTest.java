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
package org.hibernate.ejb.test.cacheable.annotation;

import java.util.Properties;
import javax.persistence.SharedCacheMode;

import org.junit.Test;

import org.hibernate.cache.internal.NoCachingRegionFactory;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cfg.Environment;
import org.hibernate.ejb.AvailableSettings;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.testing.junit4.BaseUnitTestCase;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author Steve Ebersole
 */
public class ConfigurationTest extends BaseUnitTestCase {
	@Test
	public void testSharedCacheModeNone() {
		Ejb3Configuration config = buildConfiguration( SharedCacheMode.NONE );

		PersistentClass pc = config.getClassMapping( ExplicitlyCacheableEntity.class.getName() );
		assertNull( pc.getCacheConcurrencyStrategy() );

		pc = config.getClassMapping( ExplicitlyNonCacheableEntity.class.getName() );
		assertNull( pc.getCacheConcurrencyStrategy() );

		pc = config.getClassMapping( NoCacheableAnnotationEntity.class.getName() );
		assertNull( pc.getCacheConcurrencyStrategy() );
	}

	@Test
	public void testSharedCacheModeUnspecified() {
		Ejb3Configuration config = buildConfiguration( SharedCacheMode.UNSPECIFIED );

		PersistentClass pc = config.getClassMapping( ExplicitlyCacheableEntity.class.getName() );
		assertNull( pc.getCacheConcurrencyStrategy() );

		pc = config.getClassMapping( ExplicitlyNonCacheableEntity.class.getName() );
		assertNull( pc.getCacheConcurrencyStrategy() );

		pc = config.getClassMapping( NoCacheableAnnotationEntity.class.getName() );
		assertNull( pc.getCacheConcurrencyStrategy() );
	}

	@Test
	public void testSharedCacheModeAll() {
		Ejb3Configuration config = buildConfiguration( SharedCacheMode.ALL );

		PersistentClass pc = config.getClassMapping( ExplicitlyCacheableEntity.class.getName() );
		assertNotNull( pc.getCacheConcurrencyStrategy() );

		pc = config.getClassMapping( ExplicitlyNonCacheableEntity.class.getName() );
		assertNotNull( pc.getCacheConcurrencyStrategy() );

		pc = config.getClassMapping( NoCacheableAnnotationEntity.class.getName() );
		assertNotNull( pc.getCacheConcurrencyStrategy() );
	}

	@Test
	public void testSharedCacheModeEnable() {
		Ejb3Configuration config = buildConfiguration( SharedCacheMode.ENABLE_SELECTIVE );

		PersistentClass pc = config.getClassMapping( ExplicitlyCacheableEntity.class.getName() );
		assertNotNull( pc.getCacheConcurrencyStrategy() );

		pc = config.getClassMapping( ExplicitlyNonCacheableEntity.class.getName() );
		assertNull( pc.getCacheConcurrencyStrategy() );

		pc = config.getClassMapping( NoCacheableAnnotationEntity.class.getName() );
		assertNull( pc.getCacheConcurrencyStrategy() );
	}

	@Test
	public void testSharedCacheModeDisable() {
		Ejb3Configuration config = buildConfiguration( SharedCacheMode.DISABLE_SELECTIVE );

		PersistentClass pc = config.getClassMapping( ExplicitlyCacheableEntity.class.getName() );
		assertNotNull( pc.getCacheConcurrencyStrategy() );

		pc = config.getClassMapping( ExplicitlyNonCacheableEntity.class.getName() );
		assertNull( pc.getCacheConcurrencyStrategy() );

		pc = config.getClassMapping( NoCacheableAnnotationEntity.class.getName() );
		assertNotNull( pc.getCacheConcurrencyStrategy() );
	}

	private Ejb3Configuration buildConfiguration(SharedCacheMode mode) {
		Properties properties = new Properties();
		properties.put( AvailableSettings.SHARED_CACHE_MODE, mode );
		properties.put( Environment.CACHE_REGION_FACTORY, CustomRegionFactory.class.getName() );
		Ejb3Configuration config = new Ejb3Configuration();
		config.setProperties( properties );
		config.addAnnotatedClass( ExplicitlyCacheableEntity.class );
		config.addAnnotatedClass( ExplicitlyNonCacheableEntity.class );
		config.addAnnotatedClass( NoCacheableAnnotationEntity.class );
		config.buildMappings();
		return config;
	}

	public static class CustomRegionFactory extends NoCachingRegionFactory {
		public CustomRegionFactory() {
		}

		@Override
		public AccessType getDefaultAccessType() {
			return AccessType.READ_WRITE;
		}
	}
}
