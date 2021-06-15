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
package org.hibernate.envers.entities.mapper.relation;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.entities.EntityConfiguration;
import org.hibernate.envers.entities.PropertyData;
import org.hibernate.envers.entities.mapper.PersistentCollectionChangeData;
import org.hibernate.envers.entities.mapper.PropertyMapper;
import org.hibernate.envers.exception.AuditException;
import org.hibernate.envers.reader.AuditReaderImplementor;
import org.hibernate.envers.tools.reflection.ReflectionTools;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.property.Setter;

/**
 * Base class for property mappers that manage to-one relation.
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public abstract class AbstractToOneMapper implements PropertyMapper {
    private final PropertyData propertyData;

    protected AbstractToOneMapper(PropertyData propertyData) {
        this.propertyData = propertyData;
    }

    @Override
    public boolean mapToMapFromEntity(SessionImplementor session, Map<String, Object> data, Object newObj, Object oldObj) {
        return false;
    }

    @Override
    public void mapToEntityFromMap(AuditConfiguration verCfg, Object obj, Map data, Object primaryKey,
                                   AuditReaderImplementor versionsReader, Number revision) {
        if (obj != null) {
            nullSafeMapToEntityFromMap(verCfg, obj, data, primaryKey, versionsReader, revision);
        }
    }

    @Override
    public List<PersistentCollectionChangeData> mapCollectionChanges(SessionImplementor session, String referencingPropertyName,
                                                                     PersistentCollection newColl, Serializable oldColl,
																	 Serializable id) {
        return null;
    }

    /**
     * @param verCfg Audit configuration.
     * @param entityName Entity name.
     * @return Entity class, name and information whether it is audited or not.
     */
    protected EntityInfo getEntityInfo(AuditConfiguration verCfg, String entityName) {
        EntityConfiguration entCfg = verCfg.getEntCfg().get(entityName);
        boolean isRelationAudited = true;
        if (entCfg == null) {
            // a relation marked as RelationTargetAuditMode.NOT_AUDITED
            entCfg = verCfg.getEntCfg().getNotVersionEntityConfiguration(entityName);
            isRelationAudited = false;
        }
        Class entityClass;
        try {
			entityClass = ReflectHelper.classForName(entCfg.getEntityClassName());
		}
		catch ( ClassNotFoundException e ) {
			throw new AuditException( e );
		}
        return new EntityInfo(entityClass, entityName, isRelationAudited);
    }

    protected void setPropertyValue(Object targetObject, Object value) {
        Setter setter = ReflectionTools.getSetter(targetObject.getClass(), propertyData);
        setter.set(targetObject, value, null);
    }

    /**
     * @return Bean property that represents the relation.
     */
    protected PropertyData getPropertyData() {
        return propertyData;
    }

    /**
     * Parameter {@code obj} is never {@code null}.
     * @see PropertyMapper#mapToEntityFromMap(AuditConfiguration, Object, Map, Object, AuditReaderImplementor, Number)
     */
    public abstract void nullSafeMapToEntityFromMap(AuditConfiguration verCfg, Object obj, Map data, Object primaryKey,
                                                    AuditReaderImplementor versionsReader, Number revision);

    /**
     * Simple descriptor of an entity.
     */
    protected class EntityInfo {
        private final Class entityClass;
        private final String entityName;
        private final boolean audited;

        public EntityInfo(Class entityClass, String entityName, boolean audited) {
            this.entityClass = entityClass;
            this.entityName = entityName;
            this.audited = audited;
        }

        public Class getEntityClass() { return entityClass; }
        public String getEntityName() { return entityName; }
        public boolean isAudited() { return audited; }
    }
}
