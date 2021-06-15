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
package org.hibernate.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jboss.logging.Logger;

import org.hibernate.cfg.Environment;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.integrator.spi.IntegratorService;
import org.hibernate.integrator.spi.ServiceContributingIntegrator;
import org.hibernate.internal.jaxb.Origin;
import org.hibernate.internal.jaxb.SourceType;
import org.hibernate.internal.jaxb.cfg.JaxbHibernateConfiguration;
import org.hibernate.internal.util.ValueHolder;
import org.hibernate.internal.util.config.ConfigurationException;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.classloading.spi.ClassLoaderService;
import org.hibernate.service.internal.BootstrapServiceRegistryImpl;
import org.hibernate.service.internal.JaxbProcessor;
import org.hibernate.service.internal.ProvidedService;
import org.hibernate.service.internal.StandardServiceRegistryImpl;
import org.hibernate.service.spi.BasicServiceInitiator;

/**
 * Builder for standard {@link ServiceRegistry} instances.
 *
 * @author Steve Ebersole
 * 
 * @see StandardServiceRegistryImpl
 * @see BootstrapServiceRegistryBuilder
 */
public class ServiceRegistryBuilder {
	private static final Logger log = Logger.getLogger( ServiceRegistryBuilder.class );

	public static final String DEFAULT_CFG_RESOURCE_NAME = "hibernate.cfg.xml";

	private final Map settings;
	private final List<BasicServiceInitiator> initiators = standardInitiatorList();
	private final List<ProvidedService> providedServices = new ArrayList<ProvidedService>();

	private final BootstrapServiceRegistry bootstrapServiceRegistry;

	/**
	 * Create a default builder
	 */
	public ServiceRegistryBuilder() {
		this( new BootstrapServiceRegistryImpl() );
	}

	/**
	 * Create a builder with the specified bootstrap services.
	 *
	 * @param bootstrapServiceRegistry Provided bootstrap registry to use.
	 */
	public ServiceRegistryBuilder(BootstrapServiceRegistry bootstrapServiceRegistry) {
		this.settings = Environment.getProperties();
		this.bootstrapServiceRegistry = bootstrapServiceRegistry;
	}

	/**
	 * Used from the {@link #initiators} variable initializer
	 *
	 * @return List of standard initiators
	 */
	private static List<BasicServiceInitiator> standardInitiatorList() {
		final List<BasicServiceInitiator> initiators = new ArrayList<BasicServiceInitiator>();
		initiators.addAll( StandardServiceInitiators.LIST );
		return initiators;
	}

	/**
	 * Read settings from a {@link Properties} file.  Differs from {@link #configure()} and {@link #configure(String)}
	 * in that here we read a {@link Properties} file while for {@link #configure} we read the XML variant.
	 *
	 * @param resourceName The name by which to perform a resource look up for the properties file.
	 *
	 * @return this, for method chaining
	 *
	 * @see #configure()
	 * @see #configure(String)
	 */
	@SuppressWarnings( {"unchecked"})
	public ServiceRegistryBuilder loadProperties(String resourceName) {
		InputStream stream = bootstrapServiceRegistry.getService( ClassLoaderService.class ).locateResourceStream( resourceName );
		try {
			Properties properties = new Properties();
			properties.load( stream );
			settings.putAll( properties );
		}
		catch (IOException e) {
			throw new ConfigurationException( "Unable to apply settings from properties file [" + resourceName + "]", e );
		}
		finally {
			try {
				stream.close();
			}
			catch (IOException e) {
				log.debug(
						String.format( "Unable to close properties file [%s] stream", resourceName ),
						e
				);
			}
		}

		return this;
	}

	/**
	 * Read setting information from an XML file using the standard resource location
	 *
	 * @return this, for method chaining
	 *
	 * @see #DEFAULT_CFG_RESOURCE_NAME
	 * @see #configure(String)
	 * @see #loadProperties(String)
	 */
	public ServiceRegistryBuilder configure() {
		return configure( DEFAULT_CFG_RESOURCE_NAME );
	}

