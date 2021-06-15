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
package org.hibernate.engine.spi;

import java.io.Serializable;
import java.util.Map;

import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.LockOptions;

/**
 * Definition of a named query, defined in the mapping metadata.
 *
 * @author Gavin King
 */
public class NamedQueryDefinition implements Serializable {
	private final String name;
	private final String query;
	private final boolean cacheable;
	private final String cacheRegion;
	private final Integer timeout;
	private final Integer lockTimeout;
	private final Integer fetchSize;
	private final FlushMode flushMode;
	private final Map parameterTypes;
	private CacheMode cacheMode;
	private boolean readOnly;
	private String comment;

	// kept for backward compatibility until after the 3.1beta5 release of HA
	// TODO: is this still needed?
	public NamedQueryDefinition(
			String query,
			boolean cacheable,
			String cacheRegion,
			Integer timeout,
			Integer fetchSize,
			FlushMode flushMode,
			Map parameterTypes) {
		this(
				null,
				query,
				cacheable,
				cacheRegion,
				timeout,
				fetchSize,
				flushMode,
				null,
				false,
				null,
				parameterTypes
		);
	}

	public NamedQueryDefinition(
			String name,
			String query,
			boolean cacheable,
			String cacheRegion,
			Integer timeout,
			Integer fetchSize,
			FlushMode flushMode,
			CacheMode cacheMode,
			boolean readOnly,
			String comment,
			Map parameterTypes) {
		this(name, query, cacheable, cacheRegion,
				timeout, LockOptions.WAIT_FOREVER, fetchSize,
				flushMode, cacheMode, readOnly, comment, parameterTypes);
	}

	public NamedQueryDefinition(
			String name,
			String query,
			boolean cacheable,
			String cacheRegion,
			Integer timeout,
			Integer lockTimeout,
			Integer fetchSize,
			FlushMode flushMode,
			CacheMode cacheMode,
			boolean readOnly,
			String comment,
			Map parameterTypes) {
		this.name = name;
		this.query = query;
		this.cacheable = cacheable;
		this.cacheRegion = cacheRegion;
		this.timeout = timeout;
		this.lockTimeout = lockTimeout;
		this.fetchSize = fetchSize;
		this.flushMode = flushMode;
		this.parameterTypes = parameterTypes;
		this.cacheMode = cacheMode;
		this.readOnly = readOnly;
		this.comment = comment;
	}

	public String getName() {
		return name;
	}

	public String getQueryString() {
		return query;
	}

	public boolean isCacheable() {
		return cacheable;
	}

	public String getCacheRegion() {
		return cacheRegion;
	}

	public Integer getFetchSize() {
		return fetchSize;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public FlushMode getFlushMode() {
		return flushMode;
	}

	public String toString() {
		return getClass().getName() + '(' + query + ')';
	}

	public Map getParameterTypes() {
		return parameterTypes;
	}

	public String getQuery() {
		return query;
	}

	public CacheMode getCacheMode() {
		return cacheMode;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public String getComment() {
		return comment;
	}

	public Integer getLockTimeout() {
		return lockTimeout;
	}
}
