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
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.envers.Audited;
import org.hibernate.envers.entities.PropertyData;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.reader.AuditReaderImplementor;
import org.hibernate.persister.entity.EntityPersister;

/**
 * Property mapper for {@link OneToOne} with {@link PrimaryKeyJoinColumn} relation.
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public class OneToOnePrimaryKeyJoinColumnMapper extends AbstractOneToOneMapper {
    public OneToOnePrimaryKeyJoinColumnMapper(String entityName, String referencedEntityName, PropertyData propertyData) {
        super(entityName, referencedEntityName, propertyData);
    }

    @Override
    protected Object queryForReferencedEntity(AuditReaderImplementor versionsReader, EntityInfo referencedEntity,
                                              Serializable primaryKey, Number revision) {
        if (referencedEntity.isAudited()) {
            // Audited relation.
            return versionsReader.createQuery().forEntitiesAtRevision(referencedEntity.getEntityClass(),
                                                                      referencedEntity.getEntityName(), revision)
                                               .add(AuditEntity.id().eq(primaryKey))
                                               .getSingleResult();
        } else {
            // Not audited relation.
            return createNotAuditedEntityReference(versionsReader, referencedEntity.getEntityClass(),
                                                   referencedEntity.getEntityName(), primaryKey);
        }
    }

    /**
     * Create Hibernate proxy or retrieve the complete object of referenced, not audited entity. According to
     * {@link Audited#targetAuditMode()}} documentation, reference shall point to current (non-historical) version
     * of an entity.
     */
    private Object createNotAuditedEntityReference(AuditReaderImplementor versionsReader, Class<?> entityClass,
                                                   String entityName, Serializable primaryKey) {
        EntityPersister entityPersister = versionsReader.getSessionImplementor().getFactory().getEntityPersister(entityName);
        if (entityPersister.hasProxy()) {
            // If possible create a proxy. Returning complete object may affect performance.
            return versionsReader.getSession().load(entityClass, primaryKey);
        } else {
            // If proxy is not allowed (e.g. @Proxy(lazy=false)) construct the original object.
            return versionsReader.getSession().get(entityClass, primaryKey);
        }
    }
}