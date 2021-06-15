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
import java.util.Collections;
import java.util.List;

import org.hibernate.AssertionFailure;
import org.hibernate.EntityMode;
import org.hibernate.internal.jaxb.Origin;
import org.hibernate.internal.jaxb.mapping.hbm.EntityElement;
import org.hibernate.internal.jaxb.mapping.hbm.JaxbAnyElement;
import org.hibernate.internal.jaxb.mapping.hbm.JaxbBagElement;
import org.hibernate.internal.jaxb.mapping.hbm.JaxbComponentElement;
import org.hibernate.internal.jaxb.mapping.hbm.JaxbIdbagElement;
import org.hibernate.internal.jaxb.mapping.hbm.JaxbListElement;
import org.hibernate.internal.jaxb.mapping.hbm.JaxbManyToOneElement;
import org.hibernate.internal.jaxb.mapping.hbm.JaxbMapElement;
import org.hibernate.internal.jaxb.mapping.hbm.JaxbOneToOneElement;
import org.hibernate.internal.jaxb.mapping.hbm.JaxbPropertyElement;
import org.hibernate.internal.jaxb.mapping.hbm.JaxbSetElement;
import org.hibernate.internal.jaxb.mapping.hbm.JaxbSynchronizeElement;
import org.hibernate.internal.jaxb.mapping.hbm.JaxbTuplizerElement;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.metamodel.binding.CustomSQL;
import org.hibernate.metamodel.source.LocalBindingContext;
import org.hibernate.metamodel.source.binder.AttributeSource;
import org.hibernate.metamodel.source.binder.ConstraintSource;
import org.hibernate.metamodel.source.binder.EntitySource;
import org.hibernate.metamodel.source.binder.JpaCallbackClass;
import org.hibernate.metamodel.source.binder.MetaAttributeSource;
import org.hibernate.metamodel.source.binder.SubclassEntitySource;
import org.hibernate.metamodel.source.binder.TableSource;

/**
 * @author Steve Ebersole
 * @author Hardy Ferentschik
 */
public abstract class AbstractEntitySourceImpl implements EntitySource {
	private final MappingDocument sourceMappingDocument;
	private final EntityElement entityElement;

	private List<SubclassEntitySource> subclassEntitySources = new ArrayList<SubclassEntitySource>();

	protected AbstractEntitySourceImpl(MappingDocument sourceMappingDocument, EntityElement entityElement) {
		this.sourceMappingDocument = sourceMappingDocument;
		this.entityElement = entityElement;
	}

	protected EntityElement entityElement() {
		return entityElement;
	}

	protected MappingDocument sourceMappingDocument() {
		return sourceMappingDocument;
	}

	@Override
	public Origin getOrigin() {
		return sourceMappingDocument.getOrigin();
	}

	@Override
	public LocalBindingContext getLocalBindingContext() {
		return sourceMappingDocument.getMappingLocalBindingContext();
	}

	@Override
	public String getEntityName() {
		return StringHelper.isNotEmpty( entityElement.getEntityName() )
				? entityElement.getEntityName()
				: getClassName();
	}

	@Override
	public String getClassName() {
		return getLocalBindingContext().qualifyClassName( entityElement.getName() );
	}

	@Override
	public String getJpaEntityName() {
		return null;
	}

	@Override
	public boolean isAbstract() {
		return Helper.getBooleanValue( entityElement.isAbstract(), false );
	}

	@Override
	public boolean isLazy() {
		return Helper.getBooleanValue( entityElement.isAbstract(), true );
	}

	@Override
	public String getProxy() {
		return entityElement.getProxy();
	}

	@Override
	public int getBatchSize() {
		return Helper.getIntValue( entityElement.getBatchSize(), -1 );
	}

	@Override
	public boolean isDynamicInsert() {
		return entityElement.isDynamicInsert();
	}

	@Override
	public boolean isDynamicUpdate() {
		return entityElement.isDynamicUpdate();
	}

	@Override
	public boolean isSelectBeforeUpdate() {
		return entityElement.isSelectBeforeUpdate();
	}

	protected EntityMode determineEntityMode() {
		return StringHelper.isNotEmpty( getClassName() ) ? EntityMode.POJO : EntityMode.MAP;
	}

	@Override
	public String getCustomTuplizerClassName() {
		if ( entityElement.getTuplizer() == null ) {
			return null;
		}
		final EntityMode entityMode = determineEntityMode();
		for ( JaxbTuplizerElement tuplizerElement : entityElement.getTuplizer() ) {
			if ( entityMode == EntityMode.parse( tuplizerElement.getEntityMode() ) ) {
				return tuplizerElement.getClazz();
			}
		}
		return null;
	}

