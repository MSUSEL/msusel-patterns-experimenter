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
import java.util.HashMap;
import java.util.Map;

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.entities.PropertyData;
import org.hibernate.envers.entities.mapper.id.IdMapper;
import org.hibernate.envers.entities.mapper.relation.lazy.ToOneDelegateSessionImplementor;
import org.hibernate.envers.reader.AuditReaderImplementor;
import org.hibernate.envers.tools.Tools;
import org.hibernate.envers.tools.query.Parameters;
import org.hibernate.persister.entity.EntityPersister;

/**
 * @author Adam Warski (adam at warski dot org)
 * @author HernпїЅn Chanfreau
 * @author Michal Skowronek (mskowr at o2 dot pl)
 */
public class ToOneIdMapper extends AbstractToOneMapper {
    private final IdMapper delegate;
    private final String referencedEntityName;
    private final boolean nonInsertableFake;

    public ToOneIdMapper(IdMapper delegate, PropertyData propertyData, String referencedEntityName, boolean nonInsertableFake) {
        super(propertyData);
        this.delegate = delegate;
        this.referencedEntityName = referencedEntityName;
        this.nonInsertableFake = nonInsertableFake;
    }

    public boolean mapToMapFromEntity(SessionImplementor session, Map<String, Object> data, Object newObj, Object oldObj) {
        HashMap<String, Object> newData = new HashMap<String, Object>();

        // If this property is originally non-insertable, but made insertable because it is in a many-to-one "fake"
        // bi-directional relation, we always store the "old", unchaged data, to prevent storing changes made
        // to this field. It is the responsibility of the collection to properly update it if it really changed.
        delegate.mapToMapFromEntity(newData, nonInsertableFake ? oldObj : newObj);

		for (Map.Entry<String, Object> entry : newData.entrySet()) {
			data.put(entry.getKey(), entry.getValue());
		}

        return checkModified(session, newObj, oldObj);
    }

    @Override
    public void mapModifiedFlagsToMapFromEntity(SessionImplementor session, Map<String, Object> data, Object newObj, Object oldObj) {
        if (getPropertyData().isUsingModifiedFlag()) {
            data.put(getPropertyData().getModifiedFlagPropertyName(), checkModified(session, newObj, oldObj));
        }
    }

    @Override
    public void mapModifiedFlagsToMapForCollectionChange(String collectionPropertyName, Map<String, Object> data) {
        if (getPropertyData().isUsingModifiedFlag()) {
            data.put(getPropertyData().getModifiedFlagPropertyName(), collectionPropertyName.equals(getPropertyData().getName()));
        }
    }

    protected boolean checkModified(SessionImplementor session, Object newObj, Object oldObj) {
        //noinspection SimplifiableConditionalExpression
        return nonInsertableFake ? false : !Tools.entitiesEqual(session, referencedEntityName, newObj, oldObj);
    }

    public void nullSafeMapToEntityFromMap(AuditConfiguration verCfg, Object obj, Map data, Object primaryKey,
                                           AuditReaderImplementor versionsReader, Number revision) {
        Object entityId = delegate.mapToIdFromMap(data);
        Object value = null;
        if (entityId != null) {
            if (versionsReader.getFirstLevelCache().contains(referencedEntityName, revision, entityId)) {
                value = versionsReader.getFirstLevelCache().get(referencedEntityName, revision, entityId);
            } else {
                EntityInfo referencedEntity = getEntityInfo(verCfg, referencedEntityName);
                value = ToOneEntityLoader.createProxyOrLoadImmediate(
                        versionsReader, referencedEntity.getEntityClass(), referencedEntityName,
                        entityId, revision, verCfg
                );
            }
        }

        setPropertyValue(obj, value);
    }

	public void addMiddleEqualToQuery(Parameters parameters, String idPrefix1, String prefix1, String idPrefix2, String prefix2) {
		delegate.addIdsEqualToQuery( parameters, prefix1, delegate, prefix2 );
	}
}
