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

import org.hibernate.envers.entities.PropertyData;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.reader.AuditReaderImplementor;

/**
 * Property mapper for not owning side of {@link OneToOne} relation.
 * @author Adam Warski (adam at warski dot org)
 * @author HernпїЅn Chanfreau
 * @author Michal Skowronek (mskowr at o2 dot pl)  
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public class OneToOneNotOwningMapper extends AbstractOneToOneMapper {
    private final String owningReferencePropertyName;

    public OneToOneNotOwningMapper(String notOwningEntityName, String owningEntityName, String owningReferencePropertyName,
                                   PropertyData propertyData) {
        super(notOwningEntityName, owningEntityName, propertyData);
        this.owningReferencePropertyName = owningReferencePropertyName;
    }

    @Override
    protected Object queryForReferencedEntity(AuditReaderImplementor versionsReader, EntityInfo referencedEntity,
                                              Serializable primaryKey, Number revision) {
        return versionsReader.createQuery().forEntitiesAtRevision(referencedEntity.getEntityClass(),
                                                                  referencedEntity.getEntityName(), revision)
                                           .add(AuditEntity.relatedId(owningReferencePropertyName).eq(primaryKey))
                                           .getSingleResult();
    }
}