	@Override
	public String getCustomPersisterClassName() {
		return getLocalBindingContext().qualifyClassName( entityElement.getPersister() );
	}

	@Override
	public String getCustomLoaderName() {
		return entityElement.getLoader() != null ? entityElement.getLoader().getQueryRef() : null;
	}

	@Override
	public CustomSQL getCustomSqlInsert() {
		return Helper.buildCustomSql( entityElement.getSqlInsert() );
	}

	@Override
	public CustomSQL getCustomSqlUpdate() {
		return Helper.buildCustomSql( entityElement.getSqlUpdate() );
	}

	@Override
	public CustomSQL getCustomSqlDelete() {
		return Helper.buildCustomSql( entityElement.getSqlDelete() );
	}

	@Override
	public List<String> getSynchronizedTableNames() {
		List<String> tableNames = new ArrayList<String>();
		for ( JaxbSynchronizeElement synchronizeElement : entityElement.getSynchronize() ) {
			tableNames.add( synchronizeElement.getTable() );
		}
		return tableNames;
	}

	@Override
	public Iterable<MetaAttributeSource> metaAttributes() {
		return Helper.buildMetaAttributeSources( entityElement.getMeta() );
	}

	@Override
	public String getPath() {
		return sourceMappingDocument.getMappingLocalBindingContext().determineEntityName( entityElement );
	}

	@Override
	public Iterable<AttributeSource> attributeSources() {
		List<AttributeSource> attributeSources = new ArrayList<AttributeSource>();
		for ( Object attributeElement : entityElement.getPropertyOrManyToOneOrOneToOne() ) {
			if ( JaxbPropertyElement.class.isInstance( attributeElement ) ) {
				attributeSources.add(
						new PropertyAttributeSourceImpl(
								JaxbPropertyElement.class.cast( attributeElement ),
								sourceMappingDocument().getMappingLocalBindingContext()
						)
				);
			}
			else if ( JaxbComponentElement.class.isInstance( attributeElement ) ) {
				attributeSources.add(
						new ComponentAttributeSourceImpl(
								(JaxbComponentElement) attributeElement,
								this,
								sourceMappingDocument.getMappingLocalBindingContext()
						)
				);
			}
			else if ( JaxbManyToOneElement.class.isInstance( attributeElement ) ) {
				attributeSources.add(
						new ManyToOneAttributeSourceImpl(
								JaxbManyToOneElement.class.cast( attributeElement ),
								sourceMappingDocument().getMappingLocalBindingContext()
						)
				);
			}
			else if ( JaxbOneToOneElement.class.isInstance( attributeElement ) ) {
				// todo : implement
			}
			else if ( JaxbAnyElement.class.isInstance( attributeElement ) ) {
				// todo : implement
			}
			else if ( JaxbBagElement.class.isInstance( attributeElement ) ) {
				attributeSources.add(
						new BagAttributeSourceImpl(
								JaxbBagElement.class.cast( attributeElement ),
								this
						)
				);
			}
			else if ( JaxbIdbagElement.class.isInstance( attributeElement ) ) {
				// todo : implement
			}
			else if ( JaxbSetElement.class.isInstance( attributeElement ) ) {
				attributeSources.add(
						new SetAttributeSourceImpl(
								JaxbSetElement.class.cast( attributeElement ),
								this
						)
				);
			}
			else if ( JaxbListElement.class.isInstance( attributeElement ) ) {
				// todo : implement
			}
			else if ( JaxbMapElement.class.isInstance( attributeElement ) ) {
				// todo : implement
			}
			else {
				throw new AssertionFailure( "Unexpected attribute element type encountered : " + attributeElement.getClass() );
			}
		}
		return attributeSources;
	}

	private EntityHierarchyImpl entityHierarchy;

	public void injectHierarchy(EntityHierarchyImpl entityHierarchy) {
		this.entityHierarchy = entityHierarchy;
	}

	@Override
	public void add(SubclassEntitySource subclassEntitySource) {
		add( (SubclassEntitySourceImpl) subclassEntitySource );
	}

	public void add(SubclassEntitySourceImpl subclassEntitySource) {
		entityHierarchy.processSubclass( subclassEntitySource );
		subclassEntitySources.add( subclassEntitySource );
	}

	@Override
	public Iterable<SubclassEntitySource> subclassEntitySources() {
		return subclassEntitySources;
	}

	@Override
	public String getDiscriminatorMatchValue() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Iterable<ConstraintSource> getConstraints() {
		return Collections.emptySet();
	}

	@Override
	public Iterable<TableSource> getSecondaryTables() {
		return Collections.emptySet();
	}

	@Override
	public List<JpaCallbackClass> getJpaCallbackClasses() {
	    return Collections.EMPTY_LIST;
	}
}
