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
package org.hibernate.cfg.annotations;

import java.util.Map;

import org.jboss.logging.Logger;

import org.hibernate.AnnotationException;
import org.hibernate.MappingException;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.common.reflection.XClass;
import org.hibernate.annotations.common.reflection.XProperty;
import org.hibernate.cfg.CollectionSecondPass;
import org.hibernate.cfg.Ejb3Column;
import org.hibernate.cfg.Ejb3JoinColumn;
import org.hibernate.cfg.Mappings;
import org.hibernate.cfg.PropertyHolder;
import org.hibernate.cfg.PropertyHolderBuilder;
import org.hibernate.cfg.SecondPass;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.IndexBackref;
import org.hibernate.mapping.List;
import org.hibernate.mapping.OneToMany;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.SimpleValue;

/**
 * Bind a list to the underlying Hibernate configuration
 *
 * @author Matthew Inger
 * @author Emmanuel Bernard
 */
@SuppressWarnings({"unchecked", "serial"})
public class ListBinder extends CollectionBinder {
	private static final CoreMessageLogger LOG = Logger.getMessageLogger( CoreMessageLogger.class, ListBinder.class.getName() );

	public ListBinder() {
	}

	@Override
	protected Collection createCollection(PersistentClass persistentClass) {
		return new org.hibernate.mapping.List( getMappings(), persistentClass );
	}

	@Override
	public void setSqlOrderBy(OrderBy orderByAnn) {
		if ( orderByAnn != null )
			LOG.orderByAnnotationIndexedCollection();
	}

	@Override
	public void setSort(Sort sortAnn) {
		if ( sortAnn != null )
			LOG.sortAnnotationIndexedCollection();
	}

	@Override
	public SecondPass getSecondPass(
			final Ejb3JoinColumn[] fkJoinColumns,
			final Ejb3JoinColumn[] keyColumns,
			final Ejb3JoinColumn[] inverseColumns,
			final Ejb3Column[] elementColumns,
			Ejb3Column[] mapKeyColumns,
			final Ejb3JoinColumn[] mapKeyManyToManyColumns,
			final boolean isEmbedded,
			final XProperty property,
			final XClass collType,
			final boolean ignoreNotFound,
			final boolean unique,
			final TableBinder assocTableBinder,
			final Mappings mappings) {
		return new CollectionSecondPass( mappings, ListBinder.this.collection ) {
			@Override
            public void secondPass(Map persistentClasses, Map inheritedMetas)
					throws MappingException {
				bindStarToManySecondPass(
						persistentClasses, collType, fkJoinColumns, keyColumns, inverseColumns, elementColumns,
						isEmbedded, property, unique, assocTableBinder, ignoreNotFound, mappings
				);
				bindIndex( mappings );
			}
		};
	}

	private void bindIndex(final Mappings mappings) {
		if ( !indexColumn.isImplicit() ) {
			PropertyHolder valueHolder = PropertyHolderBuilder.buildPropertyHolder(
					this.collection,
					StringHelper.qualify( this.collection.getRole(), "key" ),
					(XClass) null,
					(XProperty) null, propertyHolder, mappings
			);
			List list = (List) this.collection;
			if ( !list.isOneToMany() ) indexColumn.forceNotNull();
			indexColumn.setPropertyHolder( valueHolder );
			SimpleValueBinder value = new SimpleValueBinder();
			value.setColumns( new Ejb3Column[] { indexColumn } );
			value.setExplicitType( "integer" );
			value.setMappings( mappings );
			SimpleValue indexValue = value.make();
			indexColumn.linkWithValue( indexValue );
			list.setIndex( indexValue );
			list.setBaseIndex( indexColumn.getBase() );
			if ( list.isOneToMany() && !list.getKey().isNullable() && !list.isInverse() ) {
				String entityName = ( (OneToMany) list.getElement() ).getReferencedEntityName();
				PersistentClass referenced = mappings.getClass( entityName );
				IndexBackref ib = new IndexBackref();
				ib.setName( '_' + propertyName + "IndexBackref" );
				ib.setUpdateable( false );
				ib.setSelectable( false );
				ib.setCollectionRole( list.getRole() );
				ib.setEntityName( list.getOwner().getEntityName() );
				ib.setValue( list.getIndex() );
				referenced.addProperty( ib );
			}
		}
		else {
			Collection coll = this.collection;
			throw new AnnotationException(
					"List/array has to be annotated with an @OrderColumn (or @IndexColumn): "
							+ coll.getRole()
			);
		}
	}
}
