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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.MappingException;
import org.hibernate.internal.jaxb.mapping.hbm.EntityElement;
import org.hibernate.internal.jaxb.mapping.hbm.JaxbHibernateMapping;
import org.hibernate.internal.jaxb.mapping.hbm.JaxbJoinedSubclassElement;
import org.hibernate.internal.jaxb.mapping.hbm.JaxbSubclassElement;
import org.hibernate.internal.jaxb.mapping.hbm.JaxbUnionSubclassElement;
import org.hibernate.internal.jaxb.mapping.hbm.SubEntityElement;
import org.hibernate.metamodel.source.binder.SubclassEntityContainer;
import org.hibernate.metamodel.source.binder.SubclassEntitySource;

/**
 * @author Steve Ebersole
 */
public class HierarchyBuilder {
	private final List<EntityHierarchyImpl> entityHierarchies = new ArrayList<EntityHierarchyImpl>();

	// process state
	private final Map<String,SubclassEntityContainer> subEntityContainerMap = new HashMap<String, SubclassEntityContainer>();
	private final List<ExtendsQueueEntry> extendsQueue = new ArrayList<ExtendsQueueEntry>();

	// mapping file specific state
	private MappingDocument currentMappingDocument;

	public void processMappingDocument(MappingDocument mappingDocument) {
		this.currentMappingDocument = mappingDocument;
		try {
			processCurrentMappingDocument();
		}
		finally {
			this.currentMappingDocument = null;
		}
	}

	private void processCurrentMappingDocument() {
		for ( Object entityElementO : currentMappingDocument.getMappingRoot().getClazzOrSubclassOrJoinedSubclass() ) {
			final EntityElement entityElement = (EntityElement) entityElementO;
			if ( JaxbHibernateMapping.JaxbClass.class.isInstance( entityElement ) ) {
				// we can immediately handle <class/> elements in terms of creating the hierarchy entry
				final JaxbHibernateMapping.JaxbClass jaxbClass = (JaxbHibernateMapping.JaxbClass) entityElement;
				final RootEntitySourceImpl rootEntitySource = new RootEntitySourceImpl( currentMappingDocument,
																						jaxbClass
				);
				final EntityHierarchyImpl hierarchy = new EntityHierarchyImpl( rootEntitySource );

				entityHierarchies.add( hierarchy );
				subEntityContainerMap.put( rootEntitySource.getEntityName(), rootEntitySource );

				processSubElements( entityElement, rootEntitySource );
			}
			else {
				// we have to see if this things super-type has been found yet, and if not add it to the
				// extends queue
				final SubclassEntitySourceImpl subClassEntitySource = new SubclassEntitySourceImpl( currentMappingDocument, entityElement );
				final String entityName = subClassEntitySource.getEntityName();
				subEntityContainerMap.put( entityName, subClassEntitySource );
				final String entityItExtends = currentMappingDocument.getMappingLocalBindingContext().qualifyClassName(
						((SubEntityElement) entityElement).getExtends()
				);
				processSubElements( entityElement, subClassEntitySource );
				final SubclassEntityContainer container = subEntityContainerMap.get( entityItExtends );
				if ( container != null ) {
					// we already have this entity's super, attach it and continue
					container.add( subClassEntitySource );
				}
				else {
					// we do not yet have the super and have to wait, so add it fto the extends queue
					extendsQueue.add( new ExtendsQueueEntry( subClassEntitySource, entityItExtends ) );
				}
			}
		}
	}

	public List<EntityHierarchyImpl> groupEntityHierarchies() {
		while ( ! extendsQueue.isEmpty() ) {
			// set up a pass over the queue
			int numberOfMappingsProcessed = 0;
			Iterator<ExtendsQueueEntry> iterator = extendsQueue.iterator();
			while ( iterator.hasNext() ) {
				final ExtendsQueueEntry entry = iterator.next();
				final SubclassEntityContainer container = subEntityContainerMap.get( entry.entityItExtends );
				if ( container != null ) {
					// we now have this entity's super, attach it and remove entry from extends queue
					container.add( entry.subClassEntitySource );
					iterator.remove();
					numberOfMappingsProcessed++;
				}
			}

			if ( numberOfMappingsProcessed == 0 ) {
				// todo : we could log the waiting dependencies...
				throw new MappingException( "Unable to process extends dependencies in hbm files" );
			}
		}

		return entityHierarchies;
	}

	private void processSubElements(EntityElement entityElement, SubclassEntityContainer container) {
		if ( JaxbHibernateMapping.JaxbClass.class.isInstance( entityElement ) ) {
			final JaxbHibernateMapping.JaxbClass jaxbClass = (JaxbHibernateMapping.JaxbClass) entityElement;
			processElements( jaxbClass.getJoinedSubclass(), container );
			processElements( jaxbClass.getSubclass(), container );
			processElements( jaxbClass.getUnionSubclass(), container );
		}
		else if ( JaxbSubclassElement.class.isInstance( entityElement ) ) {
			final JaxbSubclassElement jaxbSubclass = (JaxbSubclassElement) entityElement;
			processElements( jaxbSubclass.getSubclass(), container );
		}
		else if ( JaxbJoinedSubclassElement.class.isInstance( entityElement ) ) {
			final JaxbJoinedSubclassElement jaxbJoinedSubclass = (JaxbJoinedSubclassElement) entityElement;
			processElements( jaxbJoinedSubclass.getJoinedSubclass(), container );
		}
		else if ( JaxbUnionSubclassElement.class.isInstance( entityElement ) ) {
			final JaxbUnionSubclassElement jaxbUnionSubclass = (JaxbUnionSubclassElement) entityElement;
			processElements( jaxbUnionSubclass.getUnionSubclass(), container );
		}
	}

	private void processElements(List subElements, SubclassEntityContainer container) {
		for ( Object subElementO : subElements ) {
			final SubEntityElement subElement = (SubEntityElement) subElementO;
			final SubclassEntitySourceImpl subclassEntitySource = new SubclassEntitySourceImpl( currentMappingDocument, subElement );
			container.add( subclassEntitySource );
			final String subEntityName = subclassEntitySource.getEntityName();
			subEntityContainerMap.put( subEntityName, subclassEntitySource );
		}
	}

	private static class ExtendsQueueEntry {
		private final SubclassEntitySource subClassEntitySource;
		private final String entityItExtends;

		private ExtendsQueueEntry(SubclassEntitySource subClassEntitySource, String entityItExtends) {
			this.subClassEntitySource = subClassEntitySource;
			this.entityItExtends = entityItExtends;
		}
	}
}
