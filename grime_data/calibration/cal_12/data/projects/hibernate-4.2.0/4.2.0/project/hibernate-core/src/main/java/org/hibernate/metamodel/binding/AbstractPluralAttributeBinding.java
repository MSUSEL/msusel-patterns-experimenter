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
package org.hibernate.metamodel.binding;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.hibernate.AssertionFailure;
import org.hibernate.FetchMode;
import org.hibernate.engine.FetchStyle;
import org.hibernate.engine.FetchTiming;
import org.hibernate.engine.spi.CascadeStyle;
import org.hibernate.metamodel.domain.PluralAttribute;
import org.hibernate.metamodel.relational.Table;
import org.hibernate.metamodel.relational.TableSpecification;
import org.hibernate.persister.collection.CollectionPersister;

/**
 * TODO : javadoc
 *
 * @author Steve Ebersole
 */
public abstract class AbstractPluralAttributeBinding extends AbstractAttributeBinding implements PluralAttributeBinding {
	private final CollectionKey collectionKey;
	private final AbstractCollectionElement collectionElement;

	private Table collectionTable;

	private FetchTiming fetchTiming;
	private FetchStyle fetchStyle;
	private int batchSize = -1;

	private CascadeStyle cascadeStyle;
	private boolean orphanDelete;

	private Caching caching;

	private boolean inverse;
	private boolean mutable = true;

	private Class<? extends CollectionPersister> collectionPersisterClass;

	private String where;
	private String orderBy;
	private boolean sorted;
	private Comparator comparator;
	private String comparatorClassName;

	private String customLoaderName;
	private CustomSQL customSqlInsert;
	private CustomSQL customSqlUpdate;
	private CustomSQL customSqlDelete;
	private CustomSQL customSqlDeleteAll;

	private String referencedPropertyName;

	private final java.util.Map filters = new HashMap();
	private final java.util.Set<String> synchronizedTables = new HashSet<String>();

	protected AbstractPluralAttributeBinding(
			AttributeBindingContainer container,
			PluralAttribute attribute,
			CollectionElementNature collectionElementNature) {
		super( container, attribute );
		this.collectionKey = new CollectionKey( this );
		this.collectionElement = interpretNature( collectionElementNature );
	}

	private AbstractCollectionElement interpretNature(CollectionElementNature collectionElementNature) {
		switch ( collectionElementNature ) {
			case BASIC: {
				return new BasicCollectionElement( this );
			}
			case COMPOSITE: {
				return new CompositeCollectionElement( this );
			}
			case ONE_TO_MANY: {
				return new OneToManyCollectionElement( this );
			}
			case MANY_TO_MANY: {
				return new ManyToManyCollectionElement( this );
			}
			case MANY_TO_ANY: {
				return new ManyToAnyCollectionElement( this );
			}
			default: {
				throw new AssertionFailure( "Unknown collection element nature : " + collectionElementNature );
			}
		}
	}

//	protected void initializeBinding(PluralAttributeBindingState state) {
//		super.initialize( state );
//		fetchMode = state.getFetchMode();
//		extraLazy = state.isExtraLazy();
//		collectionElement.setNodeName( state.getElementNodeName() );
//		collectionElement.setTypeName( state.getElementTypeName() );
//		inverse = state.isInverse();
//		mutable = state.isMutable();
//		subselectLoadable = state.isSubselectLoadable();
//		if ( isSubselectLoadable() ) {
//			getEntityBinding().setSubselectLoadableCollections( true );
//		}
//		cacheConcurrencyStrategy = state.getCacheConcurrencyStrategy();
//		cacheRegionName = state.getCacheRegionName();
//		orderBy = state.getOrderBy();
//		where = state.getWhere();
//		referencedPropertyName = state.getReferencedPropertyName();
//		sorted = state.isSorted();
//		comparator = state.getComparator();
//		comparatorClassName = state.getComparatorClassName();
//		orphanDelete = state.isOrphanDelete();
//		batchSize = state.getBatchSize();
//		embedded = state.isEmbedded();
//		optimisticLocked = state.isOptimisticLocked();
//		collectionPersisterClass = state.getCollectionPersisterClass();
//		filters.putAll( state.getFilters() );
//		synchronizedTables.addAll( state.getSynchronizedTables() );
//		customSQLInsert = state.getCustomSQLInsert();
//		customSQLUpdate = state.getCustomSQLUpdate();
//		customSQLDelete = state.getCustomSQLDelete();
//		customSQLDeleteAll = state.getCustomSQLDeleteAll();
//		loaderName = state.getLoaderName();
//	}

	@Override
	public PluralAttribute getAttribute() {
		return (PluralAttribute) super.getAttribute();
	}

	@Override
	public boolean isAssociation() {
		return collectionElement.getCollectionElementNature() == CollectionElementNature.MANY_TO_ANY
				|| collectionElement.getCollectionElementNature() == CollectionElementNature.MANY_TO_MANY
				|| collectionElement.getCollectionElementNature() == CollectionElementNature.ONE_TO_MANY;
	}

