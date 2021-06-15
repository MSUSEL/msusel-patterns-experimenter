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
package org.hibernate.service.jta.platform.internal;

import java.util.Map;

import org.jboss.logging.Logger;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Environment;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.jndi.JndiHelper;
import org.hibernate.service.classloading.spi.ClassLoaderService;
import org.hibernate.service.config.spi.ConfigurationService;
import org.hibernate.service.jta.platform.spi.JtaPlatform;
import org.hibernate.service.jta.platform.spi.JtaPlatformException;
import org.hibernate.service.spi.BasicServiceInitiator;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.hibernate.transaction.TransactionManagerLookup;

/**
 * Standard initiator for the standard {@link org.hibernate.service.jta.platform.spi.JtaPlatform}
 *
 * @author Steve Ebersole
 */
public class JtaPlatformInitiator implements BasicServiceInitiator<JtaPlatform> {
	public static final JtaPlatformInitiator INSTANCE = new JtaPlatformInitiator();

	private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, JtaPlatformInitiator.class.getName());

	@Override
	public Class<JtaPlatform> getServiceInitiated() {
		return JtaPlatform.class;
	}

	@Override
	@SuppressWarnings( {"unchecked"})
	public JtaPlatform initiateService(Map configurationValues, ServiceRegistryImplementor registry) {
		final Object platform = getConfiguredPlatform( configurationValues, registry );
		if ( platform == null ) {
			return new NoJtaPlatform();
		}
		return registry.getService( ConfigurationService.class )
				.cast( JtaPlatform.class, platform );

	}

	private Object getConfiguredPlatform(Map configVales, ServiceRegistryImplementor registry) {
		Object platform = configVales.get( AvailableSettings.JTA_PLATFORM );
		if ( platform == null ) {
			final String transactionManagerLookupImplName = (String) configVales.get( Environment.TRANSACTION_MANAGER_STRATEGY );
			if ( transactionManagerLookupImplName != null ) {
                LOG.deprecatedTransactionManagerStrategy(TransactionManagerLookup.class.getName(),
                                                         Environment.TRANSACTION_MANAGER_STRATEGY,
                                                         JtaPlatform.class.getName(),
                                                         AvailableSettings.JTA_PLATFORM);
				platform = mapLegacyClasses( transactionManagerLookupImplName, configVales, registry );
                LOG.debugf("Mapped %s -> %s", transactionManagerLookupImplName, platform);
			}
		}
		return platform;
	}

	private JtaPlatform mapLegacyClasses(String tmlImplName, Map configVales, ServiceRegistryImplementor registry) {
		if ( tmlImplName == null ) {
			return null;
		}

        LOG.legacyTransactionManagerStrategy(JtaPlatform.class.getName(), AvailableSettings.JTA_PLATFORM);

		if ( "org.hibernate.transaction.BESTransactionManagerLookup".equals( tmlImplName ) ) {
			return new BorlandEnterpriseServerJtaPlatform();
		}

		if ( "org.hibernate.transaction.BTMTransactionManagerLookup".equals( tmlImplName ) ) {
			return new BitronixJtaPlatform();
		}

		if ( "org.hibernate.transaction.JBossTransactionManagerLookup".equals( tmlImplName ) ) {
			return new JBossAppServerJtaPlatform();
		}

		if ( "org.hibernate.transaction.JBossTSStandaloneTransactionManagerLookup".equals( tmlImplName ) ) {
			return new JBossStandAloneJtaPlatform();
		}

		if ( "org.hibernate.transaction.JOnASTransactionManagerLookup".equals( tmlImplName ) ) {
			return new JOnASJtaPlatform();
		}

		if ( "org.hibernate.transaction.JOTMTransactionManagerLookup".equals( tmlImplName ) ) {
			return new JOTMJtaPlatform();
		}

		if ( "org.hibernate.transaction.JRun4TransactionManagerLookup".equals( tmlImplName ) ) {
			return new JRun4JtaPlatform();
		}

		if ( "org.hibernate.transaction.OC4JTransactionManagerLookup".equals( tmlImplName ) ) {
			return new OC4JJtaPlatform();
		}

		if ( "org.hibernate.transaction.OrionTransactionManagerLookup".equals( tmlImplName ) ) {
			return new OrionJtaPlatform();
		}

		if ( "org.hibernate.transaction.ResinTransactionManagerLookup".equals( tmlImplName ) ) {
			return new ResinJtaPlatform();
		}

		if ( "org.hibernate.transaction.SunONETransactionManagerLookup".equals( tmlImplName ) ) {
			return new SunOneJtaPlatform();
		}

		if ( "org.hibernate.transaction.WeblogicTransactionManagerLookup".equals( tmlImplName ) ) {
			return new WeblogicJtaPlatform();
		}

		if ( "org.hibernate.transaction.WebSphereTransactionManagerLookup".equals( tmlImplName ) ) {
			return new WebSphereJtaPlatform();
		}

		if ( "org.hibernate.transaction.WebSphereExtendedJTATransactionLookup".equals( tmlImplName ) ) {
			return new WebSphereExtendedJtaPlatform();
		}

		try {
			TransactionManagerLookup lookup = (TransactionManagerLookup) registry.getService( ClassLoaderService.class )
					.classForName( tmlImplName )
					.newInstance();
			return new TransactionManagerLookupBridge( lookup, JndiHelper.extractJndiProperties( configVales ) );
		}
		catch ( Exception e ) {
			throw new JtaPlatformException(
					"Unable to build " + TransactionManagerLookupBridge.class.getName() + " from specified " +
							TransactionManagerLookup.class.getName() + " implementation: " +
							tmlImplName
			);
		}
	}
}
