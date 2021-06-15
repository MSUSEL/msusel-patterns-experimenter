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
import org.hibernate.envers.tools.Tools;
import org.hibernate.persister.entity.EntityPersister;

/**
 * @author Adam Warski (adam at warski dot org)
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public class DelWorkUnit extends AbstractAuditWorkUnit implements AuditWorkUnit {
    private final Object[] state;
    private final EntityPersister entityPersister;
    private final String[] propertyNames;

    public DelWorkUnit(SessionImplementor sessionImplementor, String entityName, AuditConfiguration verCfg,
					   Serializable id, EntityPersister entityPersister, Object[] state) {
        super(sessionImplementor, entityName, verCfg, id, RevisionType.DEL);

        this.state = state;
        this.entityPersister = entityPersister;
        this.propertyNames = entityPersister.getPropertyNames();
    }

    public boolean containsWork() {
        return true;
    }

    public Map<String, Object> generateData(Object revisionData) {
        Map<String, Object> data = new HashMap<String, Object>();
        fillDataWithId(data, revisionData);

		if (verCfg.getGlobalCfg().isStoreDataAtDelete()) {
			verCfg.getEntCfg().get(getEntityName()).getPropertyMapper().map(sessionImplementor, data,
					propertyNames, state, state);
		} else {
			verCfg.getEntCfg().get(getEntityName()).getPropertyMapper().map(sessionImplementor, data,
					propertyNames, null, state);
		}

        return data;
    }

    public AuditWorkUnit merge(AddWorkUnit second) {
        if (Tools.arraysEqual(second.getState(), state)) {
            return null; // Return null if object's state has not changed.
        }
        return new ModWorkUnit(sessionImplementor, entityName, verCfg, id, entityPersister, second.getState(), state); 
    }

    public AuditWorkUnit merge(ModWorkUnit second) {
        return null;
    }

    public AuditWorkUnit merge(DelWorkUnit second) {
        return this;
    }

    public AuditWorkUnit merge(CollectionChangeWorkUnit second) {
        return this;
    }

    public AuditWorkUnit merge(FakeBidirectionalRelationWorkUnit second) {
        return this;
    }

    public AuditWorkUnit dispatch(WorkUnitMergeVisitor first) {
        return first.merge(this);
    }
}