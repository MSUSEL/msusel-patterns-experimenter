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
package org.hibernate.envers.entities;

import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.entities.mapper.id.IdMapper;
import org.hibernate.envers.entities.mapper.relation.lazy.ToOneDelegateSessionImplementor;
import org.hibernate.envers.exception.AuditException;
import org.hibernate.envers.reader.AuditReaderImplementor;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Adam Warski (adam at warski dot org)
 * @author Hern&aacute;n Chanfreau
 */
public class EntityInstantiator {
    private final AuditConfiguration verCfg;
    private final AuditReaderImplementor versionsReader;

    public EntityInstantiator(AuditConfiguration verCfg, AuditReaderImplementor versionsReader) {
        this.verCfg = verCfg;
        this.versionsReader = versionsReader;
    }

    /**
     * Creates an entity instance based on an entry from the versions table.
     * @param entityName Name of the entity, which instances should be read
     * @param versionsEntity An entry in the versions table, from which data should be mapped.
     * @param revision Revision at which this entity was read.
     * @return An entity instance, with versioned properties set as in the versionsEntity map, and proxies
     * created for collections.
     */
    public Object createInstanceFromVersionsEntity(String entityName, Map versionsEntity, Number revision) {
        if (versionsEntity == null) {
            return null;
        }

        // The $type$ property holds the name of the (versions) entity
        String type = verCfg.getEntCfg().getEntityNameForVersionsEntityName((String) versionsEntity.get("$type$"));

        if (type != null) {
            entityName = type;
        }

        // First mapping the primary key
        IdMapper idMapper = verCfg.getEntCfg().get(entityName).getIdMapper();
        Map originalId = (Map) versionsEntity.get(verCfg.getAuditEntCfg().getOriginalIdPropName());

        // Fixes HHH-4751 issue (@IdClass with @ManyToOne relation mapping inside)
        // Note that identifiers are always audited
        // Replace identifier proxies if do not point to audit tables
        replaceNonAuditIdProxies(originalId, revision);

        Object primaryKey = idMapper.mapToIdFromMap(originalId);

        // Checking if the entity is in cache
        if (versionsReader.getFirstLevelCache().contains(entityName, revision, primaryKey)) {
            return versionsReader.getFirstLevelCache().get(entityName, revision, primaryKey);
        }

        // If it is not in the cache, creating a new entity instance
        Object ret;
        try {
        	EntityConfiguration entCfg = verCfg.getEntCfg().get(entityName);
        	if(entCfg == null) {
        		// a relation marked as RelationTargetAuditMode.NOT_AUDITED 
        		entCfg = verCfg.getEntCfg().getNotVersionEntityConfiguration(entityName);
        	}

            Class<?> cls = ReflectHelper.classForName(entCfg.getEntityClassName());
            ret = ReflectHelper.getDefaultConstructor(cls).newInstance();
        } catch (Exception e) {
            throw new AuditException(e);
        }

        // Putting the newly created entity instance into the first level cache, in case a one-to-one bidirectional
        // relation is present (which is eagerly loaded).
        versionsReader.getFirstLevelCache().put(entityName, revision, primaryKey, ret);

        verCfg.getEntCfg().get(entityName).getPropertyMapper().mapToEntityFromMap(verCfg, ret, versionsEntity, primaryKey,
                versionsReader, revision);
        idMapper.mapToEntityFromMap(ret, originalId);

        // Put entity on entityName cache after mapping it from the map representation
        versionsReader.getFirstLevelCache().putOnEntityNameCache(primaryKey, revision, ret, entityName);
        
        return ret;
    }

    @SuppressWarnings({"unchecked"})
    private void replaceNonAuditIdProxies(Map originalId, Number revision) {
        for (Object key : originalId.keySet()) {
            Object value = originalId.get(key);
            if (value instanceof HibernateProxy) {
                HibernateProxy hibernateProxy = (HibernateProxy) value;
                LazyInitializer initializer = hibernateProxy.getHibernateLazyInitializer();
                final String entityName = initializer.getEntityName();
                final Serializable entityId = initializer.getIdentifier();
                if (verCfg.getEntCfg().isVersioned(entityName)) {
                    final String entityClassName = verCfg.getEntCfg().get(entityName).getEntityClassName();
                    Class entityClass;
                    try {
						entityClass = ReflectHelper.classForName(entityClassName);
					}
					catch ( ClassNotFoundException e ) {
						throw new AuditException( e );
					}
                    final ToOneDelegateSessionImplementor delegate = new ToOneDelegateSessionImplementor(versionsReader, entityClass, entityId, revision, verCfg);
                    originalId.put(key,
                            versionsReader.getSessionImplementor().getFactory().getEntityPersister(entityName).createProxy(entityId, delegate));
                }
            }
        }
    }

    @SuppressWarnings({"unchecked"})
    public void addInstancesFromVersionsEntities(String entityName, Collection addTo, List<Map> versionsEntities, Number revision) {
        for (Map versionsEntity : versionsEntities) {
            addTo.add(createInstanceFromVersionsEntity(entityName, versionsEntity, revision));
        }
    }

	public AuditConfiguration getAuditConfiguration() {
		return verCfg;
	}

	public AuditReaderImplementor getAuditReaderImplementor() {
		return versionsReader;
	}
}
