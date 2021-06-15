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
package org.hibernate.metamodel.source;

import org.hibernate.engine.ResultSetMappingDefinition;
import org.hibernate.engine.spi.FilterDefinition;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.NamedQueryDefinition;
import org.hibernate.engine.spi.NamedSQLQueryDefinition;
import org.hibernate.metamodel.Metadata;
import org.hibernate.metamodel.binding.EntityBinding;
import org.hibernate.metamodel.binding.FetchProfile;
import org.hibernate.metamodel.binding.IdGenerator;
import org.hibernate.metamodel.binding.PluralAttributeBinding;
import org.hibernate.metamodel.binding.TypeDef;
import org.hibernate.metamodel.relational.Database;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.TypeResolver;

/**
 * @author Steve Ebersole
 */
public interface MetadataImplementor extends Metadata, BindingContext, Mapping {
	public ServiceRegistry getServiceRegistry();

	public Database getDatabase();

	public TypeResolver getTypeResolver();

	public void addImport(String entityName, String entityName1);

	public void addEntity(EntityBinding entityBinding);

	public void addCollection(PluralAttributeBinding collectionBinding);

	public void addFetchProfile(FetchProfile profile);

	public void addTypeDefinition(TypeDef typeDef);

	public void addFilterDefinition(FilterDefinition filterDefinition);

	public void addIdGenerator(IdGenerator generator);

	public void registerIdentifierGenerator(String name, String clazz);

	public void addNamedNativeQuery(NamedSQLQueryDefinition def);

	public void addNamedQuery(NamedQueryDefinition def);

	public void addResultSetMapping(ResultSetMappingDefinition resultSetMappingDefinition);

	// todo : this needs to move to AnnotationBindingContext
	public void setGloballyQuotedIdentifiers(boolean b);

	public MetaAttributeContext getGlobalMetaAttributeContext();
}
