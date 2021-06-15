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
package org.hibernate.envers.reader;
import java.util.Map;

import org.jboss.logging.Logger;

import org.hibernate.envers.internal.EnversMessageLogger;
import org.hibernate.envers.tools.Triple;

import static org.hibernate.envers.tools.Tools.newHashMap;
import static org.hibernate.envers.tools.Triple.make;

/**
 * First level cache for versioned entities, versions reader-scoped. Each entity is uniquely identified by a
 * revision number and entity id.
 * @author Adam Warski (adam at warski dot org)
 * @author Hern&aacute;n Chanfreau
 */
public class FirstLevelCache {

    public static final EnversMessageLogger LOG = Logger.getMessageLogger(EnversMessageLogger.class, FirstLevelCache.class.getName());

    /**
     * cache for resolve an object for a given id, revision and entityName.
     */
    private final Map<Triple<String, Number, Object>, Object> cache;

    /**
     * used to resolve the entityName for a given id, revision and entity.
     */
    private final Map<Triple<Object, Number, Object>, String> entityNameCache;

    public FirstLevelCache() {
        cache = newHashMap();
        entityNameCache = newHashMap();
    }

    public Object get(String entityName, Number revision, Object id) {
        LOG.debugf("Resolving object from First Level Cache: EntityName:%s - primaryKey:%s - revision:%s", entityName, id, revision);
        return cache.get(make(entityName, revision, id));
    }

    public void put(String entityName, Number revision, Object id, Object entity) {
        LOG.debugf("Caching entity on First Level Cache:  - primaryKey:%s - revision:%s - entityName:%s", id, revision, entityName);
        cache.put(make(entityName, revision, id), entity);
    }

    public boolean contains(String entityName, Number revision, Object id) {
        return cache.containsKey(make(entityName, revision, id));
    }

    /**
     * Adds the entityName into the cache. The key is a triple make with primaryKey, revision and entity
     * @param id primaryKey
     * @param revision revision number
     * @param entity object retrieved by envers
     * @param entityName value of the cache
     */
    public void putOnEntityNameCache(Object id, Number revision, Object entity, String entityName) {
        LOG.debugf("Caching entityName on First Level Cache:  - primaryKey:%s - revision:%s - entity:%s -> entityName:%s",
                   id,
                   revision,
                   entity.getClass().getName(),
                   entityName);
    	entityNameCache.put(make(id, revision, entity), entityName);
    }

    /**
     * Gets the entityName from the cache. The key is a triple make with primaryKey, revision and entity
	 *
     * @param id primaryKey
     * @param revision revision number
     * @param entity object retrieved by envers
	 *
	 * @return The appropriate entity name
     */
    public String getFromEntityNameCache(Object id, Number revision, Object entity) {
        LOG.debugf("Trying to resolve entityName from First Level Cache: - primaryKey:%s - revision:%s - entity:%s",
                   id,
                   revision,
                   entity);
    	return entityNameCache.get(make(id, revision, entity));
    }

	/**
	 * @param id primaryKey
	 * @param revision revision number
	 * @param entity object retrieved by envers
	 * @return true if entityNameCache contains the triple
	 */
    public boolean containsEntityName(Object id, Number revision, Object entity) {
    	return entityNameCache.containsKey(make(id, revision, entity));
    }
}
