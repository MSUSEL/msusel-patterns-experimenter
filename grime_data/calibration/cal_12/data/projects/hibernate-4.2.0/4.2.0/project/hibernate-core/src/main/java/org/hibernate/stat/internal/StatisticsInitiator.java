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
package org.hibernate.stat.internal;

import org.jboss.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.service.classloading.spi.ClassLoaderService;
import org.hibernate.service.config.spi.ConfigurationService;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.hibernate.service.spi.SessionFactoryServiceInitiator;
import org.hibernate.stat.spi.StatisticsFactory;
import org.hibernate.stat.spi.StatisticsImplementor;

/**
 * @author Steve Ebersole
 */
public class StatisticsInitiator implements SessionFactoryServiceInitiator<StatisticsImplementor> {
	private static final CoreMessageLogger LOG = Logger.getMessageLogger( CoreMessageLogger.class, StatisticsInitiator.class.getName() );

	public static final StatisticsInitiator INSTANCE = new StatisticsInitiator();

	/**
	 * Names the {@link StatisticsFactory} to use.  Recognizes both a class name as well as an instance of
	 * {@link StatisticsFactory}.
	 */
	public static final String STATS_BUILDER = "hibernate.stats.factory";

	@Override
	public Class<StatisticsImplementor> getServiceInitiated() {
		return StatisticsImplementor.class;
	}

	@Override
	public StatisticsImplementor initiateService(
			SessionFactoryImplementor sessionFactory,
			Configuration configuration,
			ServiceRegistryImplementor registry) {
		final Object configValue = configuration.getProperties().get( STATS_BUILDER );
		return initiateServiceInternal( sessionFactory, configValue, registry );
	}

	@Override
	public StatisticsImplementor initiateService(
			SessionFactoryImplementor sessionFactory,
			MetadataImplementor metadata,
			ServiceRegistryImplementor registry) {
		ConfigurationService configurationService =  registry.getService( ConfigurationService.class );
		final Object configValue = configurationService.getSetting( STATS_BUILDER, null );
		return initiateServiceInternal( sessionFactory, configValue, registry );
	}

	private StatisticsImplementor initiateServiceInternal(
			SessionFactoryImplementor sessionFactory,
			Object configValue,
			ServiceRegistryImplementor registry) {

		StatisticsFactory statisticsFactory;
		if ( configValue == null ) {
			statisticsFactory = DEFAULT_STATS_BUILDER;
		}
		else if ( StatisticsFactory.class.isInstance( configValue ) ) {
			statisticsFactory = (StatisticsFactory) configValue;
		}
		else {
			// assume it names the factory class
			final ClassLoaderService classLoaderService = registry.getService( ClassLoaderService.class );
			try {
				statisticsFactory = (StatisticsFactory) classLoaderService.classForName( configValue.toString() ).newInstance();
			}
			catch (HibernateException e) {
				throw e;
			}
			catch (Exception e) {
				throw new HibernateException(
						"Unable to instantiate specified StatisticsFactory implementation [" + configValue.toString() + "]",
						e
				);
			}
		}

		StatisticsImplementor statistics = statisticsFactory.buildStatistics( sessionFactory );
		final boolean enabled = sessionFactory.getSettings().isStatisticsEnabled();
		statistics.setStatisticsEnabled( enabled );
		LOG.debugf( "Statistics initialized [enabled=%s]", enabled );
		return statistics;
	}

	private static StatisticsFactory DEFAULT_STATS_BUILDER = new StatisticsFactory() {
		@Override
		public StatisticsImplementor buildStatistics(SessionFactoryImplementor sessionFactory) {
			return new ConcurrentStatisticsImpl( sessionFactory );
		}
	};
}
