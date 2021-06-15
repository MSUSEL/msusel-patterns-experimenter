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
package org.hibernate.metamodel.source.annotations.xml.mocker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jboss.jandex.Index;
import org.jboss.logging.Logger;

import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.jaxb.mapping.orm.JaxbAccessType;
import org.hibernate.internal.jaxb.mapping.orm.JaxbEmbeddable;
import org.hibernate.internal.jaxb.mapping.orm.JaxbEntity;
import org.hibernate.internal.jaxb.mapping.orm.JaxbEntityMappings;
import org.hibernate.internal.jaxb.mapping.orm.JaxbMappedSuperclass;
import org.hibernate.internal.jaxb.mapping.orm.JaxbPersistenceUnitDefaults;
import org.hibernate.internal.jaxb.mapping.orm.JaxbPersistenceUnitMetadata;
import org.hibernate.service.ServiceRegistry;

/**
 * Parse all {@link org.hibernate.internal.jaxb.mapping.orm.JaxbEntityMappings} generated from orm.xml.
 *
 * @author Strong Liu
 */
public class EntityMappingsMocker {
	private static final CoreMessageLogger LOG = Logger.getMessageLogger(
			CoreMessageLogger.class,
			EntityMappingsMocker.class.getName()
	);
	private final List<JaxbEntityMappings> entityMappingsList;
	/**
	 * Default configuration defined in Persistence Metadata Unit, one or zero per Persistence Unit.
	 */
	private Default globalDefaults;
	private final IndexBuilder indexBuilder;
	private final GlobalAnnotations globalAnnotations;

	public EntityMappingsMocker(List<JaxbEntityMappings> entityMappingsList, Index index, ServiceRegistry serviceRegistry) {
		this.entityMappingsList = entityMappingsList;
		this.indexBuilder = new IndexBuilder( index, serviceRegistry );
		this.globalAnnotations = new GlobalAnnotations();
	}

	/**
	 * Create new {@link Index} with mocking JPA annotations from {@link org.hibernate.internal.jaxb.mapping.orm.JaxbEntityMappings} and merge them with existing {@link Index}
	 *
	 * @return new {@link Index}
	 */
	public Index mockNewIndex() {
		processPersistenceUnitMetadata( entityMappingsList );
		processEntityMappings( entityMappingsList );
		processGlobalAnnotations();
		return indexBuilder.build( globalDefaults );
	}

	/**
	 * processing PersistenceUnitMetadata, there should be only one PersistenceUnitMetadata in all mapping xml files.
	 */
	private void processPersistenceUnitMetadata(List<JaxbEntityMappings> entityMappingsList) {
		for ( JaxbEntityMappings entityMappings : entityMappingsList ) {
			//we have to iterate entityMappingsList first to find persistence-unit-metadata
			JaxbPersistenceUnitMetadata pum = entityMappings.getPersistenceUnitMetadata();
			if ( globalDefaults != null ) {
				LOG.duplicateMetadata();
				return;
			}
			if ( pum == null ) {
				continue;
			}
			globalDefaults = new Default();
			if ( pum.getXmlMappingMetadataComplete() != null ) {
				globalDefaults.setMetadataComplete( true );
				indexBuilder.mappingMetadataComplete();
			}
			JaxbPersistenceUnitDefaults pud = pum.getPersistenceUnitDefaults();
			if ( pud == null ) {
				return;
			}
			globalDefaults.setSchema( pud.getSchema() );
			globalDefaults.setCatalog( pud.getCatalog() );
			//globalDefaults.setAccess( pud.getAccess() );
			globalDefaults.setCascadePersist( pud.getCascadePersist() != null );
			new PersistenceMetadataMocker( indexBuilder, pud ).process();
		}
	}