	@Override
	public TableSpecification getCollectionTable() {
		return collectionTable;
	}

	public void setCollectionTable(Table collectionTable) {
		this.collectionTable = collectionTable;
	}

	@Override
	public CollectionKey getCollectionKey() {
		return collectionKey;
	}

	@Override
	public AbstractCollectionElement getCollectionElement() {
		return collectionElement;
	}

	@Override
	public CascadeStyle getCascadeStyle() {
		return cascadeStyle;
	}

	@Override
	public void setCascadeStyles(Iterable<CascadeStyle> cascadeStyles) {
		List<CascadeStyle> cascadeStyleList = new ArrayList<CascadeStyle>();
		for ( CascadeStyle style : cascadeStyles ) {
			if ( style != CascadeStyle.NONE ) {
				cascadeStyleList.add( style );
			}
			if ( style == CascadeStyle.DELETE_ORPHAN ||
					style == CascadeStyle.ALL_DELETE_ORPHAN ) {
				orphanDelete = true;
			}
		}

		if ( cascadeStyleList.isEmpty() ) {
			cascadeStyle = CascadeStyle.NONE;
		}
		else if ( cascadeStyleList.size() == 1 ) {
			cascadeStyle = cascadeStyleList.get( 0 );
		}
		else {
			cascadeStyle = new CascadeStyle.MultipleCascadeStyle(
					cascadeStyleList.toArray( new CascadeStyle[ cascadeStyleList.size() ] )
			);
		}
	}

	@Override
	public boolean isOrphanDelete() {
		return orphanDelete;
	}

	@Override
	public FetchMode getFetchMode() {
		if ( getFetchStyle() == FetchStyle.JOIN ) {
			return FetchMode.JOIN;
		}
		else {
			return FetchMode.SELECT;
		}
	}

	@Override
	public FetchTiming getFetchTiming() {
		return fetchTiming;
	}

	@Override
	public void setFetchTiming(FetchTiming fetchTiming) {
		this.fetchTiming = fetchTiming;
	}

	@Override
	public FetchStyle getFetchStyle() {
		return fetchStyle;
	}

	@Override
	public void setFetchStyle(FetchStyle fetchStyle) {
		this.fetchStyle = fetchStyle;
	}

	@Override
	public String getCustomLoaderName() {
		return customLoaderName;
	}

	public void setCustomLoaderName(String customLoaderName) {
		this.customLoaderName = customLoaderName;
	}

	@Override
	public CustomSQL getCustomSqlInsert() {
		return customSqlInsert;
	}

	public void setCustomSqlInsert(CustomSQL customSqlInsert) {
		this.customSqlInsert = customSqlInsert;
	}

	@Override
	public CustomSQL getCustomSqlUpdate() {
		return customSqlUpdate;
	}

	public void setCustomSqlUpdate(CustomSQL customSqlUpdate) {
		this.customSqlUpdate = customSqlUpdate;
	}

	@Override
	public CustomSQL getCustomSqlDelete() {
		return customSqlDelete;
	}

	public void setCustomSqlDelete(CustomSQL customSqlDelete) {
		this.customSqlDelete = customSqlDelete;
	}

	@Override
	public CustomSQL getCustomSqlDeleteAll() {
		return customSqlDeleteAll;
	}

	public void setCustomSqlDeleteAll(CustomSQL customSqlDeleteAll) {
		this.customSqlDeleteAll = customSqlDeleteAll;
	}

	public Class<? extends CollectionPersister> getCollectionPersisterClass() {
		return collectionPersisterClass;
	}

	public void setCollectionPersisterClass(Class<? extends CollectionPersister> collectionPersisterClass) {
		this.collectionPersisterClass = collectionPersisterClass;
	}

	public Caching getCaching() {
		return caching;
	}

	public void setCaching(Caching caching) {
		this.caching = caching;
	}

	@Override
	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	@Override
	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	@Override
	public boolean isInverse() {
		return inverse;
	}

	public void setInverse(boolean inverse) {
		this.inverse = inverse;
	}

	@Override
	public boolean isMutable() {
		return mutable;
	}

	public void setMutable(boolean mutable) {
		this.mutable = mutable;
	}

	@Override
	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}











	public String getReferencedPropertyName() {
		return referencedPropertyName;
	}

	@Override
	public boolean isSorted() {
		return sorted;
	}

	@Override
	public Comparator getComparator() {
		return comparator;
	}

	public void setComparator(Comparator comparator) {
		this.comparator = comparator;
	}

	public String getComparatorClassName() {
		return comparatorClassName;
	}

	public void addFilter(String name, String condition) {
		filters.put( name, condition );
	}

	@Override
	public java.util.Map getFilterMap() {
		return filters;
	}
}
