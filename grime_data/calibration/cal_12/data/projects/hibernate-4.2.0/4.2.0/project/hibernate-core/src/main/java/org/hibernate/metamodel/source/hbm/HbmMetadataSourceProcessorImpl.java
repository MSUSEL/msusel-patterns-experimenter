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
import java.util.List;

import org.hibernate.internal.jaxb.JaxbRoot;
import org.hibernate.internal.jaxb.mapping.hbm.JaxbHibernateMapping;
import org.hibernate.metamodel.MetadataSources;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.metamodel.source.MetadataSourceProcessor;
import org.hibernate.metamodel.source.binder.Binder;

/**
 * The {@link org.hibernate.metamodel.source.MetadataSourceProcessor} implementation responsible for processing {@code hbm.xml} sources.
 *
 * @author Steve Ebersole
 */
public class HbmMetadataSourceProcessorImpl implements MetadataSourceProcessor {
	private final MetadataImplementor metadata;

	private List<HibernateMappingProcessor> processors = new ArrayList<HibernateMappingProcessor>();
	private List<EntityHierarchyImpl> entityHierarchies;

	public HbmMetadataSourceProcessorImpl(MetadataImplementor metadata) {
		this.metadata = metadata;
	}

	@Override
	@SuppressWarnings( {"unchecked"})
	public void prepare(MetadataSources sources) {
		final HierarchyBuilder hierarchyBuilder = new HierarchyBuilder();

		for ( JaxbRoot jaxbRoot : sources.getJaxbRootList() ) {
			if ( ! JaxbHibernateMapping.class.isInstance( jaxbRoot.getRoot() ) ) {
				continue;
			}

			final MappingDocument mappingDocument = new MappingDocument( jaxbRoot, metadata );
			processors.add( new HibernateMappingProcessor( metadata, mappingDocument ) );

			hierarchyBuilder.processMappingDocument( mappingDocument );
		}

		this.entityHierarchies = hierarchyBuilder.groupEntityHierarchies();
	}

	@Override
	public void processIndependentMetadata(MetadataSources sources) {
		for ( HibernateMappingProcessor processor : processors ) {
			processor.processIndependentMetadata();
		}
	}

	@Override
	public void processTypeDependentMetadata(MetadataSources sources) {
		for ( HibernateMappingProcessor processor : processors ) {
			processor.processTypeDependentMetadata();
		}
	}

	@Override
	public void processMappingMetadata(MetadataSources sources, List<String> processedEntityNames) {
		Binder binder = new Binder( metadata, processedEntityNames );
		for ( EntityHierarchyImpl entityHierarchy : entityHierarchies ) {
			binder.processEntityHierarchy( entityHierarchy );
		}
	}

	@Override
	public void processMappingDependentMetadata(MetadataSources sources) {
		for ( HibernateMappingProcessor processor : processors ) {
			processor.processMappingDependentMetadata();
		}
	}
}