	private void processEntityMappings(List<JaxbEntityMappings> entityMappingsList) {
		List<AbstractEntityObjectMocker> mockerList = new ArrayList<AbstractEntityObjectMocker>();
		for ( JaxbEntityMappings entityMappings : entityMappingsList ) {
			final Default defaults = getEntityMappingsDefaults( entityMappings );
			globalAnnotations.collectGlobalMappings( entityMappings, defaults );
			for ( JaxbMappedSuperclass mappedSuperclass : entityMappings.getMappedSuperclass() ) {
				AbstractEntityObjectMocker mocker =
						new MappedSuperclassMocker( indexBuilder, mappedSuperclass, defaults );
				mockerList.add( mocker );
				mocker.preProcess();
			}
			for ( JaxbEmbeddable embeddable : entityMappings.getEmbeddable() ) {
				AbstractEntityObjectMocker mocker =
						new EmbeddableMocker( indexBuilder, embeddable, defaults );
				mockerList.add( mocker );
				mocker.preProcess();
			}
			for ( JaxbEntity entity : entityMappings.getEntity() ) {
				globalAnnotations.collectGlobalMappings( entity, defaults );
				AbstractEntityObjectMocker mocker =
						new EntityMocker( indexBuilder, entity, defaults );
				mockerList.add( mocker );
				mocker.preProcess();
			}
		}
		for ( AbstractEntityObjectMocker mocker : mockerList ) {
			mocker.process();
		}
	}

	private void processGlobalAnnotations() {
		if ( globalAnnotations.hasGlobalConfiguration() ) {
			indexBuilder.collectGlobalConfigurationFromIndex( globalAnnotations );
			new GlobalAnnotationMocker(
					indexBuilder, globalAnnotations
			).process();
		}
	}

	private Default getEntityMappingsDefaults(JaxbEntityMappings entityMappings) {
		Default entityMappingDefault = new Default();
		entityMappingDefault.setPackageName( entityMappings.getPackage() );
		entityMappingDefault.setSchema( entityMappings.getSchema() );
		entityMappingDefault.setCatalog( entityMappings.getCatalog() );
		entityMappingDefault.setAccess( entityMappings.getAccess() );
		final Default defaults = new Default();
		defaults.override( globalDefaults );
		defaults.override( entityMappingDefault );
		return defaults;
	}


	public static class Default implements Serializable {
		private JaxbAccessType access;
		private String packageName;
		private String schema;
		private String catalog;
		private Boolean metadataComplete;
		private Boolean cascadePersist;

		public JaxbAccessType getAccess() {
			return access;
		}

		void setAccess(JaxbAccessType access) {
			this.access = access;
		}

		public String getCatalog() {
			return catalog;
		}

		void setCatalog(String catalog) {
			this.catalog = catalog;
		}

		public String getPackageName() {
			return packageName;
		}

		void setPackageName(String packageName) {
			this.packageName = packageName;
		}

		public String getSchema() {
			return schema;
		}

		void setSchema(String schema) {
			this.schema = schema;
		}

		public Boolean isMetadataComplete() {
			return metadataComplete;
		}

		void setMetadataComplete(Boolean metadataComplete) {
			this.metadataComplete = metadataComplete;
		}

		public Boolean isCascadePersist() {
			return cascadePersist;
		}

		void setCascadePersist(Boolean cascadePersist) {
			this.cascadePersist = cascadePersist;
		}

		void override(Default globalDefault) {
			if ( globalDefault != null ) {
				if ( globalDefault.getAccess() != null ) {
					access = globalDefault.getAccess();
				}
				if ( globalDefault.getPackageName() != null ) {
					packageName = globalDefault.getPackageName();
				}
				if ( globalDefault.getSchema() != null ) {
					schema = globalDefault.getSchema();
				}
				if ( globalDefault.getCatalog() != null ) {
					catalog = globalDefault.getCatalog();
				}
				if ( globalDefault.isCascadePersist() != null ) {
					cascadePersist = globalDefault.isCascadePersist();
				}
				if ( globalDefault.isMetadataComplete() != null ) {
					metadataComplete = globalDefault.isMetadataComplete();
				}

			}
		}
	}
}