	/**
	 * Read setting information from an XML file using the named resource location
	 *
	 * @param resourceName The named resource
	 *
	 * @return this, for method chaining
	 *
	 * @see #loadProperties(String)
	 */
	@SuppressWarnings( {"unchecked"})
	public ServiceRegistryBuilder configure(String resourceName) {
		InputStream stream = bootstrapServiceRegistry.getService( ClassLoaderService.class ).locateResourceStream( resourceName );
		JaxbHibernateConfiguration configurationElement = jaxbProcessorHolder.getValue().unmarshal(
				stream,
				new Origin( SourceType.RESOURCE, resourceName )
		);
		for ( JaxbHibernateConfiguration.JaxbSessionFactory.JaxbProperty xmlProperty : configurationElement.getSessionFactory().getProperty() ) {
			settings.put( xmlProperty.getName(), xmlProperty.getValue() );
		}

		return this;
	}

	private ValueHolder<JaxbProcessor> jaxbProcessorHolder = new ValueHolder<JaxbProcessor>(
			new ValueHolder.DeferredInitializer<JaxbProcessor>() {
				@Override
				public JaxbProcessor initialize() {
					return new JaxbProcessor( bootstrapServiceRegistry.getService( ClassLoaderService.class ) );
				}
			}
	);

	/**
	 * Apply a setting value
	 *
	 * @param settingName The name of the setting
	 * @param value The value to use.
	 *
	 * @return this, for method chaining
	 */
	@SuppressWarnings( {"unchecked", "UnusedDeclaration"})
	public ServiceRegistryBuilder applySetting(String settingName, Object value) {
		settings.put( settingName, value );
		return this;
	}

	/**
	 * Apply a groups of setting values
	 *
	 * @param settings The incoming settings to apply
	 *
	 * @return this, for method chaining
	 */
	@SuppressWarnings( {"unchecked", "UnusedDeclaration"})
	public ServiceRegistryBuilder applySettings(Map settings) {
		this.settings.putAll( settings );
		return this;
	}

	/**
	 * Adds a service initiator.
	 *
	 * @param initiator The initiator to be added
	 *
	 * @return this, for method chaining
	 */
	@SuppressWarnings( {"UnusedDeclaration"})
	public ServiceRegistryBuilder addInitiator(BasicServiceInitiator initiator) {
		initiators.add( initiator );
		return this;
	}

	/**
	 * Adds a user-provided service
	 *
	 * @param serviceRole The role of the service being added
	 * @param service The service implementation
	 *
	 * @return this, for method chaining
	 */
	@SuppressWarnings( {"unchecked"})
	public ServiceRegistryBuilder addService(final Class serviceRole, final Service service) {
		providedServices.add( new ProvidedService( serviceRole, service ) );
		return this;
	}

	/**
	 * Build the service registry accounting for all settings and service initiators and services.
	 *
	 * @return The built service registry
	 */
	public ServiceRegistry buildServiceRegistry() {
		Map<?,?> settingsCopy = new HashMap();
		settingsCopy.putAll( settings );
		Environment.verifyProperties( settingsCopy );
		ConfigurationHelper.resolvePlaceHolders( settingsCopy );

		for ( Integrator integrator : bootstrapServiceRegistry.getService( IntegratorService.class ).getIntegrators() ) {
			if ( ServiceContributingIntegrator.class.isInstance( integrator ) ) {
				ServiceContributingIntegrator.class.cast( integrator ).prepareServices( this );
			}
		}

		return new StandardServiceRegistryImpl( bootstrapServiceRegistry, initiators, providedServices, settingsCopy );
	}

	/**
	 * Destroy a service registry.  Applications should only destroy registries they have explicitly created.
	 *
	 * @param serviceRegistry The registry to be closed.
	 */
	public static void destroy(ServiceRegistry serviceRegistry) {
		( (StandardServiceRegistryImpl) serviceRegistry ).destroy();
	}
}
