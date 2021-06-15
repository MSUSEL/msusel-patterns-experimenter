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
package org.hibernate.envers.synchronization.work;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.configuration.AuditConfiguration;

/**
 * @author Adam Warski (adam at warski dot org)
 * @author Michal Skowronek (mskowr at o2 dot pl)
 */
public class CollectionChangeWorkUnit extends AbstractAuditWorkUnit implements AuditWorkUnit {
    private Object entity;
	private final String collectionPropertyName;
	private final Map<String, Object> data = new HashMap<String, Object>();

    public CollectionChangeWorkUnit(SessionImplementor session, String entityName, String collectionPropertyName,
									AuditConfiguration verCfg, Serializable id, Object entity) {
        super(session, entityName, verCfg, id, RevisionType.MOD);

        this.entity = entity;
		this.collectionPropertyName = collectionPropertyName;
    }

    public boolean containsWork() {
        return true;
    }

    public Map<String, Object> generateData(Object revisionData) {
        fillDataWithId(data, revisionData);
		Map<String, Object> preGenerateData = new HashMap<String, Object>(data);
		verCfg.getEntCfg().get(getEntityName()).getPropertyMapper()
				.mapToMapFromEntity(sessionImplementor, data, entity, null);
		verCfg.getEntCfg().get(getEntityName()).getPropertyMapper()
				.mapModifiedFlagsToMapFromEntity(sessionImplementor, data, entity, entity);
		verCfg.getEntCfg().get(getEntityName()).getPropertyMapper()
				.mapModifiedFlagsToMapForCollectionChange(collectionPropertyName, data);
		data.putAll(preGenerateData);
        return data;
    }

	public void mergeCollectionModifiedData(Map<String, Object> data) {
		verCfg.getEntCfg().get(getEntityName()).getPropertyMapper()
				.mapModifiedFlagsToMapForCollectionChange(
						collectionPropertyName, data);
	}

	public AuditWorkUnit merge(AddWorkUnit second) {
        return second;
    }

    public AuditWorkUnit merge(ModWorkUnit second) {
        mergeCollectionModifiedData(second.getData());
        return second;
    }

    public AuditWorkUnit merge(DelWorkUnit second) {
        return second;
    }

    public AuditWorkUnit merge(CollectionChangeWorkUnit second) {
		second.mergeCollectionModifiedData(data);
        return this;
    }

    public AuditWorkUnit merge(FakeBidirectionalRelationWorkUnit second) {
        return second;
    }

    public AuditWorkUnit dispatch(WorkUnitMergeVisitor first) {
        return first.merge(this);
    }
}
