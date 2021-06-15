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

import javax.persistence.SharedCacheMode;

import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.EJB3NamingStrategy;
import org.hibernate.cfg.NamingStrategy;
import org.hibernate.metamodel.Metadata;
import org.hibernate.metamodel.MetadataBuilder;
import org.hibernate.metamodel.MetadataSourceProcessingOrder;
import org.hibernate.metamodel.MetadataSources;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.config.spi.ConfigurationService;

/**
 * @author Steve Ebersole
 */
public class MetadataBuilderImpl implements MetadataBuilder {
	private final MetadataSources sources;
	private final OptionsImpl options;

	public MetadataBuilderImpl(MetadataSources sources) {
		this.sources = sources;
		this.options = new OptionsImpl( sources.getServiceRegistry() );
	}

	@Override
	public MetadataBuilder with(NamingStrategy namingStrategy) {
		this.options.namingStrategy = namingStrategy;
		return this;
	}

	@Override
	public MetadataBuilder with(MetadataSourceProcessingOrder metadataSourceProcessingOrder) {
		this.options.metadataSourceProcessingOrder = metadataSourceProcessingOrder;
		return this;
	}

	@Override
	public MetadataBuilder with(SharedCacheMode sharedCacheMode) {
		this.options.sharedCacheMode = sharedCacheMode;
		return this;
	}

	@Override
	public MetadataBuilder with(AccessType accessType) {
		this.options.defaultCacheAccessType = accessType;
		return this;
	}

	@Override
	public MetadataBuilder withNewIdentifierGeneratorsEnabled(boolean enabled) {
		this.options.useNewIdentifierGenerators = enabled;
		return this;
	}

	@Override
	public Metadata buildMetadata() {
		return new MetadataImpl( sources, options );
	}

	private static class OptionsImpl implements Metadata.Options {
		private MetadataSourceProcessingOrder metadataSourceProcessingOrder = MetadataSourceProcessingOrder.HBM_FIRST;
		private NamingStrategy namingStrategy = EJB3NamingStrategy.INSTANCE;
		private SharedCacheMode sharedCacheMode = SharedCacheMode.ENABLE_SELECTIVE;
		private AccessType defaultCacheAccessType;
        private boolean useNewIdentifierGenerators;
        private boolean globallyQuotedIdentifiers;
		private String defaultSchemaName;
		private String defaultCatalogName;

		public OptionsImpl(ServiceRegistry serviceRegistry) {
			ConfigurationService configService = serviceRegistry.getService( ConfigurationService.class );

			// cache access type
			defaultCacheAccessType = configService.getSetting(
					AvailableSettings.DEFAULT_CACHE_CONCURRENCY_STRATEGY,
					new ConfigurationService.Converter<AccessType>() {
						@Override
						public AccessType convert(Object value) {
							return AccessType.fromExternalName( value.toString() );
						}
					}
			);

			useNewIdentifierGenerators = configService.getSetting(
					AvailableSettings.USE_NEW_ID_GENERATOR_MAPPINGS,
					new ConfigurationService.Converter<Boolean>() {
						@Override
						public Boolean convert(Object value) {
							return Boolean.parseBoolean( value.toString() );
						}
					},
					false
			);

			defaultSchemaName = configService.getSetting(
					AvailableSettings.DEFAULT_SCHEMA,
					new ConfigurationService.Converter<String>() {
						@Override
						public String convert(Object value) {
							return value.toString();
						}
					},
					null
			);

			defaultCatalogName = configService.getSetting(
					AvailableSettings.DEFAULT_CATALOG,
					new ConfigurationService.Converter<String>() {
						@Override
						public String convert(Object value) {
							return value.toString();
						}
					},
					null
			);

            globallyQuotedIdentifiers = configService.getSetting(
                    AvailableSettings.GLOBALLY_QUOTED_IDENTIFIERS,
                    new ConfigurationService.Converter<Boolean>() {
                        @Override
                        public Boolean convert(Object value) {
                            return Boolean.parseBoolean( value.toString() );
                        }
                    },
                    false
            );
		}


		@Override
		public MetadataSourceProcessingOrder getMetadataSourceProcessingOrder() {
			return metadataSourceProcessingOrder;
		}

		@Override
		public NamingStrategy getNamingStrategy() {
			return namingStrategy;
		}

		@Override
		public AccessType getDefaultAccessType() {
			return defaultCacheAccessType;
		}

		@Override
		public SharedCacheMode getSharedCacheMode() {
			return sharedCacheMode;
		}

		@Override
        public boolean useNewIdentifierGenerators() {
            return useNewIdentifierGenerators;
        }

        @Override
        public boolean isGloballyQuotedIdentifiers() {
            return globallyQuotedIdentifiers;
        }

        @Override
		public String getDefaultSchemaName() {
			return defaultSchemaName;
		}

		@Override
		public String getDefaultCatalogName() {
			return defaultCatalogName;
		}
	}
}
