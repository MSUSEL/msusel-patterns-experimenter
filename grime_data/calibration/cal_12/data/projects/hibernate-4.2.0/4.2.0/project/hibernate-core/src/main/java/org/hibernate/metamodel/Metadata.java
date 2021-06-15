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
package org.hibernate.metamodel;

import java.util.Map;
import javax.persistence.SharedCacheMode;

import org.hibernate.SessionFactory;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cfg.NamingStrategy;
import org.hibernate.engine.ResultSetMappingDefinition;
import org.hibernate.engine.spi.FilterDefinition;
import org.hibernate.engine.spi.NamedQueryDefinition;
import org.hibernate.engine.spi.NamedSQLQueryDefinition;
import org.hibernate.metamodel.binding.EntityBinding;
import org.hibernate.metamodel.binding.FetchProfile;
import org.hibernate.metamodel.binding.IdGenerator;
import org.hibernate.metamodel.binding.PluralAttributeBinding;
import org.hibernate.metamodel.binding.TypeDef;

/**
 * @author Steve Ebersole
 */
public interface Metadata {
	/**
	 * Exposes the options used to produce a {@link Metadata} instance.
	 */
	public static interface Options {
		public MetadataSourceProcessingOrder getMetadataSourceProcessingOrder();
		public NamingStrategy getNamingStrategy();
		public SharedCacheMode getSharedCacheMode();
		public AccessType getDefaultAccessType();
		public boolean useNewIdentifierGenerators();
        public boolean isGloballyQuotedIdentifiers();
		public String getDefaultSchemaName();
		public String getDefaultCatalogName();
	}

	public Options getOptions();

	public SessionFactoryBuilder getSessionFactoryBuilder();

	public SessionFactory buildSessionFactory();

	public Iterable<EntityBinding> getEntityBindings();

	public EntityBinding getEntityBinding(String entityName);

	/**
	 * Get the "root" entity binding
	 * @param entityName
	 * @return the "root entity binding; simply returns entityBinding if it is the root entity binding
	 */
	public EntityBinding getRootEntityBinding(String entityName);

	public Iterable<PluralAttributeBinding> getCollectionBindings();

	public TypeDef getTypeDefinition(String name);

	public Iterable<TypeDef> getTypeDefinitions();

	public Iterable<FilterDefinition> getFilterDefinitions();

	public Iterable<NamedQueryDefinition> getNamedQueryDefinitions();

	public Iterable<NamedSQLQueryDefinition> getNamedNativeQueryDefinitions();

	public Iterable<ResultSetMappingDefinition> getResultSetMappingDefinitions();

	public Iterable<Map.Entry<String, String>> getImports();

	public Iterable<FetchProfile> getFetchProfiles();

	public IdGenerator getIdGenerator(String name);
}
