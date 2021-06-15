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
package org.hibernate.metamodel.source.annotations.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.internal.jaxb.Origin;
import org.hibernate.metamodel.binding.CustomSQL;
import org.hibernate.metamodel.source.LocalBindingContext;
import org.hibernate.metamodel.source.annotations.attribute.AssociationAttribute;
import org.hibernate.metamodel.source.annotations.attribute.BasicAttribute;
import org.hibernate.metamodel.source.annotations.attribute.SingularAttributeSourceImpl;
import org.hibernate.metamodel.source.annotations.attribute.ToOneAttributeSourceImpl;
import org.hibernate.metamodel.source.binder.AttributeSource;
import org.hibernate.metamodel.source.binder.ConstraintSource;
import org.hibernate.metamodel.source.binder.EntitySource;
import org.hibernate.metamodel.source.binder.JpaCallbackClass;
import org.hibernate.metamodel.source.binder.MetaAttributeSource;
import org.hibernate.metamodel.source.binder.SubclassEntitySource;
import org.hibernate.metamodel.source.binder.TableSource;

/**
 * @author Hardy Ferentschik
 */
public class EntitySourceImpl implements EntitySource {
	private final EntityClass entityClass;
	private final Set<SubclassEntitySource> subclassEntitySources;

	public EntitySourceImpl(EntityClass entityClass) {
		this.entityClass = entityClass;
		this.subclassEntitySources = new HashSet<SubclassEntitySource>();
	}

	public EntityClass getEntityClass() {
		return entityClass;
	}

	@Override
	public Origin getOrigin() {
		return entityClass.getLocalBindingContext().getOrigin();
	}

	@Override
	public LocalBindingContext getLocalBindingContext() {
		return entityClass.getLocalBindingContext();
	}

	@Override
	public String getEntityName() {
		return entityClass.getName();
	}

	@Override
	public String getClassName() {
		return entityClass.getName();
	}

	@Override
	public String getJpaEntityName() {
		return entityClass.getExplicitEntityName();
	}

	@Override
	public TableSource getPrimaryTable() {
		return entityClass.getPrimaryTableSource();
	}

	@Override
	public boolean isAbstract() {
		return false;
	}

	@Override
	public boolean isLazy() {
		return entityClass.isLazy();
	}

	@Override
	public String getProxy() {
		return entityClass.getProxy();
	}

	@Override
	public int getBatchSize() {
		return entityClass.getBatchSize();
	}

	@Override
	public boolean isDynamicInsert() {
		return entityClass.isDynamicInsert();
	}

	@Override
	public boolean isDynamicUpdate() {
		return entityClass.isDynamicUpdate();
	}

	@Override
	public boolean isSelectBeforeUpdate() {
		return entityClass.isSelectBeforeUpdate();
	}

	@Override
	public String getCustomTuplizerClassName() {
		return entityClass.getCustomTuplizer();
	}

	@Override
	public String getCustomPersisterClassName() {
		return entityClass.getCustomPersister();
	}

	@Override
	public String getCustomLoaderName() {
		return entityClass.getCustomLoaderQueryName();
	}

	@Override
	public CustomSQL getCustomSqlInsert() {
		return entityClass.getCustomInsert();
	}

	@Override
	public CustomSQL getCustomSqlUpdate() {
		return entityClass.getCustomUpdate();
	}

	@Override
	public CustomSQL getCustomSqlDelete() {
		return entityClass.getCustomDelete();
	}

	@Override
	public List<String> getSynchronizedTableNames() {
		return entityClass.getSynchronizedTableNames();
	}

	@Override
	public Iterable<MetaAttributeSource> metaAttributes() {
		return Collections.emptySet();
	}

	@Override
	public String getPath() {
		return entityClass.getName();
	}

	@Override
	public Iterable<AttributeSource> attributeSources() {
		List<AttributeSource> attributeList = new ArrayList<AttributeSource>();
		for ( BasicAttribute attribute : entityClass.getSimpleAttributes() ) {
			attributeList.add( new SingularAttributeSourceImpl( attribute ) );
		}
		for ( EmbeddableClass component : entityClass.getEmbeddedClasses().values() ) {
			attributeList.add(
					new ComponentAttributeSourceImpl(
							component,
							"",
							entityClass.getAttributeOverrideMap()
					)
			);
		}
		for ( AssociationAttribute associationAttribute : entityClass.getAssociationAttributes() ) {
			attributeList.add( new ToOneAttributeSourceImpl( associationAttribute ) );
		}
		return attributeList;
	}

	@Override
	public void add(SubclassEntitySource subclassEntitySource) {
		subclassEntitySources.add( subclassEntitySource );
	}

	@Override
	public Iterable<SubclassEntitySource> subclassEntitySources() {
		return subclassEntitySources;
	}

	@Override
	public String getDiscriminatorMatchValue() {
		return entityClass.getDiscriminatorMatchValue();
	}

	@Override
	public Iterable<ConstraintSource> getConstraints() {
		return entityClass.getConstraintSources();
	}

	@Override
	public List<JpaCallbackClass> getJpaCallbackClasses() {
		return entityClass.getJpaCallbacks();
	}

	@Override
	public Iterable<TableSource> getSecondaryTables() {
		return entityClass.getSecondaryTableSources();
	}
}


