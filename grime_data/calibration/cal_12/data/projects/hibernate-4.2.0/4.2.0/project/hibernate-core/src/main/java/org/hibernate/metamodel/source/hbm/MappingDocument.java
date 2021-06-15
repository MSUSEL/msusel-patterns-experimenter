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
package org.hibernate.metamodel.source.hbm;

import java.util.List;

import org.hibernate.cfg.NamingStrategy;
import org.hibernate.internal.jaxb.JaxbRoot;
import org.hibernate.internal.jaxb.Origin;
import org.hibernate.internal.jaxb.mapping.hbm.EntityElement;
import org.hibernate.internal.jaxb.mapping.hbm.JaxbFetchProfileElement;
import org.hibernate.internal.jaxb.mapping.hbm.JaxbHibernateMapping;
import org.hibernate.internal.util.ValueHolder;
import org.hibernate.metamodel.domain.Type;
import org.hibernate.metamodel.source.MappingDefaults;
import org.hibernate.metamodel.source.MetaAttributeContext;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.metamodel.source.internal.OverriddenMappingDefaults;
import org.hibernate.service.ServiceRegistry;

/**
 * Aggregates together information about a mapping document.
 * 
 * @author Steve Ebersole
 */
public class MappingDocument {
	private final JaxbRoot<JaxbHibernateMapping> hbmJaxbRoot;
	private final LocalBindingContextImpl mappingLocalBindingContext;

	public MappingDocument(JaxbRoot<JaxbHibernateMapping> hbmJaxbRoot, MetadataImplementor metadata) {
		this.hbmJaxbRoot = hbmJaxbRoot;
		this.mappingLocalBindingContext = new LocalBindingContextImpl( metadata );

	}

	public JaxbHibernateMapping getMappingRoot() {
		return hbmJaxbRoot.getRoot();
	}

	public Origin getOrigin() {
		return hbmJaxbRoot.getOrigin();
	}

	public JaxbRoot<JaxbHibernateMapping> getJaxbRoot() {
		return hbmJaxbRoot;
	}

	public HbmBindingContext getMappingLocalBindingContext() {
		return mappingLocalBindingContext;
	}

	private class LocalBindingContextImpl implements HbmBindingContext {
		private final MetadataImplementor metadata;
		private final MappingDefaults localMappingDefaults;
		private final MetaAttributeContext metaAttributeContext;

		private LocalBindingContextImpl(MetadataImplementor metadata) {
			this.metadata = metadata;
			this.localMappingDefaults = new OverriddenMappingDefaults(
					metadata.getMappingDefaults(),
					hbmJaxbRoot.getRoot().getPackage(),
					hbmJaxbRoot.getRoot().getSchema(),
					hbmJaxbRoot.getRoot().getCatalog(),
					null,
					null,
					hbmJaxbRoot.getRoot().getDefaultCascade(),
					hbmJaxbRoot.getRoot().getDefaultAccess(),
					hbmJaxbRoot.getRoot().isDefaultLazy()
			);
			if ( hbmJaxbRoot.getRoot().getMeta() == null || hbmJaxbRoot.getRoot().getMeta().isEmpty() ) {
				this.metaAttributeContext = new MetaAttributeContext( metadata.getGlobalMetaAttributeContext() );
			}
			else {
				this.metaAttributeContext = Helper.extractMetaAttributeContext(
						hbmJaxbRoot.getRoot().getMeta(),
						true,
						metadata.getGlobalMetaAttributeContext()
				);
			}
		}

		@Override
		public ServiceRegistry getServiceRegistry() {
			return metadata.getServiceRegistry();
		}

		@Override
		public NamingStrategy getNamingStrategy() {
			return metadata.getNamingStrategy();
		}

		@Override
		public MappingDefaults getMappingDefaults() {
			return localMappingDefaults;
		}

		@Override
		public MetadataImplementor getMetadataImplementor() {
			return metadata;
		}

		@Override
		public <T> Class<T> locateClassByName(String name) {
			return metadata.locateClassByName( name );
		}

		@Override
		public Type makeJavaType(String className) {
			return metadata.makeJavaType( className );
		}

		@Override
		public ValueHolder<Class<?>> makeClassReference(String className) {
			return metadata.makeClassReference( className );
		}

		@Override
		public boolean isAutoImport() {
			return hbmJaxbRoot.getRoot().isAutoImport();
		}

		@Override
		public MetaAttributeContext getMetaAttributeContext() {
			return metaAttributeContext;
		}

		@Override
		public Origin getOrigin() {
			return hbmJaxbRoot.getOrigin();
		}

		@Override
		public String qualifyClassName(String unqualifiedName) {
			return Helper.qualifyIfNeeded( unqualifiedName, getMappingDefaults().getPackageName() );
		}

		@Override
		public String determineEntityName(EntityElement entityElement) {
			return Helper.determineEntityName( entityElement, getMappingDefaults().getPackageName() );
		}

		@Override
		public boolean isGloballyQuotedIdentifiers() {
			return metadata.isGloballyQuotedIdentifiers();
		}

		@Override
		public void processFetchProfiles(List<JaxbFetchProfileElement> fetchProfiles, String containingEntityName) {
			// todo : this really needs to not be part of the context
		}
	}
}
