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
package org.hibernate.ejb.test.ejb3configuration;

import java.util.Collections;
import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;

import org.junit.Test;

import org.hibernate.HibernateException;
import org.hibernate.ejb.AvailableSettings;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.ejb.packaging.PersistenceMetadata;
import org.hibernate.testing.junit4.BaseUnitTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test passing along various config settings that take objects other than strings as values.
 *
 * @author Steve Ebersole
 */
public class ConfigurationObjectSettingTest extends BaseUnitTestCase {
	@Test
	public void testContainerBootstrapSharedCacheMode() {
		// first, via the integration vars
		PersistenceUnitInfoAdapter empty = new PersistenceUnitInfoAdapter();
		{
			// as object
			Ejb3Configuration cfg = new Ejb3Configuration();
			Ejb3Configuration configured = cfg.configure(
					empty,
					Collections.singletonMap( AvailableSettings.SHARED_CACHE_MODE, SharedCacheMode.DISABLE_SELECTIVE )
			);
			assertEquals( SharedCacheMode.DISABLE_SELECTIVE.name(),configured.getProperties().get( AvailableSettings.SHARED_CACHE_MODE ) );
		}
		{
			// as string
			Ejb3Configuration cfg = new Ejb3Configuration();
			Ejb3Configuration configured = cfg.configure(
					empty,
					Collections.singletonMap( AvailableSettings.SHARED_CACHE_MODE, SharedCacheMode.DISABLE_SELECTIVE.name() )
			);
			assertEquals( SharedCacheMode.DISABLE_SELECTIVE.name(), configured.getProperties().get( AvailableSettings.SHARED_CACHE_MODE ) );
		}

		// next, via the PUI
		PersistenceUnitInfoAdapter adapter = new PersistenceUnitInfoAdapter() {
			@Override
			public SharedCacheMode getSharedCacheMode() {
				return SharedCacheMode.ENABLE_SELECTIVE;
			}
		};
		{
			Ejb3Configuration cfg = new Ejb3Configuration();
			Ejb3Configuration configured = cfg.configure( adapter, null );
			assertEquals( SharedCacheMode.ENABLE_SELECTIVE.name(), configured.getProperties().get( AvailableSettings.SHARED_CACHE_MODE ) );
		}

		// via both, integration vars should take precedence
		{
			Ejb3Configuration cfg = new Ejb3Configuration();
			Ejb3Configuration configured = cfg.configure(
					adapter,
					Collections.singletonMap( AvailableSettings.SHARED_CACHE_MODE, SharedCacheMode.DISABLE_SELECTIVE )
			);
			assertEquals( SharedCacheMode.DISABLE_SELECTIVE.name(), configured.getProperties().get( AvailableSettings.SHARED_CACHE_MODE ) );
		}
	}

	@Test
	public void testContainerBootstrapValidationMode() {
		// first, via the integration vars
		PersistenceUnitInfoAdapter empty = new PersistenceUnitInfoAdapter();
		{
			// as object
			Ejb3Configuration cfg = new Ejb3Configuration();
			Ejb3Configuration configured = cfg.configure(
					empty,
					Collections.singletonMap( AvailableSettings.VALIDATION_MODE, ValidationMode.CALLBACK )
			);
			assertEquals( ValidationMode.CALLBACK.name(), configured.getProperties().get( AvailableSettings.VALIDATION_MODE ) );
		}
		{
			// as string
			Ejb3Configuration cfg = new Ejb3Configuration();
			Ejb3Configuration configured = cfg.configure(
					empty,
					Collections.singletonMap( AvailableSettings.VALIDATION_MODE, ValidationMode.CALLBACK.name() )
			);
			assertEquals( ValidationMode.CALLBACK.name(), configured.getProperties().get( AvailableSettings.VALIDATION_MODE ) );
		}

		// next, via the PUI
		PersistenceUnitInfoAdapter adapter = new PersistenceUnitInfoAdapter() {
			@Override
			public ValidationMode getValidationMode() {
				return ValidationMode.CALLBACK;
			}
		};
		{
			Ejb3Configuration cfg = new Ejb3Configuration();
			Ejb3Configuration configured = cfg.configure( adapter, null );
			assertEquals( ValidationMode.CALLBACK.name(), configured.getProperties().get( AvailableSettings.VALIDATION_MODE ) );
		}

		// via both, integration vars should take precedence
		{
			Ejb3Configuration cfg = new Ejb3Configuration();
			Ejb3Configuration configured = cfg.configure(
					adapter,
					Collections.singletonMap( AvailableSettings.VALIDATION_MODE, ValidationMode.NONE )
			);
			assertEquals( ValidationMode.NONE.name(), configured.getProperties().get( AvailableSettings.VALIDATION_MODE ) );
		}
	}

