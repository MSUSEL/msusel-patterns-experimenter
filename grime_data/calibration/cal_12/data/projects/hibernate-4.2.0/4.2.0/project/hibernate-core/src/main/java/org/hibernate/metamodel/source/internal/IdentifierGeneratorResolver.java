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

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.NamingStrategy;
import org.hibernate.cfg.ObjectNameNormalizer;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.metamodel.binding.EntityBinding;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.service.config.spi.ConfigurationService;

/**
 * @author Gail Badner
 */
public class IdentifierGeneratorResolver {

	private final MetadataImplementor metadata;

	IdentifierGeneratorResolver(MetadataImplementor metadata) {
		this.metadata = metadata;
	}

	// IdentifierGeneratorResolver.resolve() must execute after AttributeTypeResolver.resolve()
	// to ensure that identifier type is resolved.
	@SuppressWarnings( {"unchecked"} )
	void resolve() {
		for ( EntityBinding entityBinding : metadata.getEntityBindings() ) {
			if ( entityBinding.isRoot() ) {
				Properties properties = new Properties( );
				properties.putAll(
						metadata.getServiceRegistry()
								.getService( ConfigurationService.class )
								.getSettings()
				);
				//TODO: where should these be added???
				if ( ! properties.contains( AvailableSettings.PREFER_POOLED_VALUES_LO ) ) {
					properties.put( AvailableSettings.PREFER_POOLED_VALUES_LO, "false" );
				}
				if ( ! properties.contains( PersistentIdentifierGenerator.IDENTIFIER_NORMALIZER ) ) {
					properties.put(
							PersistentIdentifierGenerator.IDENTIFIER_NORMALIZER,
							new ObjectNameNormalizerImpl( metadata )
					);
				}
				entityBinding.getHierarchyDetails().getEntityIdentifier().createIdentifierGenerator(
						metadata.getIdentifierGeneratorFactory(),
						properties
				);
			}
		}
	}

	private static class ObjectNameNormalizerImpl extends ObjectNameNormalizer implements Serializable {
		private final boolean useQuotedIdentifiersGlobally;
		private final NamingStrategy namingStrategy;

		private ObjectNameNormalizerImpl(MetadataImplementor metadata ) {
			this.useQuotedIdentifiersGlobally = metadata.isGloballyQuotedIdentifiers();
			this.namingStrategy = metadata.getNamingStrategy();
		}

		@Override
		protected boolean isUseQuotedIdentifiersGlobally() {
			return useQuotedIdentifiersGlobally;
		}

		@Override
		protected NamingStrategy getNamingStrategy() {
			return namingStrategy;
		}
	}
}
