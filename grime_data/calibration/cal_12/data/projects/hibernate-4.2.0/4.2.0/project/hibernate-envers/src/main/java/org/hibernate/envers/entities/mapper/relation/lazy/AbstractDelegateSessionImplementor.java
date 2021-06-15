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
package org.hibernate.envers.entities.mapper.relation.lazy;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.cache.spi.CacheKey;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.engine.jdbc.spi.JdbcConnectionAccess;
import org.hibernate.engine.query.spi.sql.NativeSQLQuerySpecification;
import org.hibernate.engine.spi.EntityKey;
import org.hibernate.engine.spi.LoadQueryInfluencers;
import org.hibernate.engine.spi.NonFlushedChanges;
import org.hibernate.engine.spi.PersistenceContext;
import org.hibernate.engine.spi.QueryParameters;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.transaction.spi.TransactionCoordinator;
import org.hibernate.loader.custom.CustomQuery;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.type.Type;

/**
 * @author Adam Warski (adam at warski dot org)
 */
@SuppressWarnings( {"deprecation"})
public abstract class AbstractDelegateSessionImplementor implements SessionImplementor {
    protected SessionImplementor delegate;

    public AbstractDelegateSessionImplementor(SessionImplementor delegate) {
        this.delegate = delegate;
    }

    public abstract Object doImmediateLoad(String entityName);

	@Override
    public Object immediateLoad(String entityName, Serializable id) throws HibernateException {
        return doImmediateLoad(entityName);
    }

    // Delegate methods


	@Override
	public String getTenantIdentifier() {
		return delegate.getTenantIdentifier();
	}

	@Override
	public JdbcConnectionAccess getJdbcConnectionAccess() {
		return delegate.getJdbcConnectionAccess();
	}

	@Override
	public EntityKey generateEntityKey(Serializable id, EntityPersister persister) {
		return delegate.generateEntityKey( id, persister );
	}

	@Override
	public CacheKey generateCacheKey(Serializable id, Type type, String entityOrRoleName) {
		return delegate.generateCacheKey( id, type, entityOrRoleName );
	}

	@Override
	public <T> T execute(Callback<T> callback) {
		return delegate.execute( callback );
	}

	@Override
	public LoadQueryInfluencers getLoadQueryInfluencers() {
		return delegate.getLoadQueryInfluencers();
	}

	@Override
	public Interceptor getInterceptor() {
        return delegate.getInterceptor();
    }

	@Override
    public void setAutoClear(boolean enabled) {
        delegate.setAutoClear(enabled);
    }

	@Override
	public void disableTransactionAutoJoin() {
		delegate.disableTransactionAutoJoin();
	}

	@Override
	public boolean isTransactionInProgress() {
        return delegate.isTransactionInProgress();
    }

	@Override
    public void initializeCollection(PersistentCollection collection, boolean writing) throws HibernateException {
        delegate.initializeCollection(collection, writing);
    }

	@Override
    public Object internalLoad(String entityName, Serializable id, boolean eager, boolean nullable) throws HibernateException {
        return delegate.internalLoad(entityName, id, eager, nullable);
    }

	@Override
    public long getTimestamp() {
        return delegate.getTimestamp();
    }

	@Override
    public SessionFactoryImplementor getFactory() {
        return delegate.getFactory();
    }

	@Override
    public List list(String query, QueryParameters queryParameters) throws HibernateException {
        return delegate.list(query, queryParameters);
    }

	@Override
    public Iterator iterate(String query, QueryParameters queryParameters) throws HibernateException {
        return delegate.iterate(query, queryParameters);
    }

	@Override
    public ScrollableResults scroll(String query, QueryParameters queryParameters) throws HibernateException {
        return delegate.scroll(query, queryParameters);
    }

	@Override
    public ScrollableResults scroll(Criteria criteria, ScrollMode scrollMode) {
        return delegate.scroll(criteria, scrollMode);
    }

	@Override
    public List list(Criteria criteria) {
        return delegate.list(criteria);
    }

	@Override
    public List listFilter(Object collection, String filter, QueryParameters queryParameters) throws HibernateException {
        return delegate.listFilter(collection, filter, queryParameters);
    }

	@Override
    public Iterator iterateFilter(Object collection, String filter, QueryParameters queryParameters) throws HibernateException {
        return delegate.iterateFilter(collection, filter, queryParameters);
    }

