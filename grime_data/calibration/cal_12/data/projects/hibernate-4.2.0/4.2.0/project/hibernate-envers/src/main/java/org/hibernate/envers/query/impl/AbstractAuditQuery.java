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
package org.hibernate.envers.query.impl;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.entities.EntityInstantiator;
import org.hibernate.envers.exception.AuditException;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.envers.query.criteria.AuditCriterion;
import org.hibernate.envers.query.criteria.CriteriaTools;
import org.hibernate.envers.query.order.AuditOrder;
import org.hibernate.envers.query.projection.AuditProjection;
import org.hibernate.envers.reader.AuditReaderImplementor;
import org.hibernate.envers.tools.Pair;
import org.hibernate.envers.tools.Triple;
import org.hibernate.envers.tools.query.QueryBuilder;

import static org.hibernate.envers.entities.mapper.relation.query.QueryConstants.REFERENCED_ENTITY_ALIAS;

/**
 * @author Adam Warski (adam at warski dot org)
 * @author HernпїЅn Chanfreau
 */
public abstract class AbstractAuditQuery implements AuditQuery {
    protected EntityInstantiator entityInstantiator;
    protected List<AuditCriterion> criterions;

    protected String entityName;
    protected String entityClassName;
    protected String versionsEntityName;
    protected QueryBuilder qb;

    protected boolean hasProjection;
    protected boolean hasOrder;

    protected final AuditConfiguration verCfg;
    protected final AuditReaderImplementor versionsReader;

    protected AbstractAuditQuery(AuditConfiguration verCfg, AuditReaderImplementor versionsReader,
                                    Class<?> cls) {
    	this(verCfg, versionsReader, cls, cls.getName());
    }

	protected AbstractAuditQuery(AuditConfiguration verCfg,
                                 AuditReaderImplementor versionsReader, Class<?> cls, String entityName) {
		this.verCfg = verCfg;
		this.versionsReader = versionsReader;

		criterions = new ArrayList<AuditCriterion>();
		entityInstantiator = new EntityInstantiator(verCfg, versionsReader);

		entityClassName = cls.getName();
		this.entityName = entityName;
		versionsEntityName = verCfg.getAuditEntCfg().getAuditEntityName(
				entityName);

		qb = new QueryBuilder(versionsEntityName, REFERENCED_ENTITY_ALIAS);
	}
    
    protected Query buildQuery() {
        Query query = qb.toQuery(versionsReader.getSession());
        setQueryProperties(query);
        return query;
    }
    
	protected List buildAndExecuteQuery() {
        Query query = buildQuery();

        return query.list();
    }

    public abstract List list() throws AuditException;

    public List getResultList() throws AuditException {
        return list();
    }

    public Object getSingleResult() throws AuditException, NonUniqueResultException, NoResultException {
        List result = list();

        if (result == null || result.size() == 0) {
            throw new NoResultException();
        }

        if (result.size() > 1) {
            throw new NonUniqueResultException();
        }

        return result.get(0);
    }

    public AuditQuery add(AuditCriterion criterion) {
        criterions.add(criterion);
        return this;
    }

    // Projection and order

    public AuditQuery addProjection(AuditProjection projection) {
        Triple<String, String, Boolean> projectionData = projection.getData(verCfg);
        hasProjection = true;
		String propertyName = CriteriaTools.determinePropertyName( verCfg, versionsReader, entityName, projectionData.getSecond() );
        qb.addProjection(projectionData.getFirst(), propertyName, projectionData.getThird());
        return this;
    }

    public AuditQuery addOrder(AuditOrder order) {
        hasOrder = true;
        Pair<String, Boolean> orderData = order.getData(verCfg);
		String propertyName = CriteriaTools.determinePropertyName( verCfg, versionsReader, entityName, orderData.getFirst() );
        qb.addOrder(propertyName, orderData.getSecond());
        return this;
    }

    // Query properties

    private Integer maxResults;
    private Integer firstResult;
    private Boolean cacheable;
    private String cacheRegion;
    private String comment;
    private FlushMode flushMode;
    private CacheMode cacheMode;
    private Integer timeout;
    private LockOptions lockOptions = new LockOptions(LockMode.NONE);

    public AuditQuery setMaxResults(int maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    public AuditQuery setFirstResult(int firstResult) {
        this.firstResult = firstResult;
        return this;
    }

    public AuditQuery setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
        return this;
    }

    public AuditQuery setCacheRegion(String cacheRegion) {
        this.cacheRegion = cacheRegion;
        return this;
    }

    public AuditQuery setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public AuditQuery setFlushMode(FlushMode flushMode) {
        this.flushMode = flushMode;
        return this;
    }

    public AuditQuery setCacheMode(CacheMode cacheMode) {
        this.cacheMode = cacheMode;
        return this;
    }

    public AuditQuery setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

	/**
	 * Set lock mode
	 * @param lockMode The {@link LockMode} used for this query.
	 * @return this object
	 * @deprecated Instead use setLockOptions
	 */
    public AuditQuery setLockMode(LockMode lockMode) {
        lockOptions.setLockMode(lockMode);
        return this;
    }

	/**
	 * Set lock options
	 * @param lockOptions The @{link LockOptions} used for this query.
	 * @return this object
	 */
	public AuditQuery setLockOptions(LockOptions lockOptions) {
		LockOptions.copy(lockOptions, this.lockOptions);
		return this;
	}
    protected void setQueryProperties(Query query) {
        if (maxResults != null) query.setMaxResults(maxResults);
        if (firstResult != null) query.setFirstResult(firstResult);
        if (cacheable != null) query.setCacheable(cacheable);
        if (cacheRegion != null) query.setCacheRegion(cacheRegion);
        if (comment != null) query.setComment(comment);
        if (flushMode != null) query.setFlushMode(flushMode);
        if (cacheMode != null) query.setCacheMode(cacheMode);
        if (timeout != null) query.setTimeout(timeout);
        if (lockOptions != null && lockOptions.getLockMode() != LockMode.NONE) {
			  query.setLockMode(REFERENCED_ENTITY_ALIAS, lockOptions.getLockMode());
		  }
    }
}
