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
import org.hibernate.persister.entity.EntityPersister;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class ModWorkUnit extends AbstractAuditWorkUnit implements AuditWorkUnit {
    private final Map<String, Object> data;
    private final boolean changes;

    private final EntityPersister entityPersister;
    private final Object[] oldState;
    private final Object[] newState;

    public ModWorkUnit(SessionImplementor sessionImplementor, String entityName, AuditConfiguration verCfg, 
					   Serializable id, EntityPersister entityPersister, Object[] newState, Object[] oldState) {
        super(sessionImplementor, entityName, verCfg, id, RevisionType.MOD);

        this.entityPersister = entityPersister;
        this.oldState = oldState;
        this.newState = newState;
        data = new HashMap<String, Object>();
        changes = verCfg.getEntCfg().get(getEntityName()).getPropertyMapper().map(sessionImplementor, data,
				entityPersister.getPropertyNames(), newState, oldState);
    }

    public boolean containsWork() {
        return changes;
    }

    public Map<String, Object> generateData(Object revisionData) {
        fillDataWithId(data, revisionData);

        return data;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public AuditWorkUnit merge(AddWorkUnit second) {
        return this;
    }

    public AuditWorkUnit merge(ModWorkUnit second) {
        // In case of multiple subsequent flushes within single transaction, modification flags need to be
        // recalculated against initial and final state of the given entity.
        return new ModWorkUnit(
                second.sessionImplementor, second.getEntityName(), second.verCfg, second.id,
                second.entityPersister, second.newState, this.oldState
        );
    }

    public AuditWorkUnit merge(DelWorkUnit second) {
        return second;
    }

    public AuditWorkUnit merge(CollectionChangeWorkUnit second) {
        second.mergeCollectionModifiedData(data);
        return this;
    }

    public AuditWorkUnit merge(FakeBidirectionalRelationWorkUnit second) {
        return FakeBidirectionalRelationWorkUnit.merge(second, this, second.getNestedWorkUnit());
    }

    public AuditWorkUnit dispatch(WorkUnitMergeVisitor first) {
        return first.merge(this);
    }
}