	@Override
    public EntityPersister getEntityPersister(String entityName, Object object) throws HibernateException {
        return delegate.getEntityPersister(entityName, object);
    }

	@Override
    public Object getEntityUsingInterceptor(EntityKey key) throws HibernateException {
        return delegate.getEntityUsingInterceptor(key);
    }

	@Override
    public Serializable getContextEntityIdentifier(Object object) {
        return delegate.getContextEntityIdentifier(object);
    }

	@Override
    public String bestGuessEntityName(Object object) {
        return delegate.bestGuessEntityName(object);
    }

	@Override
    public String guessEntityName(Object entity) throws HibernateException {
        return delegate.guessEntityName(entity);
    }

	@Override
    public Object instantiate(String entityName, Serializable id) throws HibernateException {
        return delegate.instantiate(entityName, id);
    }

	@Override
    public List listCustomQuery(CustomQuery customQuery, QueryParameters queryParameters) throws HibernateException {
        return delegate.listCustomQuery(customQuery, queryParameters);
    }

	@Override
    public ScrollableResults scrollCustomQuery(CustomQuery customQuery, QueryParameters queryParameters) throws HibernateException {
        return delegate.scrollCustomQuery(customQuery, queryParameters);
    }

	@Override
    public List list(NativeSQLQuerySpecification spec, QueryParameters queryParameters) throws HibernateException {
        return delegate.list(spec, queryParameters);
    }

	@Override
    public ScrollableResults scroll(NativeSQLQuerySpecification spec, QueryParameters queryParameters) throws HibernateException {
        return delegate.scroll(spec, queryParameters);
    }

	@Override
    public Object getFilterParameterValue(String filterParameterName) {
        return delegate.getFilterParameterValue(filterParameterName);
    }

	@Override
    public Type getFilterParameterType(String filterParameterName) {
        return delegate.getFilterParameterType(filterParameterName);
    }

	@Override
    public Map getEnabledFilters() {
        return delegate.getEnabledFilters();
    }

	@Override
    public int getDontFlushFromFind() {
        return delegate.getDontFlushFromFind();
    }

	@Override
    public PersistenceContext getPersistenceContext() {
        return delegate.getPersistenceContext();
    }

	@Override
    public int executeUpdate(String query, QueryParameters queryParameters) throws HibernateException {
        return delegate.executeUpdate(query, queryParameters);
    }

	@Override
    public int executeNativeUpdate(NativeSQLQuerySpecification specification, QueryParameters queryParameters) throws HibernateException {
        return delegate.executeNativeUpdate(specification, queryParameters);
    }

	@Override
	public NonFlushedChanges getNonFlushedChanges() throws HibernateException {
		return delegate.getNonFlushedChanges();
	}

	@Override
	public void applyNonFlushedChanges(NonFlushedChanges nonFlushedChanges) throws HibernateException {
		delegate.applyNonFlushedChanges( nonFlushedChanges );
	}

	@Override
    public CacheMode getCacheMode() {
        return delegate.getCacheMode();
    }

	@Override
    public void setCacheMode(CacheMode cm) {
        delegate.setCacheMode(cm);
    }

	@Override
    public boolean isOpen() {
        return delegate.isOpen();
    }

	@Override
    public boolean isConnected() {
        return delegate.isConnected();
    }

	@Override
    public FlushMode getFlushMode() {
        return delegate.getFlushMode();
    }

	@Override
    public void setFlushMode(FlushMode fm) {
        delegate.setFlushMode(fm);
    }

	@Override
    public Connection connection() {
        return delegate.connection();
    }

	@Override
    public void flush() {
        delegate.flush();
    }

	@Override
    public Query getNamedQuery(String name) {
        return delegate.getNamedQuery(name);
    }

	@Override
    public Query getNamedSQLQuery(String name) {
        return delegate.getNamedSQLQuery(name);
    }

	@Override
    public boolean isEventSource() {
        return delegate.isEventSource();
    }

	@Override
    public void afterScrollOperation() {
        delegate.afterScrollOperation();
    }

	@Override
    public void setFetchProfile(String name) {
        delegate.setFetchProfile(name);
    }

	@Override
    public String getFetchProfile() {
        return delegate.getFetchProfile();
    }

	@Override
	public TransactionCoordinator getTransactionCoordinator() {
		return delegate.getTransactionCoordinator();
	}

	@Override
	public boolean isClosed() {
        return delegate.isClosed();
    }
}