	@Test
	public void testContainerBootstrapValidationFactory() {
		final Object token = new Object();
		PersistenceUnitInfoAdapter adapter = new PersistenceUnitInfoAdapter();
		Ejb3Configuration cfg = new Ejb3Configuration();
		try {
			cfg.configure(
					adapter,
					Collections.singletonMap( AvailableSettings.VALIDATION_FACTORY, token )
			);
			fail( "Was expecting error as token did not implement ValidatorFactory" );
		}
		catch ( HibernateException e ) {
			// probably the condition we want but unfortunately the exception is not specific
			// and the pertinent info is in a cause
		}
	}

	@Test
	public void testStandaloneBootstrapSharedCacheMode() {
		// first, via the integration vars
		PersistenceMetadata metadata = new PersistenceMetadata();
		{
			// as object
			Ejb3Configuration cfg = new Ejb3Configuration();
			Ejb3Configuration configured = cfg.configure(
					metadata,
					Collections.singletonMap( AvailableSettings.SHARED_CACHE_MODE, SharedCacheMode.DISABLE_SELECTIVE )
			);
			assertEquals( SharedCacheMode.DISABLE_SELECTIVE.name(), configured.getProperties().get( AvailableSettings.SHARED_CACHE_MODE ) );
		}
		{
			// as string
			Ejb3Configuration cfg = new Ejb3Configuration();
			Ejb3Configuration configured = cfg.configure(
					metadata,
					Collections.singletonMap( AvailableSettings.SHARED_CACHE_MODE, SharedCacheMode.DISABLE_SELECTIVE.name() )
			);
			assertEquals( SharedCacheMode.DISABLE_SELECTIVE.name(), configured.getProperties().get( AvailableSettings.SHARED_CACHE_MODE ) );
		}

		// next, via the PM
		metadata.setSharedCacheMode( SharedCacheMode.ENABLE_SELECTIVE.name() );
		{
			Ejb3Configuration cfg = new Ejb3Configuration();
			Ejb3Configuration configured = cfg.configure( metadata, null );
			assertEquals( SharedCacheMode.ENABLE_SELECTIVE.name(), configured.getProperties().get( AvailableSettings.SHARED_CACHE_MODE ) );
		}

		// via both, integration vars should take precedence
		{
			Ejb3Configuration cfg = new Ejb3Configuration();
			Ejb3Configuration configured = cfg.configure(
					metadata,
					Collections.singletonMap( AvailableSettings.SHARED_CACHE_MODE, SharedCacheMode.DISABLE_SELECTIVE )
			);
			assertEquals( SharedCacheMode.DISABLE_SELECTIVE.name(), configured.getProperties().get( AvailableSettings.SHARED_CACHE_MODE ) );
		}
	}

	@Test
	public void testStandaloneBootstrapValidationMode() {
		// first, via the integration vars
		PersistenceMetadata metadata = new PersistenceMetadata();
		{
			// as object
			Ejb3Configuration cfg = new Ejb3Configuration();
			Ejb3Configuration configured = cfg.configure(
					metadata,
					Collections.singletonMap( AvailableSettings.VALIDATION_MODE, ValidationMode.CALLBACK )
			);
			assertEquals( ValidationMode.CALLBACK.name(), configured.getProperties().get( AvailableSettings.VALIDATION_MODE ) );
		}
		{
			// as string
			Ejb3Configuration cfg = new Ejb3Configuration();
			Ejb3Configuration configured = cfg.configure(
					metadata,
					Collections.singletonMap( AvailableSettings.VALIDATION_MODE, ValidationMode.CALLBACK.name() )
			);
			assertEquals( ValidationMode.CALLBACK.name(), configured.getProperties().get( AvailableSettings.VALIDATION_MODE ) );
		}

		// next, via the PUI
		metadata.setValidationMode( ValidationMode.AUTO.name() );
		{
			Ejb3Configuration cfg = new Ejb3Configuration();
			Ejb3Configuration configured = cfg.configure( metadata, null );
			assertEquals( ValidationMode.AUTO.name(), configured.getProperties().get( AvailableSettings.VALIDATION_MODE ) );
		}

		// via both, integration vars should take precedence
		{
			Ejb3Configuration cfg = new Ejb3Configuration();
			Ejb3Configuration configured = cfg.configure(
					metadata,
					Collections.singletonMap( AvailableSettings.VALIDATION_MODE, ValidationMode.NONE )
			);
			assertEquals( ValidationMode.NONE.name(), configured.getProperties().get( AvailableSettings.VALIDATION_MODE ) );
		}
	}

	@Test
	public void testStandaloneBootstrapValidationFactory() {
		final Object token = new Object();
		PersistenceMetadata metadata = new PersistenceMetadata();
		Ejb3Configuration cfg = new Ejb3Configuration();
		try {
			cfg.configure(
					metadata,
					Collections.singletonMap( AvailableSettings.VALIDATION_FACTORY, token )
			);
			fail( "Was expecting error as token did not implement ValidatorFactory" );
		}
		catch ( HibernateException e ) {
			// probably the condition we want but unfortunately the exception is not specific
			// and the pertinent info is in a cause
		}
	}
}
