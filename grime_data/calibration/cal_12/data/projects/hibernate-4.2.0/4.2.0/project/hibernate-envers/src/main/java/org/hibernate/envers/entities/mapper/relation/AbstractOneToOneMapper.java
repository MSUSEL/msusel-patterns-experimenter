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
import java.util.Map;
import javax.persistence.NoResultException;

import org.hibernate.NonUniqueResultException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.entities.PropertyData;
import org.hibernate.envers.exception.AuditException;
import org.hibernate.envers.reader.AuditReaderImplementor;

/**
 * Template class for property mappers that manage one-to-one relation.
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public abstract class AbstractOneToOneMapper extends AbstractToOneMapper {
    private final String entityName;
    private final String referencedEntityName;

    protected AbstractOneToOneMapper(String entityName, String referencedEntityName, PropertyData propertyData) {
        super(propertyData);
        this.entityName = entityName;
        this.referencedEntityName = referencedEntityName;
    }

    @Override
    public void nullSafeMapToEntityFromMap(AuditConfiguration verCfg, Object obj, Map data, Object primaryKey,
                                           AuditReaderImplementor versionsReader, Number revision) {
        EntityInfo referencedEntity = getEntityInfo(verCfg, referencedEntityName);

        Object value = null;
        try {
            value = queryForReferencedEntity(versionsReader, referencedEntity, (Serializable) primaryKey, revision);
        } catch (NoResultException e) {
            value = null;
        } catch (NonUniqueResultException e) {
            throw new AuditException("Many versions results for one-to-one relationship " + entityName +
                                     "." + getPropertyData().getBeanName() + ".", e);
        }

        setPropertyValue(obj, value);
    }

    /**
     * @param versionsReader Audit reader.
     * @param referencedEntity Referenced entity descriptor.
     * @param primaryKey Referenced entity identifier.
     * @param revision Revision number.
     * @return Referenced object or proxy of one-to-one relation.
     */
    protected abstract Object queryForReferencedEntity(AuditReaderImplementor versionsReader, EntityInfo referencedEntity,
                                                       Serializable primaryKey, Number revision);

    @Override
    public void mapModifiedFlagsToMapFromEntity(SessionImplementor session, Map<String, Object> data, Object newObj, Object oldObj) {
    }

    @Override
    public void mapModifiedFlagsToMapForCollectionChange(String collectionPropertyName, Map<String, Object> data) {
        if (getPropertyData().isUsingModifiedFlag()) {
            data.put(getPropertyData().getModifiedFlagPropertyName(), collectionPropertyName.equals(getPropertyData().getName()));
        }
    }
}
